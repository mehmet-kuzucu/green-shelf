package app.greenshelf.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import app.greenshelf.Admin;
import app.greenshelf.Customer;
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
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;

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

    private Map<String, List<Order>> orderMap = new TreeMap<String, List<Order>>(Comparator.naturalOrder());


    @FXML
    private void greenShelfLogoOnMouseClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminHomePage.fxml"));
        Parent root = loader.load();
        stage = (Stage) greenShelfLogo.getScene().getWindow();
        adminHomePageController controller = loader.getController();
        controller.initData(currentUser,root,controller);
        scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    public void initData(Admin user) {
        currentUser = user;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        List<Order> orders = dbAdapter.getAllOrders();
        dbAdapter.closeConnection();
        for (Order order : orders) {
            if (orderMap.containsKey(order.getOrderID())) {
                List<Order> orderList = orderMap.get(order.getOrderID());
                orderList.add(order);
                orderMap.put(order.getOrderID(), orderList);
            } else {
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(order);
                orderMap.put(order.getOrderID(), orderList);
            }
        }
        for (HashMap.Entry<String, List<Order>> order : orderMap.entrySet()) {
            if (order.getValue().get(0).getStatus().equals("waiting")) {
                VBox group = createVboxGroup(order.getValue().get(0), orderMap);
                waitingColumn.getChildren().add(group);
            } else if (order.getValue().get(0).getStatus().equals("inDelivery")) {
                VBox group = createVboxGroup(order.getValue().get(0), orderMap);
                inDeliveryColumn.getChildren().add(group);
            } else if (order.getValue().get(0).getStatus().equals("completed")) {
                VBox group = createVboxGroup(order.getValue().get(0), orderMap);
                completedColumn.getChildren().add(group);
            } else if (order.getValue().get(0).getStatus().equals("cancelled")) {
                VBox group = createVboxGroup(order.getValue().get(0), orderMap);
                completedColumn.getChildren().add(group);
            }
        }
    }

    public VBox createVboxGroup(Order order, Map<String, List<Order>> orderMap)
    {
        VBox orderDetails = new VBox();
        orderDetails.setAlignment(javafx.geometry.Pos.CENTER);
        orderDetails.setPrefHeight(200.0);
        orderDetails.setPrefWidth(100.0);
        orderDetails.setSpacing(5.0);
        orderDetails.setPadding(new javafx.geometry.Insets(10.0));
        orderDetails.setId("orderDetails");
        orderDetails.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());
        Text id = new Text();
        if (order.getStatus().equals("completed") || order.getStatus().equals("inDelivery"))
        {
            id.setText("#"+order.getOrderID() + "\nCarrier: " + order.getCarrierUsername());
        }
        else
        {
            id.setText("#"+order.getOrderID());
        }
        id.setFill(Color.WHITE);
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        Customer customer = dbAdapter.getUserFromId(order.getId());
        Text productInfo = new Text();
        productInfo.setFill(Color.WHITE);
        Text customerInfo = new Text(customer.getName() + " " + customer.getSurname());
        customerInfo.setFill(Color.WHITE);
        String addressString = customer.getAddress();
        Text address = new Text("Address: " + addressString);
        address.setFill(Color.WHITE);
        dbAdapter.closeConnection();
        Double totalPrice = 0.0;
        Double vat = 0.01;
        for (Order order2 : orderMap.get(order.getOrderID())) {
            Product product = dbAdapter.getProductFromId(order2.getProductID());
            String amountString = String.valueOf(order.getAmount());
            int indexOfDot = amountString.indexOf(".");
            if (indexOfDot != -1) {
                amountString = amountString.substring(0, indexOfDot);
            }
            productInfo.setText(productInfo.getText() + (product.getIsPiece() ? amountString : order2.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + product.getName() + (orderMap.get(order.getOrderID()).indexOf(order2) == orderMap.get(order.getOrderID()).size() - 1 ? "": "\n")); 
            totalPrice += (order2.getAmount()*order2.getPrice())*(1+vat);
        }
        Text totalPriceText = new Text((Math.round(totalPrice * 100) / 100.0) + "₺");
        totalPriceText.setFill(Color.WHITE);
        Text deliveryDateTime = new Text(order.getDate());
        deliveryDateTime.setFill(Color.WHITE);
        Text deliveredDate = new Text(order.getDeliveryDate());
        deliveredDate.setFill(Color.WHITE);

        if (order.getStatus().equals("waiting") || order.getStatus().equals("inDelivery")) {
            Button cancelButton = new Button("Cancel Order");
            cancelButton.setMnemonicParsing(false);
            orderDetails.getChildren().addAll(
            id, 
            productInfo, 
            customerInfo, 
            address, 
            totalPriceText, 
            deliveryDateTime, 
            cancelButton
            );
            cancelButton.setOnMouseClicked((event) -> {
                
                DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                try {
                    databaseAdapter.changeOrderStatus(order.getOrderID(), "cancelled");
                    for (Order order2 : orderMap.get(order.getOrderID())) {
                        Product product = databaseAdapter.getProductFromId(order2.getProductID());
                        databaseAdapter.updateProductPriceWhenCancel(order2.getProductID(), order2.getAmount());
                        databaseAdapter.updateProductStock(product.getId(), Math.round((product.getStock() + order2.getAmount())*100)/100.0);
                        
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                databaseAdapter.closeConnection();
                refresh();
            });
        
        } else if (order.getStatus().equals("completed") || order.getStatus().equals("cancelled")) {
            if (order.getStatus().equals("completed"))
            {
                Text status = new Text("Successfully Delivered");
                status.setFill(Color.GREEN);
                orderDetails.getChildren().addAll(
                status,
                id, 
                productInfo, 
                customerInfo, 
                address, 
                totalPriceText, 
                deliveryDateTime,
                deliveredDate
                );
            }
            else
            {
                Text status = new Text("Cancelled");
                status.setFill(Color.RED);
                orderDetails.getChildren().addAll(
                status,
                id, 
                productInfo, 
                customerInfo, 
                address, 
                totalPriceText, 
                deliveryDateTime
                );
            }
            
        }
        
        return orderDetails;
    }

    private void refresh() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminOrdersPage.fxml"));
        Parent root;
        try {
            root = loader.load();
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            adminOrdersPageController controller = loader.getController();
            controller.initData(currentUser);
            scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
