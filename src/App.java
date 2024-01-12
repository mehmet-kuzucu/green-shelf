import java.util.Locale;

import app.greenshelf.DatabaseAdapter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {

    private DatabaseAdapter databaseAdapter;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            databaseAdapter = new DatabaseAdapter(); // database kurulumu
            Locale.setDefault(Locale.ENGLISH); // dil ayarı (ingilizce
            // Load FXML files
            FXMLLoader scene = new FXMLLoader(getClass().getResource("app/greenshelf/fxml/loginPage.fxml"));
            /*
            // submit butonuna basıldığında
            submitButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                FileChooser fileChooser = new FileChooser();
                

                //Burda userdan direkt class olusturmayacak aslinda once username ve passwordu databasete kontrol edecek sonra typeina gore admin, customer veya carier clasi olusturacak
                currentUser = new User(username, password);
                userInfoLabel.setText("HOSGELDIN " + currentUser.getUsername() + "!\nGiris bilgilerin:\nname: " + currentUser.getUsername() + ", password: " + currentUser.getPassword());
                databaseAdapter.registerUserSql(currentUser.getUsername(), currentUser.getPassword());
                primaryStage.setScene(scene2);

            });
            */

            databaseAdapter.generateSqlDatabase(); // db yoksa olusturuyor, varsa hicbir sey yapmıyor
            databaseAdapter.closeConnection();
            // Set the primary stage
            primaryStage.setScene(new Scene(scene.load()));
            primaryStage.setTitle("Group12 GreenShelf");
            primaryStage.show();
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

