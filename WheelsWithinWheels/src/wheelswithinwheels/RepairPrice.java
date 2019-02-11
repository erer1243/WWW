package wheelswithinwheels;

public class RepairPrice {
    final String brand;
    final String tier;
    int price;
    int days;
    
    public RepairPrice(String brand, String tier, int price, int days) {
        this.brand = brand;
        this.tier = tier;
        this.price = price;
        this.days = days;
    }
}
