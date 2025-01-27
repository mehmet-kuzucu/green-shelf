package app.greenshelf;

public class Order {
    private int id;
    private String orderID;
    private int productID;
    private double amount;
    private String date;
    private String status;
    private double price;
    private String deliveryDate;
    private String carrierUsername;

    public Order(int id, String orderID, int productID, double amount, String date, String status, double price, String deliveryDate, String carrierUsername) {
        this.id = id;
        this.orderID = orderID;
        this.productID = productID;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.price = price;
        this.deliveryDate = deliveryDate;
        this.carrierUsername = carrierUsername;
    }

    public Order(int id, String orderID, int productID, double amount, String date, String status, double price) {
        this.id = id;
        this.orderID = orderID;
        this.productID = productID;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.price = price;
    }

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

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getCarrierUsername() {
        return carrierUsername;
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

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setCarrierUsername(String carrierUsername) {
        this.carrierUsername = carrierUsername;
    }
}
