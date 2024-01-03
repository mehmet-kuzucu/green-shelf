package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import app.greenshelf.*;


public class profileInfoPageController {

    private Stage stage;
    private Scene scene;
    
    @FXML
    private Button saveProfileButton;

    @FXML
    private ImageView profilePhotoImage;
    
    @FXML
    private Text nameDescription;

    @FXML
    private PasswordField changePasswordField;

    @FXML
    private TextField changePhoneField;

    @FXML
    private TextField changeAddressField;

    @FXML
    private TextField changeEmailField;

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private Text editErrorText;

    private Customer currentUser;

    @FXML
    private Button changeProfilePhotoButton;

    String encodedImage;


    public void initData(Customer user) {
        this.currentUser = user;
        nameDescription.setText(currentUser.getName() + " '" + currentUser.getUsername() + "' " + currentUser.getSurname());
        System.out.println(currentUser.getName());
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        changeEmailField.setText(currentUser.getEmail());
        changeAddressField.setText(currentUser.getAddress());
        changePhoneField.setText(currentUser.getPhone());
        changePasswordField.setText(currentUser.getPassword());
        encodedImage = currentUser.getProfilePicture();

    }

    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/customerHome.fxml", currentUser,null,null);
    }

    private void loadScene(String fxmlPath, Customer user, Admin admin, Carrier carrier) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                controller.initData(user); // Pass the User object to the controller
            }
            if (fxmlPath.equals("../fxml/adminHomePage.fxml")) {
                adminHomePageController controller = loader.getController();
                controller.initData(admin,root,controller); // Pass the User object to the controller
            }
            
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String encodeImageToBase64()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Change Profile Photo");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        stage = (Stage) changeProfilePhotoButton.getScene().getWindow();
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

    @FXML
    void changeProfilePhotoButtonOnMouseClicked(MouseEvent event)
    {
        System.out.println("changeProfilePhotoButtonOnMouseClicked");

        encodedImage = encodeImageToBase64();
        decodeBase64ToImage(encodedImage);
    }


    @FXML
    void saveButtonOnMouseClicked(MouseEvent event)
    {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        registerPageController registerPageController = new registerPageController();

        String new_password = changePasswordField.getText();
        String new_email = changeEmailField.getText();
        String new_address = changeAddressField.getText();
        String new_phone = changePhoneField.getText();

        if(registerPageController.checkEmailisValid(changeEmailField) == false)
        {
            System.out.println("Email is not valid");
            editErrorText.setText("Email is not valid");
            changeEmailField.setStyle("-fx-border-color: red");
            return;
        }
        if(registerPageController.checkPhoneisValid(changePhoneField) == false)
        {
            System.out.println("Phone is not valid");
            editErrorText.setText("Phone is not valid");
            changePhoneField.setStyle("-fx-border-color: red");
            return;
        }
        databaseAdapter.updateInfo(new_password, new_email, new_address, new_phone, encodedImage, currentUser.getUsername());
        try {
            this.refreshPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshPage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/profileInfoPage.fxml"));
        Parent root = loader.load();
        profileInfoPageController controller = loader.getController();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        List<String> list = databaseAdapter.loginUserSql(currentUser.getUsername());
        currentUser = new Customer(list.get(5), list.get(6), list.get(7), list.get(2), list.get(3), list.get(1), list.get(4), list.get(8));
        controller.initData(currentUser);
        stage = (Stage) greenShelfLogo.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
