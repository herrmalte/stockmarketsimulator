/**
 * Created by christopherdahlen on 2016-05-17.
 */

import java.util.Date;
import java.util.*;
import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;

public class CsvGetter {
    public ArrayList getHistorical(String ticker) throws Exception{
        ArrayList<Double> array = new ArrayList<>();
        Date date = new Date();
        //GregorianCalendar start = new GregorianCalendar(date.now())
        String url= "http://ichart.finance.yahoo.com/table.csv?s=" + ticker + "&c=2016-01-01"; // gets a csv file from uahoo finance
        try{
            URL info = new URL(url);
            URLConnection data = info.openConnection();
            Scanner dataStream = new Scanner(data.getInputStream());
            dataStream.nextLine(); // throw away first text line
            int i=0;
            while(i<30 && dataStream.hasNext()) { // get stock prices for last 30days. todays date is firts element in list
                String line = dataStream.nextLine();
                String[] splitted = line.split(",");
                array.add(Double.parseDouble(splitted[1]));
                //System.out.println(array.get(i));
                i++;
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        Collections.reverse(array); // reverses the list.
        return array;
    }
}
