import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class App extends Application {

    private Scene scene1, scene2, scene3, scene4;
    private User currentUser;
    private DatabaseAdapter databaseAdapter;

    @Override
    public void start(Stage primaryStage) {
        try {
            databaseAdapter = new DatabaseAdapter(); // database kurulumu

            // Load FXML files
            Parent root1 = FXMLLoader.load(getClass().getResource("fxml/Login2.fxml"));
            Parent root2 = FXMLLoader.load(getClass().getResource("fxml/homePage.fxml"));
            Parent root3 = FXMLLoader.load(getClass().getResource("fxml/register_deneme.fxml"));
            Parent root4 = FXMLLoader.load(getClass().getResource("fxml/register2_deneme.fxml"));

            // Create scenes
            scene1 = new Scene(root1);
            scene2 = new Scene(root2);
            scene3 = new Scene(root3);
            scene4 = new Scene(root4);

            // Get the components from Scene 1
            TextField usernameField = (TextField) root1.lookup("#usernameField");
            PasswordField passwordField = (PasswordField) root1.lookup("#passwordField");
            Button submitButton = (Button) root1.lookup("#loginButton");
            Hyperlink registerLink = (Hyperlink) root1.lookup("#register");

            // Get the label from Scene 2
            Label userInfoLabel = (Label) root2.lookup("#userInfoLabel");
            Button logoutButton = (Button) root2.lookup("#logoutButton");

            // Get the components from Scene 3
            Button nextStepButton = (Button) root3.lookup("#nextStepButton");

            // when hyperlink (or Create new profile) is clicked
            registerLink.setOnAction(e -> {
                primaryStage.setScene(scene3);
            });

            // when next step button is clicked
            nextStepButton.setOnAction(e -> {
                primaryStage.setScene(scene4);
            });
            
            

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

