package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
import app.greenshelf.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.HashMap;

public class myOrdersPageController {

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private VBox myOrdersVBox;

    @FXML
    private ImageView profilePhotoImage;

    @FXML
    private Button removeOrder;
    private Stage stage;
    private Scene scene;


    private Customer user;
    private List <Order> order;
    private int cartCount;
    private Double totalPrice;
    private HashMap<Integer, Double> productStockMap;
    private String orderID;
    private HashMap<String, List<Order>> orderMap = new HashMap<String, List<Order>>();



    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/customerHome.fxml", user, order, cartCount, totalPrice, productStockMap, orderID);
    }

    @FXML
    void profilePhotoImageOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", user, order, cartCount, totalPrice, productStockMap, orderID);
    }

    public void initData(Customer user, List <Order> order, int cartCount, Double totalPrice, HashMap<Integer, Double> productStockMap, String orderID)
    {
        this.user = user;
        this.order = order;
        this.cartCount = cartCount;
        this.totalPrice = totalPrice;
        this.productStockMap = productStockMap;
        this.orderID = orderID;
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(user.getProfilePicture()))));
        DatabaseAdapter dbAdapter = new DatabaseAdapter();

        List<Order> orders = dbAdapter.getAllOrders();
        
        for (Order order2 : orders) {
            if (orderMap.containsKey(order2.getOrderID())) {
                List<Order> orderList = orderMap.get(order2.getOrderID());
                orderList.add(order2);
                orderMap.put(order2.getOrderID(), orderList);
            } else {
                List<Order> orderList = new ArrayList<Order>();
                orderList.add(order2);
                orderMap.put(order2.getOrderID(), orderList);
            }
        }

        
        //Customer customer = dbAdapter.getUserFromId(order.get(0).getId());
        int userID = dbAdapter.getUserIDFromUsername(user.getUsername());
        System.out.println(userID);
        
        for (HashMap.Entry<String, List<Order>> order3 : orderMap.entrySet()) {
            if (order3.getValue().get(0).getId() != userID || order3.getValue().get(0).getStatus().equals("inCart")) {
                continue;
            }
            myOrdersVBox.getChildren().add(createVBox(order3.getValue().get(0), orderMap));
        }
        dbAdapter.closeConnection();
    }

    private VBox createVBox (Order order4, HashMap<String, List<Order>> orderMap) {
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(20.0);
        root.setStyle("-fx-background-color: #2c344b;");

        VBox innerVBox = new VBox();
        innerVBox.setAlignment(Pos.CENTER);
        innerVBox.setSpacing(10.0);
        innerVBox.setStyle("-fx-background-color: #BEFFBC;");

        BorderPane borderPane = new BorderPane();
        VBox centerVBox = new VBox();
        centerVBox.setAlignment(Pos.CENTER);
        centerVBox.setMaxHeight(Double.MAX_VALUE);
        centerVBox.setMaxWidth(Double.MAX_VALUE);
        centerVBox.setSpacing(10.0);

        Text expectedDeliveryDateText = new Text("Expected Delivery Date: " + order4.getDate());
        expectedDeliveryDateText.setStrokeType(StrokeType.OUTSIDE);
        expectedDeliveryDateText.setStrokeWidth(0.0);
        Text text4 = new Text();
        text4.setStrokeType(StrokeType.OUTSIDE);
        text4.setStrokeWidth(0.0);
        

        Double totalPriceDouble = 0.0;
        Double vat = 0.01;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        for (Order order2 : orderMap.get(order4.getOrderID())) {

            Product product = dbAdapter.getProductFromId(order2.getProductID());
            String amountString = String.valueOf(order4.getAmount());
            int indexOfDot = amountString.indexOf(".");
            if (indexOfDot != -1) {
                amountString = amountString.substring(0, indexOfDot);
            }
            text4.setText(text4.getText() + (product.getIsPiece() ? amountString : order2.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + product.getName() + (orderMap.get(order4.getOrderID()).indexOf(order2) == orderMap.get(order4.getOrderID()).size() - 1 ? "": "\n")); 
            totalPriceDouble += (order2.getAmount()*(product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2)) + (order2.getAmount()*(product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2) * vat);
            
        }
        Text totalPrice = new Text("Total Price: " + (Math.round(totalPriceDouble * 100) / 100.0) + "₺");
        totalPrice.setStrokeType(StrokeType.OUTSIDE);
        totalPrice.setStrokeWidth(0.0);


        Text text3 = new Text(order4.getStatus() + ((order4.getStatus().equals("completed") || order4.getStatus().equals("inDelivery")) ? " by " + order4.getCarrierUsername() : ""));
        text3.setStrokeType(StrokeType.OUTSIDE);
        text3.setStrokeWidth(0.0);

        Text deliveredTimeText = new Text("Delivered Date: " + order4.getDeliveryDate());
        deliveredTimeText.setStrokeType(StrokeType.OUTSIDE);
        deliveredTimeText.setStrokeWidth(0.0);

        Line line = new Line(-100.0, 0, 100.0, 0);
        
        
        if(order4.getDeliveryDate() != null) {
            centerVBox.getChildren().addAll(expectedDeliveryDateText, totalPrice, text3, deliveredTimeText, line, text4);
        }
        else {
            centerVBox.getChildren().addAll(expectedDeliveryDateText, totalPrice, text3, line, text4);
        }
        

        borderPane.setCenter(centerVBox);
        Text leftText = new Text(order4.getOrderID());
        leftText.setRotate(-90.0);
        leftText.setStrokeType(StrokeType.OUTSIDE);
        leftText.setStrokeWidth(0.0);
        leftText.setFont(new javafx.scene.text.Font(13.0));
        leftText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        borderPane.setLeft(leftText);
        borderPane.setAlignment(leftText, Pos.CENTER);

        Button removeOrderButton = new Button();
        removeOrderButton.setMnemonicParsing(false);
        removeOrderButton.setId("removeOrderButton");
        borderPane.setAlignment(removeOrderButton, Pos.CENTER);
        removeOrderButton.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());
        borderPane.setRight(removeOrderButton);
        if (order4.getStatus().equals("cancelled") || order4.getStatus().equals("completed") || order4.getStatus().equals("inDelivery")) {
            removeOrderButton.setVisible(false);
        }
        

        removeOrderButton.onMouseClickedProperty().set((event) -> {
            DatabaseAdapter dbAdapter2 = new DatabaseAdapter();
            try {
                for (Order order2 : orderMap.get(order4.getOrderID())) {
                    dbAdapter2.changeOrderStatus(order2.getOrderID(), "cancelled");
                    dbAdapter2.updateProductPriceWhenCancel(order2.getProductID(), order2.getAmount());
                    dbAdapter2.updateProductStock(order2.getProductID(), -1 * order2.getAmount());
                }
                productStockMap.clear();
                List<Product> products2 = dbAdapter.getAllProducts();

                for (Product product : products2) {
                    productStockMap.put(product.getId(), product.getStock());
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            refresh();
            dbAdapter2.closeConnection();
        });

        borderPane.setPadding(new Insets(10.0));

        innerVBox.getChildren().add(borderPane);
        innerVBox.setPadding(new Insets(5.0, 0, 5.0, 0));

        root.getChildren().addAll(innerVBox);
        root.setPadding(new Insets(5.0));
        return root;
    }


    private void loadScene(String fxmlPath, Customer user, List <Order> order, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhotoImage.getScene().getWindow();
            scene = new Scene(root,profilePhotoImage.getScene().getWidth(),profilePhotoImage.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
            
            if (fxmlPath.equals("../fxml/profileInfoPage.fxml")) {
                profileInfoPageController controller = loader.getController();
                controller.initData(user, order, cartCount, totalPrice, productStockMap, orderID); // Pass the User object to the controller
            }
            else if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                controller.initData(user, order, cartCount, totalPrice, productStockMap, orderID); // Pass the User object to the controller
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/myOrdersPage.fxml"));
        Parent root;
        try {
            root = loader.load();
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            myOrdersPageController controller = loader.getController();
            controller.initData(user, order, cartCount, totalPrice, productStockMap, orderID);
            scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
