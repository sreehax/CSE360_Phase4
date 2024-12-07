import static org.junit.jupiter.api.Assertions.*;

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

class StorageTest {

	
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
		Storage s = new Storage("systmedb6");//Create the test database
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123); //Create a user to add
		u.addRole('a'); //Add each role to the user
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		User m = new User("B","D", "Nel", "Ren", "n23", "2345","34", 123); //Create a second user
		m.addRole('s');
		s.registerUser(m);
		
		//s.printTable2(); //Print the user table to show results
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
		Storage s = new Storage("systmedb19"); //create database
		s.registerOneTimeCode("1234", "765", "admin");  //create one time code
		s.registerOneTimeCode("2234", "7765", "instructor");
		
		System.out.println("Code 1234's Role: "+s.getRolesFromCode("1234"));//print roles from the code
		System.out.println("Code 2234's Role: "+s.getRolesFromCode("2234"));
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
		
		Storage s = new Storage("systmedb24"); //Create Test Database
		
		ArrayList<String> references = new ArrayList<String>(); //Create Article to Add
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		Article b = new Article ("Databases help", "fdgfsdg", references, 0, "Step 1", "db","helps you", keywords);
		
		s.addArticle(a);//adds articles and prints results
		s.addArticle(b);
		s.listAllArticles();
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
		
		Article b = new Article ("Databases help 2.0", "fdSDFDSGSDFgfsdg", references, 1, "Step 4", "db","helped you", keywords);
		s.updateArticle(b);
		
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
		s.backupArticles(articles, "C:\\");
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
		Storage s = new Storage("systmedb29");//Create a test database
		
		ArrayList<String> references = new ArrayList<String>(); //Create an article for testing
		ArrayList<String> keywords = new ArrayList<String>();
		references.add("wiki");
		keywords.add("help");
		Article a = new Article ("Databases help", "fdgfsdg", references, 1, "Step 1", "db","helps you", keywords);
		s.addArticle(a);
		
		s.getArticleByID(1); //Search for an article that exists
		s.getArticleByID(2); //Search for an article that does not exist
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
		
		Storage s = new Storage(); //load storage
		s.createSpecialAccessGroup("testGroup", "tester"); //create group with invalid admin
		s.createSpecialAccessGroup("testSecretGroup", "admin");  //create group with valid admin
	}

	@Test
	void testWithin24Hours() throws SQLException {
		Storage s = new Storage("systmedb35");
		s.within24Hours("2024-01-05");
	}


}
