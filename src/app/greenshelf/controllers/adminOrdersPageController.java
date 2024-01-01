package app.greenshelf.controllers;

import java.io.IOException;
import java.util.List;
import app.greenshelf.Admin;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class adminOrdersPageController {

    @FXML
    private ImageView greenShelfLogo;

    private Stage stage;
    private Scene scene;

    private Admin currentUser;

    @FXML
    private VBox completedColumn;
    
    @FXML
    private VBox waitingColumn;

    @FXML
    private VBox inDeliveryColumn;


    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminHomePage.fxml"));
        Parent root = loader.load();
        stage = (Stage) greenShelfLogo.getScene().getWindow();
        adminHomePageController controller = loader.getController();
        controller.initData(currentUser,root,controller);


        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initData(Admin user) {
        currentUser = user;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        List<Order> orders = dbAdapter.getAllOrders();
        for (Order order : orders) {
            //System.out.println(order.getOrderID());
            if (order.getStatus().equals("waiting")) {
                VBox group = createVboxGroup(order);
                waitingColumn.getChildren().add(group);
            } else if (order.getStatus().equals("inDelivery")) {
                VBox group = createVboxGroup(order);
                inDeliveryColumn.getChildren().add(group);
            } else if (order.getStatus().equals("completed")) {
                VBox group = createVboxGroup(order);
                completedColumn.getChildren().add(group);
            }
        }
    }

    public VBox createVboxGroup(Order order)
    {
        VBox orderDetails = new VBox();
        orderDetails.setAlignment(javafx.geometry.Pos.CENTER);
        orderDetails.setMaxHeight(200.0);
        orderDetails.setMinHeight(200.0);
        orderDetails.setPrefHeight(200.0);
        orderDetails.setPrefWidth(100.0);
        orderDetails.setSpacing(5.0);
        orderDetails.setStyle("-fx-background-color: #beffbc;");

        Text orderId = new Text(order.getOrderID());
        Text productInfo = new Text("Hocam bu Product Info");
        Text customerInfo = new Text(order.getUserID());
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        String addressString = dbAdapter.getAddress(order.getUserID());
        Text address = new Text(addressString);
        dbAdapter.closeConnection();
        Text totalPrice = new Text(order.getPrice() + "₺");
        Text deliveryDateTime = new Text(order.getDate());

        Button cancelButton = new Button("Hocam bu da Cancel");
        cancelButton.setMnemonicParsing(false);

        orderDetails.getChildren().addAll(
            orderId, 
            productInfo, 
            customerInfo, 
            address, 
            totalPrice, 
            deliveryDateTime, 
            cancelButton
        );
        return orderDetails;
    }
}
