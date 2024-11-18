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
	
	private ComboBox SelectGroupToManageComboBox;
	private TextField AddUserToGroupTextField, DeleteUserFromGroupTextField;
	private Button ListAllUsersInGroupButton, BackButton;
	
	public void SelectGroupToManageComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	
	public void AddUserToGroupTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	
	public void DeleteUserFromGroupTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	
	public void ListAllUsersInGroupButtonClicked(ActionEvent event) throws IOException{
		
	}
	
	public void BackButtonClicked(ActionEvent event) throws IOException{
		
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
     * Sets the origin location indicating where the user navigated from.
     * This can be used to determine the navigation behavior based on the previous page.
     *
     * @param loc The name of the location or page the user came from
     */
	public void cameFrom(String loc) {
		this.camefrom = loc;
	}
	
}
