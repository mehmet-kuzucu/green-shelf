package app.greenshelf;

public class Order {
    private int orderID;
    private String userID;
    private int productID;
    private double quantity;
    private String date;
    private String status;
    private double price;
    
    public Order(int orderID, String userID, int productID, double quantity, String date, String status, double price) {
        this.orderID = orderID;
        this.userID = userID;
        this.productID = productID;
        this.quantity = quantity;
        this.date = date;
        this.status = status;
        this.price = price;
    }

    public int getOrderID() {
        return orderID;
    }

    public String getUserID() {
        return userID;
    }

    public int getProductID() {
        return productID;
    }

    public double getQuantity() {
        return quantity;
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

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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
