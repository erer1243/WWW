package wheelswithinwheels;

import java.util.Date;

public class Payment {
    final int amount;
    final Date date;
    final String name;
    
    public Payment(int amount, Date date, String name) {
        this.amount = amount;
        this.date = date;
        this.name = name;
    }
}
