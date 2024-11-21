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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CreateArticleController class is responsible for handling the creation of new articles.
 * It interacts with the UI elements for article attributes like title, references, headers, 
 * groups, description, keywords, and body. Upon user interaction, it stores the new article 
 * information in the database.
 */
public class CreateArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	private String camefrom;
	private String securegroup;
	private byte[] privkey;
	
	@FXML
	private TextField titleTextField, referenceLinksTextField, headersTextField, groupsTextField, descriptionTextField, keywordsTextField;
	@FXML
	private Button createArticleButton;
	@FXML
	private TextArea bodyTextField;
	
	@FXML
	private Text ip_userLabel, ca_titlelabel;
	
	String titleString, referenceString, headersString, groupsString, descriptionString, bodyString, keywordString;
	
    /**
     * Handles the action triggered when the "Create Article" button is clicked.
     * This method gathers user input from the UI, creates a new Article object,
     * sets its properties, and commits it to the database through the Storage class.
     * After saving, it loads the ManageArticles.fxml view.
     *
     * @param event The ActionEvent triggered by clicking the "Create Article" button
     * @throws IOException if there is an issue loading the ManageArticles.fxml file
     * @throws SQLException if there is an error during the database operation
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws NoSuchPaddingException 
     * @throws InvalidKeySpecException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeyException 
     * @throws InvalidAlgorithmParameterException 
     */
	@FXML
	public void ca_createArticleClicked(ActionEvent event) throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		
		boolean isSecure = (securegroup != null);
		
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
		if (isSecure) {
			this.storage.addSecureArticle(newArticle, securegroup, myusername, privkey);
		} else {
			this.storage.addArticle(newArticle);
		}
		
		System.out.println("Added the secure article to the database!");
		if (isSecure) {
			// Go back to the previous page
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InstructorLogin.fxml"));
			root = loader.load();
			
			InstructorLoginController controller = loader.getController();
			controller.userName(myusername);
			controller.setPrivkey(privkey);
			
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
			return;
		}
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
		ip_userLabel.setText("User: " + username);
		myusername = username;
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
	
	// set secure article group, if any
	public void setSecureGroup(String securegroup) {
		this.securegroup = securegroup;
		ca_titlelabel.setText("Create Secure Article");
	}
	
}
