package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.ScopedValue.Carrier;
import java.util.Base64;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.greenshelf.*;


public class profileInfoPageController {

    private Stage stage;
    private Scene scene;
    
    @FXML
    private Button saveProfileButton;

    @FXML
    private ImageView profilePhotoImage;
    
    @FXML
    private Text nameDescription;

    @FXML
    private PasswordField changePasswordField;

    @FXML
    private TextField changePhoneField;

    @FXML
    private TextField changeAddressField;

    @FXML
    private TextField changeEmailField;

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private Text editErrorText;

    private Customer currentUser;


    public void initData(Customer user) {
        this.currentUser = user;
        nameDescription.setText(currentUser.getName() + " '" + currentUser.getUsername() + "' " + currentUser.getSurname());
        System.out.println(currentUser.getName());
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        changeEmailField.setText(currentUser.getEmail());
        changeAddressField.setText(currentUser.getAddress());
        changePhoneField.setText(currentUser.getPhone());
        changePasswordField.setText(currentUser.getPassword());
    }

    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/customerHome.fxml", currentUser,null,null);
    }

    private void loadScene(String fxmlPath, Customer user, Admin admin, Carrier carrier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                controller.initData(user); // Pass the User object to the controller
            }
            if (fxmlPath.equals("../fxml/adminHomePage.fxml")) {
                adminHomePageController controller = loader.getController();
                controller.initData(admin); // Pass the User object to the controller
            }
            
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void saveButtonOnMouseClicked(MouseEvent event)
    {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        registerPageController registerPageController = new registerPageController();

        String new_password = changePasswordField.getText();
        String new_email = changeEmailField.getText();
        String new_address = changeAddressField.getText();
        String new_phone = changePhoneField.getText();

        if(registerPageController.checkEmailisValid(changeEmailField) == false)
        {
            System.out.println("Email is not valid");
            editErrorText.setText("Email is not valid");
            changeEmailField.setStyle("-fx-border-color: red");
            return;
        }
        if(registerPageController.checkPhoneisValid(changePhoneField) == false)
        {
            System.out.println("Phone is not valid");
            editErrorText.setText("Phone is not valid");
            changePhoneField.setStyle("-fx-border-color: red");
            return;
        }
        databaseAdapter.updateInfo(new_password, new_email, new_address, new_phone, currentUser.getUsername());
    }
}
