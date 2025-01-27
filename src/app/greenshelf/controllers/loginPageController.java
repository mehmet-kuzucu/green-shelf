package app.greenshelf.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import app.greenshelf.Admin;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Carrier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class loginPageController {

    private Stage stage;
    private Scene scene;
    private String style = "-fx-background-radius: 20; -fx-border-radius: 20;-fx-padding: 8;-fx-font-size: 14px; -fx-border-color: linear-gradient(to right, #00fff2, #1900ff); -fx-background-color: linear-gradient(to right, #e6e6e6, #ffffff);";

    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;

    @FXML
    private Hyperlink registerLink;

    @FXML
    private Button loginButton;

    @FXML
    private Text errorText;

    @FXML
    private void loginButtonOnMouseClicked(MouseEvent event) throws SQLException {
        performLogin();
    }

    @FXML
    private void performLogin() throws SQLException {
        usernameField.setStyle(style);
        passwordField.setStyle(style);
        errorText.setText("");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        
        List<String> userInformation = databaseAdapter.loginUserSql(usernameField.getText());
    
        if (userInformation != null) {
            String userType = userInformation.get(8);
            String password = userInformation.get(6);
            
    
            if ("customer".equals(userType) && password.equals(passwordField.getText())) {
                Customer c = createCustomer(userInformation);
                loadScene("../fxml/customerHome.fxml",c,null,null);
            } else if ("admin".equals(userType) && password.equals(passwordField.getText())) {
                Admin a = createAdmin(userInformation);
                loadScene("../fxml/adminHomePage.fxml",null,a,null);
            } else if ("carrier".equals(userType) && password.equals(passwordField.getText())) {
                Carrier c = createCarrier(userInformation);
                loadScene("../fxml/carrierHomePage.fxml",null,null,c);
            } else {
                passwordField.setStyle("-fx-border-color: red;");
                errorText.setText("Invalid password");
            }
        } else {
            usernameField.setStyle("-fx-border-color: red;");
            errorText.setText("Username not found in the database.");
        }
    }

    private Customer createCustomer(List<String> list) throws SQLException
    {
        Customer c = new Customer(list.get(4), list.get(5), list.get(6), list.get(1), list.get(2), list.get(0), list.get(3), list.get(7));
        return c;
    }

    private Admin createAdmin(List<String> list) throws SQLException
    {
        Admin a = new Admin(list.get(4), list.get(5), list.get(6), list.get(1), list.get(2), list.get(0));
        return a;
    }

    private Carrier createCarrier(List<String> list) throws SQLException
    {
        Carrier c = new Carrier(list.get(4), list.get(5), list.get(6), list.get(1), list.get(2), list.get(0), list.get(3), list.get(7));
        return c;
    }
    
    @FXML
    private void registerLinkOnMouseClicked(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/registerPage.fxml"));
            stage = (Stage) registerLink.getScene().getWindow();
            scene = new Scene(root, registerLink.getScene().getWidth(), registerLink.getScene().getHeight());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private void loadScene(String fxmlPath, Customer user, Admin admin, Carrier carrier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                controller.initData(user); 
            }
            if (fxmlPath.equals("../fxml/adminHomePage.fxml")) {
                adminHomePageController controller = loader.getController();
                controller.initData(admin,root,controller); 
            }
            if (fxmlPath.equals("../fxml/carrierHomePage.fxml")) {
                carrierHomePageController controller = loader.getController();
                controller.initData(carrier); 
            }
            
            stage = (Stage) loginButton.getScene().getWindow();
            scene = new Scene(root,loginButton.getScene().getWidth(),loginButton.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
