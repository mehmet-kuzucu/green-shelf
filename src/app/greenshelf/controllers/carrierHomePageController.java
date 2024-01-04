package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;

import app.greenshelf.Carrier;
import app.greenshelf.Customer;
import app.greenshelf.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class carrierHomePageController {
    Carrier carrier;
    private Stage stage;
    private Scene scene;
     @FXML
    private VBox availableOrders;

    @FXML
    private VBox completedOrders;

    @FXML
    private VBox currentOrders;

    @FXML
    private ImageView profilePhoto;

    @FXML
    private Text welcomeText;
    
    @FXML
    void profilePhotoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", carrier);
    }
    
    public void initData(Carrier carrier) {
        this.carrier = carrier;
        System.out.println("Carrier home page controller initialized!");
        profilePhoto.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(carrier.getProfilePicture()))));
        welcomeText.setText("Welcome, " + carrier.getName());
    }
    /*@FXML
    void silinecekOnMouseClicked(MouseEvent event) {
        // when button clicked, make the button unvisible
        silinecek.setVisible(false);
        System.out.println("silinecek button clicked!");

    }*/

    private void loadScene(String fxmlPath, Carrier carrier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhoto.getScene().getWindow();
            scene = new Scene(root,profilePhoto.getScene().getWidth(),profilePhoto.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
            
            if (fxmlPath.equals("../fxml/profileInfoPage.fxml")) {
                profileInfoPageController controller = loader.getController();
                controller.initData(carrier); // Pass the User object to the controller
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    private void createVBox(Order order)
    {
        VBox vbox = new VBox();
        vbox.setId("productInfo"); // Make sure to define the CSS styles accordingly
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxHeight(200.0);
        vbox.setMinHeight(200.0);
        vbox.setPrefHeight(200.0);
        vbox.setPrefWidth(100.0);
        vbox.setSpacing(5.0);
        vbox.setStyle("-fx-background-color: #beffbc;");

        // Create some Text nodes with the given text
        Text orderId = new Text("#" + order.getId());
        Text productNames = new Text("Product names");
        Text customerInfo = new Text("Customer Info");
        Text address = new Text("Address");
        Text totalPrice = new Text("Total price");
        Text deliveryDate = new Text("Delivery date and time");

        // Add the Text nodes to the VBox as children
        vbox.getChildren().addAll(orderId, productNames, customerInfo, address, totalPrice, deliveryDate);
    }


}
