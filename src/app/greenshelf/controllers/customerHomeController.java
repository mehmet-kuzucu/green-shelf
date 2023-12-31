package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import app.greenshelf.Customer;
import app.greenshelf.User;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class customerHomeController {

    @FXML
    private Button addToCartButton;

    @FXML
    private Group productGroup;

    @FXML
    private ImageView profilePhotoImage;

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Spinner<?> spinner;

    @FXML
    void addToCartButtonOnMouseClicked(MouseEvent event) {

    }

    private Customer currentUser;

    // This method receives the User object from the login controller
    public void initData(Customer user) {
        this.currentUser = user;
    }

    @FXML
    void initialize() {
        initData(currentUser);
        if (currentUser != null) {
            // Use currentUser here as needed
            // For example:
            System.out.println("Current user: " + currentUser.getUsername());
            // Call other methods or set up the UI based on currentUser
        } else {
            System.out.println("User not found!");
        }
    }

    @FXML
    public void decodeBase64ToImage(String encodedImage) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        Image image = new Image(new ByteArrayInputStream(decodedBytes));
        if (profilePhotoImage == null) {
            profilePhotoImage = new ImageView(image);
            profilePhotoImage.setImage(image);
            //profilePhotoImage.getImage();
            
        } else {
            profilePhotoImage.setImage(image);
        }
    }
    
}
