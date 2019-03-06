package wheelswithinwheels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Formatter {
    public static Date stringToDate(String input) throws UIParseException {
        try {
            return format.parse(input);
        } catch (ParseException e) {
            throw new UIParseException(input, "date", "date");
        }
    }
    
    public static String dateToString(Date input) {
        return format.format(input);
    }
    
    public static int parseInt(String input, String why) throws UIParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new UIParseException(input, why, "number");
        }
    }
    
    protected static SimpleDateFormat format = new SimpleDateFormat("MMddYYYY");
}
