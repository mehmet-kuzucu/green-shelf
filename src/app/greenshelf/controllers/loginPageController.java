package app.greenshelf.controllers;
import java.io.IOException;
import java.lang.ScopedValue.Carrier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import app.greenshelf.Admin;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.User;
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
    private String style = "-fx-background-radius: 20; -fx-border-radius: 20;-fx-padding: 8;-fx-font-size: 14px; -fx-border-color: linear-gradient(to right, #00fff2, #1900ff)-fx-background-color: linear-gradient(to right, #e6e6e6, #ffffff);";


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
    void loginButtonOnMouseClicked(MouseEvent event) throws SQLException {
        usernameField.setStyle(style);
        passwordField.setStyle(style);
        errorText.setText("");
        System.out.println("Login button clicked!");
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
    
        List<String> userInformation = databaseAdapter.loginUserSql(usernameField.getText());
    
        if (userInformation != null) {
            String userType = userInformation.get(9);
            String password = userInformation.get(7);
    
            if ("customer".equals(userType) && password.equals(passwordField.getText())) {
                System.out.println("Customer login successful!");
                //promote user to customer
                Customer c = createCustomer(userInformation);
                loadScene("../fxml/customerHome.fxml",c);
            } else if ("admin".equals(userType) && password.equals(passwordField.getText())) {
                System.out.println("Admin login successful!");
                //promote user to admin
                Admin a = createAdmin(userInformation);
                loadScene("../fxml/adminHomePage.fxml",null);
            } else if ("carrier".equals(userType) && password.equals(passwordField.getText())) {
                System.out.println("Carrier login successful!");
                //promote user to carrier
                //Carrier c = createCarrier(userInformation);
                loadScene("../fxml/carrierHomePage.fxml",null);
            } else {
                System.out.println("Invalid username, password, or user type.");
                passwordField.setStyle("-fx-border-color: red;");
                errorText.setText("Invalid password");
            }
        } else {
            System.out.println("Username not found in the database.");
            usernameField.setStyle("-fx-border-color: red;");
            errorText.setText("Username not found in the database.");
        }
    }

    //create customer object
    Customer createCustomer(List<String> list) throws SQLException
    {
        Customer c = new Customer(list.get(5), list.get(6), list.get(7), list.get(2), list.get(3), list.get(1), list.get(4), list.get(8));
        return c;
    }

    //create admin object
    Admin createAdmin(List<String> list) throws SQLException
    {
        Admin a = new Admin(list.get(5), list.get(6), list.get(7), list.get(2), list.get(3), list.get(1));
        return a;
    }

    //TODO: carrier classı oluşturulduğunda bu fonksiyonu da yaz
    Carrier createCarrier(List<String> list) throws SQLException
    {
        //Carrier c = new Carrier(r.getString("name"), r.getString("surname"), r.getString("password"), r.getString("email"), r.getString("phone"), r.getString("username"), r.getString("userType"), r.getString("userID"));
        return null;
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

    private void loadScene(String fxmlPath, Customer user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                //System.out.println("öncesi: " + user.getUsername());
                controller.initData(user); // Pass the User object to the controller
            }
            
            stage = (Stage) loginButton.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
