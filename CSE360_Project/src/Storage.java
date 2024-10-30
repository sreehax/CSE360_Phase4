import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
/**
 * This class handles all interactions with the SQLite database for user management,
 * login authentication, and storing user information.
 * 
 * <p>The database contains three tables: `user_info` for storing user details, 
 * `logins` for storing login information with hashed passwords, and `onetimecode` for managing 
 * one time authentication codes.</p>
 */

public class Storage {
	private Connection conn;
	 /**
     * Constructor that makes a connection to the SQLite database.
     * It also ensures the necessary database schema is created if it does not already exist.
     * 
     * @throws SQLException if there is an error connecting to the database or executing SQL queries.
     */
	public Storage() throws SQLException {
		//the storage.db is saved in C:\Users\yourname
		String homedir = System.getProperty("user.home") + File.separatorChar;
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + homedir + "storage.db");
		
		// Create the database schema if it does not exist already
		Statement statement = this.conn.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_info (username TEXT PRIMARY KEY, firstname TEXT, middlename TEXT, lastname TEXT, preferredname TEXT, email TEXT, roles TEXT, code TEXT, temppass INT, temptime TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS logins (username TEXT PRIMARY KEY, passhash TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS onetimecode (code TEXT PRIMARY KEY, time TEXT, role TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS articles (title TEXT, body TEXT, refs TEXT, id INTEGER, grouping TEXT, description TEXT, keywords TEXT)");
	}
	/**
     * Attempts to log in by verifying the provided username and password against the stored password hash.
     *
     * @param username The username of the user logging in.
     * @param password The plaintext password entered by the user.
     * @return true if the login attempt is successful, false otherwise.
     * @throws SQLException if there is an error executing the SQL query.
     * @throws NoSuchAlgorithmException if the password hashing algorithm is unavailable.
     * @throws InvalidKeySpecException if the key specification for password hashing is invalid.
     */
	public boolean loginAttempt(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		// Get the pbkdf2 hash for the user
		String strQuery = "SELECT passhash FROM logins WHERE username = ?";
		
		PreparedStatement prepared = this.conn.prepareStatement(strQuery);
		prepared.setString(1, username);
		ResultSet res = prepared.executeQuery();
		
		String hash = null;
		while (res.next()) {
			hash = res.getString("passhash");
		}
		
		if (hash == null) {
			return false;
		}
		
		return PasswordHasher.verifyPassword(password, hash);
	}	
	 /**
     * Prints the contents of the `logins` table to the console and returns whether any users are present.
     * 
     * @return true if at least one user exists in the `logins` table, false otherwise.
     * @throws SQLException if there is an error executing the SQL query.
     */
	public boolean printTable() throws SQLException {
		String strQuery = "SELECT * from logins";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		int i = 0;
		System.out.println("----logins table contents----");
		while(rs.next()) {
			i++;
			System.out.println("user " + i + ": ");
			System.out.println(rs.getString("username"));
		}
		
		System.out.println("number of users in logins: " + i);
		if (i == 0) {
			return false;
		}
		else {
			return true;
		}
	}
	/**
     * Prints the contents of the `user_info` table to the console, including all user details.
     * 
     * @throws SQLException if there is an error executing the SQL query.
     */
	public void printTable2() throws SQLException {
		String strQuery = "SELECT * from user_info";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		int i = 0;
		System.out.println("----user_info table contents----");
		while(rs.next()) {
			i++;
			System.out.println("User \t\t#" + i + " ");
			System.out.println("Username:\t" + rs.getString("username"));
			System.out.println("First Name:\t" + rs.getString("firstname"));
			System.out.println("Middle Name:\t" + rs.getString("middlename"));
			System.out.println("Last Name:\t" + rs.getString("lastname"));
			System.out.println("Preferred Name:\t" + rs.getString("preferredname"));
			System.out.println("Roles:\t\t" + rs.getString("roles"));
			System.out.println("Invitation Code:\t" + rs.getString("code"));
		}
		
		System.out.println("number of users in user_info: " + i);
	}
	//print table of codes
	public void PrintTable3() throws SQLException {
		String strQuery = "SELECT * from onetimecode";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		int i = 0;
		System.out.println("----onetimecode table contents----");
		while(rs.next()) {
			i++;
			System.out.println("item " + i + ": ");
			System.out.println(rs.getString("code"));
			System.out.println(rs.getString("time"));
			System.out.println(rs.getString("role"));
		}
		
		System.out.println("number of users in user_info: " + i);
	}
	
	/**
     * Registers a new user in the `user_info` table based on the provided {@link User} object.
     * 
     * @param user The user to register.
     * @throws SQLException if there is an error executing the SQL insert query.
     */
	public void registerUser(User user) throws SQLException {
		// Encode roles for storage in database
		String roles = "";
		ArrayList<Role> roles_list = user.getRoleList();
		for (Role r : roles_list) {
			if (r == Role.ADMIN)
				roles += "A";
			else if (r == Role.INSTRUCTOR)
				roles += "I";
			else if (r == Role.STUDENT)
				roles += "S";
		}
		
		
		// Prepare the statement
		String strQuery = "INSERT INTO user_info (username, firstname, middlename, lastname, preferredname, email, roles, code) VALUES (?,?,?,?,?,?,?,?)";
		PreparedStatement prepared = this.conn.prepareStatement(strQuery);
		prepared.setString(1, user.getUsername());
		prepared.setString(2, user.getFirstname());
		prepared.setString(3, user.getMiddlename());
		prepared.setString(4, user.getLastname());
		prepared.setString(5, user.getPreferredname());
		prepared.setString(6, user.getEmail());
		prepared.setString(7, roles);
		prepared.setString(8,  user.getCode());
		
		// Execute the statement
		prepared.executeUpdate();
	}
	  /**
     * Updates the information of an existing user in the `user_info` table.
     * 
     * @param username The username of the user to update.
     * @param firstname The new first name.
     * @param middlename The new middle name.
     * @param lastname The new last name.
     * @param preferredname The new preferred name.
     * @param email The new email address.
     * @throws SQLException if there is an error executing the SQL update query.
     */
	public void updateUser(String username, String firstname, String middlename, String lastname, String preferredname, String email) throws SQLException {
		String sql = "UPDATE user_info SET firstname = ?, middlename = ?, lastname = ?, preferredname = ?, email = ? WHERE username = ?";
		
		
		PreparedStatement stmt = this.conn.prepareStatement(sql);
		stmt.setString(1, firstname);
		stmt.setString(2, middlename);
		stmt.setString(3, lastname);
		stmt.setString(4, preferredname);
		stmt.setString(5, email);
		stmt.setString(6, username);
		
		stmt.executeUpdate();
	}
	
	public void updateTempPass(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		//find user, change password, set flag
		String query1 = "UPDATE user_info SET temppass = 1, temptime = ? WHERE username = ?";
		String query2 = "UPDATE logins SET passhash = ? WHERE username = ?";
		
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String timeNow = time.format(fmt);
		
		PreparedStatement prep1 = this.conn.prepareStatement(query1);
		
		prep1.setString(1, timeNow);
		prep1.setString(2, username);
		prep1.executeUpdate();
		
		PreparedStatement prep2 = this.conn.prepareStatement(query2);
		prep2.setString(1, PasswordHasher.hashPassword(password));
		prep2.setString(2, username);
		prep2.executeUpdate();
	}
	
	public void updateMainPass(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
		//find user, change password, set flag
		String query1 = "UPDATE user_info SET temppass = NULL, temptime = NULL WHERE username = ?";
		String query2 = "UPDATE logins SET passhash = ? WHERE username = ?";
		
		PreparedStatement prep1 = this.conn.prepareStatement(query1);
		
		prep1.setString(1, username);
		prep1.executeUpdate();
		
		PreparedStatement prep2 = this.conn.prepareStatement(query2);
		prep2.setString(1, PasswordHasher.hashPassword(password));
		prep2.setString(2, username);
		prep2.executeUpdate();
	}
	
	public boolean isTempPass(String username) throws SQLException {
		String query1 = "SELECT temppass FROM user_info WHERE username = ?";
		
		PreparedStatement prep1 = this.conn.prepareStatement(query1);
		prep1.setString(1, username);
		
		ResultSet res = prep1.executeQuery();
		if (res.next()) {
			int temppass = res.getInt("temppass");
			return temppass == 1;
		}
		return false;
	}
	
	public boolean doesUserExist(String username) throws SQLException {
		String query = "SELECT * FROM user_info WHERE username = ?";
		PreparedStatement prep = this.conn.prepareStatement(query);
		
		prep.setString(1, username);
		ResultSet res = prep.executeQuery();
		if (res.next()) {
			return true;
		}
		return false;
	}
	
	 /**
     * Registers a login for the specified username with a hashed password in the `logins` table.
     * 
     * @param username The username to register.
     * @param password The plaintext password to hash and store.
     * @throws NoSuchAlgorithmException if the password hashing algorithm is unavailable.
     * @throws InvalidKeySpecException if the key specification for password hashing is invalid.
     * @throws SQLException if there is an error executing the SQL insert query.
     */
	public void registerLogin(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
		// Generate Password Hash
		String passhash = PasswordHasher.hashPassword(password);
		
		// Prepare the statement
		String strQuery = "INSERT INTO logins (username, passhash) VALUES (?,?)";
		PreparedStatement prepared = this.conn.prepareStatement(strQuery);
		prepared.setString(1, username);
		prepared.setString(2, passhash);
		
		// Execute the statement
		prepared.executeUpdate();
	}	
	 /**
     * Checks if the user setup is complete by verifying that the first name field is filled in the `user_info` table.
     * 
     * @param username The username to check.
     * @return true if the setup is incomplete, false if the user is properly set up.
     * @throws SQLException if there is an error executing the SQL query.
     */
	public boolean userSetup(String username) throws SQLException {
		String strQuery = "SELECT * FROM user_info WHERE username = ?";
		PreparedStatement pstmt = conn.prepareStatement(strQuery);
		pstmt.setString(1, username);
		ResultSet rs = pstmt.executeQuery();
		
		if (rs.getString("firstname").equals("")) {
			return true;
		}
		else {
			return false;
		}
	}
	 /**
     * Deletes the `user_info` and `logins` tables from the database.
     * 
     * @throws SQLException if there is an error executing the SQL drop queries.
     */
	public void deleteTables() throws SQLException{
		String sql = "DROP TABLE user_info";
		String sql2 = "DROP TABLE logins";
		Statement stmt = this.conn.createStatement();
		stmt.execute(sql);
		stmt.execute(sql2);
	}
	/**
	 * Registers a one-time code with the associated time and role in the database.
	 * 
	 * @param code The one-time code to register.
	 * @param time The time associated with the code.
	 * @param roles The roles associated with the code.
	 * @throws SQLException If a database access error occurs.
	 */
	public void registerOneTimeCode(String code, String time, String roles) throws SQLException {
		String strQuery = "INSERT INTO onetimecode (code, time, role) VALUES (?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(strQuery);
		pstmt.setString(1, code);
		pstmt.setString(2, time);
		pstmt.setString(3, roles);
		
		pstmt.executeUpdate();
	}
	/**
 * Checks if a one-time code exists in the "onetimecode" table.
 *
 * @param code the one-time code to check.
 * @return true if the code exists, false otherwise.
 * @throws SQLException if a database access error occurs.
 */
	public boolean doesCodeExist(String code) throws SQLException {
		String strQuery = "SELECT * FROM onetimecode WHERE code = ?";
		PreparedStatement prepared = conn.prepareStatement(strQuery);
		prepared.setString(1, code);
		
		ResultSet res = prepared.executeQuery();
		boolean found = false;
		while (res.next()) {
			found = true;
		}
		return found;
	}
	/**
 * Retrieves the role associated with a one-time code from the "onetimecode" table.
 *
 * @param code the one-time code to retrieve the role for.
 * @return the role associated with the given code, or an empty string if no role is found.
 * @throws SQLException if a database access error occurs.
 */
	public String getRolesFromCode(String code) throws SQLException {
		String strQuery = "SELECT role FROM onetimecode WHERE code = ?";
		PreparedStatement prepared = conn.prepareStatement(strQuery);
		prepared.setString(1, code);
		
		ResultSet res = prepared.executeQuery();
		String role = "";
		while (res.next()) {
			role = res.getString("role");
		}
		
		return role;
	}
	
	/**
 * Retrieves the time associated with a one-time code from the "onetimecode" table.
 *
 * @param code the one-time code to retrieve the role for.
 * @return the time that the one-time code was created, or an empty string if no role is found.
 * @throws SQLException if a database access error occurs.
 */
	public String getTimeFromCode(String code) throws SQLException {
		String strQuery = "SELECT time FROM onetimecode WHERE code = ?";
		PreparedStatement prepared = conn.prepareStatement(strQuery);
		prepared.setString(1, code);
		
		ResultSet res = prepared.executeQuery();
		String time = "";
		while (res.next()) {
			time = res.getString("time"); 
		}
		
		return time;
	}
	
	public boolean isCodeAlreadyInUse(String code) throws SQLException {
		String strQuery = "SELECT username FROM user_info WHERE code = ?";
		PreparedStatement prepared = conn.prepareStatement(strQuery);
		prepared.setString(1, code);
		
		ResultSet res = prepared.executeQuery();
		while (res.next()) {
			return true;
		}
		return false;
	}
	
	public void deleteUser(String username) throws SQLException {
		String query1 = "DELETE FROM logins WHERE username = ?";
		String query2 = "DELETE FROM user_info WHERE username = ?";
		
		int updates = 0;
		
		PreparedStatement prep1 = conn.prepareStatement(query1);
		prep1.setString(1, username);
		updates += prep1.executeUpdate();
		
		PreparedStatement prep2 = conn.prepareStatement(query2);
		prep2.setString(1, username);
		updates += prep2.executeUpdate();
		
		if (updates == 0) {
			System.out.println("User to delete does not exist");
		}
	}
	
	public User getUser(String username) throws SQLException {
		String query = "SELECT * FROM user_info WHERE username = ?";
		PreparedStatement prepared = conn.prepareStatement(query);
		prepared.setString(1, username);
		
		ResultSet res = prepared.executeQuery();
		
		while (res.next()) {
			User user = new User();
			user.setFirstname(res.getString("firstname"));
			user.setMiddlename(res.getString("middlename"));
			user.setLastname(res.getString("lastname"));
			user.setPreferredname(res.getString("preferredname"));
			user.setEmail(res.getString("email"));
			user.setUsername(username);
			user.setCode(res.getString("code"));
			for (char c : res.getString("roles").toCharArray()) {
				if (c == 'A')
					user.addRole('a');
				if (c == 'I')
					user.addRole('i');
				if (c == 'S')
					user.addRole('s');
			}
			
			return user;
		}
		throw new SQLException("User Not Found");
	}
	
	 /**
     * Checks whether the provided time is within the last 24 hours.
     *
     * @param time the time in "yyyy-MM-dd'T'HH:mm:ss" format.
     * @return true if the time is within 24 hours; false otherwise.
     */
	public boolean within24Hours(String time) {
        LocalDateTime currentTime = LocalDateTime.now();
        // Parse the provided time and calculate the time difference
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime inputTime = LocalDateTime.parse(time, fmt);
        long secondsElapsed = ChronoUnit.SECONDS.between(inputTime, currentTime);
        return secondsElapsed < 24 * 60 * 60;
    }
}
