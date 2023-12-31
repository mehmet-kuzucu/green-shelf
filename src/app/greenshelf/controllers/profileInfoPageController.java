package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.util.Base64;

import app.greenshelf.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class profileInfoPageController {
    @FXML
    private Button saveProfileButton;

    @FXML
    private ImageView profilePhotoImage;
    
    @FXML
    private Text nameDescription;

    private Customer currentUser;

    public void initData(Customer user) {
        this.currentUser = user;
        nameDescription.setText(currentUser.getName() + " " + currentUser.getSurname());
        System.out.println(currentUser.getName());
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
    }
}
