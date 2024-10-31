import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
 * The ResetPasswordController class handles the reset password functionality.
 * It allows users to enter a new password and update it in the database.
 * The class also provides navigation back to the main scene.
 */
public class ResetPasswordController {
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String username;
	
	@FXML
	private TextField rp_txtfield;
	   /**
     * Handles the action when the "Back" button is clicked.
     * Loads the MainScene.fxml view and returns the user to the main scene.
     *
     * @param event The ActionEvent triggered by clicking the "Back" button
     * @throws IOException if there is an issue loading the MainScene.fxml file
     */
	@FXML
	public void rp_backClicked(ActionEvent event) throws IOException{ 
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
		MainSceneController controller = loader.getController();
		

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	   /**
     * Handles the action when the "Confirm" button is clicked.
     * Retrieves the new password entered in the text field, validates it, 
     * and updates the password in the database if it is not empty.
     * If no password is entered, it prompts the user to input a password.
     *
     * @param event The ActionEvent triggered by clicking the "Confirm" button
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the hashing algorithm is unavailable
     * @throws InvalidKeySpecException if the password specification is invalid
     * @throws SQLException if there is an error updating the password in the database
     */
	@FXML
	public void rp_confirmClicked(ActionEvent event) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SQLException{ 
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData(); 
		
		String newPass = rp_txtfield.getText();
		System.out.println(newPass);
		
		if (newPass.equals("")) {
			System.out.println("you need to input a password");
			return;
		}
		else {
			storage.updateMainPass(this.username, newPass);
			System.out.println("Update Completed");
		}
		
	}
	 /**
     * Sets the username for the current user.
     * This username is used to identify the account for which the password will be reset.
     *
     * @param username The username of the account to reset the password for
     */
	public void setdata(String username) {
		this.username = username;
	}
}
