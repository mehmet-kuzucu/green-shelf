package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import app.greenshelf.Customer;
import app.greenshelf.Order;
import app.greenshelf.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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


}
