package wheelswithinwheels;

import java.util.Date;

public class Order {
    final int number;
    int customer;
    String brand;
    String tier;
    Date startDate;
    String comment;
    
    int price;
    Date promiseDate;
    
    public Order(int number, int customer, String brand, String tier, int price, Date startDate, int days, String comment) {
        this.number = number;
        this.customer = customer;
        this.brand = brand;
        this.tier = tier;
        this.price = price;
        this.startDate = startDate;
        this.comment = comment;
        this.promiseDate = (Date) startDate.clone();
        promiseDate.setDate(startDate.getDate() + days);
    }
}
