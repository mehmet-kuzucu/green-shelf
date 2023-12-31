package app.greenshelf.controllers;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
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

    @FXML
    void shoppingCartButtonOnMouseClicked(MouseEvent event) {
        // when button clicked, make the button unvisible
        /* open a new stage which new product will be added */
        Stage stage = new Stage();
        FXMLLoader scene = new FXMLLoader(getClass().getResource("app/greenshelf/fxml/appProductPage.fxml"));
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
}
