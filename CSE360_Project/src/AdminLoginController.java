import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.Random;
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

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//AUTHORS: Felix Allison, and others. Presumably Ben Nelson,
//Sreehari Sreedev, and Jonathan Lin.

public class AdminLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	private byte[] privkey;
	
	@FXML
	private Text al_userLabel;
	@FXML
	private TextField al_username, al_deleteusername;
	@FXML
	private CheckBox al_admincheckbox, al_studentcheckbox, al_instructorcheckbox;
	@FXML
	private TextField al_searchBar;
	
	@FXML
	private TextField al_csName, al_csUser;
	@FXML
	private Button al_searchButton;
	
	//
	private TextField SearchArticleByTitleTextField;
	private Button ManageSpecialAccessGroupsButton, ManageArticlesButton, SearchArticlesButton;
	
	private String articleToSearch;
	
	public void searchArticleByTitleTextFieldEntered(ActionEvent event) throws IOException{
		articleToSearch = SearchArticleByTitleTextField.getText();
	}
	public void manageSpecialAccessGroupsButtonClicked(ActionEvent event) throws IOException{
		//COMPLETE METHOD HERE
	}
	public void manageArticlesButtonClicked(ActionEvent event) throws IOException{
		//COMPLETE METHOD HERE
	}
	public void searchArticlesButtonClicked(ActionEvent event) throws IOException{
		//DO SOMETHING WITH articleToSearch String
		//Sree, run that fucker through the database or whatever and
		//if needed, print the results (?)
	}
	//
	
	
	
	/**
     * Initializes the controller after the root element has been processed.
     */
	@FXML
	public void initialize() {

	}
	
	
	@FXML
	public void al_printtableClicked(ActionEvent event) throws IOException, SQLException {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
        //list the user accounts. a list of all the user accounts with the user name,
        //the individual's name, and a set of codes for the roles is displayed.
        storage.printTable2();
	}
	/**
     * Handles the event when the admin checkbox is clicked.
     *
     * @param event The action event triggered by the checkbox click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_admincheckboxClicked(ActionEvent event) throws IOException{
		
	}
	/**
     * Handles the event when the student checkbox is clicked.
     *
     * @param event The action event triggered by the checkbox click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_studentcheckboxClicked(ActionEvent event) throws IOException {
		
	}
	/**
     * Handles the event when the instructor checkbox is clicked.
     *
     * @param event The action event triggered by the checkbox click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_instructorcheckboxClicked(ActionEvent event) throws IOException {
		
	}
	/**
     * Handles the event when the reset account button is clicked.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue during the operation
	 * @throws SQLException 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws NoSuchPaddingException 
	 * @throws InvalidKeyException 
     */
	@FXML
	public void al_resetaccountbuttonClicked(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
        String username = al_username.getText(); 
        
		//find user, generate random password, replace database password with random password, 
        //set the temppass flag to 1, set time to when button was pressed
		//print password
 
        //check if user exist returns true, return if it doesn't
        boolean ifuserexistflag = storage.doesUserExist(username);
        if (!ifuserexistflag) {
        	System.out.println("no user found");
        	return;
        }
        
		Random rand = new Random();
		StringBuilder codeBuilder = new StringBuilder();
		String ALPHABET = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		while (codeBuilder.length() < 15) {
			// Generate a random index into the alphabet
			int idx = (int) (rand.nextFloat() * ALPHABET.length());
			// Place that character into the code
			codeBuilder.append(ALPHABET.charAt(idx));
		}
		String code = codeBuilder.toString();
		System.out.println(code);
	
		
		System.out.println("updated temppass");
		storage.updateTempPass(username, code);
		
	}
	  /**
     * Handles the event triggered by clicking the delete user button.
     * Retrieves the username from the input field, verifies if the user exists
     * in the storage, and if found, loads the DeleteConfirmation.fxml view.
     * If the user does not exist, it prints a message to the console.
     *
     * @param event The ActionEvent triggered by clicking the delete user button
     * @throws IOException if the DeleteConfirmation.fxml file cannot be loaded
     * @throws SQLException if there is an error querying the storage database
     */
	@FXML
	public void al_deleteuserbuttonClicked(ActionEvent event) throws IOException, SQLException{
		String username = al_deleteusername.getText();
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData();
		
		Boolean userexistflag = storage.doesUserExist(username);
		if (!userexistflag) {
			System.out.println("no user found");
			return;
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DeleteConfirmation.fxml"));
		root = loader.load();
		
		DeleteConfirmationController controller = loader.getController();
		controller.userName(this.al_deleteusername.getText());
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	/**
     * Handles the event when the invitation button is clicked.
     * Generates a one-time code for user roles and registers it in the storage.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue during the operation
     */
	@FXML
	public void al_invitationbuttonClicked(ActionEvent event) throws IOException {
		// Get the current stage and storage object
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
		//get random code
		Random rand = new Random();
		StringBuilder codeBuilder = new StringBuilder();
		String ALPHABET = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		while (codeBuilder.length() < 15) {
			// Generate a random index into the alphabet
			int idx = (int) (rand.nextFloat() * ALPHABET.length());
			// Place that character into the code
			codeBuilder.append(ALPHABET.charAt(idx));
		}
		String code = codeBuilder.toString();
		
		//get the current time
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String date = time.format(fmt);
		
		//get the role indicated
		String roles = "";
		if (al_admincheckbox.isSelected()) {
			roles += "A";
		}
		if (al_studentcheckbox.isSelected()) {
			roles += "S";
		}
		if (al_instructorcheckbox.isSelected()) {
			roles += "I";
		}
		
		if (roles.equals("")) {
			System.out.println("You must select at least one role to generate a One Time Password");
			return;
		}
		System.out.println(code);
		System.out.println(date);
		System.out.println(roles);
		// Register the one-time code in the storage
		try {
			this.storage.registerOneTimeCode(code, date, roles);
			System.out.println("One Time Code Registered");
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}
	 /**
     * Handles the event when the logout button is clicked.
     * Loads the main scene and displays it.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue loading the main scene
     */
	@FXML
	public void al_logoutClicked(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
		//AdminLoginController controller = loader.getController();
		//controller.userName(username);
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	 /**
     * Sets the user name label to display the currently logged-in user.
     *
     * @param name The name of the user to be displayed
     */
	public void userName(String name) {
		al_userLabel.setText("User: " + name);
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
	
	/**
     * Handles the action triggered by clicking the "Articles" button. Loads the 
     * ManageArticles.fxml view, passes the current username to the 
     * ManageArticleController, and displays the scene.
     *
     * @param event The ActionEvent triggered by clicking the "Articles" button
     * @throws IOException if the ManageArticles.fxml file cannot be loaded
     */
	@FXML
	public void al_toArticlesClicked(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ManageArticles.fxml"));
		root = loader.load();
		
		ManageArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.setPrivkey(privkey);
		controller.cameFrom("Admin");
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	/**
     * Handles the action triggered by clicking the "Special Access Groups" button. Loads the 
     * ManageArticles.fxml view, passes the current username to the 
     * ManageArticleController, and displays the scene.
     *
     * @param event The ActionEvent triggered by clicking the "Articles" button
     * @throws IOException if the ManageArticles.fxml file cannot be loaded
	 * @throws SQLException 
     */
	@FXML
	public void al_toSpecialAccessClicked(ActionEvent event) throws IOException, SQLException {
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
		controller.cameFrom("Admin");
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void al_createSAGClicked(ActionEvent event) throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
        // if you are adding yourself, just add yourself
        // if you are not adding yourself, add yourself then add them
     	String groupname = al_csName.getText();
     	String user = al_csUser.getText();
        
        if (user.equals(myusername)) {
        	if (!this.storage.createSpecialAccessGroup(groupname, user)) {
    			return;
    		}
        } else {
        	if (!this.storage.createSpecialAccessGroup(groupname, myusername)) {
    			return;
    		}
    		if (!this.storage.addUserToSpecialAccessGroup(groupname, user, myusername, privkey)) {
    			return;
    		}
        }
		System.out.println("Added " + user + " to group " + groupname + "!");
	}
	    /**
     * Handles the action when the "Search" button is clicked.
     * Retrieves the search term from the search bar, queries the database for articles
     * with titles that match the search term, and prints each matching article's information
     * to the console. If no matches are found, it notifies the user in the console.
     *
     * @param event The ActionEvent triggered by clicking the "Search" button
     * @throws IOException if an I/O error occurs during database operations
     * @throws SQLException if there is an error executing the database query
     */
	@FXML
	public void al_searchButtonClicked(ActionEvent event) throws IOException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		String searchterm = al_searchBar.getText();
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
