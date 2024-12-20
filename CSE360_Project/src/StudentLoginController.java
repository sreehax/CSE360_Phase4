import java.io.IOException;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

//PLEASE READ:
//
//To add an article to the list: addArticleToList(<Article Name Here>);
//To display an article after being clicked, print showSelectedArticle(); to the command line


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
	private String myusername;
	private byte[] privkey;
	
	//NOTE: Some of these buttons are being referenced by their
	//FXIDs which I think is kinda stupid but whatever. For all the
	//methods I wrote, I reference them by the IDs I manually gave them.
	//This should explain the difference in naming schema.
	
	@FXML
	private Button SBK, SBAID, RS, SGHM, SSHM;
	@FXML
	private ComboBox<String> FBG, FC;
	@FXML
	private TextField SBKText, SBAIDText;
	@FXML
	private TextArea SHM;
	@FXML
	private ListView<Article> ALLV= new ListView<Article>();
	@FXML
	private ObservableList<Article> listItems = FXCollections.observableArrayList();
	@FXML
	private Text AText;
	@FXML
	private Text sl_userLabel;

	
	@FXML
	public void searchByKeywordPressed(ActionEvent event) throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
		String keyword = SBKText.getText();
		String group = FBG.getValue();
		
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        ArrayList<Article> articles = this.storage.listSecureArticlesByKeyword(keyword, group, myusername, privkey);
        listItems.clear();
        for (Article a : articles) {
        	System.out.println("Populating with " + a.getTitle());
        	addArticleToList(a);
        }
	}
	@FXML
	public void searchByArticleIDPressed(ActionEvent event) throws IOException{
		String stringID = "";
		int intID = 0;
		
		if(SBAIDText.getText() != "" && SBAIDText.getText() != null) {
			stringID = SBAIDText.getText();
		}
		
	}
	
	@FXML
	public void sp_clickToView(ActionEvent event) throws IOException {
		Article a = ALLV.getSelectionModel().getSelectedItem();
		if (a == null) {
			return;
		}
		showSelectedArticle(a);
	}
	@FXML
	public void filterByGroupPressed(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void filterComplexityPressed(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void resetSearchButtonPressed(ActionEvent event) throws IOException{
		listItems.clear();
	}
	@FXML
	public void submitGeneralHelpMessagePressed(ActionEvent event) throws IOException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		String generalHelpMessage = SHM.getText();
		this.storage.submitHelpMessage(myusername, generalHelpMessage, "general");
		System.out.println("Submitted general help message!");
	}
	@FXML
	public void submitSpecificHelpMessagePressed(ActionEvent event) throws IOException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		String specialHelpMessage = SHM.getText();
		this.storage.submitHelpMessage(myusername, specialHelpMessage, "general");
		System.out.println("Submitted general help message!");
	}
	
	public void initialize() {
		//testingMethod();
		ALLV.setCellFactory(param -> new ListCell<Article>() {
			@Override
			protected void updateItem(Article item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null || item.getTitle() == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
			}
		});
		ALLV.setItems(listItems);
		ALLV.refresh();
		
		
	}
	
	public void addArticleToList(Article articleToInsert) {
		listItems.add(articleToInsert);
	}
	
	public void showSelectedArticle(Article articleToDisplay) {
		String insertable = "";
		insertable += "Title: \n" + articleToDisplay.getTitle() + "\n";
		insertable += "Body: \n" + articleToDisplay.getBody() + "\n";
		insertable += "Keywords: \n" + articleToDisplay.getReferencesStr() + "\n";
		insertable += "ID: \n" + articleToDisplay.getID()+ "\n";
		insertable += "Header: \n" + articleToDisplay.getHeader() + "\n";
		insertable += "Grouping: \n" + articleToDisplay.getGrouping() + "\n";
		insertable += "Description: \n" + articleToDisplay.getDescription() + "\n";
		insertable += "Keywords: \n" + articleToDisplay.getKeywordsStr() + "\n";
		
		//WRITE TO COMMAND LINE?
		// yeah
		System.out.println(insertable);
	}

	
	public void testingMethod() {
		Article firstArticle = new Article();
		firstArticle.setBody("Body");
		firstArticle.setDescription("Description");
		firstArticle.setGrouping("Group");
		firstArticle.setHeader("Header");
		firstArticle.setID(123);
		firstArticle.setTitle("Title");
		
		Article secondArticle = new Article();
		secondArticle.setBody("Body");
		secondArticle.setDescription("Description");
		secondArticle.setGrouping("Group");
		secondArticle.setHeader("Header");
		secondArticle.setID(124);
		secondArticle.setTitle("Title");
		
		Article thirdArticle = new Article();
		thirdArticle.setBody("Body");
		thirdArticle.setDescription("Description");
		thirdArticle.setGrouping("Group");
		thirdArticle.setHeader("Header");
		thirdArticle.setID(124);
		thirdArticle.setTitle("Title");
	
		Article fourthArticle = new Article();
		fourthArticle.setBody("Body");
		fourthArticle.setDescription("Description");
		fourthArticle.setGrouping("Group");
		fourthArticle.setHeader("Header");
		fourthArticle.setID(124);
		fourthArticle.setTitle("Title");
		
		addArticleToList(firstArticle);
		addArticleToList(secondArticle);
		addArticleToList(thirdArticle);
		addArticleToList(fourthArticle);
	}
	
	
	
	/**
     * Sets the private key for the logged in user.
     *
     * @param username The private key.
     */
	public void setPrivkey(byte[] privkey) {
		this.privkey = privkey;
	}
	
	
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
	
	@FXML
	//public void closeApplicationButtonPressed(ActionEvent event) throws IOException
	public void sl_killClicked(ActionEvent event) throws IOException{
		Platform.exit();
	}
	
	public void userName(String name) {
		myusername = name;
		this.sl_userLabel.setText("User: " + name);
	}
	
	public void setGroups(ArrayList<String> groups) {
		FBG.setItems(FXCollections.observableArrayList(groups));
		FBG.getSelectionModel().selectFirst();
	}
	
}
//