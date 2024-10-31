import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
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
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + homedir + "storage2.db");
		
		// Create the database schema if it does not exist already
		Statement statement = this.conn.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_info (username TEXT PRIMARY KEY, firstname TEXT, middlename TEXT, lastname TEXT, preferredname TEXT, email TEXT, roles TEXT, code TEXT, temppass INT, temptime TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS logins (username TEXT PRIMARY KEY, passhash TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS onetimecode (code TEXT PRIMARY KEY, time TEXT, role TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS articles (title TEXT, body TEXT, refs TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT, header TEXT, grouping TEXT, description TEXT, keywords TEXT)");
	}
	
	// Constructor to create an alternate database (used for self-test)
	public Storage(String dbName) throws SQLException {
		//the database is saved in your home directory
		String homedir = System.getProperty("user.home") + File.separatorChar;
		this.conn = DriverManager.getConnection("jdbc:sqlite:" + homedir + dbName);
		
		// Create the database schema if it does not exist already
		Statement statement = this.conn.createStatement();
		statement.setQueryTimeout(30);
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS user_info (username TEXT PRIMARY KEY, firstname TEXT, middlename TEXT, lastname TEXT, preferredname TEXT, email TEXT, roles TEXT, code TEXT, temppass INT, temptime TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS logins (username TEXT PRIMARY KEY, passhash TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS onetimecode (code TEXT PRIMARY KEY, time TEXT, role TEXT)");
		statement.executeUpdate("CREATE TABLE IF NOT EXISTS articles (title TEXT, body TEXT, refs TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT, header TEXT, grouping TEXT, description TEXT, keywords TEXT)");
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
	
	// Insert an article into the database
	public void addArticle(Article a) throws SQLException {
		String update = "INSERT INTO articles (title, body, refs, id, header, grouping, description, keywords) VALUES (?,?,?,?,?,?,?,?)";
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
		
		prep.executeUpdate();
	}
	
	// Update an article using the ID
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
	
	// helper method to get all Articles from a ResultSet
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
	
	// Serialize a list of articles to a file
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
	
	// Deserialize a list of articles from a file
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
	
	// dump all articles
	public ArrayList<Article> listAllArticles() throws SQLException {
		String query = "SELECT * FROM articles";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		
		return consolidateArticles(rs);
	}
	
	// get article by ID, or null if it ain't found
	public Article getArticleByID(int id) throws SQLException {
		String query = "SELECT * FROM articles WHERE id = ?";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setInt(1, id);
		ResultSet rs = prep.executeQuery();
		
		ArrayList<Article> got = consolidateArticles(rs);
		if (got.size() < 1) {
			return null;
		}
		return got.get(0);
	}
	
	// dump articles by group
	public ArrayList<Article> listArticlesByGroup(String group) throws SQLException {
		String query = "SELECT * FROM articles WHERE grouping LIKE ?";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, "%" + group + "%");
		ResultSet rs = prep.executeQuery();
		
		return consolidateArticles(rs);
	}
	
	// search article by title
	public ArrayList<Article> searchArticlesByTitle(String title) throws SQLException {
		String query = "SELECT * FROM articles WHERE title LIKE ?";
		PreparedStatement prep = this.conn.prepareStatement(query);
		prep.setString(1, "%" + title + "%");
		ResultSet rs = prep.executeQuery();
		return consolidateArticles(rs);
	}
	
	// delete all articles
	public void deleteAllArticles() throws SQLException {
		String update = "DELETE FROM articles";
		Statement stmt = this.conn.createStatement();
		stmt.executeUpdate(update);
	}
	
	// delete article by ID
	public int deleteArticleByID(int id) throws SQLException {
		String update = "DELETE FROM articles WHERE id = ?";
		PreparedStatement prep = this.conn.prepareStatement(update);
		prep.setInt(1, id);
		int affected = prep.executeUpdate();
		return affected;
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
	
	// Self test
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
			tester.registerLogin("user1", "password1");
			tester.registerLogin("user2", "password2");
			tester.registerLogin("user3", "password3");
			
			// Attempt logins: Expected pass
			current = tester.loginAttempt("user1", "password1");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with <user1, password1> FAILED, should have PASSED!");
			} else {
				System.out.println("[PASS] Login with <user1, password1>");
			}
			
			current = tester.loginAttempt("user2", "password2");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with <user2, password2> FAILED, should have PASSED!");
			} else {
				System.out.println("[PASS] Login with <user2, password2>");
			}
			
			// Attempt login: Expected fail
			current = !tester.loginAttempt("user3", "password12345");
			allGood &= current;
			if (!current) {
				System.out.println("[FAIL] Login with <user3, password3> PASSED, should have FAILED!");
			} else {
				System.out.println("[PASS] Login with <user3, password3>");
			}
			
			// Attempt updating password
			tester.updateMainPass("user1", "password11");
			
			// Attempt logging in again with the new password
			current = tester.loginAttempt("user1", "password11");
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
