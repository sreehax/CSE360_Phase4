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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

//AUTHOR: Felix Allison and maybe someone else. I don't know who
//but credit where it's due I guess.

public class InstructorLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	
	@FXML
	private Text ip_userLabel;
	@FXML
	private TextField ip_searchBar;
	@FXML
	private Button ip_searchButton;
	
	//
	private ComboBox FilterComplexityComboBox, FilterByGroupComboBox, SelectGroupComboBox;
	private TextField SearchByKeywordTextField, SearchByArticleIDTextField, StudentNameTextField;
	private Button LogoutButton, SearchByKeywordButton, SearchByArticleIDButton, CreateArticlesButton;
	private Button ListStudentMessagesButton, ManageSpecialAccessButton, CreateStudentButton, DeleteStudentButton;
	private Button AddToGroupButton, RemoveFromGroupButton;
	
	public void filterComplexityComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	public void filterByGroupComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	public void selectGroupComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	public void searchByKeywordTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	public void searchByArticleIDTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	public void logoutButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void searchByKeywordButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void searchByArticleIDButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void createArticlesButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void listStudentMessagesButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void manageSpecialAccessGroupsButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void createStudentButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void deleteStudentButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void addToGroupButtonClicked(ActionEvent event) throws IOException{
		
	}
	public void removeFromGroupButtonClicked(ActionEvent event) throws IOException{
		
	}
	//
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
		// Query the database for articles matching the search term in the title
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
