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
import javafx.scene.control.ChoiceBox;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class MainSceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	public MainSceneController() throws IOException, SQLException {
		this.storage = new Storage();
	}
	
	
	@FXML
	private TextField mainscene_usernameid, mainscene_passwordid, mainscene_onetimeinviteid;
	
	@FXML
	public void LoginButtonClicked(ActionEvent event) throws IOException {
		
		boolean flag = false;
		String username, password;
		
		username = mainscene_usernameid.getText();
		password = mainscene_passwordid.getText();
		
		// Test the password
		try {
			
			flag = this.storage.loginAttempt(username, password);
			if (flag) {
				System.out.println("Login succeeded!");
			} else {
				System.out.println("Login failed :(");
			}
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Exception thrown for incorrect algorithm " + e);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace(System.err);
		}
		
		
		//very beautiful pseudocode
		
		//flag = Database-object-name-whatever-it-is.login(username, password);
		//if (flag == true) {
			//root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			
			//passing information with the following
			//	root = loader.load();
			//	MainSceneController controller = loader.getController();
			//	controller.changeUser(username); //have to add a function inside of MainSceneController that does .setText stuff!
			
				
	        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        //scene = new Scene(root);
	        //stage.setScene(scene);
	        //stage.show();
		//} else {sysout "no user match"}
	}
	
	@FXML
	public void CreateAccountClicked(ActionEvent event) throws IOException {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
		root = loader.load();
		
		CreateAccountController controller = loader.getController();
		controller.deletenousermsg();
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void OTCButtonClicked(ActionEvent event) throws IOException, SQLException {
		System.out.println("One Time Code Button Clicked");
		this.storage.deleteTables();
	}
	
}