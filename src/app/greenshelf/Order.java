package app.greenshelf;

import java.security.SecureRandom;

public class Order {
    private String orderID;
    private String userID;
    private int productID;
    private double amount;
    private String date;
    private String status;
    private double price;
    
    public Order(String userID, int productID, double amount, String date, String status, double price) {
        setOrderID();
        this.userID = userID;
        this.productID = productID;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.price = price;
    }


    public void setOrderID() {
        /* bu 16 karakterlik string olusturuyo onu da UserID'ye atiyo */

        if (this.orderID == null){
            
            String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            SecureRandom random = new SecureRandom();
            StringBuilder sb = new StringBuilder(16);

            for (int i = 0; i < 16; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(randomIndex);
                sb.append(randomChar);
            }

            this.orderID =  sb.toString();
        } else {
            System.out.println("Order ID already exists");
        } 
    }

    public String getOrderID() {
        return orderID;
    }

    public String getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }



    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
