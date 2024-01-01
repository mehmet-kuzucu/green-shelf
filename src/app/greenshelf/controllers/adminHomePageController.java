package app.greenshelf.controllers;
import java.io.IOException;
import java.io.OutputStream;

import app.greenshelf.Admin;
import app.greenshelf.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class adminHomePageController {

    @FXML
    private Button addToCartButton;

    @FXML
    private Button profilePhoto;

    @FXML
    private Button shoppingCartButton;

    @FXML
    private Spinner<?> spinner71;

    @FXML
    private Spinner<?> spinner711;

    @FXML
    private Spinner<?> spinner7111;

    private Admin admin;

    public void initData(Admin admin)
    {
        this.admin = admin;
        System.out.println(this.admin.getName());
        
    }

    @FXML
    void shoppingCartButtonOnMouseClicked(MouseEvent event) {
        // when button clicked, make the button unvisible
        /* open a new stage which new product will be added */
        Stage stage = new Stage();
        FXMLLoader scene = new FXMLLoader(getClass().getResource("../fxml/addProductPage.fxml"));
        try {
            stage.setScene(new Scene(scene.load()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        stage.setTitle("User Information Example");
        stage.show();
        // stage.setScene(new Scene(root, 600, 400));

    }

    public void createVboxGroup(String cssID){
        Group rootGroup = new Group();

        // Create the VBox with specified properties
        VBox outerVBox = new VBox();
        outerVBox.setPadding(new javafx.geometry.Insets(5));
        outerVBox.setSpacing(15);
        outerVBox.setPrefWidth(200);
        outerVBox.setPrefHeight(400);
        outerVBox.setId(cssID);
        // Create an ImageView
        ImageView imageView = new ImageView(new Image("your_image_url.jpg"));
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);

        // Create an inner VBox with two text fields
        VBox innerVBox1 = new VBox();
        innerVBox1.getChildren().addAll(new TextField(), new TextField());

        // Create two text fields and a button
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        Button button = new Button("Click Me");

        // Create an inner VBox with text fields and button
        VBox innerVBox2 = new VBox();
        innerVBox2.getChildren().addAll(textField1, textField2, button);

        // Add components to the outer VBox
        outerVBox.getChildren().addAll(imageView, innerVBox1, innerVBox2);

    }
}
