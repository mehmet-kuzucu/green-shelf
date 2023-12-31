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
import javafx.scene.text.Text;

public class customerHomeController {

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Group productGroup;

    @FXML
    private ImageView profilePhotoImage;

    @FXML
    private Spinner<?> spinner;

    @FXML
    private Text welcomeText;

    private Customer currentUser;

    @FXML
    void shoppingCartButtonButtonOnMouseClicked(MouseEvent event) {
        System.out.println(currentUser.getPhone() + " added to cart!");

    }

    

    // This method receives the User object from the login controller
    public void initData(Customer user) {
        System.out.println("customerHomeController: initData called");
        this.currentUser = user;
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        welcomeText.setText("Welcome, " + currentUser.getName() + "!");
    }
    
}
