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

public class MainSceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private TextField mainscene_usernameid, mainscene_passwordid, mainscene_onetimeinviteid;
	
	@FXML
	public void LoginButtonClicked(ActionEvent event) throws IOException {
		
		boolean flag = false;
		String username, password;
		
		username = mainscene_usernameid.getText();
		password = mainscene_passwordid.getText();
		
		//SHA-256 the password
		try {
			String SHAPass = getSHA(password);
			password = SHAPass;
			System.out.println(password);
			
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println("Exception thrown for incorrect algorithm " + e);
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

		root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void OTCButtonClicked(ActionEvent event) throws IOException {
		System.out.println("One Time Code Button Clicked");
	}
	
	
	
	
	
	
	
	
	
	
	
	//SHA stuff, testing phase (move this somewhere else)
	public static String getSHA(String input) throws NoSuchAlgorithmException {
	  	MessageDigest md = MessageDigest.getInstance("SHA-256");
	  	
	  	byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
	  	BigInteger number = new BigInteger(1, hash);
	  	StringBuilder hexString = new StringBuilder(number.toString(16));
	  	while (hexString.length() < 64 ) {
	  		hexString.insert(0,  '0');
	  	}
	  	return hexString.toString();
	 }
	
}