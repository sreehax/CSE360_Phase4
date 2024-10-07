import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class CreateAccountController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	public void create_account_buttonClicked(ActionEvent event) throws IOException {
		
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
