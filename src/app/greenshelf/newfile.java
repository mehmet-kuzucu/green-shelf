package app.greenshelf;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class newfile extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Connect to MySQL database (replace "your_username" and "your_password")
            String url = "jdbc:mysql://localhost:3306/javafxdb";
            String user = "aliyigittas";
            String password = "Ali123ali123";
            Connection connection = DriverManager.getConnection(url, user, password);

            // Read data from the "user" table
            String selectQuery = "SELECT * FROM user";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                // Display the data in a JavaFX window
                StringBuilder data = new StringBuilder();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String age = resultSet.getString("password");
                    data.append("ID: ").append(id).append(", Name: ").append(name).append(", Age: ").append(age).append("\n");
                }

                Label label = new Label(data.toString());
                Scene scene = new Scene(label, 400, 300);

                primaryStage.setTitle("JavaFX App with MySQL");
                primaryStage.setScene(scene);
                primaryStage.show();
            }

            // Close the database connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
