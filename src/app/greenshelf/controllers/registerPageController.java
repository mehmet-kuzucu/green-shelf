package app.greenshelf.controllers;
import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.util.Base64;
import java.io.FileInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

//import from parent directory



public class registerPageController {

    private Stage stage;

    private DatabaseAdapter databaseAdapter;

    String encodedImage;

    @FXML
    private TextField registerNameField;

    @FXML
    private TextField registerSurnameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private PasswordField registerConfirmPasswordField;

    @FXML
    private TextField registerEmailField;

    @FXML
    private TextField registerPhoneField;

    @FXML
    private TextField registerAddressField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private Button chooseProfilePhotoButton;

    @FXML
    private Hyperlink existedAccount;

    @FXML
    private Button nextStepButton;

    @FXML
    private ImageView profilePhotoImage;


    @FXML
    void existedAccountOnMouseClicked(MouseEvent event) {
        System.out.println("Existed account clicked!");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/loginPage.fxml"));
            Stage stage = (Stage) existedAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    void nextStepButtonOnMouseClicked(MouseEvent event) {
        System.out.println("Next step clicked!");
        //password check
        Customer newCustomer = new Customer(registerNameField.getText(), registerSurnameField.getText(), registerPasswordField.getText(), registerEmailField.getText(), registerPhoneField.getText(), registerUsernameField.getText(),  registerAddressField.getText(), encodedImage);
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.registerUserSql(newCustomer);
        databaseAdapter.closeConnection();


        /* 

        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/registerPage2.fxml"));
            Stage stage = (Stage) existedAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }


    @FXML
    private void chooseProfilePhotoButtonOnMouseClicked(MouseEvent event) {
        System.out.println("Choose profile photo clicked!");
        encodedImage = encodeImageToBase64();
        System.out.println(encodedImage);
        decodeBase64ToImage(encodedImage);
        
        System.out.println("buraya kadar calisti");
    }
    
    //calisiyor
    @FXML
    public String encodeImageToBase64() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        stage = (Stage) chooseProfilePhotoButton.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            byte[] imageData = new byte[(int) file.length()];
            imageInFile.read(imageData);
            return Base64.getEncoder().encodeToString(imageData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //bunu test etmedim
    @FXML
    public void decodeBase64ToImage(String encodedImage) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        Image image = new Image(new ByteArrayInputStream(decodedBytes));
        if (profilePhotoImage == null) {
            profilePhotoImage = new ImageView(image);
            profilePhotoImage.setImage(image);
            //profilePhotoImage.getImage();
            
        } else {
            profilePhotoImage.setImage(image);
        }
    }

}
