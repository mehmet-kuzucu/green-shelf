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
            databaseAdapter = new DatabaseAdapter();
            Locale.setDefault(Locale.ENGLISH);
            FXMLLoader scene = new FXMLLoader(getClass().getResource("app/greenshelf/fxml/loginPage.fxml"));
            databaseAdapter.generateSqlDatabase();
            databaseAdapter.closeConnection();
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

