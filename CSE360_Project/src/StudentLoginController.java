import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * Controller class for the Student Login view.
 * Handles user interactions in the student login screen, including displaying the username
 * and handling logout actions.
 */
public class StudentLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	/** Text label to display the logged-in student's username. */
	@FXML
	private Text sl_userLabel;
	   /**
     * Handles the event when the user clicks the "Logout" button.
     * Logs the student out by navigating back to the main scene.
     *
     * @param event The event triggered by clicking the logout button.
     * @throws IOException If the FXML file for the main scene cannot be loaded.
     */
	@FXML
	public void sl_logoutClicked(ActionEvent event) throws IOException{
		// Load the MainScene.fxml file and set it as the root of the scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
	// Get the current stage and update the scene to the main scene	
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	/**
     * Sets the username of the student and updates the username label in the view.
     *
     * @param name The name of the student to be displayed.
     */
	public void userName(String name) {
		this.sl_userLabel.setText("User: " + name);
	}
}
