import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHasher {
	private static final int SALT_LENGTH = 16;
	private static final int ITERATIONS = 1000;
	private static final int HASH_BITS = 256;
	
	public static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a random salt (prevents rainbow-tables attacks)
		byte[] salt = new byte[SALT_LENGTH];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);
		
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
}
