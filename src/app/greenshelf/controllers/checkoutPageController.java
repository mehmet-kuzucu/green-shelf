package app.greenshelf.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import app.greenshelf.Customer;
import app.greenshelf.DatabaseAdapter;
import app.greenshelf.Order;
import app.greenshelf.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.security.SecureRandom;

public class checkoutPageController {
    @FXML
    private Button buyButton;

    @FXML
    private ChoiceBox<String> deliveryTimeChoiceBox;

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private VBox ordersVBox;

    @FXML
    private ImageView profilePhotoImage;

    @FXML
    private Text totalPriceText;

    private Stage stage;
    private Scene scene;

    Customer currentUser;
    List<Order> shoppingCart;
    int cartCount;
    double totalPrice;
    HashMap<Integer, Double> productStockMap;
    String orderID;
    double vat = 0.01;

    public void initData (Customer currentUser, List<Order> shoppingCart, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        this.currentUser = currentUser;
        this.shoppingCart = shoppingCart;
        this.cartCount = cartCount;
        this.totalPrice = totalPrice;
        this.productStockMap = productStockMap;
        this.orderID = orderID;
        //convert from base 64
        profilePhotoImage.setImage(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(currentUser.getProfilePicture()))));
        totalPriceText.setText("Total Price: " + (totalPrice + Math.round(totalPrice*vat*100)/100.0) + " TL (VAT included)");
        System.out.println(getCurrentTime());
        //deliveryTimeChoiceBox.getItems().addAll("hocam", "hocam2");
        
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        for (Order order : shoppingCart) 
        {
            HBox orderHBox = new HBox();
            //Text amountXpriceTextEqualsTotalPrice = new Text((product.getIsPiece() ? amountString : order.getAmount()) + (product.getIsPiece() ? " piece " : " kg ") + "x " + (product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2) + "₺" + " = " + order.getAmount()*(product.getThreshold() < product.getStock() ? product.getPrice() : product.getPrice() * 2) + "₺");
            String amountString = String.valueOf(order.getAmount());
            int indexOfDot = amountString.indexOf(".");
            if (indexOfDot != -1) {
                amountString = amountString.substring(0, indexOfDot);
            }
            Text orderText = new Text(dbAdapter.getProductFromId(order.getProductID()).getName() + " (" + order.getPrice() +"₺) x " + ((dbAdapter.getProductFromId(order.getProductID()).getIsPiece() ? amountString : order.getAmount())) + ((dbAdapter.getProductFromId(order.getProductID()).getIsPiece() ? " piece " : " kg ")) + "= " + order.getPrice() * order.getAmount() +"₺");
            //background color
            orderHBox.setStyle("-fx-background-color: #4B787C;");
            ImageView orderImage = new ImageView(new Image(new ByteArrayInputStream(Base64.getDecoder().decode(dbAdapter.getProductFromId(order.getProductID()).getImage()))));
            orderImage.setFitHeight(31);
            orderImage.setFitWidth(31);
            orderHBox.getChildren().addAll(orderImage, orderText);
            orderHBox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
            //center the image
            orderHBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            orderHBox.setSpacing(13);
            orderText.setStyle("-fx-font-size: 31px;");
            orderText.setFill(Color.WHITE);
            ordersVBox.getChildren().add(orderHBox);
            ordersVBox.setSpacing(13);
            ordersVBox.setAlignment(javafx.geometry.Pos.CENTER);

        }
        System.out.println(getCurrentTime());
        for (String possibleDeliveryTime : possibleDeliveryTimesWithin48Hours()) {
            deliveryTimeChoiceBox.getItems().add(possibleDeliveryTime);
        }
        deliveryTimeChoiceBox.setValue(deliveryTimeChoiceBox.getItems().get(0));
        
    }

    //returns a list of possible delivery times within 48 hours
    public List<String> possibleDeliveryTimesWithin48Hours()
    {
        String currentTime = getCurrentTime();
        String[] currentTimeSplit = currentTime.split(" ");
        String[] currentDateSplit = currentTimeSplit[0].split("-");
        String[] currentTimeSplit2 = currentTimeSplit[1].split(":");
        int currentDay = Integer.parseInt(currentDateSplit[0]);
        int currentMonth = Integer.parseInt(currentDateSplit[1]);
        int currentYear = Integer.parseInt(currentDateSplit[2]);
        int currentHour = Integer.parseInt(currentTimeSplit2[0]);
        String morningSession = "From 9:00 to 12:00";
        String afternoonSession = "From 12:00 to 15:00";
        String eveningSession = "From 15:00 to 18:00";
        List<String> possibleDeliveryTimes = new ArrayList<String>();
        LocalDate currentDate = LocalDate.of(currentYear, currentMonth, currentDay);
        Locale.setDefault(Locale.ENGLISH);
        //currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        //if it is 18:00 or later, then the earliest possible delivery time is tomorrow morning and the latest possible delivery time is the day after tomorrow evening (48 hours)
        if (currentHour >= 15)
        {
            
            possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
            possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
            possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
            possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
            possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
            possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
        }
        else
        {
            //if it is 15:00 or later, then the earliest possible delivery time is tomorrow morning and the latest possible delivery time is tomorrow evening (24 hours)
            if (currentHour >= 12)
            {
                possibleDeliveryTimes.add((currentDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
                possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
                possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
                possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);

            }
            else
            {
                //if it is 9:00 or later, then the earliest possible delivery time is tomorrow morning and the latest possible delivery time is tomorrow morning (3 hours)
                if (currentHour >= 9)
                {
                    possibleDeliveryTimes.add((currentDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
                    possibleDeliveryTimes.add((currentDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
                }
                else
                {
                    //if it is 9:00 or earlier, then the earliest possible delivery time is today morning and the latest possible delivery time is today morning (3 hours)
                    possibleDeliveryTimes.add((currentDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                    possibleDeliveryTimes.add((currentDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
                    possibleDeliveryTimes.add((currentDate.format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + afternoonSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(1).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + eveningSession);
                    possibleDeliveryTimes.add((currentDate.plusDays(2).format(DateTimeFormatter.ofPattern("dd-MMMM-yyyy"))) + " " + morningSession);
                }
            }
        }
        return possibleDeliveryTimes;
    }

    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/shoppingCartPage.fxml", currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID);

    }

    @FXML
    void profilePhotoImageOnMouseClicked(MouseEvent event) {
        loadScene("../fxml/profileInfoPage.fxml", currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID);
    }

    String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String currentTime = sdf.format(date);
        return currentTime;
    }

    @FXML
    void buyButtonOnMouseClicked(MouseEvent event) {
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        for (Order order : shoppingCart) 
        {
            order.setStatus("waiting");
            order.setDate(deliveryTimeChoiceBox.getValue());
            dbAdapter.changeOrderStatusAndDate(order);
        }
        
        for (Order order : shoppingCart) 
        {
            dbAdapter.updateProductStock(order.getProductID(), order.getAmount());
        }
        shoppingCart.clear();
        cartCount = 0;
        totalPrice = 0;
        productStockMap.clear();
        List<Product> products = dbAdapter.getAllProducts();

        for (Product product : products) {
            productStockMap.put(product.getId(), product.getStock());
        }
        loadScene("../fxml/customerHome.fxml", currentUser, shoppingCart, cartCount, totalPrice, productStockMap, orderID);
    }


    public String getRandomOrderID()
    {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }




    private void loadScene(String fxmlPath, Customer user, List <Order> order, int cartCount, double totalPrice, HashMap<Integer, Double> productStockMap, String orderID) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            stage = (Stage) profilePhotoImage.getScene().getWindow();
            scene = new Scene(root,profilePhotoImage.getScene().getWidth(),profilePhotoImage.getScene().getHeight());
            stage.setScene(scene);
            stage.show();
            
            if (fxmlPath.equals("../fxml/profileInfoPage.fxml")) {
                profileInfoPageController controller = loader.getController();
                controller.initData(user, order, cartCount, totalPrice, productStockMap, orderID); // Pass the User object to the controller
            }
            else if (fxmlPath.equals("../fxml/shoppingCartPage.fxml")) {
                shoppingCartPageController controller = loader.getController();
                controller.initData(user, order, cartCount, totalPrice, productStockMap, orderID); // Pass the User object to the controller
            }
            else if (fxmlPath.equals("../fxml/customerHome.fxml")) {
                customerHomeController controller = loader.getController();
                String newOrderID = getRandomOrderID();
                controller.initData(user, order, cartCount, totalPrice, productStockMap, newOrderID); // Pass the User object to the controller
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
