package wheelswithinwheels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

public class BikeShop {
    protected PriceTable priceTable = new PriceTable();
    protected HashMap<Integer, Order> orders = new HashMap<>();
    protected HashMap<Integer, Customer> customers = new HashMap<>();
    
    protected int customerCounter = 0;
    protected int orderCounter = 0;
    
    public void addRepairPrice(String brand, String tier, int price, int days) {
        priceTable.addPrice(brand, tier, price, days);
    }
    
    public void addCustomer(String firstName, String lastName) {
        customers.put(customerCounter++, new Customer(
                customers.size(),
                firstName,
                lastName
        ));
    }
    
    public void addOrder(int customerNumber, Date date, String brand, String tier, String comment) {
        RepairPrice row = priceTable.getPrice(brand, tier);
        int orderNumber = orders.size();
        
        orders.put(orderCounter++, new Order(
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
    
    public void addPayment(int customerNumber, Date date, int amount) {
        customers.get(customerNumber).payments.add(new Payment(date, amount));
    }
    
    public void markComplete(int orderNumber, Date date) {
        orders.get(orderNumber).completedDate = date;
    }
    
    public ArrayList<RepairPrice> getPrices() {
        return priceTable.getAll();
    }
    
    public String printRepairPrices() {
        return priceTable.toString();
    }
    
    public String saveState () {
        String output = "";
        /*"addrp <brand> <tier> <price> <days> - add or update repair price\n"
        + "addc <first name> <last name> - add new customer\n"
        + "addo <customer number> <date> <brand> <level> <comment> - add new order\n"
        + "addp <customer number> <date> <amount> - add new payment\n"*/
        for (RepairPrice row : priceTable.getAll()) {
            output += "addrp " + " " +  row.brand + " " +  row.tier + " " +  row.price + " " +  row.days + "\n";
        }
        
        for (int index : customers.keySet()) {
            Customer customer = customers.get(index);
            output += "rncn " + index + "\n";
            output += "addc "  +  customer.firstName + " " +  customer.lastName + "\n";
            
            for (Payment payment : customer.payments) {
                output += "addp " + customer.number + " " + payment.date + " " + payment.amount + "\n";
            }
        }
        
        for (int index : orders.keySet()) {
            Order order = orders.get(index);
            output += "rnon " + index + "\n";
            output += "addo " + order.customer + " " + order.startDate + " " + order.brand + " " + order.tier + " " + order.comment + "\n";
            
            if (order.completedDate != null) {
                output += "comp " + order.number + " " + Formatter.date(order.completedDate) + "\n";
            }
        }
        
        return output;
    }
}
