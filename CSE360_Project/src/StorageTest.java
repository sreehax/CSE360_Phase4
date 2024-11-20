import static org.junit.jupiter.api.Assertions.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

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
		Storage s = new Storage("systmedb");
		s.loginAttempt("Bob123", "1234");
		
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
		
		s.loginAttempt("ben123", "2345");
	}

	@Test
	void testCheckLoginsExist() {
		fail("Not yet implemented");
	}

	@Test
	void testPrintTable1() {
		fail("Not yet implemented");
	}

	@Test
	void testPrintTable2() {
		fail("Not yet implemented");
	}

	@Test
	void testPrintTable3() {
		fail("Not yet implemented");
	}

	@Test
	void testRegisterUser() throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
		Storage s = new Storage("systmedb");
		User u = new User("Benjamin","D", "Nelson", "Ben", "ben123", "2345","34", 123);
		u.addRole('a');
		u.addRole('i');
		u.addRole('s');
		s.registerUser(u);
	}

	@Test
	void testUpdateUser() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateTempPass() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateMainPass() {
		fail("Not yet implemented");
	}

	@Test
	void testFixSalt2() {
		fail("Not yet implemented");
	}

	@Test
	void testIsTempPass() {
		fail("Not yet implemented");
	}

	@Test
	void testDoesUserExist() {
		fail("Not yet implemented");
	}

	@Test
	void testRegisterLogin() {
		fail("Not yet implemented");
	}

	@Test
	void testUserSetup() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteTables() {
		fail("Not yet implemented");
	}

	@Test
	void testRegisterOneTimeCode() {
		fail("Not yet implemented");
	}

	@Test
	void testDoesCodeExist() {
		fail("Not yet implemented");
	}

	@Test
	void testGetRolesFromCode() {
		fail("Not yet implemented");
	}

	@Test
	void testGetTimeFromCode() {
		fail("Not yet implemented");
	}

	@Test
	void testIsCodeAlreadyInUse() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteUser() {
		fail("Not yet implemented");
	}

	@Test
	void testGetUser() {
		fail("Not yet implemented");
	}

	@Test
	void testAddArticle() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateArticle() {
		fail("Not yet implemented");
	}

	@Test
	void testBackupArticles() {
		fail("Not yet implemented");
	}

	@Test
	void testRestoreArticles() {
		fail("Not yet implemented");
	}

	@Test
	void testListAllArticles() {
		fail("Not yet implemented");
	}

	@Test
	void testGetArticleByID() {
		fail("Not yet implemented");
	}

	@Test
	void testListArticlesByGroup() {
		fail("Not yet implemented");
	}

	@Test
	void testSearchArticlesByTitle() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteAllArticles() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteArticleByID() {
		fail("Not yet implemented");
	}

	@Test
	void testListAllGroups() {
		fail("Not yet implemented");
	}

	@Test
	void testGetGroupsFromUsername() {
		fail("Not yet implemented");
	}

	@Test
	void testGetUsersFromGroup() {
		fail("Not yet implemented");
	}

	@Test
	void testCreateSpecialAccessGroup() {
		fail("Not yet implemented");
	}

	@Test
	void testWithin24Hours() {
		fail("Not yet implemented");
	}

	@Test
	void testSelfTest() {
		fail("Not yet implemented");
	}

}
