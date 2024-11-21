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
import javafx.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Authors: Felix Allison & Benjamin Nelson
/**
 * The EditArticleController class is responsible for handling the editing of existing articles.
 * It interacts with various UI components for article attributes such as title, references, 
 * headers, groups, description, keywords, and body, allowing the user to modify these fields 
 * and update the article in the database.
 */
public class GroupController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String myusername;
	private String camefrom;
	private Storage storage;
	private byte[] privkey;
	
	@FXML
	private ComboBox<String> SelectGroupToManageComboBox;
	@FXML
	private TextField AddUserToGroupTextField, DeleteUserFromGroupTextField;
	@FXML
	private Button ListAllUsersInGroupButton, BackButton, addUserButton, delUserButton, gc_createArticle;
	
	@FXML
	private Text addLabel, delLabel;
	
	
	@FXML
	public void SelectGroupToManageComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	
	@FXML
	public void AddUserToGroupTextFieldClicked(ActionEvent event) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		String userToAdd = AddUserToGroupTextField.getText();
		String group = this.SelectGroupToManageComboBox.getValue();
		if (userToAdd.equals(myusername)) {
			System.out.println("You can't add yourself to the group, you are already added!");
			return;
		}
		if (!this.storage.addUserToSpecialAccessGroup(group, userToAdd, myusername, privkey)) {
			return;
		}
		System.out.println("Successfully added " + userToAdd + " to group " + group + "!");
	}
	
	@FXML
	public void DeleteUserFromGroupTextFieldClicked(ActionEvent event) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SQLException{
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
		String userToDelete = AddUserToGroupTextField.getText();
		String group = this.SelectGroupToManageComboBox.getValue();
		// TODO: do some checking on group membership
		if (!this.storage.removeUserFromSpecialAccessGroup(group, userToDelete)) {
			System.out.println("Couldn't delete the user");
			return;
		}
		System.out.println("Successfully deleted user " + userToDelete + " to group " + group + "!");
	}
	
	@FXML
	public void gc_listAllUsersClicked(ActionEvent event) throws IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
		String group = this.SelectGroupToManageComboBox.getValue();
		ArrayList<String> users = this.storage.getUsersFromGroup(group);
		if (users.size() == 0) {
			System.out.println("No users in group " + group + ".");
			return;
		}
		
		System.out.println("Users in group " + group + ": ");
		for (String user : users) {
			System.out.println("- " + user);
		}
		System.out.println();
		
	}
	
	@FXML
	public void gc_createArticleClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateArticle.fxml"));
		root = loader.load();
		String group = this.SelectGroupToManageComboBox.getValue();
		
		CreateArticleController controller = loader.getController();
		controller.userName(myusername);
		controller.setPrivkey(privkey);
		controller.cameFrom(camefrom);
		controller.setSecureGroup(group);
		
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	/**
     * Handles the event when the back button is clicked.
     * Loads appropriate role screen.
     *
     * @param event The action event triggered by the button click
     * @throws IOException If there is an issue loading the main scene
	 * @throws SQLException 
     */
	@FXML
	public void gc_backClicked(ActionEvent event) throws IOException, SQLException {
		if (this.camefrom.equals("Admin")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
			root = loader.load();
			
			AdminLoginController controller = loader.getController();
			controller.userName(myusername);
			controller.setPrivkey(privkey);
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		} else if (this.camefrom.equals("Instructor")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InstructorLogin.fxml"));
			root = loader.load();
			
			InstructorLoginController controller = loader.getController();
			controller.userName(myusername);
			controller.setPrivkey(privkey);
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		} else if (this.camefrom.equals("Student")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentLogin.fxml"));
			root = loader.load();
			
			InstructorLoginController controller = loader.getController();
			controller.userName(myusername);
			controller.setPrivkey(privkey);
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		}
	}
	
	
	/**
     * Sets the user name label to display the currently logged-in user.
     *
     * @param name The name of the user to be displayed
     */
	public void userName(String name) {
//		ma_userLabel.setText("User: " + name);
		myusername = name;
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
     * Sets the origin location indicating where the user navigated from.
     * This can be used to determine the navigation behavior based on the previous page.
     *
     * @param loc The name of the location or page the user came from
     */
	public void cameFrom(String loc) {
		this.camefrom = loc;
		if (loc.equals("Instructor")) {
			// Since we are instructor, hide all the stuff they can't do
			addLabel.setVisible(false);
			AddUserToGroupTextField.setVisible(false);
			addUserButton.setVisible(false);
			
			delLabel.setVisible(false);
			DeleteUserFromGroupTextField.setVisible(false);
			delUserButton.setVisible(false);
		} else if (loc.equals("Admin")) {
			// What do we need to hide here?
			gc_createArticle.setVisible(false);
		}
	}
	
	public void populateGroups(ArrayList<String> specialAccessGroups) {
		this.SelectGroupToManageComboBox.setItems(FXCollections.observableArrayList(specialAccessGroups));
		this.SelectGroupToManageComboBox.getSelectionModel().selectFirst();
	}
	
}
