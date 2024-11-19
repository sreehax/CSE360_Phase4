import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
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
public class StudentLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	//NOTE: Some of these buttons are being referenced by their
	//FXIDs which I think is kinda stupid but whatever. For all the
	//methods I wrote, I reference them by the IDs I manually gave them.
	//This should explain the difference in naming schema.
	
	private ComboBox<?> FilterComplexityComboBox, FilterByGroupComboBox;
	private TextField SearchByKeywordTextField, SearchByArticleIDTextField;
	private TextArea SubmitHelpMessageTextArea;
	private Button SearchByKeywordButton, SearchByArticleIDButton, SubmitGeneralHelpMessageButton;
	private Button SubmitSpecificHelpMessageButton;
	private Button ResetSearchButton;
	
	@FXML
	private ListView<String> ArticleListListView;
	
	@FXML
	private Text sl_userLabel;
	
	//
	public void filterComplexityComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	public void filterByGroupComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	public void searchByKeywordTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	public void searchByArticleIDTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	public void submitHelpMessageTextAreaClicked(ActionEvent event) throws IOException{
		
	}
	public void logoutButtonPressed(ActionEvent event) throws IOException{
		
	}
	public void searchByKeywordButtonPressed(ActionEvent event) throws IOException{
		
	}
	public void searchByArticleIDButtonPressed(ActionEvent event) throws IOException{
		
	}
	public void submitGeneralHelpMessageButtonPressed(ActionEvent event) throws IOException{
		
	}
	public void submitSpecificHelpMessageButtonPressed(ActionEvent event) throws IOException{
		
	}
	
	@FXML
	public void resetSearchButtonPressed(ActionEvent event) throws IOException{
		ArticleListListView.getItems().clear();
	}
	
	//This is the method that adds different articles to the student article list view
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
		
		 ArticleListListView.getItems().add(insertable);
		 ArticleListListView.getItems().add(" TEST");
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