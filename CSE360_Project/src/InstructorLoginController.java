import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * Controller class for the Instructor Login view.
 * Manages user interactions in the instructor login screen, including logging out
 * and displaying the logged-in instructor's username.
 */
public class InstructorLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	/** Text label that displays the logged-in instructor's username. */
	@FXML
	private Text ip_userLabel;
	
	@FXML
	private TextField ip_searchBar;
	
	@FXML
	private Button ip_searchButton;
	   /**
     * Handles the event triggered by the instructor clicking the "Logout" button.
     * Logs the instructor out and redirects them back to the main scene.
     *
     * @param event The action event generated by clicking the logout button.
     * @throws IOException If the FXML file for the main scene cannot be loaded.
     */
	@FXML
	public void ip_logoutClicked(ActionEvent event) throws IOException{
		// Load the MainScene.fxml file and set it as the root of the new scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
	// Get the current stage and update the scene to the main scene	
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	  /**
     * Sets the username of the instructor and updates the username label in the view.
     * This method is called to display the current user's name on the login screen.
     *
     * @param name The instructor's name to display.
     */
	public void userName(String name) {
		this.ip_userLabel.setText("User: " + name);
		this.myusername = name;
	}
	
	@FXML
	public void ip_toArticlesClicked(ActionEvent event) throws IOException{
		// Load the ManageArticles.fxml file and set it as the root of the new scene
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageArticles.fxml"));
		root = loader.load();
		ManageArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.cameFrom("Instructor");
	// Get the current stage and update the scene to the main scene	
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void ip_searchButtonClicked(ActionEvent event) throws IOException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		String searchterm = ip_searchBar.getText();
		ArrayList<Article> articles = this.storage.searchArticlesByTitle(searchterm);
		
		if (articles.size() == 0) {
			System.out.println("No articles found by that search term");
			return;
		}
		System.out.println("Found " + articles.size() + " articles by that search term:");
		for (Article a: articles) {
			a.printInfo();
		}
	}
}
