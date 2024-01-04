package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
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
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
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
    private Spinner<Double> spinner;

    @FXML
    private Text welcomeText;

    @FXML
    private TilePane customerTilePane;

    @FXML
    private Text cartCountText;

    @FXML
    private Text totalPriceText;

    private Stage stage;
    private Scene scene;
    private Customer currentUser;
    private LinkedList <Order> ordersArray = new LinkedList<Order>();
    private int cartCount = 0;
    private double totalPrice = 0;
    private HashMap<Integer, Double> productStockMap = new HashMap<Integer, Double>();

    @FXML
    void shoppingCartButtonButtonOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/shoppingCartPage.fxml", currentUser, ordersArray, cartCount, totalPrice, productStockMap);
        /* 
        databaseAdapter = new DatabaseAdapter();
        Order order = new Order(currentUser.getUserID(), 1, 1, "2021-05-05", "pending", 1);
        databaseAdapter.addOrdersql(order);
        databaseAdapter.closeConnection();
        */
    }

    @FXML
    void profilePhotoImageOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", currentUser, ordersArray, cartCount, totalPrice, productStockMap);
    }

    private void loadScene(String fxmlPath, Customer user, LinkedList <Order> order, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhotoImage.getScene().getWindow();
            scene = new Scene(root,profilePhotoImage.getScene().getWidth(),profilePhotoImage.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
            
            if (fxmlPath.equals("../fxml/profileInfoPage.fxml")) {
                profileInfoPageController controller = loader.getController();
                controller.initData(user, order, cartCount, totalPrice, productStockMap); // Pass the User object to the controller
            }
            else if (fxmlPath.equals("../fxml/shoppingCartPage.fxml")) {
                shoppingCartPageController controller = loader.getController();
                controller.initData(user, order, cartCount, totalPrice, productStockMap); // Pass the User object to the controller
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public VBox createVboxGroup(Product product){
        VBox productInfo = new VBox();
        productInfo.setId("productInfo");
        productInfo.setAlignment(javafx.geometry.Pos.CENTER);
        productInfo.setPrefHeight(400.0);
        productInfo.setPrefWidth(200.0);
        productInfo.setSpacing(15.0);
        productInfo.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(200.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(product.getImage()))));

        VBox innerVBox = new VBox();
        innerVBox.setId("deneme");
        innerVBox.setAlignment(javafx.geometry.Pos.CENTER);
        innerVBox.setSpacing(5.0);
        innerVBox.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());

        Text text1 = new Text(product.getName());
        text1.setFill(javafx.scene.paint.Color.WHITE);
        Text text2 = new Text((product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2)+"₺" + " / " + (product.getIsPiece() ? "Piece" : "Kg"));
        text2.setFill(javafx.scene.paint.Color.WHITE);

        innerVBox.getChildren().addAll(text1, text2);

        Spinner<Double> spinner = new Spinner<>();
        spinner.setId("spinner");
        spinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, product.getStock(), 1.0, product.getIsPiece() ? 1.0 : 0.1));
        spinner.setEditable(false);

        Button addToCartButton = new Button();
        addToCartButton.setId("addToCartButton");
        addToCartButton.setMnemonicParsing(false);
        addToCartButton.onMouseClickedProperty().set((MouseEvent event) -> {

            if (productStockMap.get(product.getId()) < spinner.getValue()) {
                System.out.println("Not enough stock");
                return;
            }

            if (checkIfOrderExists(product.getId())){
                for (Order order : ordersArray) {
                    if (order.getProductID() == product.getId()) {
                        order.setAmount(order.getAmount() + spinner.getValue());
                    }
                }
            } else {
                Order order = new Order(0,currentUser.getUserID(), product.getId(), spinner.getValue(), "", "inCart", product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2);
                ordersArray.add(order);
                cartCount += 1;
            }
            productStockMap.put(product.getId(), productStockMap.get(product.getId()) - spinner.getValue());

            
            totalPrice += (product.getThreshold() < product.getStock()) ? (product.getPrice() * spinner.getValue()) : (product.getPrice() * spinner.getValue() * 2);
            try {
                refreshPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            

        });

        productInfo.getChildren().addAll(imageView, innerVBox, spinner, addToCartButton);
        return productInfo;
        
    }


    private boolean checkIfOrderExists(int productID) {
        for (Order order : ordersArray) {
            if (order.getProductID() == productID) {
                return true;
            }
        }
        return false;
    }
    public void refreshPage() throws IOException{
        System.out.println("you are ın the refresh page");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/customerHome.fxml"));
        Parent root = loader.load();
        customerHomeController controller = loader.getController();
        controller.initData(currentUser, ordersArray, cartCount, totalPrice, productStockMap);
        stage = (Stage) shoppingCartButton.getScene().getWindow();
        scene = new Scene(root,shoppingCartButton.getScene().getWidth(),shoppingCartButton.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

    
    // This method receives the User object from the login controller
    public void initData(Customer user) {
        System.out.println("customerHomeController: initData called");
        this.currentUser = user;
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        welcomeText.setText("Welcome, " + currentUser.getName() + "!");

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        List<Product> products = dbAdapter.getAllProducts();

        for (Product product : products) {
            productStockMap.put(product.getId(), product.getStock());
        }

        // Add each product to the VBox
        for (Product product : products) {
            VBox group = createVboxGroup(product);
            customerTilePane.getChildren().add(group);
        }
        cartCountText.setText(String.valueOf(cartCount));
        totalPriceText.setText(String.valueOf("Total price: " + totalPrice));

        // Add the VBox to the ScrollPane
    
        dbAdapter.closeConnection();
    }

    public void initData(Customer user, LinkedList <Order> ordersArray, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap) {
        this.currentUser = user;
        this.ordersArray = ordersArray;
        this.cartCount = cartCount;
        this.totalPrice = totalPrice;
        this.productStockMap = productStockMap;
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        welcomeText.setText("Welcome, " + currentUser.getName() + "!");

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        List<Product> products = dbAdapter.getAllProducts();

        // Add each product to the VBox
        for (Product product : products) {
            VBox group = createVboxGroup(product);
            customerTilePane.getChildren().add(group);
        }
        cartCountText.setText(String.valueOf(cartCount));
        totalPriceText.setText(String.valueOf("Total price: " + totalPrice));

        // Add the VBox to the ScrollPane
    
        dbAdapter.closeConnection();
    }
    
}
