package app.greenshelf.controllers;

import java.io.IOException;
import java.util.HashMap;

import app.greenshelf.Admin;
import app.greenshelf.DatabaseAdapter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class adminStatisticsPageController {

    @FXML
    private ImageView greenShelfLogo;

    @FXML
    private Text totalRevenueText;

    @FXML
    private Text completedOrderCountText;

    @FXML
    private Text waitingOrderCountText;

    @FXML
    private Text cancelledOrderCountText;

    @FXML
    private PieChart pieChart;

    @FXML
    private LineChart<Integer, String> lineChart;

    private Stage stage;
    private Scene scene;
    private Admin currentUser;


    public void initData(Admin user) {
        currentUser = user;
        DatabaseAdapter db = new DatabaseAdapter();
        double totalRevenue = db.getAllOrdersRevenue();
        totalRevenueText.setText("Total Revenue: " + totalRevenue + " ₺");
        int orderCount = db.getAllOrdersDifferentCount("completed");
        completedOrderCountText.setText("Total Completed Orders: " + orderCount);
        orderCount = db.getAllOrdersDifferentCount("waiting");
        waitingOrderCountText.setText("Total Waiting Orders: " + orderCount);
        orderCount = db.getAllOrdersDifferentCount("cancelled");
        cancelledOrderCountText.setText("Total Cancelled Orders: " + orderCount);
        //piechart title
        pieChart.setTitle("Order Status");
        pieChart.getData().add(new Data("Completed Orders", db.getAllOrdersDifferentCount("completed")));
        pieChart.getData().add(new Data("Waiting Orders", db.getAllOrdersDifferentCount("waiting")));
        pieChart.getData().add(new Data("Cancelled Orders", db.getAllOrdersDifferentCount("cancelled")));
        pieChart.setLabelsVisible(false);
        
        
        HashMap<String, Integer> orderHmap = db.getOrdersRevenueByDate();
        for (String key : orderHmap.keySet()) {
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data(key, orderHmap.get(key)));
            lineChart.getData().add(series);
            lineChart.setLegendVisible(false);
        }
    }
    @FXML
    void greenShelfLogoOnMouseClicked(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/adminHomePage.fxml"));
        Parent root = loader.load();
        stage = (Stage) greenShelfLogo.getScene().getWindow();
        adminHomePageController controller = loader.getController();
        controller.initData(currentUser,root,controller);
        scene = new Scene(root, greenShelfLogo.getScene().getWidth(), greenShelfLogo.getScene().getHeight());
        stage.setScene(scene);
        stage.show();
    }

}
