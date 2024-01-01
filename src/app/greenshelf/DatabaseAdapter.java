package app.greenshelf;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import com.mysql.cj.jdbc.Blob;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import app.greenshelf.Product;

public class DatabaseAdapter {
    private String url;
    private Connection connection;
    
    public void generateSqlDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Create the database
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS javafxdb;");
            statement.executeUpdate("USE javafxdb;");
            // Create a table in the database
            String query = "CREATE TABLE IF NOT EXISTS user (id INT PRIMARY KEY AUTO_INCREMENT, " +
                                                                        "userid VARCHAR(16), " +
                                                                        "username VARCHAR(50), " +
                                                                        "email VARCHAR(50), " +
                                                                        "phone VARCHAR(50), " +
                                                                        "address VARCHAR(50), " +
                                                                        "name VARCHAR(50)," + 
                                                                        "surname VARCHAR(50), " +
                                                                        "password VARCHAR(50), " +
                                                                        "profilePicture LONGBLOB, " +
                                                                        "userType VARCHAR(50), " +
                                                                        "UNIQUE(userid), " +
                                                                        "UNIQUE(username), " +
                                                                        "UNIQUE(email), " +
                                                                        "UNIQUE(phone)" + 
                                                                        ");";
            statement.executeUpdate(query);

            String queryProducts = "CREATE TABLE IF NOT EXISTS products (id INT PRIMARY KEY AUTO_INCREMENT, " +
                                                                        "name VARCHAR(50), " +
                                                                        "stock DOUBLE, " +
                                                                        "image LONGBLOB, " +
                                                                        "price DOUBLE, " +
                                                                        "threshold DOUBLE, " +
                                                                        "type VARCHAR(50) " +
                                                                        ");";
            statement.executeUpdate(queryProducts);

            String queryOrders = "CREATE TABLE IF NOT EXISTS orders (id INT PRIMARY KEY AUTO_INCREMENT, " +
                                                                        "orderid VARCHAR(16)," +
                                                                        "userid VARCHAR(16), " +
                                                                        "productid INT, " +
                                                                        "amount DOUBLE, " +
                                                                        "date VARCHAR(50), " +
                                                                        "price DOUBLE, " +
                                                                        "status VARCHAR(50) " +
                                                                        ");";
            statement.executeUpdate(queryOrders);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void registerUserSql(Customer customer) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("USE javafxdb;");
            String query = "INSERT INTO user (userid, username, email, phone, address, name, surname, password, profilePicture, userType) VALUES ('" +
                            customer.getUserID() + "', '" +
                            customer.getUsername() + "', '" +
                            customer.getEmail() + "', '" +
                            customer.getPhone() + "', '" +
                            customer.getAddress() + "', '" +
                            customer.getName() + "', '" +
                            customer.getSurname() + "', '" +
                            customer.getPassword() + "', '" +
                            customer.getProfilePicture() + "', '" +
                            customer.getUserType() + "');";
            statement.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> loginUserSql(String username) {
        try {
            // Ensure that the connection is not null
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            // Select the database
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            // Check if the username exists
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                // Check if there is a result
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        System.out.println("Username does not exist!");
                        return null;
                    }
                }
            }
    
            // Your main query to retrieve the password and userType
            String query = "SELECT * FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("ADMIN!");
                // Check if there is a result
                if (resultSet.next()) {
                    List<String> list = List.of(resultSet.getString("userid"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("phone"), resultSet.getString("address"), resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("password"), resultSet.getString("profilePicture"), resultSet.getString("userType"));
                    
                    return list;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }




    /* REGISTER CONTROL PAGE FUNCTIONS */

    public Boolean checkUsernameSql(String username) {
        
        try {
            // Ensure that the connection is not null
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            // Select the database
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            // Check if the username exists
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                // Check if there is a result
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*username doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Boolean checkEmailSql(String email) {
        
        try {
            // Ensure that the connection is not null
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            // Select the database
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            // Check if the username exists
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                // Check if there is a result
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*email doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public Boolean checkPhoneSql(String phone) {
        
        try {
            // Ensure that the connection is not null
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            // Select the database
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            // Check if the username exists
            String queryUsernameExists = "SELECT COUNT(*) FROM user WHERE phone = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, phone);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                // Check if there is a result
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*phone doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public boolean checkProductName(String productName){
        try {
            // Ensure that the connection is not null
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }
    
            // Select the database
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }
    
            // Check if the username exists
            String queryUsernameExists = "SELECT COUNT(*) FROM products WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(queryUsernameExists)) {
                preparedStatement.setString(1, productName);
                ResultSet resultSet2 = preparedStatement.executeQuery();
    
                // Check if there is a result
                if (resultSet2.next()) {
                    if (resultSet2.getInt(1) == 0) {
                        /*phone doesn't exists */
                        return false;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }





    Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public DatabaseAdapter() {
        url = "jdbc:mysql://localhost:3306";                // your database connection

        zorunlu user = new zorunlu();

        
        try {
            connection = DriverManager.getConnection(url, user.name, user.pass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    

    public void updateInfo(String password, String email, String address, String phone, String profilePhoto, String username)
    {
        String url = "jdbc:mysql://localhost:3306/javafxdb";
        zorunlu user = new zorunlu();
        
        try (Connection connection = DriverManager.getConnection(url, user.name, user.pass)) 
        {
            String updateQuery = "UPDATE user SET password = ?, email = ?, address = ?, phone = ?, profilePicture = ? WHERE username = ?";
            //String updateQuery = "UPDATE user SET password = ?, email = ?, address = ?, phone = ? WHERE username = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) 
        {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phone);
            preparedStatement.setBytes(5, profilePhoto.getBytes()); 
            preparedStatement.setString(6, username);
            preparedStatement.executeUpdate();
        }
            System.out.println("Data updated successfully!");
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }

    }

    public void addProductToDb(Product product){
        /* add product to database */
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "INSERT INTO products (name, stock, image, price, threshold, type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getStock());
            preparedStatement.setBytes(3, product.getImage().getBytes());
            preparedStatement.setDouble(4, product.getPrice());
            preparedStatement.setDouble(5, product.getThreshold());
            preparedStatement.setString(6, product.getType());
            preparedStatement.executeUpdate();
            System.out.println("Product added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Product> getAllProducts(){
        List<Product> products = new ArrayList<>();
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "SELECT * FROM products";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Product product = new Product(resultSet.getString("name"), resultSet.getDouble("stock"), resultSet.getString("image"), resultSet.getDouble("price"), resultSet.getDouble("threshold"), resultSet.getString("type"), resultSet.getInt("id"));
                products.add(product);
            }
        } catch (SQLException e) {
        }
        return products;
    }

    public void addOrdersql(Order order){
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("USE javafxdb;");
            String query = "INSERT INTO orders (orderid, userid, productid, amount, date, price, status) VALUES ('" +
                            order.getOrderID() + "', '" +
                            order.getUserID() + "', '" +
                            order.getProductID() + "', '" +
                            order.getAmount() + "', '" +
                            order.getDate() + "', '" +
                            order.getPrice() + "', '" +
                            order.getStatus() + "');";
            statement.executeUpdate(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateProduct(Product product){
        /* update product in database */
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "UPDATE products SET name = ?, stock = ?, price = ?, threshold = ?, type = ? WHERE id = ?";
            //print the id
            System.out.println(product.getId());
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                System.out.println("hocam önemli");
                preparedStatement.setString(1, product.getName());
                preparedStatement.setDouble(2, product.getStock());
                preparedStatement.setDouble(3, product.getPrice());
                preparedStatement.setDouble(4, product.getThreshold());
                preparedStatement.setString(5, product.getType());
                preparedStatement.setInt(6, product.getId());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeProduct(int id)
    {
        try{
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            zorunlu user = new zorunlu();
            Connection connection = DriverManager.getConnection(url, user.name, user.pass);
            String query = "DELETE FROM products WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                System.out.println("Siliniyor...");
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Product deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
