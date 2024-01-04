package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;

import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
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
    private Button buyButton;

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private VBox productsVBox;

    private Customer currentUser;
    private Stage stage;
    private Scene scene;
    //private LinkedList <Order> orderArray;
    private int cartCount;
    private double totalPrice;
    private HashMap<Integer, Double> productStockMap = new HashMap<Integer, Double>();
    private List <Order> shoppingCart;


    public void initData(Customer user, List <Order> shoppingCart, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap) {
        currentUser = user;
        this.shoppingCart = shoppingCart;
        //get products from shopping cart

        System.out.println("Size of the order array: " + this.shoppingCart.size());
        this.cartCount = cartCount;
        this.totalPrice = totalPrice;
        this.productStockMap = productStockMap;

        for (Order order : this.shoppingCart) {
            VBox group = createVboxGroup(order);
            productsVBox.getChildren().add(group);
        }

        
        
    }

    @FXML
    void greenShelfLogoOnMouseClicked() {
        loadScene("../fxml/customerHome.fxml", currentUser, shoppingCart, cartCount, totalPrice, productStockMap);
    }

    private void loadScene(String fxmlPath, Customer user, List <Order> orderArray, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            customerHomeController controller = loader.getController();
            controller.initData(user, orderArray, cartCount, totalPrice, productStockMap);
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
        //productInfo.setPrefHeight(400.0);
        //productInfo.setPrefWidth(200.0);
        productInfo.setSpacing(15.0);
        productInfo.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());

        /*
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(200.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(product.getImage()))));
        */
        BorderPane borderPane = new BorderPane();
        HBox innerHBox = new HBox();
        innerHBox.setId("deneme");
        innerHBox.setAlignment(javafx.geometry.Pos.CENTER);
        innerHBox.setSpacing(5.0);
        innerHBox.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Product product = databaseAdapter.getProductFromId(order.getProductID());
        //getAmount to string
        String amountString = String.valueOf(order.getAmount());
        int indexOfDot = amountString.indexOf(".");
        if (indexOfDot != -1) {
            amountString = amountString.substring(0, indexOfDot);
        }
        Text amountXpriceTextEqualsTotalPrice = new Text((product.getIsPiece() ? amountString : order.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + "x " + (product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2) + "₺" + " = " + order.getAmount()*(product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2) + "₺");
        amountXpriceTextEqualsTotalPrice.setFill(Color.WHITE);
        amountXpriceTextEqualsTotalPrice.setFont(new Font(20));
        Text productNameText = new Text(product.getName());
        productNameText.setFill(Color.WHITE);
        productNameText.setFont(new Font(20));
        ImageView productImage = new ImageView();
        productImage.setFitHeight(150.0);
        productImage.setFitWidth(200.0);
        productImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(product.getImage()))));

        //innerHBox.getChildren().addAll(productImage, productNameText, amountXpriceText, totalPriceText);
        HBox HBoxLeft = new HBox();
        Button deleteButton = new Button();
        deleteButton.setId("deleteButton");
        deleteButton.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());
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
        
        
        /* 
        Spinner<Double> spinner = new Spinner<>();
        spinner.setId("spinner");
        spinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, product.getStock(), 1.0, 0.1));
        spinner.setEditable(false);
        */
        

        productInfo.getChildren().addAll(borderPane);
        return productInfo;
        
    }




}
