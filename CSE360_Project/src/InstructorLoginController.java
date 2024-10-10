import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class InstructorLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	@FXML
	private Text ip_userLabel;
	
	@FXML
	public void ip_logoutClicked(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	public void userName(String name) {
		this.ip_userLabel.setText("User: " + name);
	}
}