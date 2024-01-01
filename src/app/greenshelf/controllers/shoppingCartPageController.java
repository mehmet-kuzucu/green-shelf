package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import app.greenshelf.Customer;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class shoppingCartPageController {
    @FXML
    private Button buyButton;

    @FXML
    private ImageView profilePhotoImage;

    private Customer currentUser;

    public void initData(Customer user) {
        currentUser = user;
        //convert byte array back to Image
        profilePhotoImage.setImage( new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        //get products from shopping cart
        
    }
}
