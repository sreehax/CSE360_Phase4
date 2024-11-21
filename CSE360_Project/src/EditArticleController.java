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
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The EditArticleController class is responsible for handling the editing of existing articles.
 * It interacts with various UI components for article attributes such as title, references, 
 * headers, groups, description, keywords, and body, allowing the user to modify these fields 
 * and update the article in the database.
 */
public class EditArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	private String camefrom;
	private int myid;
	private byte[] privkey;
	
	@FXML
	private TextField e_titleTextField, e_referenceLinksTextField, e_headersTextField, e_groupsTextField, e_descriptionTextField, e_keywordsTextField;
	@FXML
	private Button e_editArticleButton;
	@FXML
	private TextArea e_bodyTextField;
	
	@FXML
	private Text e_userLabel, e_idNumLabel;
	
	String titleString, referenceString, headersString, groupsString, descriptionString, bodyString, keywordString;
	  /**
     * Handles the action triggered when the "Edit Article" button is clicked.
     * Gathers updated user input from the UI fields, creates a modified Article object 
     * with the new data, and updates the article in the database using the Storage class.
     * After updating, it navigates back to the ManageArticles.fxml view.
     *
     * @param event The ActionEvent triggered by clicking the "Edit Article" button
     * @throws IOException if there is an issue loading the ManageArticles.fxml file
     * @throws SQLException if there is an error during the database operation
     */
	@FXML
	public void e_editArticleClicked(ActionEvent event) throws IOException, SQLException {
		 // Obtain the current stage and retrieve the Storage object
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        // Gather updated article details from the input fields
		titleString = e_titleTextField.getText();
		referenceString = e_referenceLinksTextField.getText();
		headersString = e_headersTextField.getText();
		groupsString = e_groupsTextField.getText();
		descriptionString = e_descriptionTextField.getText();
		bodyString = e_bodyTextField.getText();
		keywordString = e_keywordsTextField.getText();
		
		//NOTE: Keywords are going to be separated by spaces
	    String[] referenceArray = referenceString.split(" ");
	    String[] keywordsArray = keywordString.split(" ");
	        // Create a new article and populate its fields with updated values
		Article newArticle = new Article();
		newArticle.setTitle(titleString);
		for(int i = 0; i < referenceArray.length; i++) {
			newArticle.addReference(referenceArray[i]);
		}
		for(int j = 0; j < keywordsArray.length; j++) {
			newArticle.addKeyword(keywordsArray[j]);
		}
		newArticle.setBody(bodyString);
		newArticle.setHeader(headersString);
		newArticle.setDescription(descriptionString);
		
		//SET ARTICLE ID to the working ID
		newArticle.setID(myid);
		
		//SET ARTICLE GROUPINGS
		//multiple groups separated by space are already handled in the backend group search
		newArticle.setGrouping(groupsString);
		
		// Commit the article to the database
		this.storage.updateArticle(newArticle);
		
		System.out.println("Edited the article!");
		
		// Go back to the previous page
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageArticles.fxml"));
		root = loader.load();
		
		ManageArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.cameFrom(camefrom);
		controller.setPrivkey(privkey);
		
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	 /**
     * Sets the username label to display the currently logged-in user's name.
     *
     * @param username The username to be displayed
     */
	public void userName(String username) {
		e_userLabel.setText("User: " + username);
		myusername = username;
	}
	
    /**
     * Sets the fields in the edit article form with the current data of an article.
     * Populates the fields such as title, references, header, groups, keywords, 
     * description, and body, and displays the article's ID.
     *
     * @param a The Article object whose details are to be edited
     */
	public void setArticle(Article a) {
		myid = a.getID();
		e_idNumLabel.setText("ID: " + myid);
		e_titleTextField.setText(a.getTitle());
		e_referenceLinksTextField.setText(a.getReferencesStr().replaceAll(",", ""));
		e_headersTextField.setText(a.getHeader());
		e_groupsTextField.setText(a.getGrouping());
		e_keywordsTextField.setText(a.getKeywordsStr().replaceAll(",", ""));
		e_descriptionTextField.setText(a.getDescription());
		e_bodyTextField.setText(a.getBody());
	}
	  /**
     * Sets the origin location indicating where the user navigated from.
     * This information can be used to customize behavior based on the previous page.
     *
     * @param loc The name of the location or page the user came from
     */
	public void cameFrom(String loc) {
		this.camefrom = loc;
	}
	
	/**
     * Sets the private key for the logged in user.
     *
     * @param username The private key.
     */
	public void setPrivkey(byte[] privkey) {
		this.privkey = privkey;
	}
}
