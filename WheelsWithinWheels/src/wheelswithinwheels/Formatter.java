package wheelswithinwheels;

import java.util.Date;

public class Formatter {
    public static Date date (int input) {
        int year = input % 10000;
        int day = (input / 10000) % 100;
        int month = (input / 1000000) % 100;
        return new Date(year - 1900, month - 1, day);
    }
    
    public static int date (Date input) {
        return 1000000 * (input.getMonth() + 1) + 10000 * input.getDate() + input.getYear() + 1900;
    }
}
