import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The CreateAccountController class manages the account creation process
 * for users within the application. It handles user input, validates
 * passwords, and interacts with the Storage class to register new users.
 */
public class CreateAccountController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String currentCode;
	private boolean admincreation = true;
	
	
	@FXML
	private TextField ca_usernameid, ca_passwordid, ca_password2id;
	@FXML
	private Text ca_nouserstxt;
	/**
     * Handles the event when the create account button is clicked.
     * Validates user input, checks password matching, and registers
     * the new user in the storage.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue loading the next scene
     */
	@FXML
	public void create_account_buttonClicked(ActionEvent event) throws IOException {
		
		boolean flag = false;
		boolean invitea = false;
		boolean invitei = false;
		boolean invites = false;
		
		String username, password, password2;
		
		username = ca_usernameid.getText();
		password = ca_passwordid.getText();
		password2 = ca_password2id.getText();
		
		// Passwords should match
		// TODO: Show some better feedback
		if (!password.equals(password2)) {
			System.out.println("Passwords must match!");
			return;
		};
		
		//connect storage
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Storage s = (Storage) stage.getUserData();
		
		char c;
		
		try {
			String listofRoles = s.getRolesFromCode(currentCode);
			System.out.println(listofRoles);
			// Check which roles are assigned based on the invite code
			for (int i = 0; i < listofRoles.length(); i++) {
				c = listofRoles.charAt(i);
				c = Character.toLowerCase(c);
				switch (c) {
					case 'a':
						invitea = true;
						break;
					case 'i':
						invitei = true;
						break;
					case 's':
						invites = true;
						break;
				}
			}
			
			//interpret list of roles.
			// Create new user and set properties
			User user = new User();
			user.setUsername(username);
			user.setCode(currentCode);
			// Add roles to user based on flags
			if(admincreation) {
				user.addRole('a');
			}
			if(invitea) {
				user.addRole('a');
			}
			if(invitei) {
				user.addRole('i');
			}
			if(invites) {
				user.addRole('s');
			}
			
			// Register the new user and login information
			s.registerUser(user);
			s.registerLogin(username, password);
			System.out.println("Successfully registered user!");
		} catch (SQLException e) {
			System.out.println("User already exists!");
			e.printStackTrace(System.err);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		this.admincreation = false;
	}
	/*
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	@param event The action event triggered by the button click
 	@throws IOException If there is an issue loading the previous scene
     */
	@FXML
	public void create_account_backClicked(ActionEvent event) throws IOException {
		if (this.admincreation) {
			ca_nouserstxt.setText("You have to register an admin first");
			return;
		}
		// Load the main scene if an admin has been created
		root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	 /**
     * Clears the message displayed to the user regarding
     * the necessity of creating an admin user.
     */
	public void deletenousermsg() {
		ca_nouserstxt.setText("");
		this.admincreation = false;
	}
	/**
     * Initializes the controller with a one-time invite code
     * for user registration and sets the corresponding message.
     *
     * @param code The one-time invite code provided for registration
     */
	public void onetimeinvite(String code) {
		ca_nouserstxt.setText("One Time Invite Code User Registration");
		this.admincreation = false;
		
		currentCode = code;
	}
}
