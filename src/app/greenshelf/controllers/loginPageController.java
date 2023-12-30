package app.greenshelf.controllers;
import java.io.IOException;

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
        System.out.println("Login button clicked!");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        String passwordFromDatabase = databaseAdapter.loginUserSql(usernameField.getText());
        if (passwordFromDatabase.equals(passwordField.getText())) {
            System.out.println("Login successful!");
            try {
                Parent root = FXMLLoader.load(getClass().getResource("../fxml/mainPage.fxml"));
                stage = (Stage) loginButton.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            } 
        } else {
            System.out.println("Login failed!");
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
}