import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentLoginController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	@FXML
	private Text sl_userLabel;
	
	@FXML
	public void sl_logoutClicked(ActionEvent event) throws IOException{
		
	}
	
	public void userName(String name) {
		this.sl_userLabel.setText("User: " + name);
	}
}
