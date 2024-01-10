package app.greenshelf;
public class Product {
    private int id;
    private String name;
    private double stock;
    private double price;
    private double threshold;
    private String type;
    private String image;
    private boolean isPiece;
    
    public Product(String name, double stock, String image, double price, double threshold, String type, int id, boolean isPiece) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.threshold = threshold;
        this.type = type;
        this.image = image;
        this.id = id;
        this.isPiece = isPiece;
    }

    public Product(String name, double stock, double price, double threshold, String type, int id, boolean isPiece) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.threshold = threshold;
        this.type = type;
        this.id = id;
        this.isPiece = isPiece;
    }

    public boolean getIsPiece() {
        return isPiece;
    }


    public String getName() {
        return name;
    }

    public double getStock() {
        return stock;
    }

    public double getPrice() {
        return price;
    }

    public double getThreshold() {
        return threshold;
    }

    public String getType() {
        return type;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }  

    public void setStock(double stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

     public void setIsPiece(boolean isPiece) {
        this.isPiece = isPiece;
    }


}
