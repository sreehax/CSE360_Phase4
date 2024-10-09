import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Storage {
	private Connection conn;
	
	public Storage() throws SQLException {
		//the storage.db is saved in C:\Users\yourname
		String homedir = System.getProperty("user.home") + File.separatorChar;
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + homedir + "storage.db");
		
		// Create the database schema if it does not exist already
		Statement statement = this.conn.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_info (username TEXT PRIMARY KEY, firstname TEXT, middlename TEXT, lastname TEXT, preferredname TEXT, email TEXT, roles TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS logins (username TEXT PRIMARY KEY, passhash TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS onetimecode (code TEXT PRIMARY KEY, time TEXT, role TEXT)");
	}
	
	//returns boolean flag whether login was successful or not
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
	
	//print table of logins, returns true if at least 1 user exist.
	public boolean printTable() throws SQLException {
		String strQuery = "SELECT * from logins";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		int i = 0;
		System.out.println("----logins table contents----");
		while(rs.next()) {
			i++;
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
	//print table of user_info
	public void printTable2() throws SQLException {
		String strQuery = "SELECT * from user_info";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		int i = 0;
		System.out.println("----user_info table contents----");
		while(rs.next()) {
			i++;
			System.out.println(rs.getString("username"));
			System.out.println(rs.getString("firstname"));
			System.out.println(rs.getString("middlename"));
			System.out.println(rs.getString("lastname"));
			System.out.println(rs.getString("preferredname"));
			System.out.println(rs.getString("roles"));
		}
		
		System.out.println("number of users in user_info: " + i);
	}
	
	//creates user in the database based off of the class of user
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
		String strQuery = "INSERT INTO user_info (username, firstname, middlename, lastname, preferredname, email, roles) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement prepared = this.conn.prepareStatement(strQuery);
		prepared.setString(1, user.getUsername());
		prepared.setString(2, user.getFirstname());
		prepared.setString(3, user.getMiddlename());
		prepared.setString(4, user.getLastname());
		prepared.setString(5, user.getPreferredname());
		prepared.setString(6, user.getEmail());
		prepared.setString(7, roles);
		
		// Execute the statement
		prepared.executeUpdate();
	}
	
	//updates user information
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
	
	//
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
	
	//check if user is set up properly, return true if it is not set up, return false if it is set up.
	public boolean userSetup(String username) throws SQLException {
		String strQuery = "SELECT * FROM user_info WHERE username = '" + username + "'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		if (rs.getString("firstname").equals("")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void deleteTables() throws SQLException{
		String sql = "DROP TABLE user_info";
		String sql2 = "DROP TABLE logins";
		Statement stmt = this.conn.createStatement();
		stmt.execute(sql);
		stmt.execute(sql2);
	}
	
	
	
}
