import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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

public class ResetPasswordController {
	//rp_backClicked
	//rp_confirmClicked
	
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String username;
	
	@FXML
	private TextField rp_txtfield;
	
	@FXML
	public void rp_backClicked(ActionEvent event) throws IOException{ 
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
		MainSceneController controller = loader.getController();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void rp_confirmClicked(ActionEvent event) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SQLException{ 
		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		storage = (Storage) stage.getUserData(); 
		//needs to update database with selected password and reset flag
		String newPass = rp_txtfield.getText();
		System.out.println(newPass);
		
		if (newPass.equals("")) {
			System.out.println("you need to input a password");
			return;
		}
		else {
			storage.updateMainPass(this.username, newPass);
			System.out.println("Update Completed");
		}
		
	}
	
	public void setdata(String username) {
		this.username = username;
	}
}
