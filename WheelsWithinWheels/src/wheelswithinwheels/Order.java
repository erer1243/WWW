package wheelswithinwheels;

import java.time.LocalDate;

public class Order {
    final int number;
    int customer, price;
    String brand, tier, comment;
    LocalDate startDate, promiseDate, completedDate = null;
    
    public Order(int number, int customer, String brand, String tier, 
                 int price, LocalDate startDate, int days, String comment) {
        this.number = number;
        this.customer = customer;
        this.brand = brand;
        this.tier = tier;
        this.price = price;
        this.startDate = startDate;
        this.comment = comment;
        this.promiseDate = startDate.plusDays(days);
    }
}
