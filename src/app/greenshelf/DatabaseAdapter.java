package app.greenshelf;
import app.greenshelf.Customer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DatabaseAdapter {
    private String url;
    private String user;
    private String pass;
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
    
    
}
