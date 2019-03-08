package wheelswithinwheels;

import java.time.LocalDate;

public class Payment implements Comparable<Payment>{
    final LocalDate date;
    final int amount;

    public Payment(LocalDate date, int amount) {
        this.date = date;
        this.amount = amount;
    }
    
    public String toString() {
        return "\t" + Formatter.dateToString(date) + "\t\t$" + amount;
    }
    
    public int compareTo(Payment p) {
        return p.date.compareTo(this.date);
    }
}
