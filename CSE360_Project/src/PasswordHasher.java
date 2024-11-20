import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utility class for hashing and verifying passwords using PBKDF2 with HMAC-SHA1.
 */
public class PasswordHasher {
	private static final int SALT_LENGTH = 16;
	private static final int ITERATIONS = 1000;
	private static final int HASH_BITS = 256;

	/**
     * Hashes a password using PBKDF2 with a random salt and returns the hash and salt
     * as a single string for storage.
     *
     * @param password the plaintext password to hash.
     * @return a string containing the iterations, salt, and hashed password in base64 encoding.
     * @throws NoSuchAlgorithmException if PBKDF2 algorithm is not available.
     * @throws InvalidKeySpecException  if the key specification is invalid.
     */
	public static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a random salt (prevents rainbow-tables attacks)
		byte[] salt = PasswordHasher.genSalt();
		
		// Perform the rest of the hash
		return PasswordHasher.hashPassword(password, salt);
	}
	
	/**
     * Hashes a password using PBKDF2 with a specified salt and returns the hash and salt
     * as a single string for storage.
     * 
     * @param password the plaintext password to hash.
     * @param salt the salt to use
     * @return a byte array to use for encryption
     * @throws NoSuchAlgorithmException if PBKDF2 algorithm is not available.
     * @throws InvalidKeySpecException  if the key specification is invalid.
     */
	public static String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Set up PBKDF2 with our defined parameters
		int iterations = ITERATIONS;
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, HASH_BITS);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		
		// Generate the hash
		byte[] hash = skf.generateSecret(spec).getEncoded();
		
		// Encode parts to base64 so that we can return a string for easy storage
		Encoder enc = Base64.getEncoder();
		String strSalt = enc.encodeToString(salt);
		String strHash = enc.encodeToString(hash);
		
		// Return a full representation that tells the verifier all the required parameters
		return iterations + ":" + strSalt + ":" + strHash;
	}

	/**
     * Verifies a password by hashing it using the stored salt and iterations and comparing
     * it to the stored hash.
     *
     * @param password the plaintext password to verify.
     * @param strHash  the stored hash in the format iterations:salt:hash.
     * @return true if the password matches the stored hash, false otherwise.
     * @throws NoSuchAlgorithmException if PBKDF2 algorithm is not available.
     * @throws InvalidKeySpecException  if the key specification is invalid.
     */
	public static boolean verifyPassword(String password, String strHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Obtain the iterations, salt, and hash
		String[] hashParts = strHash.split(":");
		int iterations = Integer.parseInt(hashParts[0]);
		
		// Decode the salt and hash into their byte array forms
		Decoder dec = Base64.getDecoder();
		byte[] salt = dec.decode(hashParts[1]);
		byte[] hash = dec.decode(hashParts[2]);
		
		// Set up PBKDF2 with the known parameters and the password we are checking
		// Even if we change default parameters in the future old password hashes will not break
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		
		// Generate the hash
		byte[] computedHash = skf.generateSecret(spec).getEncoded();
		
		// Constant time comparison to prevent timing attacks
		return constantTimeComparison(hash, computedHash);
	}
	 /**
     * Compares two byte arrays in constant time to prevent timing attacks.
     *
     * @param first  the first byte array.
     * @param second the second byte array.
     * @return true if both arrays are of equal length and contain the same bytes.
     */
	public static boolean constantTimeComparison(byte[] first, byte[] second) {
		// Any differences will accumulate due to the properties of an XOR
		int accumulated = first.length ^ second.length;
		
		for (int i = 0; i < first.length && i < second.length; i++) {
			accumulated = accumulated | (first[i] ^ second[i]);
		}
		
		// This will only be true if the arrays are the same length and they contain the same bytes
		// Since the loop runs the same number of iterations no matter how many bytes are right,
		// Timing attacks are impossible to use for an attacker trying to exfiltrate a password
		return accumulated == 0;
	}
	  /**
     * Conducts a self-test for the PasswordHasher class to validate the hashing,
     * verification, and constant-time comparison functionalities.
     * 
     * - Tests hashing and verifying a password for correct functionality.
     * - Tests constant-time comparison with both matching and non-matching byte arrays.
     * - Outputs test results to the console, marking each test as "[PASS]" or "[FAIL]".
     * 
     * @return true if all tests pass, false if any test fails
     */
	/*public static boolean selfTest() {
		System.out.println("PasswordHasher SelfTest");
		boolean allGood = true;
		boolean current = true;
		
		// Try hashing a password
		try {
			String hash = PasswordHasher.hashPassword("password1234");
			System.out.println("[PASS] hashed password properly!");
		} catch (Exception e) {
			System.out.println("[FAIL] Couldn't hash the password properly");
			System.out.println(e.getMessage());
			allGood = false;
		}
		
		// Try verifying the password, expected pass and fail
		try {
			current = PasswordHasher.verifyPassword("password1234", PasswordHasher.hashPassword("password1234"));
			allGood &= current;
			if (current) {
				System.out.println("[PASS] Verified hashed password");
			} else {
				System.out.println("[FAIL] Couldn't verify password, should be verified!");
			}
		} catch (Exception e) {
			System.out.println("[FAIL] Exception while verifying password!");
			System.out.println(e.getMessage());
		}
		
		try {
			current = !PasswordHasher.verifyPassword("password1234", PasswordHasher.hashPassword("password1984"));
			allGood &= current;
			if (current) {
				System.out.println("[PASS] Invalid password successfully rejected");
			} else {
				System.out.println("[FAIL] Invalid password was accepted, it shouldn't be!");
			}
		} catch (Exception e) {
			System.out.println("[FAIL] Exception while verifying password!");
			System.out.println(e.getMessage());
		}
		
		// Finally, test constant time comparison
		byte[] a1 = new byte[] { (byte)0xde, (byte)0xad, (byte)0xbe, (byte)0xef, (byte)0xd0, (byte)0x0d, (byte)0xfe, (byte)0xed };
		byte[] a3 = new byte[] { (byte)0xde, (byte)0xad, (byte)0xbe, (byte)0xef, (byte)0xd0, (byte)0x0d, (byte)0xfe, (byte)0xed };
		byte[] a2 = new byte[8];
		SecureRandom random = new SecureRandom();
		random.nextBytes(a2);
		
		current = PasswordHasher.constantTimeComparison(a1, a3);
		allGood &= current;
		if (current) {
			System.out.println("[PASS] constantTimeComparison a1 == a3");
		} else {
			System.out.println("[FAIL] constantTimeComparison failed to identify equal arrays a1 and a3!");
		}
		// Test: Constant-time comparison with non-matching byte arrays
		current = !PasswordHasher.constantTimeComparison(a1, a2);
		allGood &= current;
		if (current) {
			System.out.println("[PASS] constantTimeComparison a1 != a2");
		} else {
			System.out.println("[FAIL] constantTimeComparison said a1 == a2, but they are not equal values!");
		}
		return allGood;
	}*/
	
	public static byte[] genSalt() {
		byte[] salt = new byte[SALT_LENGTH];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		return salt;
	}
}
