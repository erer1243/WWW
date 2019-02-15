package wheelswithinwheels;
import java.util.ArrayList;
import java.util.Date;

public class BikeShop {
    protected PriceTable priceTable = new PriceTable();
    protected ArrayList<Order> orders = new ArrayList<>();
    protected ArrayList<Customer> customers = new ArrayList<>();
    
    public void addRepairPrice(String brand, String tier, int price, int days) {
        priceTable.addPrice(brand, tier, price, days);
    }
    
    public void addCustomer(String firstName, String lastName) {
        customers.add(new Customer(
                customers.size(),
                firstName,
                lastName
        ));
    }
    
    public void addOrder(int customerNumber, Date date, String brand, String tier, String comment) {
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
    
    public void addPayment(int customerNumber, Date date, int amount) {
        customers.get(customerNumber).payments.add(new Payment(date, amount));
    }
    
    public void markComplete(int orderNumber, Date date) {
        orders.get(orderNumber).completedDate = date;
    }
    
    public ArrayList<RepairPrice> getPrices() {
        return priceTable.getAll();
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
        
        for (Customer customer : customers) {
            output += "addc "  +  customer.firstName + " " +  customer.lastName + "\n";
            
            for (Payment payment : customer.payments) {
                output += "addp " + customer.number + " " + payment.date + " " + payment.amount + "\n";
            }
        }
        
        for (Order order : orders) {
            output += "addo " + order.customer + " " + order.startDate + " " + order.brand + " " + order.tier + " " + order.comment + "\n";
        }
        
        return output;
    }
}
