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

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AdminLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	@FXML
	private Text al_userLabel;
	@FXML
	private CheckBox al_admincheckbox, al_studentcheckbox, al_instructorcheckbox;
	
	
	@FXML
	public void initialize() {
		
	}
	
	@FXML
	public void al_admincheckboxClicked(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void al_studentcheckboxClicked(ActionEvent event) throws IOException {
		
	}
	@FXML
	public void al_instructorcheckboxClicked(ActionEvent event) throws IOException {
		
	}
	
	@FXML
	public void al_resetaccountbuttonClicked(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void al_deleteuserbuttonClicked(ActionEvent event) throws IOException{
		
	}
	@FXML
	public void al_invitationbuttonClicked(ActionEvent event) throws IOException {
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        this.storage = (Storage) stage.getUserData();
        
		//get random code
		Random rand = new Random();
		StringBuilder codeBuilder = new StringBuilder();
		String ALPHABET = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		while (codeBuilder.length() < 15) {
			// Generate a random index into the alphabet
			int idx = (int) (rand.nextFloat() * ALPHABET.length());
			// Place that character into the code
			codeBuilder.append(ALPHABET.charAt(idx));
		}
		String code = codeBuilder.toString();
		
		//get the current time
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String date = time.format(fmt);
		
		System.out.println(code);
		System.out.println(date);
		//get the role indicated
		System.out.println("Admin: " + al_admincheckbox.isSelected());
		System.out.println("Student: " + al_studentcheckbox.isSelected());
		System.out.println("Instructor: " + al_instructorcheckbox.isSelected());
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
