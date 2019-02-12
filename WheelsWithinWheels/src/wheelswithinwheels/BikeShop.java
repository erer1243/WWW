package wheelswithinwheels;
import java.util.ArrayList;
import java.util.Date;

public class BikeShop {
    protected PriceTable priceTable = new PriceTable();
    protected ArrayList<Order> orders = new ArrayList<>();
    protected ArrayList<Customer> customers = new ArrayList<>();
    
    
    public void addRepairPrice (String brand, String tier, int price, int days) {
        priceTable.setPrice(brand, tier, price, days);
    }
    
    public void addCustomer (String firstName, String lastName) {
        customers.add(new Customer(
                customers.size(),
                firstName,
                lastName
        ));
    }
    
    
    public void addOrder (int customerNumber, Date date, String brand, String tier, String comment) {
        RepairPrice row = priceTable.getPrice(brand, tier);
        int orderNumber = orders.size();
        
        orders.add(new Order(
                orderNumber,
                customerNumber,
                row.brand,
                row.tier,
                row.price,
                date,
                row.days,
                comment
        ));
        
        customers.get(customerNumber).orderNumbers.add(orderNumber);
    }
    
    public void addPayment (int customerNumber, Date date, int amount) {
        customers.get(customerNumber).payments.add(new Payment(date, amount));
    }
    
    public void markComplete (int orderNumber, Date date) {
        orders.get(orderNumber).completedDate = date;
    }
    
    public ArrayList<RepairPrice> getPrices () {
        return priceTable.getAll();
    }
    
}
