import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricEncryption {
	// Use the ChaCha20 stream cipher with the Poly1305 MAC in an AEAD configuration
	
	private SecretKey key;
	
	// Initialize using a password hash as an encryption key
	// WARNING! Use a different salt
	public SymmetricEncryption(String passhash) throws NoSuchAlgorithmException {
		// Obtain the iterations, salt, and hash
		String[] hashParts = passhash.split(":");

		// Decode hash into byte form
		Decoder dec = Base64.getDecoder();
		byte[] hash = dec.decode(hashParts[2]);
		
		// Since the hash is 256 bits, use it as a ChaCha20 key
		this.key = new SecretKeySpec(hash, "ChaCha20");
	}
	
	// Initialize using bytes
	public SymmetricEncryption(byte[] keydata) {
		this.key = new SecretKeySpec(keydata, "ChaCha20");
	}
	
	public byte[] encrypt(String msg) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
		return this.encrypt(msgBytes, generateNonce());
	}
	
	public byte[] encrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return this.encrypt(data, generateNonce());
	}
	
	public byte[] encrypt(byte[] data, byte[] nonce) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(nonce);
		cipher.init(Cipher.ENCRYPT_MODE, this.key, ivParameterSpec);
		
		byte[] ciphertext = cipher.doFinal(data);
		
		byte[] ret = new byte[ciphertext.length + nonce.length];
		// copy ciphertext and nonce into the return value
		for (int i = 0; i < ciphertext.length; i++)
			ret[i] = ciphertext[i];
		
		for (int i = 0; i < nonce.length; i++)
			ret[ciphertext.length + i] = nonce[i];
		return ret;
	}
	
	public byte[] decrypt(byte[] encdata) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// extract the ciphertext and nonce from the encrypted data
		byte[] ciphertext = new byte[encdata.length - 12];
		byte[] nonce = new byte[12];
		
		for (int i = 0; i < ciphertext.length; i++)
			ciphertext[i] = encdata[i];
		
		for (int i = 0; i < nonce.length; i++)
			nonce[i] = encdata[ciphertext.length + i];
		
		// Set up the cipher and decrypt
		Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(nonce);
		cipher.init(Cipher.DECRYPT_MODE, this.key, ivParameterSpec);
		
		return cipher.doFinal(ciphertext);
	}
	
	public static byte[] generateNonce() {
		byte[] ret = new byte[12];
		SecureRandom random = new SecureRandom();
		random.nextBytes(ret);
		return ret;
	}
}