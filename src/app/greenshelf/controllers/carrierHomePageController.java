package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import app.greenshelf.Carrier;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;	


public class carrierHomePageController {
    private Carrier carrier;
    private Stage stage;
    private Scene scene;
    private HashMap<String, List<Order>> orderMap = new HashMap<String, List<Order>>();
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
    private Button logoutButton;

    @FXML
    private void profilePhotoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", carrier);
    }

    @FXML
    private void logoutButtonOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/loginPage.fxml");
    }
    
    public void initData(Carrier carrier) {
        this.carrier = carrier;
        profilePhoto.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(carrier.getProfilePicture()))));
        welcomeText.setText("Welcome, " + carrier.getName());
        DatabaseAdapter db = new DatabaseAdapter();
        List<Order> orders = db.getAllOrders();
        for (Order order : orders) {
            if(orderMap.containsKey(order.getOrderID()))
            {
                orderMap.get(order.getOrderID()).add(order);
            }
            else
            {
                List<Order> orderList = new LinkedList<Order>();
                orderList.add(order);
                orderMap.put(order.getOrderID(), orderList);
            }
        }

        for (String key : orderMap.keySet()) {
            List<Order> orderList = orderMap.get(key);
            
            if (orderList.get(0).getStatus().equals("waiting")) {
                availableOrders.getChildren().add(createVBoxWaiting(orderList.get(0)));
            } else if (orderList.get(0).getStatus().equals("inDelivery") && orderList.get(0).getCarrierUsername() != null && orderList.get(0).getCarrierUsername().equals(carrier.getUsername())) {
                currentOrders.getChildren().add(createVBoxCurrent(orderList.get(0)));
            } else if (orderList.get(0).getStatus().equals("completed") && orderList.get(0).getCarrierUsername() != null && orderList.get(0).getCarrierUsername().equals(carrier.getUsername())) {
                completedOrders.getChildren().add(createVBoxCompleted(orderList.get(0)));
            }
            
        }
        db.closeConnection();
    }

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
                controller.initData(carrier); 
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

        Text productsText = new Text();
        productsText.setFill(Color.WHITE);
        Customer customer = db.getUserFromId(order.getId());

        Double totalPriceDouble = 0.0;
        Double vat = 0.01;
        for (Order order2 : orderMap.get(order.getOrderID())) {

            Product product = db.getProductFromId(order2.getProductID());
            String amountString = String.valueOf(order.getAmount());
            int indexOfDot = amountString.indexOf(".");
            if (indexOfDot != -1) {
                amountString = amountString.substring(0, indexOfDot);
            }

            productsText.setText(productsText.getText() + (product.getIsPiece() ? amountString : order2.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + product.getName() + (orderMap.get(order.getOrderID()).indexOf(order2) == orderMap.get(order.getOrderID()).size() - 1 ? "": "\n")); 
            totalPriceDouble += (order2.getAmount()*order2.getPrice())*(1+vat);        }

        Text totalPrice = new Text("Total Price: " + (Math.round(totalPriceDouble * 100) / 100.0) + "₺");
        totalPrice.setFill(Color.WHITE);
        totalPrice.setStrokeType(StrokeType.OUTSIDE);
        totalPrice.setStrokeWidth(0.0);
        

        Text customerInfo = new Text(customer.getName() + " " + customer.getSurname());
        Text customerAddress = new Text(customer.getAddress());
        Text customerDate = new Text(order.getDate());
        customerInfo.setFill(Color.WHITE);
        customerAddress.setFill(Color.WHITE);
        customerDate.setFill(Color.WHITE);
        Button button = new Button("Accept");
        button.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: #000000; -fx-border-radius: 10px; -fx-border-width: 1px;");
        button.setOnMouseClicked(e -> {
            order.setStatus("inDelivery");
            order.setCarrierUsername(carrier.getUsername());
            db.updateOrderStatus(order);
            availableOrders.getChildren().remove(vBox);
            currentOrders.getChildren().add(createVBoxCurrent(order));
        });
        vBox.getChildren().addAll(customerInfo, customerAddress, customerDate, productsText, totalPrice, button);
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
        Text productsText = new Text();
        productsText.setFill(Color.WHITE);
        Customer customer = db.getUserFromId(order.getId());

        Double totalPriceDouble = 0.0;
        Double vat = 0.01;
       
        for (Order order2 : orderMap.get(order.getOrderID())) {

            Product product = db.getProductFromId(order2.getProductID());
            String amountString = String.valueOf(order.getAmount());
            int indexOfDot = amountString.indexOf(".");
            if (indexOfDot != -1) {
                amountString = amountString.substring(0, indexOfDot);
            }

            productsText.setText(productsText.getText() + (product.getIsPiece() ? amountString : order2.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + product.getName() + (orderMap.get(order.getOrderID()).indexOf(order2) == orderMap.get(order.getOrderID()).size() - 1 ? "": "\n")); 
            totalPriceDouble += (order2.getAmount()*order2.getPrice())*(1+vat); 
        }

        Text totalPrice = new Text("Total Price: " + (Math.round(totalPriceDouble * 100) / 100.0) + "₺");
        totalPrice.setFill(Color.WHITE);
        totalPrice.setStrokeType(StrokeType.OUTSIDE);
        totalPrice.setStrokeWidth(0.0);

        Text customerInfo = new Text(customer.getName() + " " + customer.getSurname());
        Text customerAddress = new Text(customer.getAddress());
        Text customerDate = new Text(order.getDate());
        customerInfo.setFill(Color.WHITE);
        customerAddress.setFill(Color.WHITE);
        customerDate.setFill(Color.WHITE);

        Button button = new Button("Complete");
        button.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10px; -fx-border-color: #000000; -fx-border-radius: 10px; -fx-border-width: 1px;");
        button.setOnMouseClicked(e -> {
            Locale.setDefault(Locale.ENGLISH);
            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm");
            String formattedDateTime = currentDateTime.format(formatter);
            
            order.setStatus("completed");
            order.setDeliveryDate(formattedDateTime);
            db.updateOrderStatusandDeliveryDate(order);
            currentOrders.getChildren().remove(vBox);
            completedOrders.getChildren().add(createVBoxCompleted(order));
        });
        vBox.getChildren().addAll(customerInfo, customerAddress, customerDate, productsText, totalPrice, button);
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
        Text productsText = new Text();
        productsText.setFill(Color.WHITE);
        Customer customer = db.getUserFromId(order.getId());

        Double totalPriceDouble = 0.0;
        Double vat = 0.01;
       
        for (Order order2 : orderMap.get(order.getOrderID())) {

            Product product = db.getProductFromId(order2.getProductID());
            String amountString = String.valueOf(order.getAmount());
            int indexOfDot = amountString.indexOf(".");
            if (indexOfDot != -1) {
                amountString = amountString.substring(0, indexOfDot);
            }

            productsText.setText(productsText.getText() + (product.getIsPiece() ? amountString : order2.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + product.getName() + (orderMap.get(order.getOrderID()).indexOf(order2) == orderMap.get(order.getOrderID()).size() - 1 ? "": "\n")); 
            totalPriceDouble += (order2.getAmount()*order2.getPrice())*(1+vat); 
        }

        Text totalPrice = new Text("Total Price: " + (Math.round(totalPriceDouble * 100) / 100.0) + "₺");
        totalPrice.setFill(Color.WHITE);
        totalPrice.setStrokeType(StrokeType.OUTSIDE);
        totalPrice.setStrokeWidth(0.0);

        Text customerInfo = new Text(customer.getName() + " " + customer.getSurname());
        Text customerAddress = new Text(customer.getAddress());
        Text customerDate = new Text(order.getDate());
        customerInfo.setFill(Color.WHITE);
        customerAddress.setFill(Color.WHITE);
        customerDate.setFill(Color.WHITE);

        vBox.getChildren().addAll(customerInfo, customerAddress, customerDate, productsText, totalPrice);
        return vBox;

    }

    private void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) logoutButton.getScene().getWindow();
            scene = new Scene(root,logoutButton.getScene().getWidth(),logoutButton.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
