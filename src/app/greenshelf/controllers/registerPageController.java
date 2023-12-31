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
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.util.Base64;
import java.io.FileInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.regex.Pattern;

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
    private Text errorTextRegister;

    private String style = "-fx-background-radius: 20; -fx-border-radius: 20;-fx-padding: 8;-fx-font-size: 14px; -fx-border-color: linear-gradient(to right, #00fff2, #1900ff); -fx-background-color: linear-gradient(to right, #e6e6e6, #ffffff);";

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
        errorTextRegister.setText("");
        registerPasswordField.setStyle(style);
        registerConfirmPasswordField.setStyle(style);
        registerUsernameField.setStyle(style);
        registerEmailField.setStyle(style);
        registerPhoneField.setStyle(style);
        


        /* DO THE CHECKERS */
        if (areFieldsEmpty())
        {
            return;
        }
        if (!checkPassword(registerPasswordField, registerConfirmPasswordField)) {
            System.out.println("Passwords do not match!");
            errorTextRegister.setText("Passwords do not match!");
            registerPasswordField.setStyle("-fx-border-color: red;");
            registerConfirmPasswordField.setStyle("-fx-border-color: red;");
            return;
        }
        else if (checkUsername(registerUsernameField)) {
            System.out.println("Username already exists!");
            errorTextRegister.setText("Username already exists!");
            registerUsernameField.setStyle("-fx-border-color: red;");
            return;
        }
        else if (!checkEmailisValid(registerEmailField)) {
            System.out.println("Email is not valid!");
            errorTextRegister.setText("Email is not valid!");
            registerEmailField.setStyle("-fx-border-color: red;");
            return;
        }
        else if (checkEmail(registerEmailField)) {
            System.out.println("Email already exists!");
            errorTextRegister.setText("Email already exists!");
            registerEmailField.setStyle("-fx-border-color: red;");
            return;
        }
        else if (checkPhone(registerPhoneField)) {
            System.out.println("Phone already exists!");
            errorTextRegister.setText("Phone already exists!");
            registerPhoneField.setStyle("-fx-border-color: red;");
            return;
        }
        else if (!checkPhoneisValid(registerPhoneField)) {
            System.out.println("Phone is not valid!");
            errorTextRegister.setText("Phone is not valid!");
            registerPhoneField.setStyle("-fx-border-color: red;");
            return;
        }
        else {
            System.out.println("Everything is okay!");
            
        }

        if (encodedImage == null) {
            encodedImage = encodeImageToBase64("/images/defaultProfilePicture.png");
        }

        //password check
        Customer newCustomer = new Customer(registerNameField.getText(), registerSurnameField.getText(), registerPasswordField.getText(), registerEmailField.getText(), registerPhoneField.getText(), registerUsernameField.getText(),  registerAddressField.getText(), encodedImage);
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.registerUserSql(newCustomer);
        databaseAdapter.closeConnection();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/loginPage.fxml"));
            Stage stage = (Stage) existedAccount.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    private Boolean checkPassword(PasswordField registerPasswordField, PasswordField registerConfirmPasswordField) {
        if (registerPasswordField.getText().equals(registerConfirmPasswordField.getText())) {
            return true;
        } else {
            return false;
        }
    }


    @FXML
    private Boolean checkUsername(TextField registerUsernameField) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        if (databaseAdapter.checkUsernameSql(registerUsernameField.getText())) {
            return true;
        } else {
            return false;
        }
    }
    @FXML
    private Boolean checkEmailisValid(TextField registerEmailField) {
        String email= registerEmailField.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    }

    @FXML
    private Boolean checkEmail(TextField registerEmailField) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        if (databaseAdapter.checkEmailSql(registerEmailField.getText())) {
            return true;
        } else {
            return false;
        }
    }


    @FXML
    private Boolean checkPhone(TextField registerPhoneField) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        if (databaseAdapter.checkPhoneSql(registerPhoneField.getText())) {
            return true;
        } else {
            return false;
        }
    }

    /* check valid phone number */
    private Boolean checkPhoneisValid(TextField registerPhoneField) {
        String phone= registerPhoneField.getText();
        String phoneRegex = "^[0-9]{10}$"; 
                              
        Pattern pat = Pattern.compile(phoneRegex); 
        if (phone == null) 
            return false; 
        return pat.matcher(phone).matches(); 
    }

    @FXML
    private boolean areFieldsEmpty() {
        boolean isEmpty = false;
        registerNameField.setStyle(style);
        registerSurnameField.setStyle(style);
        registerPasswordField.setStyle(style);
        registerConfirmPasswordField.setStyle(style);
        registerEmailField.setStyle(style);
        registerPhoneField.setStyle(style);
        registerAddressField.setStyle(style);
        registerUsernameField.setStyle(style);

        if (registerNameField.getText().isEmpty())
        {
            registerNameField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerSurnameField.getText().isEmpty())
        {
            registerSurnameField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerPasswordField.getText().isEmpty())
        {
            registerPasswordField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerConfirmPasswordField.getText().isEmpty())
        {
            registerConfirmPasswordField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerEmailField.getText().isEmpty())
        {
            registerEmailField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerPhoneField.getText().isEmpty())
        {
            registerPhoneField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerAddressField.getText().isEmpty())
        {
            registerAddressField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (registerUsernameField.getText().isEmpty())
        {
            registerUsernameField.setStyle("-fx-border-color: red;");
            isEmpty = true;
        }
        if (isEmpty)
        {
            errorTextRegister.setText("Please fill the required fields!");
        }
        return isEmpty;
        
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

    @FXML
    public String encodeImageToBase64(String path) {
        File file = new File(path);
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
