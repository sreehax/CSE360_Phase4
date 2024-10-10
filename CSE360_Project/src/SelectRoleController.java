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
/**
 * Controller class for selecting a user role.
 * Manages role selection and navigation to different login views based on the selected role.
 */
public class SelectRoleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	private String username;
	
	private ArrayList<Role> list;
	 /** Text label to display the username. */
	@FXML
	private Text sr_userLabel;
	/** Choice box for selecting the user's role. */
	@FXML
	private ChoiceBox sr_combobox;
	/**
     * Initializes the controller class. This method is called after the FXML file has been loaded.
     */
	@FXML
	private void initialize() {
	}
	/**
     * Handles the event when the user clicks the "Select Session" button.
     * Loads the appropriate login view based on the selected role.
     *
     * @param event The event triggered by clicking the button.
     * @throws IOException If the FXML file for the selected role cannot be loaded.
     */
	@FXML
	public void sr_selectSessionClicked(ActionEvent event) throws IOException {
		//check session view selected and change scene based on selection
		String selectedRole = (String) sr_combobox.getValue();
		System.out.println(selectedRole);
		// Load the appropriate FXML file and set the scene based on the selected role
		if (selectedRole.equals("Admin")) {
			System.out.println("admin selected");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
			root = loader.load();
			
			AdminLoginController controller = loader.getController();
			controller.userName(username);

	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
			
		} else if (selectedRole.equals("Instructor")) {
			System.out.println("instructor selected");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InstructorLogin.fxml"));
			root = loader.load();
			
			InstructorLoginController controller = loader.getController();
			controller.userName(username);

	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
	        
		} else if (selectedRole.equals("Student")) {
			System.out.println("student selected");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("StudentLogin.fxml"));
			root = loader.load();
			
			StudentLoginController controller = loader.getController();
			controller.userName(username);

	        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
	        
		} else {
			System.out.println("Error, no role found");
		}
	}
	 /**
     * Handles the event when the user clicks the "Logout" button.
     * Navigates back to the main scene.
     *
     * @param event The event triggered by clicking the button.
     * @throws IOException If the FXML file for the main scene cannot be loaded.
     */
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
	/**
     * Sets the username of the user and updates the user label.
     *
     * @param username The username to set.
     */
	public void changeUsername(String username) {
		this.username = username;
		sr_userLabel.setText("User: " + username);
	}
	 /**
     * Populates the combo box with the available roles based on the given list of roles.
     *
     * @param list The list of roles available to the user.
     */
	public void addroles(ArrayList<Role> list) {
		this.list = list;
		System.out.println("list size: " + this.list.size());
		// Populate the combo box with the roles
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
