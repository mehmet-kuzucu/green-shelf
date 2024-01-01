package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

import app.greenshelf.Admin;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class adminHomePageController {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private DatabaseAdapter dbAdapter;
    private adminHomePageController controller;
    @FXML
    private Button addToCartButton;

    @FXML
    private Button profilePhoto;

    @FXML
    private TilePane adminTilePane;

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Spinner<?> spinner71;

    @FXML
    private Group group;
    @FXML
    private Spinner<?> spinner711;

    @FXML
    private Spinner<?> spinner7111;

    private Admin admin;

    public void initData(Admin admin, Parent root, adminHomePageController controller)
    {
        this.admin = admin;
        this.root = root;
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
        System.out.println("kasldnlkasnd");
    
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
        
        stage.setTitle("User Information Example");
        stage.show();
        // stage.setScene(new Scene(root, 600, 400));

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
        priceField.setText(product.getPrice() + " TL");
        innerVBox1.getChildren().addAll(nameField, priceField);
        innerVBox1.getChildren().get(0);
        // Create two text fields and a button
        TextField quantityField = new TextField();
        quantityField.setText(Double.valueOf(product.getStock()).toString() + " kg");
        TextField thresholField = new TextField();
        thresholField.setText(product.getThreshold() + " kg");
        Button button = new Button("Click Me");

        // Create an inner VBox with text fields and button
        VBox innerVBox2 = new VBox();
        innerVBox2.getChildren().addAll(quantityField, thresholField, button);

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
