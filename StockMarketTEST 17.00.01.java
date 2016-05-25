/**
 * Created by christopherdahlen on 2016-05-13.
 */

import static org.junit.Assert.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import org.junit.Test;

/**
 * Created by christopherdahlen on 2016-05-10.
 */
public class StockMarketTEST {
    DBcon db = new DBcon();
    Connection c = db.main("");
    StockScraper s = new StockScraper();
    String usr = "patman";
    String psw = "0123";
    String ticker = "ibm";
    String trans_type = "Buy";
    Double price = Double.parseDouble(s.urlParser(ticker));
    //PortfolioOverview po = new PortfolioOverview();


    public StockMarketTEST() throws Exception {
    }

    @Test
    public void test_getHistorical() throws Exception {
        CsvGetter historical = new CsvGetter();
        ArrayList historical_ans = historical.getHistorical(ticker);
        assertTrue(historical_ans instanceof ArrayList);
    }

    @Test
    public void test_urloutput() throws Exception {
        assertTrue(price instanceof java.lang.Double); // checks if the the returnvalue of urlParser is of type Double (after string conversion)
    }

    @Test
    public void test_userLogin() throws Exception {
        assertTrue(db.userLogin(c, usr, psw)); //
    }

    @Test
    public void test_signUp() throws Exception{
        //db.signUp(c, usr , psw);
        assertFalse(db.signUp(c, usr , psw));
    }

    @Test
    public void test_updateStockuser() throws Exception {
        db.updateStockuser(c, usr, 5.0, trans_type); // sql function works but for som reason returns an empty query,
    }
    @Test
    public void test_insertTransaction() throws Exception {
        db.insertTransaction(c, usr, ticker, price, 10, trans_type);
    }
    @Test
    public void test_updatePortfolio() throws Exception {
        db.updatePortfolio(c, usr, ticker, price , 40, trans_type);
    }
    @Test
    public void test_getPortfolio()throws Exception{
        ArrayList<String> res = db.getPortfolio(c, usr);
        assertTrue( res instanceof ArrayList);
    }
    @Test
    public void test_completeTransaction()throws Exception {
    }

    @Test
    public void test_display() throws Exception {
        //po.display(c, usr);
    }

    }

