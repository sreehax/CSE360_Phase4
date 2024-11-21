import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;

//AUTHOR: Felix Allison and maybe someone else. I don't know who
//but credit where it's due I guess.

public class InstructorLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	private byte[] privkey;
	ArrayList<String> groups;
	
	@FXML
	private Text ip_userLabel;
	@FXML
	private TextField ip_searchBar;
	@FXML
	private Button ip_searchButton;

	@FXML
	private Button SBK, SBAID, RS, SGHM, SSHM;
	@FXML
	private ComboBox<String> FBG, FC;
	@FXML
	private TextField SBKText, SBAIDText;
	@FXML
	private TextArea MS;
	@FXML
	private TextField SN;
	@FXML
	private ListView<Article> ALLV= new ListView<Article>();
	@FXML
	private ObservableList<Article> listItems = FXCollections.observableArrayList();
	@FXML
	private Text AText;
	@FXML
	private ComboBox<String> SG;
	
	
	@FXML
	public void searchByKeywordPressed(ActionEvent event) throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException{
		String keyword = SBKText.getText();
		String group = FBG.getValue();
		
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        ArrayList<Article> articles = this.storage.listSecureArticlesByKeyword(keyword, group, myusername, privkey);
        listItems.clear();
        for (Article a : articles) {
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
	public void ip_clickToView(ActionEvent event) throws IOException {
		Article a = ALLV.getSelectionModel().getSelectedItem();
		showSelectedArticle(a);
	}
	@FXML
	public void ip_clickToEdit(ActionEvent event) throws IOException {
		// open EditARticle
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("EditArticle.fxml"));
		root = loader.load();
		
		EditArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.setPrivkey(privkey);
		controller.cameFrom("Instructor");
		
		Article a = ALLV.getSelectionModel().getSelectedItem();
		controller.setArticle(a);
		String group = FBG.getValue();
		controller.setSecureGroup(group);
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	@FXML
	public void ip_listStudentMessages(ActionEvent event) throws IOException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        this.storage.printHelpMessages();
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
	public void submitGeneralHelpMessagePressed(ActionEvent event) throws IOException{
		String generalHelpMessage = MS.getText();
	}
	@FXML
	public void submitSpecificHelpMessagePressed(ActionEvent event) throws IOException{
		String specialHelpMessage = MS.getText();
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
	
	@FXML
	public void ip_removeFromGroup(ActionEvent event) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SQLException {
		//read the student name and remove them to the group
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        String student = SN.getText();
        String group = FBG.getValue();
        if (this.storage.removeUserFromSpecialAccessGroup(group, student)) {
        	System.out.println("Successfully removed from the group");
        }
	}
	
	@FXML
	public void ip_addToGroup(ActionEvent event) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, SQLException {
		//read the student name and remove them to the group
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		this.storage = (Storage) stage.getUserData();
		String student = SN.getText();
		String group = FBG.getValue();
		if (this.storage.addUserToSpecialAccessGroup(group, student, myusername, privkey)) {
			System.out.println("Successfully added to the group!");
		}
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
	@FXML
	public void selectGroupPressed(ActionEvent event) throws IOException {
		
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
	/**
     * Sets the private key for the logged in user.
     *
     * @param username The private key.
     */
	public void setPrivkey(byte[] privkey) {
		this.privkey = privkey;
	}
	
	@FXML
	
public void ip_toArticlesClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageArticles.fxml"));
		root = loader.load();
		ManageArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.setPrivkey(privkey);
		controller.cameFrom("Instructor");
	// Get the current stage and update the scene to the main scene	
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	@FXML
	public void ip_toSpecialAccess(ActionEvent event) throws IOException, SQLException {
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
        ArrayList<String> groups = this.storage.getGroupsFromUsername(myusername);
        if (groups.size() == 0) {
        	System.out.println("You are not in any special access groups :(");
        	System.out.println("Try creating one.");
        	return;
        }
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GroupSpecial.fxml"));
		root = loader.load();
		
		GroupController controller = loader.getController();
		controller.userName(myusername);
		controller.setPrivkey(privkey);
		controller.populateGroups(groups);
		controller.cameFrom("Instructor");
		
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
	
	public void setGroups(ArrayList<String> groups) {
		this.groups = groups;
		FBG.setItems(FXCollections.observableArrayList(groups));
		FBG.getSelectionModel().selectFirst();
		SG.setItems(FXCollections.observableArrayList(groups));
		SG.getSelectionModel().selectFirst();
	}
}
