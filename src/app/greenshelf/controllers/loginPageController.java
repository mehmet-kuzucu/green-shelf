package app.greenshelf.controllers;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import app.greenshelf.DatabaseAdapter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class loginPageController {

    private Stage stage;
    private Scene scene;
    

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Button loginButton;

    @FXML
    void loginButtonOnMouseClicked(MouseEvent event) {
        usernameField.setStyle("-fx-border-color: transparent;");
        passwordField.setStyle("-fx-border-color: transparent;");
        System.out.println("Login button clicked!");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
    
        Map<String, String> userInformation = databaseAdapter.loginUserSql(usernameField.getText());
    
        if (userInformation != null) {
            String userType = userInformation.get("userType");
            String password = userInformation.get("password");
    
            if ("customer".equals(userType) && password.equals(passwordField.getText())) {
                System.out.println("Customer login successful!");
                loadScene("../fxml/customerHome.fxml");
            } else if ("admin".equals(userType) && password.equals(passwordField.getText())) {
                System.out.println("Admin login successful!");
                loadScene("../fxml/adminHomePage.fxml");
            } else if ("carrier".equals(userType) && password.equals(passwordField.getText())) {
                System.out.println("Carrier login successful!");
                loadScene("../fxml/carrierHomePage.fxml");
            } else {
                System.out.println("Invalid username, password, or user type.");
                passwordField.setStyle("-fx-border-color: red;");
            }
        } else {
            System.out.println("Username not found in the database.");
            usernameField.setStyle("-fx-border-color: red;");
        }
    }
    
    @FXML
    void registerLinkOnMouseClicked(MouseEvent event) {
        System.out.println("Register link clicked!");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/registerPage.fxml"));
            stage = (Stage) registerLink.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage = new Stage();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
