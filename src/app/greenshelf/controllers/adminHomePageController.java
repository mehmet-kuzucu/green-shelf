package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import app.greenshelf.Admin;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class adminHomePageController {
    private Stage stage;
    private Scene scene;
    private adminHomePageController controller;
    @FXML
    private Button adminOrdersButton;

    @FXML
    private TilePane adminTilePane;

    @FXML
    private ImageView profilePhoto;

    @FXML
    private Button shoppingCartButton;

    private Admin admin;


    public void initData(Admin admin, Parent root, adminHomePageController controller)
    {
        this.admin = admin;
        this.controller = controller;
        // Get the products from the database
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        List<Product> products = dbAdapter.getAllProducts();

        // Add each product to the VBox
        for (Product product : products) {
            VBox group = createVboxGroup(product);
            adminTilePane.getChildren().add(group);
        }

        // Add the VBox to the ScrollPane
    
        dbAdapter.closeConnection();
    
    }

    @FXML
    void adminOrdersButtonOnMouseClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminOrdersPage.fxml"));
        Parent root = loader.load();
        adminOrdersPageController controller = loader.getController();
        controller.initData(admin);
        stage = (Stage) adminOrdersButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void shoppingCartButtonOnMouseClicked(MouseEvent event) throws IOException {
        // when button clicked, make the button unvisible
        /* open a new stage which new product will be added */
        Stage stage = new Stage();
        FXMLLoader scene = new FXMLLoader(getClass().getResource("../fxml/addProductPage.fxml"));
        Parent root = scene.load();
        addProductPageController controller2 = scene.getController();
        controller2.initData(controller); // Pass the User object to the controller
        
        stage.setScene(new Scene(root));
        
        stage.setTitle("Add Product");
        stage.show();
        // stage.setScene(new Scene(root, 600, 400));

    }

    @FXML
    void recruitCarrierMouseClicked(MouseEvent event) throws IOException {
        FXMLLoader scene = new FXMLLoader(getClass().getResource("../fxml/employFireCarrierPage.fxml"));
        Parent root = scene.load();
        employFireCarrierPageController controller2 = scene.getController();
        controller2.initData(this.admin,controller2); // Pass the User object to the controller
        stage = (Stage) profilePhoto.getScene().getWindow();
        stage.setScene(new Scene(root));
        
        stage.setTitle("Employ / Fire Carrier");
        stage.show();
    }

    public VBox createVboxGroup(Product product){
        // Create the VBox with specified properties
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
        TextField nameField = new TextField();
        nameField.setText(product.getName());
        TextField priceField = new TextField();
        HBox priceHbox = new HBox();
        HBox quantityHbox = new HBox();
        HBox thresholdHbox = new HBox();
        Text currencyText = new Text(" TL");
        Text quantityText = new Text(product.getIsPiece() ? "Piece" : "Kg");
        Text thresholdText = new Text(product.getIsPiece() ? "Piece" : "Kg");
        quantityText.setStyle("-fx-fill: white;");
        currencyText.setStyle("-fx-fill: white;");
        thresholdText.setStyle("-fx-fill: white;");
        priceHbox.getChildren().addAll(priceField, currencyText);
        
        
        //hbox center
        priceHbox.setSpacing(5);
        quantityHbox.setSpacing(5);
        thresholdHbox.setSpacing(5);
        priceHbox.setStyle("-fx-alignment: center;");
        priceField.setText(Double.valueOf(product.getPrice()).toString());
        innerVBox1.getChildren().addAll(nameField, priceHbox);
        innerVBox1.getChildren().get(0);
        // Create two text fields and a button
        TextField quantityField = new TextField();
        quantityHbox.getChildren().addAll(quantityField, quantityText);
        quantityField.setText((product.getIsPiece() ? Integer.valueOf((int)product.getStock()).toString() : Double.valueOf(product.getStock()).toString()));
        TextField thresholdField = new TextField();
        thresholdHbox.getChildren().addAll(thresholdField, thresholdText);
        Text emptyPlaces = new Text();
        emptyPlaces.setStyle("-fx-fill: red;");
        thresholdField.setText((product.getIsPiece() ? Integer.valueOf((int)product.getThreshold()).toString() : Double.valueOf(product.getThreshold()).toString()));
        Button updateButton = new Button("Update");
        updateButton.setId("button");
        updateButton.setOnMouseClicked(e -> {
            addProductPageController addProductPageController = new addProductPageController();

            if (addProductPageController.checkIfEmpty(nameField.getText(), priceField.getText(), quantityField.getText(), thresholdField.getText(),"")) {
                emptyPlaces.setText("Please fill all the places");
            }
            else if (!addProductPageController.checkIfPriceNumber(priceField.getText())) {
                emptyPlaces.setText("Please enter a number for price");
            }
            else if (!addProductPageController.checkIfStockNumber(quantityField.getText(), product.getIsPiece())) {
                emptyPlaces.setText("Please enter a number for stock");
            }
            else if (!addProductPageController.checkIfThresholdNumber(thresholdField.getText(), product.getIsPiece())) {
                emptyPlaces.setText("Please enter a number for threshold");
            }
            else if (!addProductPageController.checkIfStockInteger(quantityField.getText(), null)) {
                emptyPlaces.setText("Please enter an integer for stock when piece is selected");
            }
            else if (!addProductPageController.checkIfThresholdDouble(thresholdField.getText(), null)) {
                emptyPlaces.setText("Please enter an integer for threshold when piece is selected");
            } 
            else{
                DatabaseAdapter dbAdapter = new DatabaseAdapter();
                Product product2 = new Product(nameField.getText(),  Double.parseDouble(quantityField.getText()), Double.parseDouble(priceField.getText()), Double.parseDouble(thresholdField.getText()), product.getType(), product.getId(), product.getIsPiece());
                dbAdapter.updateProduct(product2);
                dbAdapter.closeConnection();
                try {
                    refreshPage();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        // Create an inner VBox with text fields and button
        VBox innerVBox2 = new VBox();
        Button removeButton = new Button("Remove");
        removeButton.setId("button");
        removeButton.setOnMouseClicked(e -> {
            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            dbAdapter.removeProduct(product.getId());
            dbAdapter.closeConnection();
            try {
                refreshPage();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        innerVBox2.getChildren().addAll(quantityHbox, thresholdHbox, updateButton, removeButton, emptyPlaces);

        // Add components to the outer VBox
        outerVBox.getChildren().addAll(imageView, innerVBox1, innerVBox2);
        return outerVBox;
        
    }

    

    public void refreshPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminHomePage.fxml"));
        Parent root = loader.load();
        adminHomePageController controller = loader.getController();
        controller.initData(admin,root,controller); // Pass the User object to the controller
        stage = (Stage) profilePhoto.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
}
