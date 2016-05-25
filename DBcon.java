
/**
 * Created by christopherdahlen on 2016-05-05.
 */
import org.jsoup.*;

import java.security.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import javafx.application.Application;
import javafx.stage.Stage;
import org.postgresql.util.PGTimestamp;


public class DBcon {
    public ResultSet rs = null;
    //public Connection c = null;
    StockScraper s = new StockScraper();

    public static Connection main(String args) throws SQLException {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql:// address....", "enter username", "enter password");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        return c;

    }

    public double getCash(Connection c, String usr) throws  SQLException{
        PreparedStatement stmt = c.prepareStatement("SELECT DISTINCT cash FROM stockuser WHERE user_nick=?");
        stmt.setString(1,usr);
        ResultSet rs = stmt.executeQuery();
        double cash=0;
        while (rs.next()) {
            cash = rs.getDouble("cash");
        }
        return cash;
    }

    public ArrayList getPortfolio(Connection c, String usr) throws SQLException{
        PreparedStatement ticker_list = c.prepareStatement("SELECT DISTINCT stock, units FROM portfolio WHERE user_nick=?");
        ticker_list.setString(1, usr);
        ResultSet rs = ticker_list.executeQuery();

        ArrayList<String> array = new ArrayList<>(); // initialize an array that can hold both Strings and doubles
        int i = 0;
        while (rs.next()) {
            String stock = rs.getString("stock");
            int units= rs.getInt("units");
            //System.out.println(units);
            //System.out.println(stock);
            Double price =  Double.parseDouble(s.urlParser(stock)); // get the current price of the stock
            array.add(stock); // add stock (even index)
            array.add(String.valueOf(price*units)); //and price (odd index)
            i++;
        }
        return array;
    }


    public void completeTransaction(Connection c,String usr, String trans_type,String ticker, Integer units) throws SQLException {

        StockScraper scraper = new StockScraper();
        String stock_price = scraper.urlParser(ticker);
        Double price = Double.parseDouble(stock_price);
        Double amount = units * price;
        // Update stockuser
        updateStockuser(c, usr, amount , trans_type);
        //insert transaction into db
        insertTransaction(c, usr, ticker, price, units, trans_type);
        //insert into portfolio db
        updatePortfolio(c, usr, ticker, price, units, trans_type);
    }

    public void updateStockuser(Connection c, String usr, Double amount, String trans_type) throws SQLException {

        // query fungerar men svårt att testa med Junittest
        if(trans_type.equals("Buy")){
            PreparedStatement stmt = c.prepareStatement("UPDATE stockuser SET cash=cash - ? WHERE user_nick=?");
            stmt.setDouble(1, amount);
            stmt.setString(2, usr);
            stmt.executeUpdate();
        } else if(trans_type.equals("Sell")){
            PreparedStatement stmt = c.prepareStatement("UPDATE stockuser SET cash=cash + ? WHERE user_nick=?");
            stmt.setDouble(1, amount);
            stmt.setString(2, usr);
            stmt.executeUpdate();
            }


    }
    public void insertTransaction(Connection c, String usr, String ticker, Double price, Integer units, String trans_type) throws SQLException {
        // function is working but testcase is failing because the execution of the question is returning empty
        java.util.Date date= new java.util.Date();
        java.sql.Timestamp time = new java.sql.Timestamp(date.getTime());
        PreparedStatement ins_trans = c.prepareStatement("INSERT INTO transactions VALUES(?, ?, ?, ?, ?, ?)");
        ins_trans.setString(1,usr);
        ins_trans.setString(2,trans_type);
        ins_trans.setString(3,ticker);
        ins_trans.setDouble(4,price);
        ins_trans.setInt(5,units);
        ins_trans.setTimestamp(6,time);
        ins_trans.executeUpdate();

    }
    public void updatePortfolio(Connection c, String usr, String ticker, Double price, Integer units, String trans_type) throws SQLException {
        // if user wants to buy a stock
        if (trans_type.equals("Buy")) {
            PreparedStatement checkticker = c.prepareStatement("SELECT DISTINCT stock FROM portfolio WHERE user_nick=?");
            checkticker.setString(1, usr);
            ResultSet rs = checkticker.executeQuery();

            ArrayList<String> array = new ArrayList<>();
            while (rs.next()) {
                String stock = rs.getString("stock");
                //System.out.println(stock);
                array.add(stock); // add stock to later check if user already owns that stock
            }

            int temp = array.contains(ticker) ? 1 : 0; // returns 0 if user does not own that stock
            if(temp==0) {                              // if user doesnt own the stock, --> insert to table
                PreparedStatement ins_portfolio = c.prepareStatement("INSERT INTO portfolio VALUES(?, ?, ?, ?)");
                ins_portfolio.setString(1, usr);        // set the variables
                ins_portfolio.setString(2, ticker);
                ins_portfolio.setDouble(3, price);
                ins_portfolio.setInt(4, units);
                ins_portfolio.executeUpdate();           // execute the query
            }else{                                  // if user already owns stock, --> update table
                PreparedStatement ins_portfolio = c.prepareStatement("UPDATE portfolio SET units=units+? WHERE user_nick=? AND stock=? ");
                ins_portfolio.setInt(1, units);
                ins_portfolio.setString(2, usr);
                ins_portfolio.setString(3, ticker);
                ins_portfolio.executeUpdate();
            }
        } else if(trans_type.equals("Sell")){ // if user wants to sell a stock
            PreparedStatement ins_portfolio = c.prepareStatement("UPDATE portfolio SET units=units-? WHERE user_nick=? AND stock=? ");
            ins_portfolio.setInt(1, units);
            ins_portfolio.setString(2, usr);
            ins_portfolio.setString(3, ticker);
            ins_portfolio.executeUpdate();
        }
    }
    public boolean userLogin(Connection c, String nick, String psw)  {
        try {
            String usr="";
            String p="";
            PreparedStatement stmt = c.prepareStatement("SELECT user_nick, psw FROM stockuser WHERE user_nick=? AND psw = ?" );
            stmt.setString(1, nick);
            stmt.setString(2, psw);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                usr = rs.getString("user_nick");
                p = rs.getString("psw");
            }
            if(nick.equals(usr) && psw.equals(p)) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean signUp(Connection c, String usr, String psw) throws SQLException {
         // return true if username doesnt exist. or throw exception

        PreparedStatement ins_user = c.prepareStatement("INSERT INTO stockuser VALUES(?, ?, ?)"); // add new user
        ins_user.setString(1, usr);
        ins_user.setDouble(2, 1000000.0);
        ins_user.setString(3, psw);
        ins_user.executeUpdate();
        return true;


    }
}
