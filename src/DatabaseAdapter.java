import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;


public class DatabaseAdapter {
    private String url;
    private String user;
    private String pass;
    private Connection connection;

    void generateSqlDatabase() {
        try (Statement statement = connection.createStatement()) {
            // Create the database
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS javafxdb;");
            statement.executeUpdate("USE javafxdb;");
            // Create a table in the database
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(50), password VARCHAR(50));");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void registerUserSql(String username, String password) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO user (name, password) VALUES ('" + username + "', '" + password + "');");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    DatabaseAdapter() {
        url = "jdbc:mysql://localhost:3306";                // your database connection
        // TODO: Bu kısmı kendi bilgisayarınıza göre değiştirin
        // TODO: BUnu sonra duzeltmek gerekecek ama otomatik almasi icin
        user = "mehmet";                               // your username
        pass = "Mk12345678";                              // your password
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    
}
