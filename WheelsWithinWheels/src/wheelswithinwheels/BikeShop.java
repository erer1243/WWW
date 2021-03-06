package wheelswithinwheels;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.util.Pair;

public class BikeShop {
    //Note: use customers.get or orders.get if you know it will work.
    //If unsure, always use getCustomer or getOrder.
    
    protected PriceTable priceTable = new PriceTable();
    
    protected HashMap<Integer, Order> orders = new HashMap<>();
    protected int orderCounter = 0;
    
    protected HashMap<Integer, Customer> customers = new HashMap<>();
    protected int customerCounter = 0;
    
    // rncn
    public void setOrderCounter(int newValue) {
        orderCounter = newValue;
    }
    
    // rnon
    public void setCustomerCounter(int newValue) {
        customerCounter = newValue;
    }
    
    //GETS======================================================================
    
    public RepairPrice getRepairPrice(String brand, String tier) throws NullPriceException {
        RepairPrice row = priceTable.getPrice(brand, tier);
        if (row == null)
            throw new NullPriceException(brand, tier);
        
        return row;
    }
    
    public Order getOrder(int orderNumber) throws NullOrderException {
        Order order = orders.get(orderNumber);
        if (order == null)
            throw new NullOrderException(orderNumber);
        
        return order;
    }
    
    public Customer getCustomer(int customerNumber) throws NullCustomerException {
        Customer customer = customers.get(customerNumber);
        if (customer == null)
            throw new NullCustomerException(customerNumber);
        
        return customer;
    }
    
    public int getCustomerTransactionTotal(Customer customer) {
        int sum = 0;
        for (int orderNumber : customer.orderNumbers)
            sum += orders.get(orderNumber).price;
        
        return sum;
    }
    
    public int getCustomerDue(Customer customer) {
        return getCustomerTransactionTotal(customer) - customer.paid();
    }
    
    //GET MULTIPLE==============================================================
    
    public ArrayList<Pair<Order, Customer>> getOrders() {
        ArrayList<Pair<Order, Customer>> output = new ArrayList<>();
        for (Order order : orders.values()) 
            output.add(new Pair<Order, Customer> (order, customers.get(order.customer)));
        
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
    
    public ArrayList<Order> getOrdersOfCustomer(Customer customer) {
        ArrayList<Order> customerOrders = new ArrayList<>();
        
        for (int orderNumber : customer.orderNumbers)
            customerOrders.add(orders.get(orderNumber));
        
        return customerOrders;
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
    
    public void addOrder(int customerNumber, LocalDate date, String brand, String tier, String comment) throws NullCustomerException, NullPriceException {
        Customer customer = getCustomer(customerNumber);
        if (customer == null) 
            throw new NullCustomerException(customerNumber);
        
        RepairPrice row = priceTable.getPrice(brand, tier);
        if (row == null) 
            throw new NullPriceException(brand, tier);
        
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
    
    public void addPayment(int customerNumber, LocalDate date, int amount) throws NullCustomerException {
        Customer customer = getCustomer(customerNumber);
        if (customer == null)
            throw new NullCustomerException(customerNumber);
        
        customer.payments.add(new Payment(date, amount));
    }
    
    //EDITS=====================================================================
    
    public void markComplete(int orderNumber, LocalDate date) throws NullOrderException {
        Order order = getOrder(orderNumber);
        if (order == null)
            throw new NullOrderException(orderNumber);
        
        order.completedDate = date;
    }
    
    //REMOVES===================================================================
    
    public void removeCustomer(int customerNumber) throws NullCustomerException {
        Customer customer = getCustomer(customerNumber);
        for (int orderNumber : customer.orderNumbers)
            orders.remove(orderNumber);

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
        for (RepairPrice row : priceTable.getAll())
            output += "addrp " + " " +  row.brand + " " +  row.tier + " " +  row.price + " " +  row.days + "\n";
        
        for (Customer customer : customers.values()) {
            output += "rncn " + customer.number + "\n";
            output += "addc "  +  customer.firstName + " " +  customer.lastName + "\n";
            
            for (Payment payment : customer.payments)
                output += "addp " + customer.number + " " + Formatter.dateToString(payment.date) + " " + payment.amount + "\n";
        }
        
        for (Order order : orders.values()) {
            output += "rnon " + order.number + "\n";
            output += "addo " + order.customer + " " + Formatter.dateToString(order.startDate) + " " + order.brand + " " + order.tier + " " + order.comment + "\n";
            
            if (order.completedDate != null)
                output += "comp " + order.number + " " + Formatter.dateToString(order.completedDate) + "\n";
        }
        
        return output;
    }
}
