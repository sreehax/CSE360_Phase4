import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/**
 * Controller class for saving account information in a JavaFX application.
 * Handles user interactions related to saving account details like first name, middle name, last name, preferred name, and email.
 */
public class SaveAccountInfoController {
	
	private String username;
	private Stage stage;
	private Scene scene;
	private Parent root;
	//private Storage storage;
	
	@FXML
	private TextField firstnameid, middlenameid, lastnameid, preferrednameid, emailid;
	/**
     * Sets the current username for the logged-in user. 
     * This method allows the controller to know which user's account details are being updated.
     *
     * @param username the username of the currently logged-in user.
     */
	public void currentUser(String username) {
		this.username = username;
	}
	
    /**
     * Handles the action when the "Save Account" button is clicked.
     * It retrieves the input data from the TextField elements, validates it, and saves the account information using the `Storage` class.
     *
     * @param event the event triggered by the button click.
     * @throws IOException if there is an input/output error during scene transition.
     * @throws SQLException if a database access error occurs.
     */
	@FXML
	public void save_account_button_buttonClicked(ActionEvent event) throws IOException, SQLException{
		
		System.out.println("Clicked save account");
		
		// Retrieve input data from the text fields
		String firstname, middlename, lastname, preferredname, email;
		firstname = firstnameid.getText();
		middlename = middlenameid.getText();
		lastname = lastnameid.getText();
		preferredname = preferrednameid.getText();
		email = emailid.getText();

		// Validate that all necessary fields are filled in
		if (firstname.equals("")) {
			System.out.println("you must have a first name");
			return;
		}
		if (middlename.equals("")) {
			System.out.println("you must have a middle name");
			return;
		}
		if (lastname.equals("")) {
			System.out.println("you must have a last name");
			return;
		}
		if (email.equals("")) {
			System.out.println("you must input an email");
			return;
		}
		// If no preferred name is provided, default to the first name
		if (preferredname.equals("")) {
			preferredname = firstname;
		}
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Storage s = (Storage) stage.getUserData();
		//Storage s = new Storage();
		s.updateUser(username, firstname, middlename, lastname, preferredname, email);
		
	}
	/**
     * Handles the action when the "Back" button is clicked.
     * This transitions the user back to the main scene.
     *
     * @param event the event triggered by the back button click.
     * @throws IOException if there is an error during scene loading or transition.
     */
	@FXML
	public void save_account_backClicked(ActionEvent event) throws IOException {
		//System.out.println("Clicked back button");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
		//CreateAccountController controller = loader.getController();
		//controller.deletenousermsg();
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
}
