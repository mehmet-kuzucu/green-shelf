package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.List;

public class shoppingCartPageController {
    @FXML
    private Button checkoutButton;

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private VBox productsVBox;

    @FXML
    private Text totalPriceText;

    private Customer currentUser;
    private Stage stage;
    private Scene scene;
    private int cartCount;
    private double totalPrice;
    private HashMap<Integer, Double> productStockMap = new HashMap<Integer, Double>();
    private List <Order> shoppingCart;
    private String orderID;
    private double vat = 0.01;


    public void initData(Customer user, List <Order> shoppingCart, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        currentUser = user;
        this.shoppingCart = shoppingCart;
        this.orderID = orderID;
        this.cartCount = cartCount;
        this.totalPrice = totalPrice;
        this.productStockMap = productStockMap;

        totalPriceText.setText("Total Price: " + Math.round(totalPrice*100)/100.0 + "₺" +"\n" + "Total Products: " + cartCount + " products" + "\n" + "VAT: " + Math.round(totalPrice*vat*100)/100.0 + "₺" + "\n" + "Total Price with VAT: " + Math.round(((totalPrice) + (totalPrice*vat))*100)/100.0 + "₺");
        totalPriceText.setFont(new Font(13));
        for (Order order : this.shoppingCart) {
            VBox group = createVboxGroup(order);
            productsVBox.getChildren().add(group);
        }
    }

    @FXML
    private void greenShelfLogoOnMouseClicked() {
        loadScene("../fxml/customerHome.fxml", currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID);
    }


    private void loadScene(String fxmlPath, Customer user, List <Order> orderArray, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                controller.initData(user, orderArray, cartCount, totalPrice, productStockMap, orderID);
            }
            else if (fxmlPath.equals("../fxml/checkoutPage.fxml")) {
                checkoutPageController controller = loader.getController();
                controller.initData(user, orderArray, cartCount, totalPrice, productStockMap, orderID);
            }
            scene = new Scene(root,greenShelfLogo.getScene().getWidth(),greenShelfLogo.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VBox createVboxGroup(Order order){
        VBox productInfo = new VBox();
        productInfo.setId("productInfo");
        productInfo.setAlignment(javafx.geometry.Pos.CENTER);

        productInfo.setSpacing(15.0);
        productInfo.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());

        BorderPane borderPane = new BorderPane();
        HBox innerHBox = new HBox();
        innerHBox.setId("deneme");
        innerHBox.setAlignment(javafx.geometry.Pos.CENTER);
        innerHBox.setSpacing(5.0);
        innerHBox.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Product product = databaseAdapter.getProductFromId(order.getProductID());
        String amountString = String.valueOf(order.getAmount());
        int indexOfDot = amountString.indexOf(".");
        if (indexOfDot != -1) {
            amountString = amountString.substring(0, indexOfDot);
        }
        Text amountXpriceTextEqualsTotalPrice = new Text((product.getIsPiece() ? amountString : order.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + "x " + (product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2) + "₺" + " = " + order.getAmount()*Math.round((product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2)*100)/100.0 + "₺");
        amountXpriceTextEqualsTotalPrice.setFill(Color.WHITE);
        amountXpriceTextEqualsTotalPrice.setFont(new Font(20));
        Text productNameText = new Text(product.getName());
        productNameText.setFill(Color.WHITE);
        productNameText.setFont(new Font(20));
        ImageView productImage = new ImageView();
        productImage.setFitHeight(150.0);
        productImage.setFitWidth(200.0);
        productImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(product.getImage()))));

        HBox HBoxLeft = new HBox();
        Button deleteButton = new Button();
        deleteButton.setId("deleteButton");
        deleteButton.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());
        deleteButton.setOnMouseClicked((MouseEvent event) -> {
            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            Order orderToDelete = dbAdapter.getOrderFromId(orderID, order.getProductID());
            dbAdapter.deleteFromCart(orderToDelete);
            totalPrice -= dbAdapter.getProductFromId(order.getProductID()).getPrice() * order.getAmount();
            cartCount--;
            for (int i = 0; i < shoppingCart.size(); i++) {
                if (shoppingCart.get(i).getProductID() == order.getProductID()) {
                    shoppingCart.remove(i);
                    break;
                }
            }
            productStockMap.put(orderToDelete.getProductID(), productStockMap.get(orderToDelete.getProductID()) + orderToDelete.getAmount());
            try {
                refreshPage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        VBox rightVBox = new VBox();
        rightVBox.setAlignment(javafx.geometry.Pos.CENTER);
        rightVBox.getChildren().addAll(deleteButton);
        rightVBox.setPadding(new javafx.geometry.Insets(13, 13, 13, 13));
        
        HBoxLeft.setSpacing(10);
        HBoxLeft.setPadding(new javafx.geometry.Insets(13, 13, 13, 13));
        HBoxLeft.getChildren().addAll(productImage, productNameText);
        HBoxLeft.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        borderPane.setLeft(HBoxLeft);
        borderPane.setCenter(amountXpriceTextEqualsTotalPrice);
        borderPane.setRight(rightVBox);
        productInfo.getChildren().addAll(borderPane);
        return productInfo;
    }

    @FXML
    private void checkoutButtonOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/checkoutPage.fxml", currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID);

    }

    public void refreshPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/shoppingCartPage.fxml"));
        Parent root = loader.load();
        shoppingCartPageController controller = loader.getController();
        controller.initData(currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID); 
        stage = (Stage) checkoutButton.getScene().getWindow();
        scene = new Scene(root,checkoutButton.getScene().getWidth(),checkoutButton.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }
}
