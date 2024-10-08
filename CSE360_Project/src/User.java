import java.util.List;

enum Role {
	ADMIN,
	STUDENT,
	INSTRUCTOR
}

public class User {
	
	private String firstname, middlename, lastname, preferredname, username, password;
	private List<Role> rolelist;
	
	public User() {
		this.firstname = "";
		this.middlename = "";
		this.lastname = "";
		this.preferredname = "";
		this.username = "";
		this.rolelist = null;
		//user passwords are stored on the database
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
	public void setRole(String role) {
		
	}
	//getters
	
}