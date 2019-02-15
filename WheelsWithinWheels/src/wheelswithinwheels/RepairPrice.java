package wheelswithinwheels;

public class RepairPrice {
    final String brand, tier;
    int price, days;
    
    public RepairPrice(String brand, String tier, int price, int days) {
        this.brand = brand;
        this.tier = tier;
        this.price = price;
        this.days = days;
    }
}
