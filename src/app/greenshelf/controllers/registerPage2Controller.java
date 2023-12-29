package app.greenshelf.controllers;

import java.io.IOException;
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

public class registerPage2Controller {

    @FXML
    private Hyperlink existedAccount;

    @FXML
    private Button registerButton;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private PasswordField registerPasswordField1;

    @FXML
    private TextField registerUsernameField1;

    @FXML
    private TextField registerUsernameField2;

    @FXML
    void registerButtonOnMouseClicked(MouseEvent event) {
        System.out.println("Register clicked!");



        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/customerHome.fxml"));
            Stage stage = (Stage) existedAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
