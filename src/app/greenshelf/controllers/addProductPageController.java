package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import app.greenshelf.DatabaseAdapter;

public class addProductPageController {

    private Stage stage;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private TextField stock;

    @FXML
    private TextField threshold;

    @FXML
    private Text emptyPlaces;
    
    private DatabaseAdapter dbAdapter;

    public void initialize() {
        comboBox.getItems().addAll("Fruit", "Vegetable");
    }

    @FXML
    private ImageView productImage;

    @FXML
    void addImageButtonOnMouseClicked(MouseEvent event) {
        String encodedImage = encodeImageToBase64();
        decodeBase64ToImage(encodedImage);
    }

    @FXML
    void shoppingCartButtonOnMouseClicked(MouseEvent event) {
        if (checkIfEmpty()) {
            emptyPlaces.setText("Please fill all the places");
        }
        if(doesNameExist()){
            emptyPlaces.setText("This product name already exists");
        }
        else {
            /*
            dbAdapter = new DatabaseAdapter();
            dbAdapter.addProduct(name.getText(), comboBox.getValue(), price.getText(), stock.getText(),
                    threshold.getText(), encodeImageToBase64());
            dbAdapter.closeConnection();
            emptyPlaces.setText("Product added successfully"); */
        }
    }

    private boolean checkIfEmpty() {
        if (name.getText().isEmpty() || price.getText().isEmpty() || stock.getText().isEmpty()
                || threshold.getText().isEmpty() || comboBox.getValue() == null) {
            return true;
        }
        return false;
    }

    private boolean doesNameExist(){
        boolean result = false;
        dbAdapter = new DatabaseAdapter();
        result = dbAdapter.checkProductName(name.getText());
        dbAdapter.closeConnection();
        return result;
    }
    public String encodeImageToBase64() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Photo");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        stage = (Stage) price.getScene().getWindow();
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
        if (productImage == null) {
            productImage = new ImageView(image);
            productImage.setImage(image);
            //profilePhotoImage.getImage();
            
        } else {
            productImage.setImage(image);
        }
    }

}
