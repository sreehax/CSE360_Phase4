import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

	@Test
	void testHashPasswordString() throws NoSuchAlgorithmException, InvalidKeySpecException {
		PasswordHasher.hashPassword("password");
	}

	@Test
	void testHashPasswordStringByteArray() throws NoSuchAlgorithmException, InvalidKeySpecException {
		PasswordHasher.hashPassword("password", PasswordHasher.genSalt());
	}

	@Test
	void testVerifyPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
		PasswordHasher.verifyPassword("password", PasswordHasher.hashPassword("password"));
	}

	@Test
	void testConstantTimeComparison() {
		byte[] first = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		byte[] second = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		PasswordHasher.constantTimeComparison(first, second);
		
		byte[] second2 = {1,3,3,3,5,6,7,8,9,10,11,12,13,14,15,16};
		PasswordHasher.constantTimeComparison(first, second2);
	}

	//@Test
	//void testSelfTest() {
	//	PasswordHasher.selfTest();
	//}

	@Test
	void testGenSalt() {
		PasswordHasher.genSalt();
	}

}
