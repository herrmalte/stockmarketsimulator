s
/**
 * Created by christopherdahlen on 2016-05-12.
 */

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;


public class StockChart {

    private static CsvGetter stock;

    public static void display(String ticker) throws Exception {
        Stage stage = new Stage();

        stage.setTitle("Line Chart Sample");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("TIME");
        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock development last 30 days");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Stock");


        stock = new CsvGetter();
        ArrayList<Double> prices = stock.getHistorical(ticker); // connects to database
        int i = 30;
        for(Double elem: prices){
            series1.getData().add(new XYChart.Data(String.valueOf(i) + " Days ago", elem));
            i--;
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series1);

        stage.setScene(scene);
        stage.show();
    }



}
