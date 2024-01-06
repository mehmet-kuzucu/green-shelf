package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import app.greenshelf.Carrier;
import app.greenshelf.DatabaseAdapter;

public class addCarrierPageController {
    private Stage stage;
    private String encodedImage;
    private employFireCarrierPageController employFireCarrierPageController;
    @FXML
    private Button addToCartButton;

    @FXML
    private ImageView carrierPicture;

    @FXML
    private TextField email;

    @FXML
    private Text emptyPlaces;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private TextField surname;

    @FXML
    private TextField username;

    public void initData(employFireCarrierPageController controller) {
        this.employFireCarrierPageController = controller;
    }
    @FXML
    void addImageButtonOnMouseClicked(MouseEvent event) {
        this.encodedImage = encodeImageToBase64();
        decodeBase64ToImage(encodedImage);
    }

    @FXML
    void addToCarrierClicked(MouseEvent event) throws IOException {
        if (!checkEmptyPlaces(name.getText(), surname.getText(), password.getText(), email.getText(), phone.getText(), username.getText())) {
            emptyPlaces.setText("Please fill all the places");
        }
        else if (checkIfUsernameExists(username.getText())) {
            emptyPlaces.setText("Username already exists");
        }
        else if (!checkEmailisValid(email.getText())) {
            emptyPlaces.setText("Email is not valid");
        }
        else if (!checkPhoneisValid(phone.getText())) {
            emptyPlaces.setText("Phone number is not valid");
        }
        else if (encodedImage == null)
        {
            emptyPlaces.setText("Please add a photo");
        }
        else {
            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            if(dbAdapter.checkUsernameSql(encodedImage)){
                emptyPlaces.setText("Username already exists");
                return;
            }else if(dbAdapter.checkEmailSql(email.getText())){
                emptyPlaces.setText("Email already exists");
                return;
            }else if(dbAdapter.checkPhoneSql(phone.getText())){
                emptyPlaces.setText("Phone number already exists");
                return;
            }
            Carrier carrier = new Carrier(name.getText(), surname.getText(), password.getText(), email.getText(), phone.getText(), username.getText(), "carrier", encodedImage);
            carrier.setAddress("Default");
            dbAdapter.registerUserSql(carrier);
            dbAdapter.closeConnection();
            emptyPlaces.setText("Carrier added successfully");
            emptyPlaces.setStyle("-fx-fill: green");
            /* close the scene */
            Platform.runLater(() -> {
                Stage stage = (Stage) addToCartButton.getScene().getWindow();
                stage.close();
            });
            employFireCarrierPageController.refreshPage();
        }
    }

    public boolean checkEmptyPlaces(String name, String surname, String password, String email, String phone, String username) {
        if (name.isEmpty() || surname.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || username.isEmpty()) {
            return false;
        } else {
            return true;
        }
        
    }

    public boolean checkEmptyPlaces(String name, String surname, String password, String email, String phone) {
        if (name.isEmpty() || surname.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            return false;
        } else {
            return true;
        }
        
    }

    public boolean checkIfUsernameExists(String username) {
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        if (dbAdapter.checkUsernameSql(username)) {
            return true;
        } else {
            return false;
        }
    }


    @FXML
    public Boolean checkUsername(TextField registerUsernameField) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        if (databaseAdapter.checkUsernameSql(registerUsernameField.getText())) {
            return true;
        } else {
            return false;
        }
    }
    @FXML
    public Boolean checkEmailisValid(String email) {
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
    public Boolean checkEmail(TextField registerEmailField) {
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
    public Boolean checkPhoneisValid(String phone) {
        String phoneRegex = "^[0-9]{10}$"; 
                              
        Pattern pat = Pattern.compile(phoneRegex); 
        if (phone == null) 
            return false; 
        return pat.matcher(phone).matches(); 
    }

    public String encodeImageToBase64() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Photo");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        stage = (Stage) username.getScene().getWindow();
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

    public void decodeBase64ToImage(String encodedImage) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedImage);
        Image image = new Image(new ByteArrayInputStream(decodedBytes));
        if (carrierPicture == null) {
            carrierPicture = new ImageView(image);
            carrierPicture.setImage(image);
            
        } else {
            carrierPicture.setImage(image);
        }
    }

    

}
