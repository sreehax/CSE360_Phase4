import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Controller class for handling the main login scene in a JavaFX application.
 * It allows users to log in using a username and password or a one-time code (OTC).
 */
public class MainSceneController {
	// Stage and Scene objects for transitioning between different UI scenes
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	// FXML UI elements linked to the main scene
	@FXML
	private TextField mainscene_usernameid, mainscene_passwordid, mainscene_onetimeinviteid;
	/**
     * Initializes the controller when the scene is loaded.
     * This method is called automatically by the JavaFX framework.
     */
	public void initialize() {
		
	}
	  /**
     * Handles the login process when the "Login" button is clicked.
     * Verifies the username and password against stored data, and navigates to the appropriate scene based on the result.
     *
     * @param event the event triggered by clicking the login button.
     * @throws IOException if an I/O error occurs while loading the next scene.
     */
	@FXML
	public void LoginButtonClicked(ActionEvent event) throws IOException {
		// Get the current stage from the event source
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData();
		
		boolean flag = false;
		String username, password;
		
		username = mainscene_usernameid.getText();
		password = mainscene_passwordid.getText();
		
		// Test the password
		try {
			
			flag = this.storage.loginAttempt(username, password);
			if (flag) {
				System.out.println("Login succeeded!");
				
				//check user setup
				if (this.storage.userSetup(username)) {
					//load account set up page
					//FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveAccountInfo.fxml"));
					//root = FXMLLoader.load(getClass().getResource("SaveAccountInfo.fxml"));
		            
					stage.setUserData(storage);
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveAccountInfo.fxml"));
					root = loader.load();
					// Pass the current username to the account setup controller
					SaveAccountInfoController controller = loader.getController();
					controller.currentUser(username);
					
			        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			        scene = new Scene(root);
			        stage.setScene(scene);
			        stage.show();
					
				}
				else {
					//log in
					
					stage.setUserData(storage);
					
					//check users role, if it is more than 1 go to selection screen, otherwise go to one of the 3 login pages.
					
					User u = storage.getUser(username);
					ArrayList<Role> list = u.getRoleList();
					
					if (list.size() > 1) {
						//go to selection page
						System.out.println("more than one role");
					}
					else {
						//choose between 3
						if (list.get(0) == Role.ADMIN) {
							System.out.println("role is admin");
						}
						if (list.get(0) == Role.INSTRUCTOR) {
							System.out.println("role is instructor");
						}
						if (list.get(0) == Role.STUDENT) {
							System.out.println("role is student");
						}
					}
					
						// Load the admin login page for users without a complete setup
					FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
					root = loader.load();
						// Pass the current username to the admin login controller
					AdminLoginController controller = loader.getController();
					controller.userName(username);
						// Transition to the admin login scene	
			        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			        scene = new Scene(root);
			        stage.setScene(scene);
			        stage.show();
			        
			        
					
					
				}
				
				
			} else {
				System.out.println("Login failed :(");
			}
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Exception thrown for incorrect algorithm " + e);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace(System.err);
		}
		
	}
	
	/**
     * Handles the one-time code (OTC) login process when the "Use OTC" button is clicked.
     * Checks the validity of the one-time code, ensuring it exists, is within 24 hours, and is not already used.
     * Loads the registration page if the code is valid.
     *
     * @param event the event triggered by clicking the OTC button.
     * @throws IOException if an I/O error occurs while loading the next scene.
     * @throws SQLException if a database access error occurs.
     */
	@FXML
	public void OTCButtonClicked(ActionEvent event) throws IOException, SQLException {
		// Get the current stage and storage object from the event source
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData();
		
		String code = mainscene_onetimeinviteid.getText();
		
		//check database to see if one time code entered exists
		boolean flag = storage.doesCodeExist(code);
		if (!flag) {
			System.out.println("Doesn't exist");
			return;
		}
		
		String time = storage.getTimeFromCode(code);
		if (!this.within24Hours(time)) {
			System.out.println("Code expired!");
			return;
		}
		
		//check if it's already in use
		boolean flag2 = storage.isCodeAlreadyInUse(code);
		if (flag2) {
			System.out.println("Code is already used");
			return;
		}
		
		//if it exist load registration.
		if (flag) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
			root = loader.load();
			
			CreateAccountController controller = loader.getController();
			controller.onetimeinvite(code);
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		}
		
	}
	 /**
     * Checks whether the provided time is within the last 24 hours.
     *
     * @param time the time in "yyyy-MM-dd'T'HH:mm:ss" format.
     * @return true if the time is within 24 hours; false otherwise.
     */
	private boolean within24Hours(String time) {
		LocalDateTime currentTime = LocalDateTime.now();
		// Parse the provided time and calculate the time difference
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime inputTime = LocalDateTime.parse(time, fmt);
		long secondsElapsed = ChronoUnit.SECONDS.between(inputTime, currentTime);
		return secondsElapsed < 24 * 60 * 60;
	}
}
