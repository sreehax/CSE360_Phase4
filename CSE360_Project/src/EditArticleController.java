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
public class EditArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	private String camefrom;
	private int myid;
	
	@FXML
	private TextField e_titleTextField, e_referenceLinksTextField, e_headersTextField, e_groupsTextField, e_descriptionTextField, e_keywordsTextField;
	@FXML
	private Button e_editArticleButton;
	@FXML
	private TextArea e_bodyTextField;
	
	@FXML
	private Text e_userLabel, e_idNumLabel;
	
	String titleString, referenceString, headersString, groupsString, descriptionString, bodyString, keywordString;
	
	@FXML
	public void e_editArticleClicked(ActionEvent event) throws IOException, SQLException {
		
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
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
		
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	public void userName(String username) {
		e_userLabel.setText("User: " + username);
		myusername = username;
	}
	
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
	
	public void cameFrom(String loc) {
		this.camefrom = loc;
	}
}
