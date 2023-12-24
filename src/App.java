import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class App extends Application {

    private Scene scene1, scene2;
    private User currentUser;
    private DatabaseAdapter databaseAdapter;

    @Override
    public void start(Stage primaryStage) {
        try {
            databaseAdapter = new DatabaseAdapter(); // database kurulumu

            // Load FXML files
            Parent root1 = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
            Parent root2 = FXMLLoader.load(getClass().getResource("fxml/homePage.fxml"));

            // Create scenes
            scene1 = new Scene(root1);
            scene2 = new Scene(root2);

            // Get the components from Scene 1
            TextField usernameField = (TextField) root1.lookup("#usernameField");
            PasswordField passwordField = (PasswordField) root1.lookup("#passwordField");
            Button submitButton = (Button) root1.lookup("#submitButton");

            // Get the label from Scene 2
            Label userInfoLabel = (Label) root2.lookup("#userInfoLabel");
            Button logoutButton = (Button) root2.lookup("#logoutButton");

            // submit butonuna basıldığında
            submitButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                
                currentUser = new User(username, password);
                userInfoLabel.setText("HOSGELDIN " + currentUser.getUsername() + "!\nGiris bilgilerin:\nname: " + currentUser.getUsername() + ", password: " + currentUser.getPassword());
                databaseAdapter.registerUserSql(currentUser.getUsername(), currentUser.getPassword());
                primaryStage.setScene(scene2);

            });

            databaseAdapter.generateSqlDatabase(); // db yoksa olusturuyor, varsa hicbir sey yapmıyor

            // back butonuna basıldığında
            logoutButton.setOnAction(e -> {
                //databaseAdapter.closeConnection();
                primaryStage.setScene(scene1);
            });

            primaryStage.setScene(scene1);
            primaryStage.setTitle("User Information Example");
            primaryStage.show();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

