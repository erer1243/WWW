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
    
    public String toString() {
        return "\t" + brand + "\t\t" + tier + "\t\t" + price + "\t" + days;
    }
}
