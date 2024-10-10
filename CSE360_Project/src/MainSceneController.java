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

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainSceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	
	@FXML
	private TextField mainscene_usernameid, mainscene_passwordid, mainscene_onetimeinviteid;
	
	public void initialize() {
		
	}
	
	
	@FXML
	public void LoginButtonClicked(ActionEvent event) throws IOException {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData();
		
		boolean flag = false;
		String username, password;
		
		username = mainscene_usernameid.getText();
		password = mainscene_passwordid.getText();
		
		// Test the password
		try {
			
			flag = this.storage.loginAttempt(username, password);
			if (flag) {
				System.out.println("Login succeeded!");
				
				//check user setup
				if (this.storage.userSetup(username)) {
					//load account set up page
					//FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveAccountInfo.fxml"));
					//root = FXMLLoader.load(getClass().getResource("SaveAccountInfo.fxml"));
		            
					stage.setUserData(storage);
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("SaveAccountInfo.fxml"));
					root = loader.load();
					
					SaveAccountInfoController controller = loader.getController();
					controller.currentUser(username);
					
			        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			        scene = new Scene(root);
			        stage.setScene(scene);
			        stage.show();
					
				}
				else {
					//log in
					
					stage.setUserData(storage);
					
					FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
					root = loader.load();
					
					AdminLoginController controller = loader.getController();
					controller.userName(username);
					
			        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			        scene = new Scene(root);
			        stage.setScene(scene);
			        stage.show();
					
					
				}
				
				
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
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData();
		
		String code = mainscene_onetimeinviteid.getText();
		
		//check database to see if one time code entered exists
		boolean flag = storage.doesCodeExist(code);
		String time = storage.getTimeFromCode(code);
		if (!this.within24Hours(time)) {
			System.out.println("Code expired!");
			return;
		}
		
		//check if it's already in use
		boolean flag2 = storage.isCodeAlreadyInUse(code);
		if (flag2) {
			System.out.println("Code is already used");
			return;
		}
		
		//if it exist load registration.
		if (flag) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
			root = loader.load();
			
			CreateAccountController controller = loader.getController();
			controller.onetimeinvite(code);
			
	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
		}
		else {
			System.out.println("no code exist on database");
		}
		
	}
	
	private boolean within24Hours(String time) {
		LocalDateTime currentTime = LocalDateTime.now();
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		LocalDateTime inputTime = LocalDateTime.parse(time, fmt);
		long secondsElapsed = ChronoUnit.SECONDS.between(inputTime, currentTime);
		return secondsElapsed < 24 * 60 * 60;
	}
}