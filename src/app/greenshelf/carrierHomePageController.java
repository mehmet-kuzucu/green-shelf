package app.greenshelf;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class carrierHomePageController {


    @FXML
    private Button gosterilecek;

    @FXML
    private Button silinecek;

    @FXML
    void silinecekOnMouseClicked(MouseEvent event) {
        // when button clicked, make the button unvisible
        silinecek.setVisible(false);
        System.out.println("silinecek button clicked!");

    }

}
