import static org.junit.jupiter.api.Assertions.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;
import org.sqlite.SQLiteConfig;

class AllTest {

	@Test
	void testArticleStringStringArrayListOfStringIntStringStringStringArrayListOfString() {
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		
	}

	@Test
	void testArticle() {
		Article a = new Article();
	}

	@Test
	void testPrintInfo() {
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		a.printInfo();
	}

	@Test
	void testGetTitle() {
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		a.getTitle();
	}

	@Test
	void testSetTitle() {
		Article a = new Article();
		a.setTitle("testTitle");
	}

	@Test
	void testGetBody() {
		Article a = new Article();
		a.getBody();
	}

	@Test
	void testSetBody() {
		Article a = new Article();
		a.setBody("testBody");
	}

	@Test
	void testGetReferences() {
		Article a = new Article();
		a.getReferences();
	}

	@Test
	void testGetReferencesStr() {
		Article a = new Article();
		a.getReferencesStr();
	}

	@Test
	void testAddReference() {
		Article a = new Article();
		a.addReference("testReference");
	}

	@Test
	void testGetID() {
		Article a = new Article();
		a.getID();
	}

	@Test
	void testSetID() {
		Article a = new Article();
		a.setID(1);
	}

	@Test
	void testGetHeader() {
		Article a = new Article();
		a.getHeader();
	}

	@Test
	void testSetHeader() {
		Article a = new Article();
		a.setHeader("testHeader");
	}

	@Test
	void testGetGrouping() {
		Article a = new Article();
		a.getGrouping();
	}

	@Test
	void testSetGrouping() {
		Article a = new Article();
		a.setGrouping("testGroup");
	}

	@Test
	void testGetDescription() {
		Article a = new Article();
		a.getDescription();
	}

	@Test
	void testSetDescription() {
		Article a = new Article();
		a.setDescription("testDescription");
	}

	@Test
	void testGetKeywords() {
		Article a = new Article();
		a.getKeywords();
	}

	@Test
	void testGetKeywordsStr() {
		Article a = new Article();
		a.getKeywords();
	}

	@Test
	void testAddKeyword() {
		Article a = new Article();
		a.addKeyword("testKeyword");
	}

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
		byte[] message = "hello there".getBytes();
		RSAEncryption.encryptFor(RSAEncryption.getPubkey(RSAEncryption.genKeyPair()), message);

	}

	@Test
	void testDecryptMsg() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] message = "hello there".getBytes();
		KeyPair key =RSAEncryption.genKeyPair();
		byte[] ciphertext = RSAEncryption.encryptFor(RSAEncryption.getPubkey(key), message);

		RSAEncryption.decryptMsg(RSAEncryption.getPrivkey(key), ciphertext);
	}
	
	@Test
	void testSymmetricEncryptionString() throws NoSuchAlgorithmException, InvalidKeySpecException {
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
	}

	@Test
	void testSymmetricEncryptionByteArray() {
		byte[] keydata = {0,1,2,3,4,5};
		SymmetricEncryption s = new SymmetricEncryption(keydata);
	}

	@Test
	void testEncryptByteArray() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
		
		byte[] data = {0,1,2,3,4,5};
		s.encrypt(data);
	}

	@Test
	void testEncryptByteArrayByteArray() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		String passhash = PasswordHasher.hashPassword("Password");
		SymmetricEncryption s = new SymmetricEncryption(passhash);
		
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
	@Test
	void testUser() {
		User u = new User();
	}

	@Test
	void testUserStringStringStringStringStringStringStringInt() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
	}

	@Test
	void testSetFirstname() {
		User u = new User();
		u.setFirstname("Bob");
	}

	@Test
	void testSetMiddlename() {
		User u = new User();
		u.setMiddlename("Billy");
	}

	@Test
	void testSetLastname() {
		User u = new User();
		u.setLastname("Johnson");
	}

	@Test
	void testSetPreferredname() {
		User u = new User();
		u.setPreferredname("Joe");
	}

	@Test
	void testSetUsername() {
		User u = new User();
		u.setUsername("Bob123");
	}

	@Test
	void testSetEmail() {
		User u = new User();
		u.setEmail("Bob123@asu.edu");
	}

	@Test
	void testSetCode() {
		User u = new User();
		u.setCode("2134asf");
	}

	@Test
	void testSetTemppass() {
		User u = new User();
		u.setTemppass(124);
	}

	@Test
	void testGetFirstname() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getFirstname();
	}

	@Test
	void testGetMiddlename() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getMiddlename();
	}

	@Test
	void testGetLastname() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getLastname();
	}

	@Test
	void testGetPreferredname() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getPreferredname();
	}

	@Test
	void testGetUsername() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getUsername();
	}

	@Test
	void testGetEmail() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getEmail();
	}

	@Test
	void testGetCode() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getCode();
	}

	@Test
	void testGetTemppass() {
		User u = new User("Benjame","D", "Nelson", "Ben", "123456", "2345","34", 123);
		u.getTemppass();
	}

	@Test
	void testAddRole() {
		User u = new User();
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		u.addRole('e');
	}

	@Test
	void testDeleteRole() {
		User u = new User();
		u.deleteRole('a');
		u.deleteRole('i');
		u.deleteRole('s');
		u.deleteRole('e');
		
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		
		u.deleteRole('a');
		u.deleteRole('i');
		u.deleteRole('s');
	}

	@Test
	void testGetRoleList() {
		User u = new User();
		u.getRoleList();
	}

	@Test
	void testStorage() throws SQLException {	
			Storage s = new Storage();
	}

	@Test
	void testStorageString() throws SQLException {
		Storage s = new Storage("systmedb");
	}

	@Test
	void testLoginAttempt() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		Storage s = new Storage("systmedb1");
		s.loginAttempt("Bob123", "1234");
		
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		
	}

	@Test
	void testCheckLoginsExist() throws SQLException {
		Storage s = new Storage("systmedb2");
		s.checkLoginsExist();
		
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		s.checkLoginsExist();
		
	}

	@Test
	void testPrintTable1() throws SQLException {
		Storage s = new Storage("systmedb3");
		s.printTable1();
	}

	@Test
	void testPrintTable2() throws SQLException {
		Storage s = new Storage("systmedb4");
		s.printTable2();
	}

	@Test
	void testPrintTable3() throws SQLException {
		Storage s = new Storage("systmedb5");
		s.printTable1();
	}

	@Test
	void testRegisterUser() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
		Storage s = new Storage("systmedb6");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
	}

	@Test
	void testUpdateUser() throws SQLException {
		Storage s = new Storage("systmedb7");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.updateUser("bob123", "bob", "billy", "joe", "bobby", "bobby5@asu.edu");
	}

	@Test
	void testUpdateTempPass() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SQLException {
		Storage s = new Storage("systmedb8");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.updateTempPass("ben123","2345");
	}

	@Test
	void testUpdateMainPass() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SQLException {
		Storage s = new Storage("systmedb9");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.updateMainPass("ben123", "2345");
	}

	@Test
	void testFixSalt2() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SQLException {
		Storage s = new Storage("systmedb10");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.fixSalt2("ben123","2345");
		s.fixSalt2("ben13","2345");
		s.fixSalt2("ben123","45");
	}

	@Test
	void testIsTempPass() throws SQLException {
		Storage s = new Storage("systmedb11");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.isTempPass("ben123");
		s.isTempPass("be3");
	}

	@Test
	void testDoesUserExist() throws SQLException {
		Storage s = new Storage("systmedb12");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.doesUserExist("ben123");
		s.doesUserExist("ben3");
	}

	@Test
	void testRegisterLogin() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SQLException {
		Storage s = new Storage("systmedb13");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.registerLogin("ben123", "2345");
	}

	@Test
	void testUserSetup() throws SQLException {
		Storage s = new Storage("systmedb14");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.userSetup("ben123");
	}

	@Test
	void testDeleteTables() throws SQLException {
		Storage s = new Storage("systmedb15");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.deleteTables();
	}

	@Test
	void testRegisterOneTimeCode() throws SQLException {
		Storage s = new Storage("systmedb17");
		
		s.registerOneTimeCode("1234", "765", "a");
	}

	@Test
	void testDoesCodeExist() throws SQLException {
		Storage s = new Storage("systmedb18");
		
		s.registerOneTimeCode("1234", "765", "admin");
		
		s.doesCodeExist("1234");
		s.doesCodeExist("12");
	}

	@Test
	void testGetRolesFromCode() throws SQLException {
		Storage s = new Storage("systmedb19");
		s.registerOneTimeCode("1234", "765", "admin");
		
		s.getRolesFromCode("1234");
	}

	@Test
	void testGetTimeFromCode() throws SQLException {
		Storage s = new Storage("systmedb20");
		s.registerOneTimeCode("1234", "765", "admin");
		
		s.getTimeFromCode("1234");
	}

	@Test
	void testIsCodeAlreadyInUse() throws SQLException {
		Storage s = new Storage("systmedb21");
		s.registerOneTimeCode("1234", "765", "admin");
		
		s.isCodeAlreadyInUse("1234");
	}

	@Test
	void testDeleteUser() throws SQLException {
		Storage s = new Storage("systmedb22");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.deleteUser("ben123");
		s.deleteUser("bob");
	}

	@Test
	void testGetUser() throws SQLException {
		Storage s = new Storage("systmedb23");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.getUser("ben123");
	}

	@Test
	void testAddArticle() throws SQLException {
		Storage s = new Storage("systmedb24");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
	}

	@Test
	void testUpdateArticle() throws SQLException {
		Storage s = new Storage("systmedb25");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
	}

	
	@Test
	void testBackupArticles() throws IOException, SQLException {
		Storage s = new Storage("systmedb26");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		ArrayList<Article> articles = new ArrayList<Article>();
		articles.add(a);
		s.backupArticles(articles, "C:\\Users\\bdnel");
	}

	@Test
	void testRestoreArticles() throws IOException, SQLException {
		Storage s = new Storage("systmedb27");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		ArrayList<Article> articles = new ArrayList<Article>();
		articles.add(a);
		s.backupArticles(articles, "C:\\Users\\bdnel");
		s.restoreArticles("C:\\Users\\bdnel");
	}

	@Test
	void testListAllArticles() throws SQLException {
		Storage s = new Storage("systmedb28");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.listAllArticles();
	}

	@Test
	void testGetArticleByID() throws SQLException {
		Storage s = new Storage("systmedb29");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.getArticleByID(1);
	}

	@Test
	void testListArticlesByGroup() throws SQLException {
		Storage s = new Storage("systmedb30");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.listArticlesByGroup("db");
	}

	@Test
	void testSearchArticlesByTitle() throws SQLException {
		Storage s = new Storage("systmedb31");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.searchArticlesByTitle("Databases help");
	}

	@Test
	void testDeleteAllArticles() throws SQLException {
		Storage s = new Storage("systmedb32");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.deleteAllArticles();
	}

	@Test
	void testDeleteArticleByID() throws SQLException {
		Storage s = new Storage("systmedb33");
		ArrayList<String> references = new ArrayList<String>();
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.deleteArticleByID(1);
	}

	@Test
	void testListAllGroups() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SQLException {
		Storage s = new Storage("systmedb36");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		s.registerUser(u);
		s.createSpecialAccessGroup("testGroup", "ben123");
		s.listAllGroups();
	}

	@Test
	void testGetGroupsFromUsername() {
		
	}

	@Test
	void testGetUsersFromGroup() {
		
	}

	@Test
	void testCreateSpecialAccessGroup() throws SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Storage s = new Storage("systmedb34");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		s.registerUser(u);
		s.createSpecialAccessGroup("testGroup", "ben123");
	}

	@Test
	void testWithin24Hours() throws SQLException {
		Storage s = new Storage("systmedb35");
		s.within24Hours("2024-01-05");
	}
	
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
