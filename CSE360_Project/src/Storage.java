import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.sqlite.SQLiteConfig;
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
		this("storage3.db");
	}
	
	// Constructor to create an alternate database (used for self-test)
	public Storage(String dbName) throws SQLException {
		//the database is saved in your home directory
		String homedir = System.getProperty("user.home") + File.separatorChar;
		
		// Enforce Foreign Keys
		SQLiteConfig config = new SQLiteConfig();
		config.enforceForeignKeys(true);
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + homedir + dbName, config.toProperties());
		
		// Create the database schema if it does not exist already
		Statement statement = this.conn.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_info (u_user TEXT PRIMARY KEY, firstname TEXT, middlename TEXT, lastname TEXT, preferredname TEXT, email TEXT, roles TEXT, code TEXT, temppass INT, temptime TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS logins (l_user TEXT PRIMARY KEY, passhash TEXT, privkey BLOB, pubkey BLOB, salt2 BLOB, FOREIGN KEY(l_user) REFERENCES user_info(u_user) ON DELETE CASCADE)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS onetimecode (code TEXT PRIMARY KEY, time TEXT, role TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS articles (title TEXT, body TEXT, refs TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT, header TEXT, grouping TEXT, description TEXT, keywords TEXT, isSecure INTEGER, article_group_id INTEGER)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS special_groups (group_id INTEGER PRIMARY KEY, group_name TEXT UNIQUE)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS special_access (s_user TEXT, access_group_id INTEGER, group_key BLOB, FOREIGN KEY(access_group_id) REFERENCES special_groups(group_id) ON DELETE CASCADE, FOREIGN KEY(s_user) REFERENCES user_info(u_user) ON DELETE CASCADE)");
//		statement.executeUpdate("CREATE TABLE IF NOT EXISTS special_articles (article_id INTEGER, article_group_id INTEGER, FOREIGN KEY(article_group_id) REFERENCES special_groups(group_id) ON DELETE CASCADE, FOREIGN KEY(article_id) REFERENCES articles(id) ON DELETE CASCADE, PRIMARY KEY (article_id, article_group_id))");
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
		String strQuery = "SELECT passhash FROM logins WHERE l_user = ?";
		
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
     * Checks if `logins` table contain more than one registered account.
     * 
     * @return true if at least one user exists in the `logins` table, false otherwise.
     * @throws SQLException if there is an error executing the SQL query.
     */
	public boolean checkLoginsExist() throws SQLException {
		int size = 0;
		String strQuery = "SELECT count(*) AS row_count FROM logins";
		
		try {
			PreparedStatement stmt = this.conn.prepareStatement(strQuery);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
				size = rs.getInt("row_count");
			}
		}
		catch (Exception e) {
			System.out.println("checkLoginsExist failed");
		}
		
		
		if (size == 0) {
			return false;
		}
		else {
			return true;
		}
	}
	 /**
     * Prints the contents of the `logins` table to the console and returns whether any users are present.
     * 
     * @return true if at least one user exists in the `logins` table, false otherwise.
     * @throws SQLException if there is an error executing the SQL query.
     */
	public void printTable1() throws SQLException {
		String strQuery = "SELECT * from logins";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(strQuery);
		
		int i = 0;
		System.out.println("----logins table contents----");
		while(rs.next()) {
			i++;
			System.out.println("user " + i + ": ");
			System.out.println(rs.getString("l_user"));
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
			System.out.println("Username:\t" + rs.getString("u_user"));
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
		String strQuery = "INSERT INTO user_info (u_user, firstname, middlename, lastname, preferredname, email, roles, code) VALUES (?,?,?,?,?,?,?,?)";
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
		String sql = "UPDATE user_info SET firstname = ?, middlename = ?, lastname = ?, preferredname = ?, email = ? WHERE u_user = ?";
		
		
		PreparedStatement stmt = this.conn.prepareStatement(sql);
		stmt.setString(1, firstname);
		stmt.setString(2, middlename);
		stmt.setString(3, lastname);
		stmt.setString(4, preferredname);
		stmt.setString(5, email);
		stmt.setString(6, username);
		
		stmt.executeUpdate();
	}
	
	public void updateTempPass(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		//find user, change password, set flag
		String query1 = "UPDATE user_info SET temppass = 1, temptime = ? WHERE u_user = ?";
		String query3 = "UPDATE logins SET passhash = ? WHERE l_user = ?";
		
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String timeNow = time.format(fmt);
		
		PreparedStatement prep1 = this.conn.prepareStatement(query1);
		
		prep1.setString(1, timeNow);
		prep1.setString(2, username);
		prep1.executeUpdate();
		
		PreparedStatement prep2 = this.conn.prepareStatement(query3);
		prep2.setString(1, PasswordHasher.hashPassword(password));
		prep2.setString(2, username);
		prep2.executeUpdate();
		
		// fixup special group issues
		this.fixSalt2(username, password);
	}
	
	public void updateMainPass(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		//find user, change password, set flag
		String query1 = "UPDATE user_info SET temppass = NULL, temptime = NULL WHERE u_user = ?";
		String query2 = "UPDATE logins SET passhash = ? WHERE l_user = ?";
		
		PreparedStatement prep1 = this.conn.prepareStatement(query1);
		
		prep1.setString(1, username);
		prep1.executeUpdate();
		
		PreparedStatement prep2 = this.conn.prepareStatement(query2);
		prep2.setString(1, PasswordHasher.hashPassword(password));
		prep2.setString(2, username);
		prep2.executeUpdate();
		
		// fixup special group issues
		this.fixSalt2(username, password);
	}
	
	public void fixSalt2(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, SQLException {
		String query1 = "DELETE FROM special_access WHERE s_user = ?";
		String query2 = "UPDATE logins SET privkey = ?, pubkey = ?, salt2 = ? WHERE l_user = ?";
		
		
		// Generate the second salt and symmetric encryption key
		KeyPair kp = RSAEncryption.genKeyPair();
		byte[] salt2 = PasswordHasher.genSalt();
		String key_string = PasswordHasher.hashPassword(password, salt2);
		SymmetricEncryption senc = new SymmetricEncryption(key_string);
		
		// Encrypt the RSA private key so that only the user can decrypt it
		byte[] encryptedPrivKey = senc.encrypt(RSAEncryption.getPrivkey(kp).getEncoded());
		byte[] pubkey = RSAEncryption.getPubkey(kp).getEncoded();
		
		PreparedStatement prep1 = this.conn.prepareStatement(query1);
		prep1.setString(1, username);
		prep1.executeUpdate();
		
		PreparedStatement prep2 = this.conn.prepareStatement(query2);
		prep2.setBytes(1, encryptedPrivKey);
		prep2.setBytes(2, pubkey);
		prep2.setBytes(3, salt2);
		prep2.setString(4, username);
		prep2.executeUpdate();
	}
	
	public boolean isTempPass(String username) throws SQLException {
		String query1 = "SELECT temppass FROM user_info WHERE u_user = ?";
		
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
		String query = "SELECT * FROM user_info WHERE u_user = ?";
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
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
     */
	public void registerLogin(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// Generate Password Hash and RSA keypair
		String passhash = PasswordHasher.hashPassword(password);
		KeyPair kp = RSAEncryption.genKeyPair();
		
		// Generate the second salt and symmetric encryption key
		byte[] salt2 = PasswordHasher.genSalt();
		String key_string = PasswordHasher.hashPassword(password, salt2);
		SymmetricEncryption senc = new SymmetricEncryption(key_string);
		
		// Encrypt the RSA private key so that only the user can decrypt it
		byte[] encryptedPrivKey = senc.encrypt(RSAEncryption.getPrivkey(kp).getEncoded());
		byte[] pubkey = RSAEncryption.getPubkey(kp).getEncoded();
		
		// Prepare the statement
		String strQuery = "INSERT INTO logins (l_user, passhash, privkey, pubkey, salt2) VALUES (?,?,?,?,?)";
		PreparedStatement prepared = this.conn.prepareStatement(strQuery);
		prepared.setString(1, username);
		prepared.setString(2, passhash);
		prepared.setBytes(3, encryptedPrivKey);
		prepared.setBytes(4, pubkey);
		prepared.setBytes(5, salt2);
		
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
		String strQuery = "SELECT * FROM user_info WHERE u_user = ?";
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
	  /**
     * Checks if a one-time code is already associated with a user in the "user_info" table.
     *
     * @param code the one-time code to check
     * @return true if the code is already in use, false otherwise
     * @throws SQLException if a database access error occurs
     */
	public boolean isCodeAlreadyInUse(String code) throws SQLException {
		String strQuery = "SELECT u_user FROM user_info WHERE code = ?";
		PreparedStatement prepared = conn.prepareStatement(strQuery);
		prepared.setString(1, code);
		
		ResultSet res = prepared.executeQuery();
		while (res.next()) {
			return true;
		}
		return false;
	}
	 /**
     * Deletes a user from the "logins" and "user_info" tables based on the username.
     *
     * @param username the username of the user to delete
     * @throws SQLException if a database access error occurs
     */
	public void deleteUser(String username) throws SQLException {
		// thanks to ON DELETE CASCADE, the database removes the old entry from logins
		String query2 = "DELETE FROM user_info WHERE u_user = ?";
				
		PreparedStatement prep2 = conn.prepareStatement(query2);
		prep2.setString(1, username);
		int updates = prep2.executeUpdate();
		
		if (updates == 0) {
			System.out.println("User to delete does not exist");
		}
	}
	   /**
     * Retrieves a User object populated with user details based on the provided username.
     *
     * @param username the username of the user to retrieve
     * @return the User object containing user details
     * @throws SQLException if a database access error occurs or if the user is not found
     */
	public User getUser(String username) throws SQLException {
		String query = "SELECT * FROM user_info WHERE u_user = ?";
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
     * Inserts a new Article into the database.
     *
     * @param a the Article to insert
     * @throws SQLException if a database access error occurs
     */
	public void addArticle(Article a) throws SQLException {
		String update = "INSERT INTO articles (title, body, refs, id, header, grouping, description, keywords, isSecure) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement prep = conn.prepareStatement(update);
		String references = a.getReferencesStr();
		String keywords = a.getKeywordsStr();
		int id = a.getID();
		
		prep.setString(1, a.getTitle());
		prep.setString(2, a.getBody());
		prep.setString(3, references);
		if (id > 0) {
			prep.setInt(4, a.getID());
		} else {
			prep.setNull(4, Types.NULL);
		}
		prep.setString(5, a.getHeader());
		prep.setString(6, a.getGrouping());
		prep.setString(7, a.getDescription());
		prep.setString(8, keywords);
		prep.setInt(9, 0);
		
		prep.executeUpdate();
	}
	
	 /**
     * Inserts a new Article into the database.
     *
     * @param a the Article to insert
     * @throws SQLException if a database access error occurs
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
	 * @throws InvalidAlgorithmParameterException 
     */
	public boolean addSecureArticle(Article a, String groupname, String youruser, byte[] privkey_bytes) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		// using your privkey, decrypt the group key
		String query0 = "SELECT group_key FROM special_access, special_groups WHERE group_id = access_group_id AND s_user = ? AND group_name = ?";
		PreparedStatement s0 = this.conn.prepareStatement(query0);
		s0.setString(1, youruser);
		s0.setString(2, groupname);
		ResultSet rs0 = s0.executeQuery();
		byte[] pre_groupkey;
		if (!rs0.next()) {
			System.out.println("Could not find user " + youruser + "!");
			return false;
		}
		pre_groupkey = rs0.getBytes("group_key");
		PrivateKey privkey = RSAEncryption.getPrivKey(privkey_bytes);
		byte[] groupkey = RSAEncryption.decryptBytes(privkey, pre_groupkey);
		
		String bruh = "SELECT group_id FROM special_groups WHERE group_name = ? LIMIT 1";
		PreparedStatement b = conn.prepareStatement(bruh);
		b.setString(1, groupname);
		ResultSet stupid = b.executeQuery();
		if (!stupid.next()) {
			System.out.println("Could not find group " + groupname + "!");
			return false;
		}
		int group_id = stupid.getInt("group_id");
		
		// encrypt the body with ChaCha20-Poly1305
		SymmetricEncryption senc = new SymmetricEncryption(groupkey);
		byte[] body_encrypted = senc.encrypt(a.getBody());
		Encoder enc = Base64.getEncoder();
		
		
		String update = "INSERT INTO articles (title, body, refs, id, header, grouping, description, keywords, isSecure, article_group_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement prep = conn.prepareStatement(update);
		String references = a.getReferencesStr();
		String keywords = a.getKeywordsStr();
		int id = a.getID();
		
		prep.setString(1, a.getTitle());
		prep.setString(2, enc.encodeToString(body_encrypted));
		prep.setString(3, references);
		if (id > 0) {
			prep.setInt(4, a.getID());
		} else {
			prep.setNull(4, Types.NULL);
		}
		prep.setString(5, a.getHeader());
		prep.setString(6, a.getGrouping());
		prep.setString(7, a.getDescription());
		prep.setString(8, keywords);
		prep.setInt(9, 1);
		prep.setInt(10, group_id);
		
		prep.executeUpdate();
		
		return true;
	}
	  /**
     * Updates an existing Article in the database using its ID.
     *
     * @param a the Article with updated details
     * @throws SQLException if a database access error occurs
     */
	public void updateArticle(Article a) throws SQLException {
		String update = "UPDATE articles SET title = ?, body = ?, refs = ?, header = ?, grouping = ?, description = ?, keywords = ? WHERE id = ?";
		PreparedStatement prep = conn.prepareStatement(update);
		String references = a.getReferencesStr();
		String keywords = a.getKeywordsStr();
		int id = a.getID();
		
		prep.setString(1, a.getTitle());
		prep.setString(2, a.getBody());
		prep.setString(3, references);
		prep.setString(4, a.getHeader());
		prep.setString(5, a.getGrouping());
		prep.setString(6, a.getDescription());
		prep.setString(7, keywords);
		prep.setInt(8, id);
		
		prep.executeUpdate();
	}
	
	  /**
     * Helper method to convert a ResultSet into a list of Article objects.
     *
     * @param rs the ResultSet containing article data
     * @return a list of Article objects
     * @throws SQLException if a database access error occurs
     */
	private ArrayList<Article> consolidateArticles(ResultSet rs) throws SQLException {
		ArrayList<Article> ret = new ArrayList<Article>();
		while (rs.next()) {
			String title = rs.getString("title");
			String body = rs.getString("body");
			String[] references_arr = rs.getString("refs").split(", ");
			int id = rs.getInt("id");
			String header = rs.getString("header");
			String grouping = rs.getString("grouping");
			String description = rs.getString("description");
			String[] keywords_arr = rs.getString("keywords").split(", ");
			ArrayList<String> references = new ArrayList<String>();
			ArrayList<String> keywords = new ArrayList<String>();
			for (String ref : references_arr) {
				references.add(ref);
			}
			for (String keyword : keywords_arr) {
				keywords.add(keyword);
			}
			
			ret.add(new Article(title, body, references, id, header, grouping, description, keywords));
		}
		
		return ret;
	}
	
	    /**
     * Serializes a list of articles to a specified file.
     *
     * @param articles the list of articles to backup
     * @param path the file path to write to
     * @return the absolute path of the backup file
     * @throws IOException if an I/O error occurs
     */
	public String backupArticles(ArrayList<Article> articles, String path) throws IOException {
		File file = new File(path);
		FileWriter fw = new FileWriter(path);
		BufferedWriter bw = new BufferedWriter(fw);
		int count = articles.size();
		bw.write(Integer.toString(count));
		bw.newLine();
		
		// Body and Description can be multiple lines, so base64 encode it
		Encoder enc = Base64.getEncoder();
		for (Article a : articles) {
			bw.write(a.getTitle());
			bw.newLine();
			bw.write(enc.encodeToString(a.getBody().getBytes("UTF-8")));
			bw.newLine();
			bw.write(a.getReferencesStr());
			bw.newLine();
			bw.write(Integer.toString(a.getID()));
			bw.newLine();
			bw.write(a.getHeader());
			bw.newLine();
			bw.write(a.getGrouping());
			bw.newLine();
			bw.write(enc.encodeToString(a.getDescription().getBytes("UTF-8")));
			bw.newLine();
			bw.write(a.getKeywordsStr());
			bw.newLine();
		}
		
		bw.close();
		fw.close();
		
		return file.getAbsolutePath();
	}
	
	    /**
     * Deserializes a list of articles from a specified file.
     *
     * @param path the file path to read from
     * @return a list of Article objects
     * @throws IOException if an I/O error occurs
     */
	public ArrayList<Article> restoreArticles(String path) throws IOException {
		ArrayList<Article> ret = new ArrayList<Article>();
		BufferedReader reader = new BufferedReader(new FileReader(path));
		
		int count = Integer.parseInt(reader.readLine());
		
		// Body and Description can be multiple lines, so base64 decode them
		Decoder dec = Base64.getDecoder();
		for(int i = 0; i < count; i++) {
			String title = reader.readLine();
			String body = new String(dec.decode(reader.readLine()), StandardCharsets.UTF_8);
			String[] references_arr = reader.readLine().split(", ");
			int id = Integer.parseInt(reader.readLine());
			String header = reader.readLine();
			String grouping = reader.readLine();
			String description = new String(dec.decode(reader.readLine()), StandardCharsets.UTF_8);
			String[] keywords_arr = reader.readLine().split(", ");
			ArrayList<String> references = new ArrayList<String>();
			ArrayList<String> keywords = new ArrayList<String>();
			for (String ref : references_arr) {
				references.add(ref);
			}
			for (String keyword : keywords_arr) {
				keywords.add(keyword);
			}
			
			ret.add(new Article(title, body, references, id, header, grouping, description, keywords));
		}
		
		reader.close();
		return ret;
	}
	
	    /**
     * Retrieves all articles from the "articles" table.
     *
     * @return a list of Article objects containing all articles in the database
     * @throws SQLException if a database access error occurs
     */
	public ArrayList<Article> listAllArticles() throws SQLException {
		String query = "SELECT * FROM articles WHERE isSecure = 0";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		return consolidateArticles(rs);
	}
	
	  /**
     * Retrieves an article by its ID.
     *
     * @param id the ID of the article to retrieve
     * @return the Article object if found, or null if no article with the specified ID exists
     * @throws SQLException if a database access error occurs
     */
	public Article getArticleByID(int id) throws SQLException {
		String query = "SELECT * FROM articles WHERE id = ? AND isSecure = 0";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		
		ArrayList<Article> got = consolidateArticles(rs);
		if (got.size() < 1) {
			return null;
		}
		return got.get(0);
	}
	
	    /**
     * Retrieves articles by their grouping attribute.
     *
     * @param group the grouping category to filter articles by
     * @return a list of Article objects matching the specified group
     * @throws SQLException if a database access error occurs
     */
	public ArrayList<Article> listArticlesByGroup(String group) throws SQLException {
		String query = "SELECT * FROM articles WHERE grouping LIKE ? AND isSecure = 0";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, "%" + group + "%");
		ResultSet rs = prep.executeQuery();
		
		return consolidateArticles(rs);
	}
	
	  /**
     * Searches for articles by title.
     *
     * @param title the title or partial title to search for
     * @return a list of Article objects whose titles contain the specified search term
     * @throws SQLException if a database access error occurs
     */
	public ArrayList<Article> searchArticlesByTitle(String title) throws SQLException {
		String query = "SELECT * FROM articles WHERE title LIKE ? AND isSecure = 0";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, "%" + title + "%");
		ResultSet rs = prep.executeQuery();
		return consolidateArticles(rs);
	}
	
	/**
     * Deletes all articles from the "articles" table.
     *
     * @throws SQLException if a database access error occurs
     */
	public void deleteAllArticles() throws SQLException {
		String update = "DELETE FROM articles";
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate(update);
	}
	
	 /**
     * Deletes an article by its ID.
     *
     * @param id the ID of the article to delete
     * @return the number of rows affected (1 if the article was deleted, 0 if no article was found)
     * @throws SQLException if a database access error occurs
     */
	public int deleteArticleByID(int id) throws SQLException {
		String update = "DELETE FROM articles WHERE id = ?";
		PreparedStatement prep = this.conn.prepareStatement(update);
		prep.setInt(1, id);
		int affected = prep.executeUpdate();
		return affected;
	}
	
	// special access group stuff
	public ArrayList<String> listAllGroups() throws SQLException {
		String query = "SELECT group_name FROM special_groups";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		ArrayList<String> ret = new ArrayList<String>();
		while (rs.next()) {
			ret.add(rs.getString("group_name"));
		}
		
		return ret;
	}
	
	public ArrayList<String> getGroupsFromUsername(String username) throws SQLException {
		// implicit JOIN
		String query1 = "SELECT group_name FROM special_access, special_groups WHERE group_id = access_group_id AND s_user = ?";
		PreparedStatement prep = this.conn.prepareStatement(query1);
		prep.setString(1, username);
		ResultSet rs = prep.executeQuery();
		
		ArrayList<String> ret = new ArrayList<String>();
		while (rs.next()) {
			ret.add(rs.getString("group_name"));
		}
		
		return ret;
	}
	
	public ArrayList<String> getUsersFromGroup(String groupname) throws SQLException {
		// implicit JOIN
		String query = "SELECT s_user FROM special_access, special_groups WHERE group_id = access_group_id AND group_name = ?";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, groupname);
		ResultSet rs = prep.executeQuery();
		
		ArrayList<String> ret = new ArrayList<String>();
		while (rs.next()) {
			ret.add(rs.getString("s_user"));
		}
		
		return ret;
	}
	
	public boolean createSpecialAccessGroup(String groupname, String admin) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// 1. Add the special_groups entry
		// 2. Generate an encryption key to be used for the group
		// 3. Encrypt the group key with RSA for the public key of the admin user
		// 4. store this special_access record
		
		byte[] group_key_raw = new byte[32];
		SecureRandom random = new SecureRandom();
		random.nextBytes(group_key_raw);
		
		String query2 = "SELECT pubkey FROM logins WHERE l_user = ?";
		PreparedStatement prep2 = this.conn.prepareStatement(query2);
		prep2.setString(1, admin);
		ResultSet rs = prep2.executeQuery();
		byte[] pubkey_raw;
		if (!rs.next()) {
			System.out.println("Could not find user " + admin + "!");
			return false;
		}
		pubkey_raw = rs.getBytes("pubkey");
		PublicKey pubkey = RSAEncryption.getPubKey(pubkey_raw);
		byte[] group_key_enc = RSAEncryption.encryptFor(pubkey, group_key_raw);
		
		String query1 = "INSERT INTO special_groups (group_name) VALUES (?)";
		PreparedStatement stmt1 = this.conn.prepareStatement(query1);
		stmt1.setString(1, groupname);
		stmt1.executeUpdate();

				
		String query15 = "SELECT last_insert_rowid() AS the_id";
		Statement stmt15 = this.conn.createStatement();
		ResultSet rs0 = stmt15.executeQuery(query15);
		if (!rs0.next()) {
			System.out.println("Group creation failed!");
			return false;
		}
		int group_id = rs0.getInt("the_id");
		
		String query3 = "INSERT INTO special_access (s_user, access_group_id, group_key) VALUES (?,?,?)";
		PreparedStatement prep3 = this.conn.prepareStatement(query3);
		prep3.setString(1, admin);
		prep3.setInt(2, group_id);
		prep3.setBytes(3, group_key_enc);
		prep3.executeUpdate();
		return true;
	}
	
	public boolean addUserToSpecialAccessGroup(String groupname, String user_to_add, String youruser, byte[] privkey_bytes) throws SQLException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		// using your privkey, decrypt the group key
		String query0 = "SELECT group_key FROM special_access, special_groups WHERE group_id = access_group_id AND s_user = ? AND group_name = ?";
		PreparedStatement s0 = this.conn.prepareStatement(query0);
		s0.setString(1, youruser);
		s0.setString(2, groupname);
		ResultSet rs0 = s0.executeQuery();
		byte[] pre_groupkey;
		if (!rs0.next()) {
			System.out.println("Could not find user " + youruser + "!");
			return false;
		}
		pre_groupkey = rs0.getBytes("group_key");
		PrivateKey privkey = RSAEncryption.getPrivKey(privkey_bytes);
		byte[] groupkey = RSAEncryption.decryptBytes(privkey, pre_groupkey);
		
		// get the pubkey of the new user, and encrypt the groupkey for them
		String query1 = "SELECT pubkey FROM logins WHERE l_user = ?";
		PreparedStatement s1 = this.conn.prepareStatement(query1);
		s1.setString(1, user_to_add);
		ResultSet rs = s1.executeQuery();
		byte[] pubkey_raw;
		if (!rs.next()) {
			System.out.println("Could not find user " + user_to_add + "!");
			return false;
		}
		pubkey_raw = rs.getBytes("pubkey");
		PublicKey pubkey = RSAEncryption.getPubKey(pubkey_raw);
		byte[] group_key_enc = RSAEncryption.encryptFor(pubkey, groupkey);
		
		// add a record for the new user to access the groupkey
		String query2 = "INSERT INTO special_access (s_user, access_group_id, group_key) SELECT ?, group_id, ? FROM special_groups WHERE group_name = ?";
		PreparedStatement s2 = this.conn.prepareStatement(query2);
		s2.setString(1, user_to_add);
		s2.setBytes(2, group_key_enc);
		s2.setString(3, groupname);
		s2.executeUpdate();
		return true;
	}
	
	// It is up to the caller to make sure that you are not trying to remove yourself!
	public boolean removeUserFromSpecialAccessGroup(String groupname, String user_to_remove) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		// Delete the special_access record
		// I guess an explicit join is preferred
		String query = "DELETE FROM special_access WHERE s_user = ? AND access_group_id = (SELECT group_id FROM special_groups WHERE group_name = ? LIMIT 1)";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, user_to_remove);
		prep.setString(2, groupname);
		int affected = prep.executeUpdate();
		
		return affected != 0;
	}
	
	public byte[] getPrivkey(String username, String password) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// get salt2
		String query = "SELECT salt2, privkey FROM logins WHERE l_user = ?";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, username);
		ResultSet rs = prep.executeQuery();
		if (!rs.next()) {
			System.out.println("Could not find user " + username + "!");
			return null;
		}
		byte[] salt2 = rs.getBytes("salt2");
		byte[] privkey_enc = rs.getBytes("privkey");
		
		// Compute passhash2 for symmetric encryption
		String passhash2 = PasswordHasher.hashPassword(password, salt2);
		SymmetricEncryption senc = new SymmetricEncryption(passhash2);
		
		// Use senc to decrypt the RSA private key that is needed for future operations
		byte[] privkey = senc.decrypt(privkey_enc);
		
		return privkey;
	}
	
	
	 /**
     * Checks whether the provided time is within the last 24 hours.
     *
     * @param time the time in "yyyy-MM-dd'T'HH:mm:ss" format.o	
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
	
	  /**
     * Performs a self-test for user, storage, and article functionalities, such as registering,
     * logging in, updating, and deleting users and articles. Outputs test results to the console.
     *
     * @return true if all tests pass, false if any test fails
     */
	public static boolean selfTest() {
		System.out.println("User, Storage, and Article SelfTest");
		boolean allGood = true;
		boolean current = true;
		try {
			// Delete the database if it exists
			String homedir = System.getProperty("user.home") + File.separatorChar;
			String dbpath = homedir + "testing.db";
			File db = new File(dbpath);
			db.delete();
			Storage tester = new Storage("testing.db");
			// Register some users with their passwords
			User user4 = new User("First4", "Middle4", "Last4", "Pref4", "user4", "****", null, 0);
			user4.addRole('a');
			tester.registerUser(user4);
			tester.registerLogin("user4", "password4");
			
			User user5 = new User("First5", "Middle5", "Last5", "Pref5", "user5", "****", null, 0);
			user5.addRole('a');
			tester.registerUser(user5);
			tester.registerLogin("user5", "password5");
			
			User user6 = new User("First6", "Middle6", "Last6", "Pref6", "user6", "****", null, 0);
			user6.addRole('a');
			tester.registerUser(user6);
			tester.registerLogin("user6", "password6");
			
			// Attempt logins: Expected pass
			current = tester.loginAttempt("user4", "password4");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with <user4, password4> FAILED, should have PASSED!");
			} else {
				System.out.println("[PASS] Login with <user4, password4>");
			}
			
			current = tester.loginAttempt("user5", "password5");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with <user5, password5> FAILED, should have PASSED!");
			} else {
				System.out.println("[PASS] Login with <user5, password5>");
			}
			
			// Attempt login: Expected fail
			current = !tester.loginAttempt("user6", "password12345");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with <user6, password12345> PASSED, should have FAILED!");
			} else {
				System.out.println("[PASS] Login with <user6, password12345>");
			}
			
			// Attempt updating password
			tester.updateMainPass("user4", "password44");
			
			// Attempt logging in again with the new password
			current = tester.loginAttempt("user4", "password44");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with changed password FAILED, should have PASSED!");
			} else {
				System.out.println("[PASS] Login with changed password");
			}
			
			// Make our admin user
			User user1 = new User("First1", "Middle1", "Last1", "Pref1", "user1", "****", null, 0);
			user1.addRole('a');
			tester.registerUser(user1);
			
			System.out.println("[PASS] Registered admin");
			
			// Update the admin user
			user1.setFirstname("Admin");
			tester.updateUser("user1", "Admin", "Middle", "Root", "The Admin", "iamthe@admin.org");
						
			// Make sure the update worked
			User admin = tester.getUser("user1");
			current = admin.getFirstname().equals("Admin");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Admin user did not update");
			} else {
				System.out.println("[PASS] Admin user info updated");
			}
			
			// Make a code for an instructor and student
			tester.registerOneTimeCode("TESTINGCODE1234", "1970-01-01T23:34:45", "IS");
			
			current = tester.doesCodeExist("TESTINGCODE1234");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] One time code TESTINGCODE1234 does not exist, but it should");
			} else {
				System.out.println("[PASS] One time code exists");
			}
			
			// Check that the roles of the code match
			String gotRoles = tester.getRolesFromCode("TESTINGCODE1234");
			current = gotRoles.toUpperCase().equals("IS") || gotRoles.toUpperCase().equals("SI");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Couldn't get the roles back from the one time code");
			} else {
				System.out.println("[PASS] Retrieved role info from one time code");
			}
			
			// Article Testing (ALSO TESTS ARTICLE CLASS)
			current = tester.listAllArticles().size() == 0;
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] There are articles when there shouldn't be");
			} else {
				System.out.println("[PASS] There aren't any articles already");
			}
			
			// Create articles
			ArrayList<String> refs = new ArrayList<String>();
			refs.add("a ref");
			ArrayList<String> keywords = new ArrayList<String>();
			keywords.add("linux");
			keywords.add("macos");
			Article a1 = new Article("Title", "Body", refs, 3, "Header", "system user global", "Description", keywords);
			tester.addArticle(a1);
			
			System.out.println("[PASS] Created Article 1");
			
			ArrayList<String> refs2 = new ArrayList<String>();
			refs.add("a ref");
			ArrayList<String> keywords2 = new ArrayList<String>();
			keywords2.add("windows");
			keywords2.add("unix");
			Article a2 = new Article("Title", "Body", refs2, 4, "Header", "user java global", "Description", keywords2);
			tester.addArticle(a2);
			
			System.out.println("[PASS] Created Article 2");
			
			ArrayList<String> refs3 = new ArrayList<String>();
			refs.add("a ref");
			ArrayList<String> keywords3 = new ArrayList<String>();
			keywords3.add("linux");
			keywords3.add("ios");
			Article a3 = new Article("Title", "Body", refs3, 5, "Header", "testing global", "Description", keywords3);
			tester.addArticle(a3);
			
			System.out.println("[PASS] Created Article 3");
			
			// Now, test retrieving of the articles.
			current = tester.listAllArticles().size() == 3;
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] There is an improper amount of articles, insertion failed");
			} else {
				System.out.println("[PASS] There are the right amount of articles inserted");
			}
			
			// Test grouping too
			current = tester.listArticlesByGroup("user").size() == 2;
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Grouping selector for article listing failed!");
			} else {
				System.out.println("[PASS] Grouping selector for article listing works!");
			}
			
			// Test backup and restore
			ArrayList<Article> articles = new ArrayList<Article>();
			articles.add(a1);
			articles.add(a2);
			articles.add(a3);
			
			String temppath = homedir + "tempfile.txt";
			String fullBackupPath = tester.backupArticles(articles, temppath);
			System.out.println("[PASS] Backed up articles to " + fullBackupPath + "!");
			
			ArrayList<Article> restored = tester.restoreArticles(temppath);
			current = restored.size() == 3;
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Could not properly restore from file!");
			} else {
				System.out.println("[PASS] Restored articles from backup!");
			}
			
		} catch (Exception e) {
			System.out.println("Exception occurred during Storage testing!");
			System.out.println(e.getMessage());
			allGood = false;
		}
		
		if (allGood) {
			System.out.println("All Storage, User, and Article self tests passed!");
		} else {
			System.out.println("[!] Some Storage/User/Article self tests failed, check previous input");
		}
		return allGood;
	}
}
