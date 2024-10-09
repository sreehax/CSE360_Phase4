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
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_info (username TEXT PRIMARY KEY, firstname TEXT, middlename TEXT, lastname TEXT, preferredname TEXT, roles TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS logins (username TEXT PRIMARY KEY, passhash TEXT)");
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
		String strQuery = "INSERT INTO user_info (username, firstname, middlename, lastname, preferredname, roles) VALUES (?,?,?,?,?,?)";
		PreparedStatement prepared = this.conn.prepareStatement(strQuery);
		prepared.setString(1, user.getUsername());
		prepared.setString(2, user.getFirstname());
		prepared.setString(3, user.getMiddlename());
		prepared.setString(4, user.getLastname());
		prepared.setString(5, user.getPreferredname());
		prepared.setString(6, roles);
		
		// Execute the statement
		prepared.executeUpdate();
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
}
