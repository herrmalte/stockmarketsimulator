/**
 * Created by patricridell on 2016-05-17.
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.*;
import javafx.scene.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.application.Application.launch;

//public class PortfolioOverview extends Application {
public class PortfolioOverview{
    static DBcon db = new DBcon();
    Connection c = db.main(""); // initialize connection to pass to getPortfolio

    public PortfolioOverview() throws SQLException {
    }

    //@Override  //// ERASE @Override WHEN CONNECTED TO GUI AND CHANGE name to display
    public static void display(Connection c, String usr)throws SQLException {
    //public void start(Stage primary) throws SQLException {

        //Stage window = primary;
        Stage window = new Stage();
        GridPane portfoliogrid = new GridPane();
        portfoliogrid.setPadding(new Insets(15,15,15,15));
        portfoliogrid.setVgap(10);
        portfoliogrid.setHgap(10);
        portfoliogrid.add( new Label( "Ticker " ), 1 , 1 );
        portfoliogrid.add( new Label( "Value" ), 2 , 1 );


        // array to hold tickers and value of each stock in portfolio (units*price)
        ArrayList<String> arr = db.getPortfolio(c, usr); // CHANGE TO usr when connected to GUI
        double stockvalue = 0;
        int p_index = 1;
        for(int i=0; i < arr.size();i=i+2){
            String ticker = arr.get(i);
            String value = arr.get(p_index);

            portfoliogrid.add( new Label( ticker ), 1 , i+2 );
            portfoliogrid.add( new Label( value ), 2 , i+2 );
            System.out.println(arr.get(i)); // gets the names of the stocks
            System.out.println(arr.get(p_index)); // gets the prices of the stocks
            stockvalue = stockvalue + Double.parseDouble(arr.get(p_index));
            p_index=p_index +2;
        }
        double cash = db.getCash(c,usr);
        portfoliogrid.add( new Label( " Portfolio total: " ), 5 , 1 );
        String v = String.valueOf(stockvalue + cash);
        portfoliogrid.add( new Label( v ), 6 , 1); // adds total

        portfoliogrid.add( new Label( " Cash total: " ), 5 , 2 );
        String amount = String.valueOf(cash);
        portfoliogrid.add( new Label( amount ), 6 , 2); // adds total

        portfoliogrid.add( new Label( " Stocks total: " ), 5 , 3 );
        String sv= String.valueOf(stockvalue );
        portfoliogrid.add( new Label( sv ), 6 , 3); // adds total

        portfoliogrid.add( new Label( " Portfolio development: " ), 5 , 4 );
        double tot = ((stockvalue+cash)-1000000) / 1000000 * 100;
        String pr= String.valueOf(tot);
        portfoliogrid.add( new Label( pr +" %" ), 6 , 4); // adds total



        Scene scene = new Scene(portfoliogrid, 400, 500);
        window.setScene(scene);
        window.show();

    }
}
