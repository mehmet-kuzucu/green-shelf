package app.greenshelf;
import app.greenshelf.Customer;
import java.sql.Connection;
import java.sql.Statement;

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
                                                                        "profilePicture BLOB, " +
                                                                        "userType VARCHAR(50), " +
                                                                        "UNIQUE(userid), " +
                                                                        "UNIQUE(username), " +
                                                                        "UNIQUE(email), " +
                                                                        "UNIQUE(phone)" + 
                                                                        ");";
            statement.executeUpdate(query);
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

    public String loginUserSql(String username) {
        try {
            // Ensure that the connection is not null
            if (connection == null) {
                throw new SQLException("Connection not established.");
            }

            // Select the database
            try (PreparedStatement useStatement = connection.prepareStatement("USE javafxdb;")) {
                useStatement.execute();
            }

            // Your main query to retrieve the password
            String query = "SELECT password FROM user WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if there is a result
                if (resultSet.next()) {
                    return resultSet.getString("password");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
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
