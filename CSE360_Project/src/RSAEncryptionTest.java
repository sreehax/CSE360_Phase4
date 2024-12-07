import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

class RSAEncryptionTest {

	@Test
	void testGenKeyPair() throws NoSuchAlgorithmException {
		RSAEncryption.genKeyPair();
	}

	@Test
	void testGetPrivkey() throws NoSuchAlgorithmException {
		RSAEncryption.getPrivkey(RSAEncryption.genKeyPair());
	}

	@Test
	void testGetPrivKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] serialized = {1,2,3,4,5};
		RSAEncryption.getPrivKey(serialized);
	}

	@Test
	void testGetPubkey() throws NoSuchAlgorithmException {
		RSAEncryption.getPubkey(RSAEncryption.genKeyPair());
	}

	@Test
	void testGetPubKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] serialized = {1,2,3,4,5};
		RSAEncryption.getPubKey(serialized);
	}

	@Test
	void testEncryptForPublicKeyString() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		RSAEncryption.encryptFor(RSAEncryption.getPubkey(RSAEncryption.genKeyPair()), "Hello there");
	}

	@Test
	void testEncryptForPublicKeyByteArray() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] message = "hello there".getBytes(); //message to be encrypted (as bytes)
		
		byte[] cipher = RSAEncryption.encryptFor(RSAEncryption.getPubkey(RSAEncryption.genKeyPair()), message); //encrypt message
		
		System.out.println("ciphertext: "); //prints encrypted bytes of message
		for(int i=0;i<cipher.length;i++) {
			System.out.print(cipher[i]);
		}
		System.out.println();

	}

	@Test
	void testDecryptMsg() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] message = "hello there".getBytes(); // message to be encrypted
		KeyPair key =RSAEncryption.genKeyPair(); //generate a key pair for encrypting and decrypting
		byte[] ciphertext = RSAEncryption.encryptFor(RSAEncryption.getPubkey(key), message); //encrypt the message

		String newtext = RSAEncryption.decryptMsg(RSAEncryption.getPrivkey(key), ciphertext); //decrypt the message
		System.out.println("Decrypted text: " + newtext);
	}

}
