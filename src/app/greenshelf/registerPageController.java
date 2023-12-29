package app.greenshelf;

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

//import from parent directory



public class registerPageController {
    @FXML
    private TextField registerNameField;

    @FXML
    private TextField registerSurnameField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private PasswordField registerConfirmPasswordField;

    @FXML
    private Hyperlink existedAccount;

    @FXML
    private Button nextStepButton;

    @FXML
    void existedAccountOnMouseClicked(MouseEvent event) {
        System.out.println("Existed account clicked!");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/loginPage.fxml"));
            Stage stage = (Stage) existedAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void nextStepButtonOnMouseClicked(MouseEvent event) {
        System.out.println("Next step clicked!");
        Customer newCustomer = new Customer(registerNameField.getText(), registerSurnameField.getText(), registerPasswordField.getText(), "email", "phone", registerUsernameField.getText(), "customer");





        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/registerPage2.fxml"));
            Stage stage = (Stage) existedAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




}
