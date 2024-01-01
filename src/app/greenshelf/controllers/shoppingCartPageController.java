package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import app.greenshelf.Customer;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class shoppingCartPageController {
    @FXML
    private Button buyButton;

    @FXML
    private ImageView profilePhotoImage;

    @FXML
    private ImageView greenShelfLogo;

    private Customer currentUser;
    private Stage stage;
    private Scene scene;

    public void initData(Customer user) {
        currentUser = user;
        //convert byte array back to Image
        profilePhotoImage.setImage( new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        //get products from shopping cart
        
    }

    @FXML
    void greenShelfLogoOnMouseClicked() {
        loadScene("../fxml/customerHome.fxml", currentUser);
    }

    private void loadScene(String fxmlPath, Customer user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhotoImage.getScene().getWindow();
            customerHomeController controller = loader.getController();
            controller.initData(user); // Pass the User object to the controller
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
