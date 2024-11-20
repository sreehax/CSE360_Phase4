import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

//AUTHOR: Felix Allison & Someone else, probably Jonathan Lin since
//A lot of the code looks kinda bad.

/**
 * Controller class for the Student Login view.
 * Handles user interactions in the student login screen, including displaying the username
 * and handling logout actions.
 */
public class StudentLoginController{
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	//NOTE: Some of these buttons are being referenced by their
	//FXIDs which I think is kinda stupid but whatever. For all the
	//methods I wrote, I reference them by the IDs I manually gave them.
	//This should explain the difference in naming schema.
	
	@FXML
	private ComboBox<?> FilterComplexityComboBox, FilterByGroupComboBox;
	private TextField SearchByKeywordTextField, SearchByArticleIDTextField;
	private TextArea SubmitHelpMessageTextArea;
	private Button SearchByKeywordButton, SearchByArticleIDButton, SubmitGeneralHelpMessageButton;
	private Button SubmitSpecificHelpMessageButton;
	private Button ResetSearchButton, OpenArticleButton;
	
	@FXML
	private ListView<Button> ArticleListListView = new ListView<Button>();
	@FXML
	private ObservableList<Button> articleButtons = FXCollections.observableArrayList();
	@FXML
	private Text sl_userLabel;
	
	//
	//SREE: Implement database functionality
	public void filterComplexityComboBoxClicked(ActionEvent event) throws IOException{
		//Not quite sure what this does
		//Sree: Implement database functionality with this method.
	}
	//SREE: Implement database functionality
	public void filterByGroupComboBoxClicked(ActionEvent event) throws IOException{
		//Filters by article group
		//Sree: Implement database functionality with this method.
	}
	//SREE: Implement database functionality
	public void searchByKeywordButtonPressed(ActionEvent event) throws IOException{
		Article newArticle = new Article();
		newArticle.setBody("This is the article body for testing.");
		newArticle.setDescription("This is a description that says stuff.");
		newArticle.setGrouping("This is the grouping area. It does something.");
		newArticle.setHeader("Insert header here");
		newArticle.setID(12345);
		newArticle.setTitle("Generic Article Title");
		newArticle.addKeyword("Keyword1");
		newArticle.addReference("Reference1");
	}
	//SREE: Implement database functionality
	public void searchByArticleIDButtonPressed(ActionEvent event) throws IOException{
		//Depending on which is easier to search, I've included both the String and
		//integer variants of the ID value.
		String IDtoSearchString = SearchByArticleIDTextField.getText();
		int IDtoSearchInt = Integer.parseInt(IDtoSearchString);
	}
	//SREE: Implement database functionality
	public void submitGeneralHelpMessageButtonPressed(ActionEvent event) throws IOException{
		//Send to database
		//Sree: Implement database functionality with this method.
		String generalHelpMessage = SubmitHelpMessageTextArea.getText();
	}
	@FXML
	//SREE: Implement database functionality
	public void submitSpecificHelpMessageButtonPressed(ActionEvent event) throws IOException{
		String specificHelpMessage = SubmitHelpMessageTextArea.getText();
		
	}
	
	@FXML
	//COMPLETE
	public void resetSearchButtonPressed(ActionEvent event) throws IOException{
		ArticleListListView.getItems().clear();
	}
	
	@FXML
	//SREE: Implement database functionality
	public void addArticleToArticleListView(Article articleToInsert) {
		//Construct a bigass String to insert into the list  
		String insertable = "";
		insertable += "Title: " +articleToInsert.getTitle() + "\n";
		insertable += "Body:" + articleToInsert.getBody() + "\n";
		insertable += "Keywords: " + articleToInsert.getReferencesStr() + "\n";
		insertable += "ID: " + articleToInsert.getID()+ "\n";
		insertable += "Header: " + articleToInsert.getHeader() + "\n";
		insertable += "Grouping: " + articleToInsert.getGrouping() + "\n";
		insertable += "Description: " + articleToInsert.getDescription() + "\n";
		insertable += "Keywords: " + articleToInsert.getKeywordsStr() + "\n";
		
		Button tempButton = new Button();
		tempButton.setText(articleToInsert.getTitle());
		articleButtons.add(tempButton);
		ArticleListListView.refresh();
	}
	
	@FXML
	public void openArticleButtonClicked(ActionEvent event) throws IOException{
		DialogPane openArticle = new DialogPane();
		openArticle.snapPositionX(0);
		openArticle.snapPositionY(0);
		openArticle.resize(200, 200);
		
	}
    
	@FXML
	public void initialize() {
		ArticleListListView.setItems(articleButtons);
	}
	
	//
	@FXML
	//public void logoutButtonPressed(ActionEvent event) throws IOException
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
	
	@FXML
	//public void closeApplicationButtonPressed(ActionEvent event) throws IOException
	public void sl_killClicked(ActionEvent event) throws IOException{
		Platform.exit();
	}
	
	public void userName(String name) {
		this.sl_userLabel.setText("User: " + name);
	}
	
}
//