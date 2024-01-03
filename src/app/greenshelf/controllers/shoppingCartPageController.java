package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.LinkedList;

import app.greenshelf.Customer;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class shoppingCartPageController {
    @FXML
    private Button buyButton;

    @FXML
    private ImageView profilePhotoImage;

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private VBox productsVBox;

    private Customer currentUser;
    private Stage stage;
    private Scene scene;
    private LinkedList <Order> orderArray;

    public void initData(Customer user, LinkedList <Order> orderArray) {
        currentUser = user;
        this.orderArray = orderArray;
        //convert byte array back to Image
        profilePhotoImage.setImage( new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        //get products from shopping cart

        System.out.println("Size of the order array: " + this.orderArray.size());
        for (Order order : this.orderArray) {
            VBox group = createVboxGroup(this.orderArray.getFirst());
            productsVBox.getChildren().add(group);
        }

        
        
    }

    @FXML
    void greenShelfLogoOnMouseClicked() {
        loadScene("../fxml/customerHome.fxml", currentUser);
    }

    private void loadScene(String fxmlPath, Customer user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhotoImage.getScene().getWindow();
            customerHomeController controller = loader.getController();
            controller.initData(user, orderArray, 0, 0, null);
            scene = new Scene(root);
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
        VBox innerVBox = new VBox();
        innerVBox.setId("deneme");
        innerVBox.setAlignment(javafx.geometry.Pos.CENTER);
        innerVBox.setSpacing(5.0);
        innerVBox.getStylesheets().add(getClass().getResource("../css/Style.css").toExternalForm());

        Text text1 = new Text(order.getOrderID());
        text1.setFill(javafx.scene.paint.Color.WHITE);
        Text text2 = new Text(order.getAmount()+"₺");
        text2.setFill(javafx.scene.paint.Color.WHITE);

        innerVBox.getChildren().addAll(text1, text2);
        /* 
        Spinner<Double> spinner = new Spinner<>();
        spinner.setId("spinner");
        spinner.setValueFactory(new javafx.scene.control.SpinnerValueFactory.DoubleSpinnerValueFactory(1.0, product.getStock(), 1.0, 0.1));
        spinner.setEditable(false);
        */
        

        productInfo.getChildren().addAll(/*imageView,*/ innerVBox);
        return productInfo;
        
    }




}
