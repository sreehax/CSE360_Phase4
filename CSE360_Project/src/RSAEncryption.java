import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAEncryption {
	public static KeyPair genKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		
		return kpg.generateKeyPair();
	}
	
	public static PrivateKey getPrivkey(KeyPair pair) {
		return pair.getPrivate();
	}
	
	public static PrivateKey getPrivKey(byte[] serialized) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		EncodedKeySpec eks = new X509EncodedKeySpec(serialized);
		return kf.generatePrivate(eks);
	}
	
	public static PublicKey getPubkey(KeyPair pair) {
		return pair.getPublic();
	}
	
	public static PublicKey getPubKey(byte[] serialized) throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory kf = KeyFactory.getInstance("RSA");
		EncodedKeySpec eks = new X509EncodedKeySpec(serialized);
		return kf.generatePublic(eks);
	}
	
	public static byte[] encryptFor(PublicKey recipient, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
		return RSAEncryption.encryptFor(recipient, msgBytes);
	}
	
	public static byte[] encryptFor(PublicKey recipient, byte[] message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, recipient);
		return cipher.doFinal(message);
	}
	
	public static String decryptMsg(PrivateKey priv, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priv);
		byte[] plaintext = cipher.doFinal(ciphertext);
		return new String(plaintext, StandardCharsets.UTF_8);
	}
	
	public static byte[] decryptBytes(PrivateKey priv, byte[] ciphertext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priv);
		byte[] plaintext = cipher.doFinal(ciphertext);
		return plaintext;
	}
}
