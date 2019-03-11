package wheelswithinwheels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Formatter {
    public static LocalDate stringToDate(String input) throws UIParseException {
        try {
            return LocalDate.parse(input, dateFormat);
        } catch (DateTimeException e) {
            throw new UIParseException(input, "date", "date");
        }
    }
    
    public static String dateToString(LocalDate input) {
        return dateFormat.format(input);
    }
    
    public static int parseInt(String input, String why) throws UIParseException {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new UIParseException(input, why, "number");
        }
    }
    
    protected static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMddyyyy");
}
