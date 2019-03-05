package wheelswithinwheels;

import java.util.Date;

public class Payment implements Comparable<Payment>{
    final Date date;
    final int amount;

    public Payment(Date date, int amount) {
        this.date = date;
        this.amount = amount;
    }
    
    public String toString() {
        return "\t" + date + "\t\t$" + amount;
    }
    
    public int compareTo(Payment p) {
        return p.date.compareTo(this.date);
    }
}
