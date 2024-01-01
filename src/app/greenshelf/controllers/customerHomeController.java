package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
import app.greenshelf.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
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

    private Stage stage;
    private Scene scene;
    private Customer currentUser;
    private DatabaseAdapter databaseAdapter;

    @FXML
    void shoppingCartButtonButtonOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/shoppingCartPage.fxml", currentUser);
        /* 
        databaseAdapter = new DatabaseAdapter();
        Order order = new Order(currentUser.getUserID(), 1, 1, "2021-05-05", "pending", 1);
        databaseAdapter.addOrdersql(order);
        databaseAdapter.closeConnection();
        */
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
        Text text2 = new Text(product.getPrice()+"₺");
        text2.setFill(javafx.scene.paint.Color.WHITE);

        innerVBox.getChildren().addAll(text1, text2);

        Spinner<Double> spinner = new Spinner<>();
        spinner.setId("spinner");
        spinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, product.getStock(), 1.0, 0.1));
        spinner.setEditable(false);

        Button addToCartButton = new Button();
        addToCartButton.setId("addToCartButton");
        addToCartButton.setMnemonicParsing(false);

        productInfo.getChildren().addAll(imageView, innerVBox, spinner, addToCartButton);
        return productInfo;
        // Create the VBox with specified properties
        /*
        VBox outerVBox = new VBox();
        
        outerVBox.setStyle("fx-background-color: #f5429b;");
        outerVBox.setPadding(new javafx.geometry.Insets(5));
        outerVBox.setSpacing(15);
        outerVBox.setPrefWidth(200);
        outerVBox.setPrefHeight(400);
        outerVBox.setId("productInfo");
        // Create an ImageView
        byte[] decodedBytes = Base64.getDecoder().decode(product.getImage());
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(decodedBytes)));
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);

        // Create an inner VBox with two text fields
        VBox innerVBox1 = new VBox();
        Text productName = new Text();
        productName.setText(product.getName());
        Text productPrice = new Text();
        productPrice.setText(product.getPrice() + " TL");
        innerVBox1.getChildren().addAll(productName, productPrice);
        innerVBox1.getChildren().get(0);
        // Create two text fields and a button
        //TextField quantityField = new TextField();
        //quantityField.setText(Double.valueOf(product.getStock()).toString() + " kg");
        //TextField thresholdField = new TextField();
        //thresholdField.setText(product.getThreshold() + " kg");
        Button addToCartButton = new Button("Update");
        addToCartButton.setOnMouseClicked(e -> {
            /* 
                DatabaseAdapter dbAdapter = new DatabaseAdapter();
                String quantity = quantityField.getText().substring(0, quantityField.getText().length() - 3);
                String threshold = thresholdField.getText().substring(0, thresholdField.getText().length() - 3);
                String price = priceField.getText().substring(0, priceField.getText().length() - 3);
                Product product2 = new Product(nameField.getText(),  Double.parseDouble(quantity.split(" ")[0]), Double.parseDouble(price), Double.parseDouble(threshold.split(" ")[0]), product.getType(), product.getId());
                dbAdapter.updateProduct(product2);
                dbAdapter.closeConnection();
                try {
                    //refreshPage();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            
            
        });
        // Create an inner VBox with text fields and button
        VBox innerVBox2 = new VBox();
        Button removeButton = new Button("Remove");
        removeButton.setOnMouseClicked(e -> {
            /*
            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            dbAdapter.removeProduct(product.getId());
            dbAdapter.closeConnection();
            try {
                refreshPage();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            
        });
        innerVBox2.getChildren().addAll(removeButton);

        // Add components to the outer VBox
        outerVBox.getChildren().addAll(imageView, innerVBox1, innerVBox2);
        return outerVBox;
        */
        
    }
    // This method receives the User object from the login controller
    public void initData(Customer user) {
        System.out.println("customerHomeController: initData called");
        this.currentUser = user;
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        welcomeText.setText("Welcome, " + currentUser.getName() + "!");

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        List<Product> products = dbAdapter.getAllProducts();

        // Add each product to the VBox
        for (Product product : products) {
            VBox group = createVboxGroup(product);
            customerTilePane.getChildren().add(group);
        }

        // Add the VBox to the ScrollPane
    
        dbAdapter.closeConnection();
    }
    
}
