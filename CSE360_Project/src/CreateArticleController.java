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

//
public class CreateArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	private String camefrom;
	
	@FXML
	private TextField titleTextField, referenceLinksTextField, headersTextField, groupsTextField, descriptionTextField, keywordsTextField;
	@FXML
	private Button createArticleButton;
	@FXML
	private TextArea bodyTextField;
	
	@FXML
	private Text ip_userLabel;
	
	String titleString, referenceString, headersString, groupsString, descriptionString, bodyString, keywordString;
	
	@FXML
	public void ca_createArticleClicked(ActionEvent event) throws IOException, SQLException {
		
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
		titleString = titleTextField.getText();
		referenceString = referenceLinksTextField.getText();
		headersString = headersTextField.getText();
		groupsString = groupsTextField.getText();
		descriptionString = descriptionTextField.getText();
		bodyString = bodyTextField.getText();
		keywordString = keywordsTextField.getText();
		
		//NOTE: Keywords are going to be separated by spaces
	    String[] referenceArray = referenceString.split(" ");
	    String[] keywordsArray = keywordString.split(" ");
	        
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
		
		//SET ARTICLE ID to -1 (NULL) to take advantage of auto increment
		newArticle.setID(-1);
		
		//SET ARTICLE GROUPINGS
		//multiple groups separated by space are already handled in the backend group search
		newArticle.setGrouping(groupsString);
		
		// Commit the article to the database
		this.storage.addArticle(newArticle);
		
		System.out.println("Added the article to the database!");
		
		// Go back to the previous page
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageArticles.fxml"));
		root = loader.load();
		
		ManageArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.cameFrom(camefrom);
		
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	public void userName(String username) {
		ip_userLabel.setText("User: " + username);
		myusername = username;
	}
	
	public void cameFrom(String loc) {
		this.camefrom = loc;
	}
	
}
