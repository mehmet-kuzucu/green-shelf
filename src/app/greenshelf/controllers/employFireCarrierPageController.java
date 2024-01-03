package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.greenshelf.Admin;
import app.greenshelf.Carrier;
import app.greenshelf.DatabaseAdapter;

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
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        List<Carrier> carriers = databaseAdapter.getCarriers();
        for(Carrier carrier : carriers){
            System.out.println(carrier.getName());
            VBox carrierInfo = createVBox(carrier);
            tilePane.getChildren().add(carrierInfo);
        }
        databaseAdapter.closeConnection();
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

    public VBox createVBox(Carrier carrier){
        VBox carrierInfo = new VBox();
        carrierInfo.setId("productInfo"); // Make sure to define the CSS styles accordingly
        carrierInfo.setAlignment(javafx.geometry.Pos.CENTER);
        carrierInfo.setPrefHeight(400.0);
        carrierInfo.setPrefWidth(200.0);
        carrierInfo.setSpacing(15.0);

        byte[] decodedBytes = Base64.getDecoder().decode(carrier.getProfilePicture());
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(decodedBytes)));
        imageView.setFitHeight(150.0);
        imageView.setFitWidth(200.0);
        imageView.setPreserveRatio(true);

        TextField nameTextField = new TextField();
        nameTextField.setText(carrier.getName());

        TextField surnameTextField = new TextField();
        surnameTextField.setText(carrier.getSurname());

        TextField phoneTextField = new TextField();
        phoneTextField.setText(carrier.getPhone());

        TextField emailTextField = new TextField();
        emailTextField.setText(carrier.getEmail());

        PasswordField passwordField = new PasswordField();
        passwordField.setText(carrier.getPassword());

        Text totalDeliveriesText = new Text("Total Deliveries");
        totalDeliveriesText.setFill(javafx.scene.paint.Color.WHITE);

        Button addToCartButton = new Button("Update");

        addToCartButton.setOnAction(e -> {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter();
            Carrier carrier2 = new Carrier(nameTextField.getText(), surnameTextField.getText(), passwordField.getText(), emailTextField.getText(), phoneTextField.getText(), carrier.getUsername(), null, carrier.getProfilePicture());
            try {
                databaseAdapter.updateCarrier(carrier2);
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                
                refreshPage();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            databaseAdapter.closeConnection();
        });
        Button removeButton = new Button("Remove");

        carrierInfo.getChildren().addAll(
                imageView, nameTextField, surnameTextField,
                phoneTextField, emailTextField, passwordField, totalDeliveriesText,
                addToCartButton, removeButton
        );

        carrierInfo.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
        return carrierInfo;
    }
    

}
