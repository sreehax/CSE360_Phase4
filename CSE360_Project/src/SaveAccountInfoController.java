import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class SaveAccountInfoController {
	
	private String username;
	
	@FXML
	private TextField firstnameid, middlenameid, lastnameid, preferrednameid, emailid;
	
	
	public void currentUser(String username) {
		this.username = username;
	}
	
	@FXML
	public void save_account_button_buttonClicked(ActionEvent event) throws IOException, SQLException{
		System.out.println("Clicked save account");
		
		String firstname, middlename, lastname, preferredname, email;
		firstname = firstnameid.getText();
		middlename = middlenameid.getText();
		lastname = lastnameid.getText();
		preferredname = preferrednameid.getText();
		email = emailid.getText();
		
		if (firstname.equals("")) {
			System.out.println("you must have a first name");
			return;
		}
		if (middlename.equals("")) {
			System.out.println("you must have a middle name");
			return;
		}
		if (lastname.equals("")) {
			System.out.println("you must have a last name");
			return;
		}
		if (email.equals("")) {
			System.out.println("you must input an email");
			return;
		}
		if (preferredname.equals("")) {
			preferredname = firstname;
		}
		
		Storage s = new Storage();
		s.updateUser(username, firstname, middlename, lastname, preferredname, email);
		
		
	}
	@FXML
	public void save_account_backClicked(ActionEvent event) throws IOException {
		System.out.println("Clicked back button");
	}
}
