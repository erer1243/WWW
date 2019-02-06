/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelswithinwheels;
import java.util.Date;

/**
 *
 * @author Gideon_mac
 */
public class Formatter {
    public static Date date (String input) {
        int numDate;
        try {
            numDate = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return null;
        }
        int year = numDate % 10000;
        int day = (numDate / 10000) % 100;
        int month = (numDate / 1000000) % 100;
        return new Date(year - 1900, month - 1, day);
    }
    
    public static int date (Date input) {
        return 1000000 * (input.getMonth() + 1) + 10000 * input.getDate() + input.getYear() + 1900;
    }
}