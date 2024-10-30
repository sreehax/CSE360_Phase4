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

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
public class CreateArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private TextField titleTextField, referenceLinksTextField, headersTextField, groupsTextField, descriptionTextField, bodyTextField, keywordsTextField;
	private Button createArticleButton;
	
	String titleString, referenceString, headersString, groupsString, descriptionString, bodyString, keywordString;
	
	public void pressCreateArticleButton(ActionEvent event) throws IOException {
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
		
		//SET ARTICLE ID 
		//SET ARTICLE GROUPINGS
	}
	
}
