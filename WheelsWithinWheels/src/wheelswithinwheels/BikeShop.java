package wheelswithinwheels;
import java.util.ArrayList;
import java.util.Date;

public class BikeShop {
    protected PriceTable priceTable = new PriceTable();
    protected ArrayList<Order> orders = new ArrayList<Order>();
    protected ArrayList<Customer> customers = new ArrayList<Customer>();
    
    
    protected int nextOrderNumber () {
        return orders.size();
    }
    
    public void setPrice(String brand, String tier, int price, int days) {
        priceTable.setPrice(brand, tier, price, days);
    }
    
    public void addOrder (int customerNumber, Date date, String brand, String tier, String commment) {
        RepairPrice row = priceTable.getPrice(brand, tier);
        
        orders.add(new Order(
                nextOrderNumber(),
                customerNumber,
                row.brand,
                row.tier,
                row.price,
                date,
                row.days    
        ));
    }
}
