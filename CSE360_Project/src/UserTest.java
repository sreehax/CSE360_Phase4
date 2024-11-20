import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

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

}
