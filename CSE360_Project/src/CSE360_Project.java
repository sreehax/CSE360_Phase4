/*******
 * <p> CSE360_Project Class. </p>
 * 
 * <p> A JavaFX application that loads an FXML file to display the main user interface. 
 * This class is necessary for structuring the application’s lifecycle by extending {@link Application} and defining the main GUI window. </p>
 * 
 * <p> Copyright: © 2024 Team TU39 </p>
 * 
 * @author Team TU39
 * 
 * @version 1.00	2024-10-08 Initial implementation of a JavaFX project.
 */
//mainline created by Jonathan Lin 9/25/2024

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * CSE360_Project Class
 * 
 * This class extends JavaFX's {@link Application} to load and display the main user interface.
 */

public class CSE360_Project extends Application {

	 /**
     * Default constructor required by JavaFX.
     * 
     * <p> The default constructor ensures that the JavaFX framework can instantiate 
     * the class properly during the application lifecycle. This is necessary for 
     * setting up the application's main stage. </p>
     */
    public CSE360_Project() {
        super();
        // Default Constructor
    }
	  /**
     * The main entry point for the JavaFX application.
     *
     * @param primaryStage the main stage for this application.
	 * @throws SQLException 
     */
	@Override
	public void start(Stage primaryStage) throws SQLException {
		
		//go to create account if there are no users, otherwise go to main home page 
		Storage storage = new Storage();
		boolean flag = storage.printTable();
		storage.printTable2();
		if (flag) {
			System.out.println("entering main scene");
			try {
	            Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
	            Scene scene = new Scene(root);
	            primaryStage.setScene(scene);
	            primaryStage.show();
	            
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
		}
		else {
			try {
				Parent root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);;
				primaryStage.show();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	 /**
     * The main method that launches the JavaFX application.
     *
     * @param args the command-line arguments.
     */
	
	public static void main(String[] args) {
		System.out.println("starting process");
		
		launch(args);
	}
}

