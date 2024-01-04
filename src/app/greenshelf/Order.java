package app.greenshelf;

import java.security.SecureRandom;

public class Order {
    private int id;
    private String orderID;
    private int productID;
    private double amount;
    private String date;
    private String status;
    private double price;
    

    public Order(int id, String orderID, int productID, double amount, String date, String status, double price) {
        //setOrderID();
        this.id = id;
        this.orderID = orderID;
        this.productID = productID;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.price = price;
    }


    /*
    public void setOrderID() {
        //bu 16 karakterlik string olusturuyo onu da UserID'ye atiyo

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
     */
    public int getId() {
        return id;
    }

    public String getOrderID() {
        return orderID;
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


    public void setId(int id) {
        this.id = id;
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
