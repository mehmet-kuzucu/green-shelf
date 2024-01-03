package app.greenshelf.controllers;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Product;
import app.greenshelf.controllers.adminHomePageController;
public class addProductPageController {
    
    private String encodedImage;
    private adminHomePageController adminHomePageController;
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

    @FXML
    private ToggleButton pieceToggleButton;

    @FXML
    private ToggleButton kiloToggleButton;
    
    private DatabaseAdapter dbAdapter;

    public void initialize() {
        comboBox.getItems().addAll("Fruit", "Vegetable");
    }

    public void initData(adminHomePageController adminHomePageController) {
        this.adminHomePageController = adminHomePageController;
        kiloToggleButton.setSelected(true);
        //price is only numbers
        
    }
    
    @FXML
    private ImageView productImage;

    @FXML
    void addImageButtonOnMouseClicked(MouseEvent event) {
        this.encodedImage = encodeImageToBase64();
        decodeBase64ToImage(encodedImage);
    }

    @FXML
    void addToCartClicked(MouseEvent event) {
        if (checkIfEmpty(name.getText(), price.getText(), stock.getText(), threshold.getText(), comboBox.getValue())) {
            emptyPlaces.setText("Please fill all the places");
        }
        else if(doesNameExist(name.getText())){
            emptyPlaces.setText("This product name already exists");
        }
        else if (!checkIfPriceNumber(price.getText())) {
            emptyPlaces.setText("Please enter a number for price");
        }
        else if (!checkIfStockNumber(stock.getText(), pieceToggleButton.isSelected())) {
            emptyPlaces.setText("Please enter a number for stock");
        }
        else if (!checkIfThresholdNumber(threshold.getText(), pieceToggleButton.isSelected())) {
            emptyPlaces.setText("Please enter a number for threshold");
        }
        else if (!checkIfStockInteger(stock.getText(), pieceToggleButton)) {
            emptyPlaces.setText("Please enter an integer for stock when piece is selected");
        }
        else if (!checkIfThresholdDouble(threshold.getText(), pieceToggleButton)) {
            emptyPlaces.setText("Please enter an integer for threshold when piece is selected");
        }
        else {
            boolean isPiece = kiloToggleButton.isSelected() ? false : true;
            Product product = new Product(name.getText(), Double.parseDouble(stock.getText()),this.encodedImage,
                    Double.parseDouble(price.getText()), Double.parseDouble(threshold.getText()), comboBox.getValue(), 0, isPiece); // niye 0?
            dbAdapter = new DatabaseAdapter();
            dbAdapter.addProductToDb(product);
            dbAdapter.closeConnection();
            emptyPlaces.setText("Product added successfully"); 
            emptyPlaces.setStyle("-fx-fill: green");
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(e -> {
                try {
                    this.adminHomePageController.refreshPage();
                    Stage stage = (Stage) emptyPlaces.getScene().getWindow();
                    stage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            pause.play();
        }
    }

    @FXML
    private void pieceToggleButtonOnMouseClicked(MouseEvent event) {
        kiloToggleButton.setSelected(false);
        pieceToggleButton.setSelected(true);
    }
    @FXML
    private void kiloToggleButtonOnMouseClicked(MouseEvent event) {
        pieceToggleButton.setSelected(false);
        kiloToggleButton.setSelected(true);
    }

    public boolean checkIfEmpty(String name, String price, String stock, String threshold, String type) {
        if (name.isEmpty() || price.isEmpty() || stock.isEmpty()
                || threshold.isEmpty() || type == null) {
            return true;
        }
        return false;
    }

    public boolean doesNameExist(String name){
        boolean result = false;
        dbAdapter = new DatabaseAdapter();
        result = dbAdapter.checkProductName(name);
        dbAdapter.closeConnection();
        return result;
    }

    public boolean checkIfPriceNumber(String price) {
        try {
            Double.parseDouble(price);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkIfThresholdNumber(String threshold, Boolean isPiece) {
        try {
            if (isPiece && threshold.contains(".")) {
                return false;
            }
            Double.parseDouble(threshold);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    } 


    public boolean checkIfStockInteger(String stock, ToggleButton pieceToggleButton) {
        try {

            if (pieceToggleButton == null) {
                return true;
            }
            if  (pieceToggleButton.isSelected()) {
                Integer.parseInt(stock);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkIfThresholdDouble(String threshold, ToggleButton pieceToggleButton) {
        try {

            if (pieceToggleButton == null) {
                return true;
            }
            if  (pieceToggleButton.isSelected()) {
                Integer.parseInt(threshold);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean checkIfStockNumber(String stock, Boolean isPiece) {
        try {

            if (isPiece && stock.contains(".")) {
                return false;
            }
            Double.parseDouble(stock);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public String encodeImageToBase64() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Photo");
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
            
        } else {
            productImage.setImage(image);
        }
    }

}
