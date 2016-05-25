
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.util.*;


import static java.text.Normalizer.Form.NFC;

public class StockScraper{

    public String urlParser (String ticker) {
        try {
            Document doc = Jsoup.connect("http://finance.yahoo.com/q?s="+ticker).get();
            org.jsoup.select.Elements links = doc.select("span[id=yfs_l84_" + ticker + "]");
            String s= "";
            double n = 0;
            for(Element e: links){
                s = String.valueOf(e.text()); // converts the suopObject-type to a regular string.
                //n = Double.parseDouble(e.text());
                //System.out.println(s);
            }
            return s;
        } catch (IOException ex){
            Logger.getLogger(StockScraper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "fail";
    }

}
