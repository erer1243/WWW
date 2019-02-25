package wheelswithinwheels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    public static Date date (String input) throws ParseException {
        return format.parse(input);
    }
    
    public static String date (Date input) {
        return format.format(input);
    }
    
    protected static SimpleDateFormat format = new SimpleDateFormat("MMddYYYY");
}
