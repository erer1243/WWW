package wheelswithinwheels;

import java.util.Date;

public class Order {
    final int number;
    int customer;
    String brand;
    String tier;
    Date startDate;
    
    int price;
    Date promiseDate;
    
    public Order(int number, int customer, String brand, String tier, int price, Date startDate, int days) {
        this.number = number;
        this.customer = customer;
        this.brand = brand;
        this.tier = tier;
        this.price = price;
        this.startDate = startDate;
        this.promiseDate = (Date) startDate.clone();
        promiseDate.setDate(startDate.getDate() + days);
    }
}
