import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SelectRoleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String username;
	
	private ArrayList<Role> list;
	
	@FXML
	private Text sr_userLabel;
	@FXML
	private ChoiceBox sr_combobox;
	
	@FXML
	private void initialize() {
	}
	
	@FXML
	public void sr_selectSessionClicked(ActionEvent event) throws IOException {
		System.out.println("selectsessionclicked");
		String selectedRole = (String) sr_combobox.getValue();
		System.out.println(selectedRole);
		//check session view selected and change scene based on selection
		
		
	}
	@FXML
	public void sr_logoutClicked(ActionEvent event) throws IOException {
		System.out.println("logoutclicked");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
		root = loader.load();
		
		MainSceneController controller = loader.getController();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
	public void changeUsername(String username) {
		this.username = username;
		sr_userLabel.setText("User: " + username);
	}
	
	public void addroles(ArrayList<Role> list) {
		this.list = list;
		System.out.println("list size: " + this.list.size());
		
		ArrayList<String> test = new ArrayList<String>();
		Role temp;
		
		for(int i = 0; i < list.size();i++) {
			temp = list.get(i);
			switch (temp) {
			case ADMIN:
				test.add("Admin");
				break;
			case INSTRUCTOR:
				test.add("Instructor");
				break;
			case STUDENT:
				test.add("Student");
				break;
			}
		}
		
		ObservableList<String> rolelist = FXCollections.observableArrayList(test);
		sr_combobox.setItems(rolelist);
	}
}
