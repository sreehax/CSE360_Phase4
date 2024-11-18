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
	
	@FXML public void initialize() {
		// Make decisions on what to hide based on the current role.
	}
	
	@FXML
	public void SelectGroupToManageComboBoxClicked(ActionEvent event) throws IOException{
		
	}
	
	@FXML
	public void AddUserToGroupTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	
	@FXML
	public void DeleteUserFromGroupTextFieldClicked(ActionEvent event) throws IOException{
		
	}
	
	@FXML
	public void ListAllUsersInGroupButtonClicked(ActionEvent event) throws IOException{
		
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
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		} else if (this.camefrom.equals("Instructor")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InstructorLogin.fxml"));
			root = loader.load();
			
			InstructorLoginController controller = loader.getController();
			controller.userName(myusername);
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		} else if (this.camefrom.equals("Student")) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentLogin.fxml"));
			root = loader.load();
			
			InstructorLoginController controller = loader.getController();
			controller.userName(myusername);
			
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
     * Sets the origin location indicating where the user navigated from.
     * This can be used to determine the navigation behavior based on the previous page.
     *
     * @param loc The name of the location or page the user came from
     */
	public void cameFrom(String loc) {
		this.camefrom = loc;
	}
	
}
