import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;




public class App extends Application {

    private User currentUser;
    private DatabaseAdapter databaseAdapter;
    

    @Override
    public void start(Stage primaryStage) {
        try {
            databaseAdapter = new DatabaseAdapter(); // database kurulumu

            // Load FXML files
            FXMLLoader scene = new FXMLLoader(getClass().getResource("app/greenshelf/fxml/loginPage.fxml"));
            /*
            Parent root2 = FXMLLoader.load(getClass().getResource("fxml/homePage.fxml"));
            Parent root3 = FXMLLoader.load(getClass().getResource("fxml/register_deneme.fxml"));
            Parent root4 = FXMLLoader.load(getClass().getResource("fxml/register2_deneme.fxml"));
            Parent root5 = FXMLLoader.load(getClass().getResource("fxml/customerHome.fxml"));
            */

            // Create scenes
            /* 
            scene2 = new Scene(root2);
            scene3 = new Scene(root3);
            scene4 = new Scene(root4);
            scene5 = new Scene(root5);
*/

            /*
            // Get the label from Scene 2
            Label userInfoLabel = (Label) root2.lookup("#userInfoLabel");
            Button logoutButton = (Button) root2.lookup("#logoutButton");

            // Get the components from Scene 3
            Button nextStepButton = (Button) root3.lookup("#nextStepButton");
            Hyperlink existedAccount = (Hyperlink) root3.lookup("#existedaccount");

            // Get the components from Scene 4
            Hyperlink existedAccount2 = (Hyperlink) root4.lookup("#existedaccount");

            // Get the components from Scene 5
            Button registerButton = (Button) root4.lookup("#registerButton");

            */

            // when hyperlink (or Create new profile) is clicked
            /* 
            registerLink.setOnAction(e -> {
                primaryStage.setScene(scene3);
            });
*/        /*
            // when next step button is clicked
            nextStepButton.setOnAction(e -> {
                primaryStage.setScene(scene4);
            });
*/

            /* 
            // when existedaccount hyperlink is clicked
            existedAccount.setOnAction(e -> {
                primaryStage.setScene(scene1);
            });
*/          /* 
            existedAccount2.setOnAction(e -> {
                primaryStage.setScene(scene1);
            });
*/          /*
            // when register button is clicked
            registerButton.setOnAction(e -> {
                primaryStage.setScene(scene5);
            });
*/
            // spinner
            

            
            
            /*
            // submit butonuna basıldığında
            submitButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                FileChooser fileChooser = new FileChooser();
                File selectedFile = fileChooser.showOpenDialog(primaryStage);

                
                //Burda userdan direkt class olusturmayacak aslinda once username ve passwordu databasete kontrol edecek sonra typeina gore admin, customer veya carier clasi olusturacak
                currentUser = new User(username, password);
                userInfoLabel.setText("HOSGELDIN " + currentUser.getUsername() + "!\nGiris bilgilerin:\nname: " + currentUser.getUsername() + ", password: " + currentUser.getPassword());
                databaseAdapter.registerUserSql(currentUser.getUsername(), currentUser.getPassword());
                primaryStage.setScene(scene2);

            });
            */

            databaseAdapter.generateSqlDatabase(); // db yoksa olusturuyor, varsa hicbir sey yapmıyor

            // back butonuna basıldığında
            /* 
            logoutButton.setOnAction(e -> {
                //databaseAdapter.closeConnection();
                primaryStage.setScene(scene1);
            });
*/
            // Set the primary stage
            primaryStage.setScene(new Scene(scene.load()));
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

