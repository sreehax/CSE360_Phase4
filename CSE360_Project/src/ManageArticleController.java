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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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

/**
 * The AdminLoginController class manages the admin login interface,
 * including user interactions for account management and invitation code generation.
 * It handles the setup of the admin panel, allowing the admin to reset accounts,
 * delete users, and create invitation codes for different user roles.
 */
public class ManageArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String myusername;
	
	@FXML
	private Text ma_userLabel;
	
	@FXML
	private TextField ma_backup, ma_backupGroup, ma_restore, ma_filterGroups;
	
	@FXML
	private Button ma_back, ma_makeArticle, ma_makeBackup, ma_doMerge, ma_doOverwrite, ma_list, ma_listGroup ;
	
	/**
     * Initializes the controller after the root element has been processed.
     */
	@FXML
	public void initialize() {
		
	}
	
	
	/**
     * Takes user to create Article page
     */
	@FXML
	public void al_makeArticlesClicked(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateArticle.fxml"));
		root = loader.load();
		
		CreateArticleController controller = loader.getController();
		controller.userName(myusername);
		
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	/**
     * Creates Backup file 
	 * @throws SQLException 
     */
	@FXML
	public void ma_makeBackupClicked(ActionEvent event) throws IOException, SQLException {
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		
		//Code to create backup files
		//If the backup group file is texfield is empty, backup all articles
		String backupfile = ma_backup.getText();
		String group = ma_backupGroup.getText();
		if (backupfile.isEmpty()) {
			System.out.println("Error: specify a file to backup to!");
			return;
		}
		
		// Get the articles to backup depending on the group scope
		ArrayList<Article> articles;
		if (group.isEmpty()) {
			articles = this.storage.listAllArticles();
		} else {
			articles = this.storage.listArticlesByGroup(group);
		}
		
		// Back them up to a file
		String abspath = this.storage.backupArticles(articles, backupfile);
		
		System.out.println("Created backup at " + abspath + "!");
	}
	
	/**
     * Performs a merge restore based on inputed file name 
	 * @throws SQLException 
     */
	@FXML
	public void ma_doMergeClicked(ActionEvent event) throws IOException, SQLException {
		
		//Code merge files, if two files have the same ID, keep the old one
		String mergeFile = ma_restore.getText();
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		
        ArrayList<Article> articles = this.storage.restoreArticles(mergeFile);
        
        int merged = 0;
        for (Article a : articles) {
        	// If there is no article stored with the same ID as one we restored,
        	// then we should place it into our storage
        	if (this.storage.getArticleByID(a.getID()) == null) {
        		this.storage.addArticle(a);
        		merged += 1;
        	}
        	// else, skip over it
        }
        System.out.println("Read in " + articles.size() + " articles, merged in " + merged + " of them.");
	}
	
	/**
     * Performs an overwrite restore based on inputed file name 
	 * @throws SQLException 
     */
	@FXML
	public void ma_doOverwriteClicked(ActionEvent event) throws IOException, SQLException {
		
		//Overwrites all existing help articles and replaces them with backup
		String restoreFile = ma_restore.getText();
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		ArrayList<Article> articles = this.storage.restoreArticles(restoreFile);
		
		// nuke our articles
		this.storage.deleteAllArticles();
		
		// Import in all the articles
		for (Article a : articles) {
			this.storage.addArticle(a);
		}
		System.out.println("Read in " + articles.size() + " articles.");
	}
	
	/**
     * Lists all articles in the database
	 * @throws SQLException 
     */
	@FXML
	public void ma_listClicked(ActionEvent event) throws IOException, SQLException {
		
		//Code to list all articles in database
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        ArrayList<Article> articles = this.storage.listAllArticles();
        
        for (Article a : articles) {
        	a.printInfo();
        }
	}
	
	/**
     * Lists articles based on inputed group
	 * @throws SQLException 
     */
	@FXML
	public void ma_listGroupClicked(ActionEvent event) throws IOException, SQLException {
		
		//Code to list articles based on group
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
        String group = this.ma_filterGroups.getText();
        ArrayList<Article> articles = this.storage.listArticlesByGroup(group);
        
        if (articles.size() == 0) {
        	System.out.println("No articles found by that group");
        	return;
        }
        for (Article a : articles) {
        	a.printInfo();
        }
		
	}
	
	/**
     * Handles the event when the back button is clicked.
     * Loads appropriate role screen.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue loading the main scene
     */
	@FXML
	public void ma_backClicked(ActionEvent event) throws IOException {
		
		//This method should load either the admin or instructor page based on what the role is for the current user
		
		//FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		//root = loader.load();
		
		//AdminLoginController controller = loader.getController();
		//controller.userName(username);
		
        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        //scene = new Scene(root);
        //stage.setScene(scene);
        //stage.show();
	}
	 /**
     * Sets the user name label to display the currently logged-in user.
     *
     * @param name The name of the user to be displayed
     */
	public void userName(String name) {
		ma_userLabel.setText("User: " + name);
		myusername = name;
	}
	

}
