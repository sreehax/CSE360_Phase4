import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class CreateAccountController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean admincreation = true;
	
	
	
	@FXML
	private TextField ca_usernameid, ca_passwordid, ca_password2id;
	@FXML
	private Text ca_nouserstxt;
	
	@FXML
	public void create_account_buttonClicked(ActionEvent event) throws IOException {
		boolean flag = false;
		String username, password, password2;
		
		username = ca_usernameid.getText();
		password = ca_passwordid.getText();
		password2 = ca_password2id.getText();
		
		// Passwords should match
		// TODO: Show some better feedback
		if (!password.equals(password2)) {
			System.out.println("Passwords must match!");
			return;
		};
		
		// Register the user in "logins" and "user_info"
		User user = new User();
		user.setUsername(username);
		if(admincreation) {
			user.addRole('a');
		}
		
		try {
			Storage s = new Storage();
			s.registerLogin(username, password);
			s.registerUser(user);
			System.out.println("Successfully registered user!");
		} catch (SQLException e) {
			System.out.println("User already exists!");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		
		this.admincreation = false;
	}
	
	@FXML
	public void create_account_backClicked(ActionEvent event) throws IOException {
		if (this.admincreation) {
			ca_nouserstxt.setText("You have to register an admin first");
			return;
		}
		root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	public void deletenousermsg() {
		ca_nouserstxt.setText("");
		this.admincreation = false;
	}
}
