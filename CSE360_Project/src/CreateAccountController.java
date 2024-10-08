import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class CreateAccountController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private TextField ca_usernameid, ca_passwordid, ca_password2id;
	
	@FXML
	public void create_account_buttonClicked(ActionEvent event) throws IOException {
		boolean flag = false;
		String username, password, password2;
		
		username = ca_usernameid.getText();
		password = ca_passwordid.getText();
		password2 = ca_password2id.getText();
		
		//SHA-256 the password
		try {
			// The .getSHA function needs to be moved to somewhere that's not the MainSceneController
			password = MainSceneController.getSHA(password);
			password2 = MainSceneController.getSHA(password2);
			
			System.out.println(password);
			System.out.println(password2);
			
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println("Exception thrown for incorrect algorithm " + e);
		}
		
	}
	
	@FXML
	public void create_account_backClicked(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
}
