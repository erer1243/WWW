package wheelswithinwheels;
import java.util.ArrayList;

public class WheelsWithinWheels {
    public PriceTable priceTable = new PriceTable();
    public ArrayList<Order> orders = new ArrayList<Order>();
    public ArrayList<Customer> customers = new ArrayList<Customer>();
    
    protected String saveToStulin () {
        String output = "";
        for (RepairPrice row : priceTable.getArray()) {
            output += "addrp " + row.brand + " " + row.tier + " " + row.price + " " + row.days + "\n";
        }
        /*for (Order order: orders) {
            output += "addo " + order.customer + " " + order.date + " " +  + " " +  + " " +  + " " +  +"\n";
        }*/ //We need to store price data in here somewhere or update the repair prices every single time
        return null;
    }
    
    protected void reset () {
        bikeShop = new WheelsWithinWheels();
    }
    
    protected static WheelsWithinWheels bikeShop = new WheelsWithinWheels();
    
    public static void main(String[] args) {
        
    }
    
}
