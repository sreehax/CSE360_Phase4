import java.util.ArrayList;

enum Role {
	ADMIN,
	STUDENT,
	INSTRUCTOR
}

public class User {
	
	private String firstname, middlename, lastname, preferredname, username, password;
	private ArrayList<Role> rolelist;
	
	public User() {
		this.firstname = "";
		this.middlename = "";
		this.lastname = "";
		this.preferredname = "";
		this.username = "";
		this.rolelist = new ArrayList<Role>();
		//user passwords are stored in the database
	}
	
	//constructor
	public User(String firstname, String middlename, String lastname, String preferredname, String username, String password) {
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.preferredname = preferredname;
		this.username = username;
	}
	
	//setters
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	//getters
	public String getFirstname() {
		return firstname;
	}
	public String getMiddlename() {
		return middlename;
	}
	public String getLastname() {
		return lastname;
	}
	public String getPreferredname() {
		return preferredname;
	}
	public String getUsername() {
		return username;
	}
	
	//Role operations
	public void addRole(Role role) {
		this.rolelist.add(role);
	}
	public void deleteRole(Role role) {
		//check role exist in rolelist
		//if it exist, delete it
		//if it doesn't exist, print it doesn't exist and exit
	}
	public ArrayList<Role> getRoleList() {
		return rolelist;
	}
}