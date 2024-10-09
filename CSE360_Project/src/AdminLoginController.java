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


public class AdminLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private Text al_userLabel;
	@FXML
	private ChoiceBox al_choicebox;
	
	ObservableList<String> list = FXCollections.observableArrayList("Admin","Student","Instructor");
	
	@FXML
	public void initialize() {
		al_choicebox.setItems(list);
	}
	
	
	@FXML
	public void al_resetaccountbuttonClicked(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void al_deleteuserbuttonClicked(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void al_invitationbuttonClicked(ActionEvent event) throws IOException {
		//get random code
		//get the current time
		//get the role indicated
		
		
	}
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
	
	public void userName(String name) {
		al_userLabel.setText("User: " + name);
	}
	
}
