package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import app.greenshelf.Customer;
import app.greenshelf.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

    private Stage stage;
    private Scene scene;
    private Customer currentUser;

    @FXML
    void shoppingCartButtonButtonOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/shoppingCartPage.fxml", currentUser);

    }

    @FXML
    void profilePhotoImageOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", currentUser);
    }

    private void loadScene(String fxmlPath, Customer user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhotoImage.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            if (fxmlPath.equals("../fxml/profileInfoPage.fxml")) {
                profileInfoPageController controller = loader.getController();
                controller.initData(user); // Pass the User object to the controller
            }
            else if (fxmlPath.equals("../fxml/shoppingCartPage.fxml")) {
                shoppingCartPageController controller = loader.getController();
                controller.initData(user); // Pass the User object to the controller
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    // This method receives the User object from the login controller
    public void initData(Customer user) {
        System.out.println("customerHomeController: initData called");
        this.currentUser = user;
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        welcomeText.setText("Welcome, " + currentUser.getName() + "!");
    }
    
}
