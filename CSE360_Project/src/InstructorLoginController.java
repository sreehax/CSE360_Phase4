import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	@FXML
	private Text ip_userLabel;
	@FXML
	private TextField ip_searchBar;
	@FXML
	private Button ip_searchButton;

	@FXML
	private Button SBK, SBAID, RS, SGHM, SSHM;
	@FXML
	private ComboBox FBG, FC;
	@FXML
	private TextField SBKText, SBAIDText;
	@FXML
	private TextArea MS;
	@FXML
	private ListView<String> ALLV= new ListView();
	@FXML
	private ObservableList<String> listItems = FXCollections.observableArrayList();
	@FXML
	private Text AText;
	@FXML
	private ComboBox SG;
	
	@FXML
	public void searchByKeywordPressed(ActionEvent event) throws IOException{
		String keyword = SBKText.getText();
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
		ALLV.setItems(listItems);
		ALLV.refresh();
	}
	
	public void addArticleToList(Article articleToInsert) {
		listItems.add(articleToInsert.getTitle());
	}
	
	@FXML
	public void showSelectedArticle(Article articleToDisplay) {
		String insertable = "";
		insertable += "Title: " + articleToDisplay.getTitle() + "\n";
		insertable += "Body: " + articleToDisplay.getBody() + "\n";
		insertable += "Keywords: " + articleToDisplay.getReferencesStr() + "\n";
		insertable += "ID: " + articleToDisplay.getID()+ "\n";
		insertable += "Header: " + articleToDisplay.getHeader() + "\n";
		insertable += "Grouping: " + articleToDisplay.getGrouping() + "\n";
		insertable += "Description: " + articleToDisplay.getDescription() + "\n";
		insertable += "Keywords: " + articleToDisplay.getKeywordsStr() + "\n";
		
		//WRITE TO COMMAND LINE?
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
		// Load the ManageArticles.fxml file and set it as the root of the new scene
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
