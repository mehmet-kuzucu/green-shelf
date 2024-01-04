package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
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
    private Carrier currentCarrier;
    @FXML
    private Button changeProfilePhotoButton;

    String encodedImage;

    private List <Order> shoppingCart;
    private String orderID;
    private int cartCount;
    private double totalPrice;
    private HashMap<Integer, Double> productStockMap = new HashMap<Integer, Double>();


    public void initData(Customer user, List<Order> shoppingCart, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        this.currentUser = user;
        this.shoppingCart = shoppingCart;
        this.cartCount = cartCount;
        this.totalPrice = totalPrice;
        this.productStockMap = productStockMap;
        this.orderID = orderID;
        nameDescription.setText(currentUser.getName() + " '" + currentUser.getUsername() + "' " + currentUser.getSurname());
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        changeEmailField.setText(currentUser.getEmail());
        changeAddressField.setText(currentUser.getAddress());
        changePhoneField.setText(currentUser.getPhone());
        changePasswordField.setText(currentUser.getPassword());
        encodedImage = currentUser.getProfilePicture();

    }

    public void initData(Carrier user) {
        this.currentCarrier = user;
        nameDescription.setText(currentCarrier.getName() + " '" + currentCarrier.getUsername() + "' " + currentCarrier.getSurname());
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentCarrier.getProfilePicture()))));
        changeEmailField.setText(currentCarrier.getEmail());
        changeAddressField.setText(currentCarrier.getAddress());
        changePhoneField.setText(currentCarrier.getPhone());
        changePasswordField.setText(currentCarrier.getPassword());
        encodedImage = currentCarrier.getProfilePicture();

    }

    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) {
        if (currentUser != null)
            loadScene("../fxml/customerHome.fxml", currentUser,null,null, shoppingCart, cartCount, totalPrice, productStockMap, orderID);
        else if (currentCarrier != null)
            loadScene("../fxml/carrierHomePage.fxml",null,null,currentCarrier, shoppingCart, cartCount, totalPrice, productStockMap, orderID);
    }

    private void loadScene(String fxmlPath, Customer user, Admin admin, Carrier carrier, List <Order> orderArray, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                controller.initData(user, orderArray, cartCount, totalPrice, productStockMap,orderID); // Pass the User object to the controller
            }
            if (fxmlPath.equals("../fxml/adminHomePage.fxml")) {
                adminHomePageController controller = loader.getController();
                controller.initData(admin,root,controller); // Pass the User object to the controller
            }
            if (fxmlPath.equals("../fxml/carrierHomePage.fxml")) {
                carrierHomePageController controller = loader.getController();
                controller.initData(carrier); // Pass the User object to the controller
            }
            
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
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
        encodedImage = encodeImageToBase64();
        decodeBase64ToImage(encodedImage);
    }


    @FXML
    void saveButtonOnMouseClicked(MouseEvent event)
    {
        if(this.currentUser != null)
        {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter();
            registerPageController registerPageController = new registerPageController();

            String new_password = changePasswordField.getText();
            String new_email = changeEmailField.getText();
            String new_address = changeAddressField.getText();
            String new_phone = changePhoneField.getText();
            if(new_password.equals("") || new_email.equals("") || new_address.equals("") || new_phone.equals(""))
            {
                editErrorText.setText("Please fill all the fields");
                return;
            }
            if(registerPageController.checkEmailisValid(changeEmailField) == false)
            {
                editErrorText.setText("Email is not valid");
                changeEmailField.setStyle("-fx-border-color: red");
                return;
            }
            if(registerPageController.checkPhoneisValid(changePhoneField) == false)
            {
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
        else if(this.currentCarrier != null)
        {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter();
            registerPageController registerPageController = new registerPageController();

            String new_password = changePasswordField.getText();
            String new_email = changeEmailField.getText();
            String new_address = changeAddressField.getText();
            String new_phone = changePhoneField.getText();
            if(changeAddressField.getText().equals("") || changeEmailField.getText().equals("") || changePhoneField.getText().equals("") || changePasswordField.getText().equals(""))
            {
                editErrorText.setText("Please fill all the fields");
                System.out.println("Please fill all the fields");
                return;
            }
            if(registerPageController.checkEmailisValid(changeEmailField) == false)
            {
                editErrorText.setText("Email is not valid");
                changeEmailField.setStyle("-fx-border-color: red");
                return;
            }
            if(registerPageController.checkPhoneisValid(changePhoneField) == false)
            {
                editErrorText.setText("Phone is not valid");
                changePhoneField.setStyle("-fx-border-color: red");
                return;
            }
            databaseAdapter.updateInfo(new_password, new_email, new_address, new_phone, encodedImage, this.currentCarrier.getUsername());
            try {
                System.out.println("carrier refreshing page");
                this.refreshPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }
        
    }

    public void refreshPage() throws IOException{
        if (this.currentUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/profileInfoPage.fxml"));
            Parent root = loader.load();
            profileInfoPageController controller = loader.getController();
            DatabaseAdapter databaseAdapter = new DatabaseAdapter();
            List<String> list = databaseAdapter.loginUserSql(currentUser.getUsername());
            currentUser = new Customer(list.get(5), list.get(6), list.get(7), list.get(2), list.get(3), list.get(1), list.get(4), list.get(8));
            controller.initData(currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID);
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        }
        else if (this.currentCarrier != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/profileInfoPage.fxml"));
            Parent root = loader.load();
            profileInfoPageController controller = loader.getController();
            DatabaseAdapter databaseAdapter = new DatabaseAdapter();
            List<String> list = databaseAdapter.loginUserSql(currentCarrier.getUsername());
            currentCarrier = new Carrier(list.get(5), list.get(6), list.get(7), list.get(2), list.get(3), list.get(1), list.get(4), list.get(8));
            controller.initData(currentCarrier);
            stage = (Stage) greenShelfLogo.getScene().getWindow();
            scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
        }
        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/profileInfoPage.fxml"));
        Parent root = loader.load();
        profileInfoPageController controller = loader.getController();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        List<String> list = databaseAdapter.loginUserSql(currentUser.getUsername());
        currentUser = new Customer(list.get(5), list.get(6), list.get(7), list.get(2), list.get(3), list.get(1), list.get(4), list.get(8));
        controller.initData(currentUser, orderArray, cartCount, totalPrice, productStockMap);
        stage = (Stage) greenShelfLogo.getScene().getWindow();
        scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
        stage.setScene(scene);
        stage.show();*/

    }
}
