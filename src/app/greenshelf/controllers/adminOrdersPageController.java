package app.greenshelf.controllers;

import java.io.IOException;

import app.greenshelf.Admin;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class adminOrdersPageController {

    @FXML
    private ImageView greenShelfLogo;

    private Stage stage;
    private Scene scene;

    private Admin currentUser;

    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminHomePage.fxml"));
        Parent root = loader.load();
        stage = (Stage) greenShelfLogo.getScene().getWindow();
        adminHomePageController controller = loader.getController();
        controller.initData(currentUser,root,controller);


        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
