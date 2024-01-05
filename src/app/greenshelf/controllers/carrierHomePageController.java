package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import app.greenshelf.Carrier;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
    //TODO: add new column to database for order and carrier match
    @FXML
    void profilePhotoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", carrier);
    }
    
    public void initData(Carrier carrier) {
        this.carrier = carrier;
        System.out.println(carrier.getName());
        profilePhoto.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(carrier.getProfilePicture()))));
        welcomeText.setText("Welcome, " + carrier.getName());
        DatabaseAdapter db = new DatabaseAdapter();
        List<Order> orders = db.getAllOrders();
        for (Order order : orders) {
            if (order.getStatus().equals("waiting")) {
                availableOrders.getChildren().add(createVBoxWaiting(order));
            } else if (order.getStatus().equals("In transition")) {
                currentOrders.getChildren().add(createVBoxCurrent(order));
            } else if (order.getStatus().equals("Completed")) {
                completedOrders.getChildren().add(createVBoxCompleted(order));
            }
        }
        db.closeConnection();
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

    private VBox createVBoxWaiting(Order order)
    {
        DatabaseAdapter db = new DatabaseAdapter();
        VBox vBox = new VBox();
        vBox.setId("productInfo");
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(200);
        Customer customer = db.getUserFromId(order.getId());
        Text text = new Text(customer.getName() + "\n" + order.getDate() + "\n" + customer.getAddress() + "\n" + order.getPrice() + " TL");
        Button button = new Button("Accept");
        button.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: #000000; -fx-border-radius: 10px; -fx-border-width: 1px;");
        button.setOnMouseClicked(e -> {
            order.setStatus("In transition");
            db.updateOrder(order);
            availableOrders.getChildren().remove(vBox);
            currentOrders.getChildren().add(createVBoxCurrent(order));
        });
        vBox.getChildren().addAll(text, button);
        db.closeConnection();
        return vBox;    
    }

    private VBox createVBoxCurrent(Order order) {
        DatabaseAdapter db = new DatabaseAdapter();
        VBox vBox = new VBox();
        vBox.setId("productInfo");
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(200);
        Customer customer = db.getUserFromId(order.getId());
        Text text = new Text(customer.getName() + "\n" + order.getDate() + "\n" + customer.getAddress() + "\n" + order.getPrice() + " TL");
        Button button = new Button("Complete");
        button.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: #000000; -fx-border-radius: 10px; -fx-border-width: 1px;");
        button.setOnMouseClicked(e -> {
            order.setStatus("Completed");
            db.updateOrder(order);
            currentOrders.getChildren().remove(vBox);
            completedOrders.getChildren().add(createVBoxCompleted(order));
        });
        vBox.getChildren().addAll(text, button);
        return vBox;    
    }

    private VBox createVBoxCompleted(Order order) {
        DatabaseAdapter db = new DatabaseAdapter();
        VBox vBox = new VBox();
        vBox.setId("productInfo");
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setPrefWidth(200);
        vBox.setPrefHeight(200);
        vBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: #000000; -fx-border-radius: 10px; -fx-border-width: 1px;");
        Customer customer = db.getUserFromId(order.getId());
        Text text = new Text(customer.getName() + "\n" + order.getDate() + "\n" + customer.getAddress() + "\n" + order.getPrice() + " TL");
        vBox.getChildren().addAll(text);
        return vBox;

    }


}
