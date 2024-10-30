import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;


//
public class CreateArticleController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private TextField titleTextField, referenceLinksTextField, headersTextField, groupsTextField, descriptionTextField, bodyTextField;
	private Button createArticleButton;
	
	String titleString, referenceString, headersString, groupsString, descriptionString, bodyString;
	
	public void pressCreateArticleButton(ActionEvent event) throws IOException {
		titleString = titleTextField.getText();
		referenceString = referenceLinksTextField.getText();
		headersString = headersTextField.getText();
		groupsString = groupsTextField.getText();
		descriptionString = descriptionTextField.getText();
		bodyString = bodyTextField.getText();
		
		//NEED TO ADD SOMETHING THAT UPLOADS THIS INFO TO THE DATABASE
		
	}
	
}
