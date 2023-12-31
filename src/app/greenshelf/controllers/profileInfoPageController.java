package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import app.greenshelf.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.controllers.registerPageController;


public class profileInfoPageController {
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
            return;
        }
        if(registerPageController.checkPhoneisValid(changePhoneField) == false)
        {
            System.out.println("Phone is not valid");
            return;
        }
        databaseAdapter.updateInfo(new_password, new_email, new_address, new_phone, currentUser.getUsername());
    }
}
