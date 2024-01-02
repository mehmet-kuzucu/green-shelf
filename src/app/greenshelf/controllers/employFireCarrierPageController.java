package app.greenshelf.controllers;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import app.greenshelf.Admin;

public class employFireCarrierPageController {
    employFireCarrierPageController controller;
    private Stage stage;
    private Scene scene;
    private Admin admin;
    @FXML
    private Button addToCartButton;

    @FXML
    private Button profilePhoto;

    @FXML
    private Button shoppingCartButton;

    @FXML
    private TilePane tilePane;

    public void initData(Admin admin){
        this.admin = admin;

    }

    @FXML
    void addEmployeeClicked(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader scene = new FXMLLoader(getClass().getResource("../fxml/addCarrierPage.fxml"));
        Parent root = scene.load();
        addCarrierPageController controller2 = scene.getController();
        controller2.initData(controller); // Pass the User object to the controller
        
        stage.setScene(new Scene(root));
        
        stage.setTitle("Add Carrier");
        stage.show();
        // stage.setScene(new Scene(root, 600, 400));
    }

    public void refreshPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/employFireCarrierPage.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        this.controller.initData(admin); // Pass the User object to the controller
        stage = (Stage) profilePhoto.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    

}
