import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DeleteConfirmationController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	
	@FXML
	private TextField dc_confirmtxtBox;
	
	
	@FXML
	public void dc_backClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
		root = loader.load();
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	@FXML
	public void dc_confirmbuttonClicked(ActionEvent event) throws IOException {
		String input = this.dc_confirmtxtBox.getText();
		if (!input.equals("Yes")) {
			System.out.println("You must type in 'Yes' to delete!");
			return;
		}
		
		this.storage = (Storage) stage.getUserData();
		// delete the user here
	}
}