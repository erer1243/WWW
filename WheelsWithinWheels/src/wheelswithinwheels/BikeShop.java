package wheelswithinwheels;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import javafx.util.Pair;

public class BikeShop {
    //Note: use customers.get or orders.get if you know it will work.
    //If unsure, always use getCustomer or getOrder.
    
    protected PriceTable priceTable = new PriceTable();
    
    protected HashMap<Integer, Order> orders = new HashMap<>();
    protected int orderCounter = 0;
    
    protected HashMap<Integer, Customer> customers = new HashMap<>();
    protected int customerCounter = 0;
    
    public void updateOrderCounter(int newValue) {orderCounter = newValue;}
    public void updateCustomerCounter(int newValue) {customerCounter = newValue;}
    
    protected boolean backwardsCompatability = true;
    
    public boolean isBackwardsCompatable() {
        return backwardsCompatability;
    }
    
    public void allowExtraFunctions() {
        backwardsCompatability = false;
    }
    //GETS======================================================================
    
    public RepairPrice getRepairPrice(String brand, String tier) throws NullPriceException {
        RepairPrice row = priceTable.getPrice(brand, tier);
        if (row == null) {
            throw new NullPriceException(brand, tier);
        }
        return row;
    }
    
    public Order getOrder(int orderNumber) throws NullOrderException {
        Order order = orders.get(orderNumber);
        if (order == null) {
            throw new NullOrderException(orderNumber);
        }
        return order;
    }
    
    public Customer getCustomer(int customerNumber) throws NullCustomerException {
        Customer customer = customers.get(customerNumber);
        if (customer == null) {
            throw new NullCustomerException(customerNumber);
        }
        return customer;
    }
    
    public int getCustomerDue (Customer customer) {
        int sum = 0;
        for (int orderNumber : customer.orderNumbers) {
            sum += orders.get(orderNumber).price;
        }
        return sum;
    }
    
    //GET MULTIPLE==============================================================
    
    public ArrayList<Pair<Order, Customer>> getOrders () {
        ArrayList<Pair<Order, Customer>> output = new ArrayList<>();
        for (Order order : orders.values()) {
            output.add(new Pair<Order, Customer> (order, customers.get(order.customer)));
        }
        return output;
    }
    
    public ArrayList<RepairPrice> getRepairPrices() {
        return priceTable.getAll();
    }
    
    public ArrayList<Customer> getCustomers() {
        ArrayList<Customer> output = new ArrayList<>();
        for (Customer c : customers.values())
            output.add(c);
        
        return output;
    }
    
    //ADDS======================================================================
    
    
    public void addRepairPrice(String brand, String tier, int price, int days) throws NullPriceException {
        priceTable.addPrice(brand, tier, price, days);
    }
    
    public int addCustomer(String firstName, String lastName) {
        int customerNumber = customerCounter++;
        
        customers.put(customerNumber, new Customer(
                customerNumber,
                firstName,
                lastName
        ));
        
        return customerNumber;
    }
    
    public void addOrder(int customerNumber, Date date, String brand, String tier, String comment) throws NullCustomerException, NullPriceException {
        Customer customer = getCustomer(customerNumber);
        if (customer == null) {
            throw new NullCustomerException(customerNumber);
        }
        
        RepairPrice row = priceTable.getPrice(brand, tier);
        if (row == null) {
            throw new NullPriceException(brand, tier);
        }
        
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
    
    public void addPayment(int customerNumber, Date date, int amount) throws NullCustomerException {
        Customer customer = getCustomer(customerNumber);
        if (customer == null) {
            throw new NullCustomerException(customerNumber);
        }
        
        customer.payments.add(new Payment(date, amount));
    }
    
    //EDITS=====================================================================
    
    public void markComplete(int orderNumber, Date date) throws NullOrderException {
        Order order = getOrder(orderNumber);
        if (order == null) {
            throw new NullOrderException(orderNumber);
        }
        
        order.completedDate = date;
    }
    
    //REMOVES===================================================================
    
    public void removeCustomer(int customerNumber) throws NullCustomerException {
        Customer customer = getCustomer(customerNumber);
        for (int orderNumber : customer.orderNumbers) {
            orders.remove(orderNumber);
        }
        customers.remove(customerNumber);
    }
    
    public void removeOrder(int orderNumber) throws NullOrderException {
        Order order = getOrder(orderNumber);
        Customer customer = customers.get(order.customer);
        customer.orderNumbers.remove(orderNumber);
        orders.remove(orderNumber);
    }
    
    //FILE INTERACTIONS=========================================================
    
    public String saveState() {
        String output = "";
        /*"addrp <brand> <tier> <price> <days> - add or update repair price\n"
        + "addc <first name> <last name> - add new customer\n"
        + "addo <customer number> <date> <brand> <level> <comment> - add new order\n"
        + "addp <customer number> <date> <amount> - add new payment\n"*/
        for (RepairPrice row : priceTable.getAll()) {
            output += "addrp " + " " +  row.brand + " " +  row.tier + " " +  row.price + " " +  row.days + "\n";
        }
        
        for (Customer customer : customers.values()) {
            output += "rncn " + customer.number + "\n";
            output += "addc "  +  customer.firstName + " " +  customer.lastName + "\n";
            
            for (Payment payment : customer.payments) {
                output += "addp " + customer.number + " " + Formatter.date(payment.date) + " " + payment.amount + "\n";
            }
        }
        
        for (Order order : orders.values()) {
            output += "rnon " + order.number + "\n";
            output += "addo " + order.customer + " " + Formatter.date(order.startDate) + " " + order.brand + " " + order.tier + " " + order.comment + "\n";
            
            if (order.completedDate != null) {
                output += "comp " + order.number + " " + Formatter.date(order.completedDate) + "\n";
            }
        }
        
        return output;
    }
}
