import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

class SymmetricEncryptionTest {

	@Test
	void testSymmetricEncryptionString() throws NoSuchAlgorithmException, InvalidKeySpecException {
		//Creates encryption with string
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
	}

	@Test
	void testSymmetricEncryptionByteArray() {
		//creates Encryption with Byte Array
		byte[] keydata = {0,1,2,3,4,5};
		SymmetricEncryption s = new SymmetricEncryption(keydata);
	}

	@Test
	void testEncryptByteArray() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
		//Encrypt a byte array for testing
		byte[] data = {0,1,2,3,4,5};
		s.encrypt(data);
	}

	@Test
	void testEncryptByteArrayByteArray() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
		//Encrypt byte array with generated Nonce value
		byte[] data = {0,1,2,3,4,5};
		s.encrypt(data, SymmetricEncryption.generateNonce());
	}

	@Test
	void testDecrypt() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
		
		byte[] data = ("Password12345").getBytes();
		data = s.encrypt(data, SymmetricEncryption.generateNonce());
		s.decrypt(data);
	}

	@Test
	void testGenerateNonce() throws NoSuchAlgorithmException, InvalidKeySpecException {
		SymmetricEncryption.generateNonce();
	}

}
