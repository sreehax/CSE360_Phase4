import java.util.ArrayList;
/**
 * Represents a user in the system with personal information and roles.
 * 
 * <p> This class is used to manage user data, including their first name, middle name, last name, preferred name,
 * username, password, and roles within the application. Roles define the user's permissions within the system.</p>
 */
public class User {
	
	private String firstname, middlename, lastname, preferredname, username, password, email, code;
	private int temppass;
	private ArrayList<Role> rolelist;

	 /**
     * Default constructor for the User class.
     * <p> Initializes the user's personal information to empty strings and creates an empty list of roles. </p>
     */	
	public User() {
		this.firstname = "";
		this.middlename = "";
		this.lastname = "";
		this.preferredname = "";
		this.username = "";
		this.code = "";
		this.temppass = 0;
		this.rolelist = new ArrayList<Role>();
		//user passwords are stored in the database
	}
	  /**
     * Parameterized constructor for the User class.
     * 
     * @param firstname The first name of the user.
     * @param middlename The middle name of the user.
     * @param lastname The last name of the user.
     * @param preferredname The preferred name of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     */
	public User(String firstname, String middlename, String lastname, String preferredname, String username, String password, String code, int temppass) {
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.preferredname = preferredname;
		this.username = username;
		this.code = code;
		this.temppass = 0;
	}
	
	// Setters

    /**
     * Sets the user's first name.
     * 
     * @param firstname The first name to set.
     */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	   /**
     * Sets the user's middle name.
     * 
     * @param middlename The middle name to set.
     */
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	 /**
     * Sets the user's last name.
     * 
     * @param lastname The last name to set.
     */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	 /**
     * Sets the user's preferred name.
     * 
     * @param preferredname The preferred name to set.
     */
	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}
	 /**
     * Sets the user's username.
     * 
     * @param username The username to set.
     */
	public void setUsername(String username) {
		this.username = username;
	}
	 /**
     * Sets the user's email.
     * 
     * @param user input email to set.
     */
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setTemppass(int value) {
		this.temppass = value;
	}
	
	//getters
	/**
     * Gets the user's first name.
     * 
     * @return The first name of the user.
     */
	public String getFirstname() {
		return firstname;
	}
	 /**
     * Gets the user's middle name.
     * 
     * @return The middle name of the user.
     */
	public String getMiddlename() {
		return middlename;
	}
	 /**
     * Gets the user's last name.
     * 
     * @return The last name of the user.
     */
	public String getLastname() {
		return lastname;
	}
	/**
     * Gets the user's preferred name.
     * 
     * @return The preferred name of the user.
     */
	public String getPreferredname() {
		return preferredname;
	}
	   /**
     * Gets the user's username.
     * 
     * @return The username of the user.
     */
	public String getUsername() {
		return username;
	}
	   /**
     * Gets the user's email.
     * 
     * @return The email of the user.
     */
	public String getEmail() {
		return email;
	}
	
	public String getCode() {
		return code;
	}
	
	public int getTemppass() {
		return temppass;
	}
	
	//Role operations
	 /**
     * Adds a role to the user's role list.
     * 
     * @param role The role to be added.
     */
	public void addRole(char role) {
		Role temp;
		switch(role) {
		case 'a':
			temp = Role.ADMIN;
			this.rolelist.add(temp);
			break;
		case 'i':
			temp = Role.INSTRUCTOR;
			this.rolelist.add(temp);
			break;
		case 's':
			temp = Role.STUDENT;
			this.rolelist.add(temp);
			break;
		}
	}
	 /**
     * Deletes a role from the user's role list.
     * 
     * @param role The role to be deleted.
     */
	public void deleteRole(Role role) {
		//check role exist in rolelist
		//if it exist, delete it
		//if it doesn't exist, print it doesn't exist and exit
	}
	/**
     * Gets the list of roles assigned to the user.
     * 
     * @return An ArrayList containing the user's roles.
     */
	public ArrayList<Role> getRoleList() {
		return rolelist;
	}
}
