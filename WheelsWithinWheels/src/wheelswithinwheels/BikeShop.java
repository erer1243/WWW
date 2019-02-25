package wheelswithinwheels;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Date;
import javafx.util.Pair;
import java.lang.Exception;

public class BikeShop {
    protected PriceTable priceTable = new PriceTable();
    
    protected HashMap<Integer, Order> orders = new HashMap<>();
    protected int orderCounter = 0;
    
    protected HashMap<Integer, Customer> customers = new HashMap<>();
    protected int customerCounter = 0;
    
    
    //GETS======================================================================
    
    public RepairPrice getRepairPrice (String brand, String tier) throws NullPriceException {
        RepairPrice row = priceTable.getPrice(brand, tier);
        if (row == null) {
            throw new NullPriceException(brand, tier);
        }
        return row;
    }
    
    public Order getOrder (int orderNumber) throws NullOrderException {
        Order order = orders.get(orderNumber);
        if (order == null) {
            throw new NullOrderException(orderNumber);
        }
        return order;
    }
    
    public Customer getCustomer (int customerNumber) throws NullCustomerException {
        Customer customer = customers.get(customerNumber);
        if (customer == null) {
            throw new NullCustomerException(customerNumber);
        }
        return customer;
    }
    
    //GET MULTIPLE==============================================================
    
    public ArrayList<Pair<Order, Customer>> getOrders() {
        ArrayList<Pair<Order, Customer>> output = new ArrayList<>();
        for (Order order : orders.values()) {
            output.add(new Pair<Order, Customer> (order, customers.get(order.customer)));
        }
        return output;
    }
    
    public ArrayList<RepairPrice> getRepairPrices() {
        return priceTable.getAll();
    }
    
    //ADDS======================================================================
    
    
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
    
    public void addOrder (int customerNumber, Date date, String brand, String tier, String comment) throws NullCustomerException, NullPriceException {
        Customer customer = getCustomer(customerNumber);
        
        RepairPrice row = priceTable.getPrice(brand, tier);
        
        int orderNumber = orderCounter++;

        customer.orderNumbers.add(orderNumber);
        
        orders.put(orderNumber, new Order(
                orderNumber,
                customerNumber,
                row.brand,
                row.tier,
                row.price,
                date,
                row.days,
                comment
        ));
    }
    
    public void addPayment (int customerNumber, Date date, int amount) throws NullCustomerException {
        Customer customer = getCustomer(customerNumber);
        customer.payments.add(new Payment(date, amount));
    }
    
    //EDITS=====================================================================
    
    public void markComplete(int orderNumber, Date date) throws NullOrderException {
        getOrder(orderNumber).completedDate = date;
    }
    
    //FILE INTERACTIONS=========================================================
    
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
