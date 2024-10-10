import java.io.IOException;
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

public class DeleteConfirmationController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private Storage storage;
	private String username;
	
	@FXML
	private TextField dc_confirmtxtBox;
	
	public void userName(String name) {
		this.username = name;
		
	}
	
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
	public void dc_confirmbuttonClicked(ActionEvent event) throws IOException, SQLException {
		String input = this.dc_confirmtxtBox.getText();
		
		if (!input.equals("Yes")) {
			System.out.println("You must type in 'Yes' to delete!");
			return;
		}
		
		this.stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		this.storage = (Storage) stage.getUserData();
		try {
			this.storage.deleteUser(this.username);
			System.out.println("Deleted user " + this.username + " successfully!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
		root = loader.load();
		
		//pass in username so admin is still logged in
		
		
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
}