import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The AdminLoginController class manages the admin login interface,
 * including user interactions for account management and invitation code generation.
 * It handles the setup of the admin panel, allowing the admin to reset accounts,
 * delete users, and create invitation codes for different user roles.
 */
public class AdminLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	@FXML
	private Text al_userLabel;
	@FXML
	private CheckBox al_admincheckbox, al_studentcheckbox, al_instructorcheckbox;
	
	/**
     * Initializes the controller after the root element has been processed.
     */
	@FXML
	public void initialize() {
		
	}
	/**
     * Handles the event when the admin checkbox is clicked.
     *
     * @param event The action event triggered by the checkbox click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_admincheckboxClicked(ActionEvent event) throws IOException{
		
	}
	/**
     * Handles the event when the student checkbox is clicked.
     *
     * @param event The action event triggered by the checkbox click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_studentcheckboxClicked(ActionEvent event) throws IOException {
		
	}
	/**
     * Handles the event when the instructor checkbox is clicked.
     *
     * @param event The action event triggered by the checkbox click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_instructorcheckboxClicked(ActionEvent event) throws IOException {
		
	}
	/**
     * Handles the event when the reset account button is clicked.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_resetaccountbuttonClicked(ActionEvent event) throws IOException{
		
	}
	/**
     * Handles the event when the delete user button is clicked.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_deleteuserbuttonClicked(ActionEvent event) throws IOException{
		
	}
	/**
     * Handles the event when the invitation button is clicked.
     * Generates a one-time code for user roles and registers it in the storage.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_invitationbuttonClicked(ActionEvent event) throws IOException {
		// Get the current stage and storage object
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
		//get random code
		Random rand = new Random();
		StringBuilder codeBuilder = new StringBuilder();
		String ALPHABET = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		while (codeBuilder.length() < 15) {
			// Generate a random index into the alphabet
			int idx = (int) (rand.nextFloat() * ALPHABET.length());
			// Place that character into the code
			codeBuilder.append(ALPHABET.charAt(idx));
		}
		String code = codeBuilder.toString();
		
		//get the current time
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String date = time.format(fmt);
		
		System.out.println(code);
		System.out.println(date);
		//get the role indicated
		String roles = "";
		if (al_admincheckbox.isSelected()) {
			roles += "A";
		}
		if (al_studentcheckbox.isSelected()) {
			roles += "S";
		}
		if (al_instructorcheckbox.isSelected()) {
			roles += "I";
		}
		
		if (roles.equals("")) {
			System.out.println("You must select at least one role to generate a One Time Password");
			return;
		}
		System.out.println(roles);
		// Register the one-time code in the storage
		try {
			this.storage.registerOneTimeCode(code, date, roles);
			System.out.println("One Time Code Registered");
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}
	 /**
     * Handles the event when the logout button is clicked.
     * Loads the main scene and displays it.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue loading the main scene
     */
	@FXML
	public void al_logoutClicked(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
		//AdminLoginController controller = loader.getController();
		//controller.userName(username);
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	 /**
     * Sets the user name label to display the currently logged-in user.
     *
     * @param name The name of the user to be displayed
     */
	public void userName(String name) {
		al_userLabel.setText("User: " + name);
	}
	
}
