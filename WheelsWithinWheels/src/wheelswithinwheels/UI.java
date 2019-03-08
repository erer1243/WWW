package wheelswithinwheels;

import java.io.*;
import java.util.*;
import javafx.util.Pair;

public class UI {
    protected final String helpMessage 
        = "quit - quit bike system\n"
        + "help - print this help message\n"
        + "help <command> - prints more detailed information about a specific command\n"
        + "addrp <brand> <tier> <price> <days> - add or update repair price\n"
        + "addc <first name> <last name> - add new customer\n"
        + "addo <customer number> <date> <brand> <level> <comment> - add new order\n"
        + "addp <customer number> <date> <amount> - add new payment\n"
        + "comp <order number> <completion date> - mark order as completed\n"
        + "printrp - print repair prices\n"
        + "printcnum - print customers by ID number\n"
        + "printcname - print customers by name\n"
        + "printo - print orders\n"
        + "printp - print payments\n"
        + "printt - print transactions\n"
        + "printr - print receivables\n"
        + "prints - print statements\n"
        + "readc <filename> - read commands from disk file filename\n"
        + "savebs <filename> - save bike shop as a file of commands in file filename\n"
        + "restorebs <filename> - restore a previously saved bike shop file from file filename";
    
    protected BikeShop bikeShop = new BikeShop();
    
    protected Stack<String> readingFiles = new Stack<>();
    
    // Returns true if the program should continue, false on quit
    public boolean parseLine(String line, boolean isRestoring) throws IOException {
        String[] commandParts = splitStringIntoParts(line);
        String[] args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
        
        try {
            switch (commandParts[0]) {
                case "quit":
                    System.out.println("Goodbye");
                    return false;
                
                case "help":
                    help();
                    break;
                    
                case "addrp":
                    addRepairPrice(args);
                    break;

                case "addc":
                    addCustomer(args);
                    break;

                case "addo":
                    addOrder(args);
                    break;    

                case "addp":
                    addPayment(args);
                    break;

                case "comp":
                    markComplete(args);
                    break;    
                
                case "printrp":
                    printRepairPrices(args);
                    break;

                case "printo":
                    printOrders(args);
                    break;
                
                case "printp":
                    printPayments(args);
                    break;
                
                case "printt":
                    printTransactions(args);
                    break;
                
                case "printr":
                    printReceivables(args);
                    break;
                    
                case "prints":
                    printStatements(args);
                    break;
                
                case "readc":
                    readScript(args, false);
                    break;
                    
                case "printcname":
                    printCustomersByName();
                    break;
                    
                case "printcnum":
                    printCustomersByNumber();
                    break;
                    
                case "savebs":
                    saveBikeShop(args);
                    break;  

                case "restorebs":
                    restoreBikeShop(args);
                    break;
                
                case "rncn":
                    if (isRestoring)
                        updateCustomerCounter(args);
                    else
                        System.out.println("Unknown command " + commandParts[0]);
                    break;
                    
                case "rnon":
                    if (isRestoring)   
                        updateOrderCounter(args);
                    else
                        System.out.println("Unknown command " + commandParts[0]);
                    break;
                
                case "remc":
                    removeCustomer(args);
                    break;
                    
                case "remo":
                    removeOrder(args);
                    break;

                case "":
                    break;

                default:
                    System.out.println("Unknown command " + commandParts[0]);
            }
        } catch (UIParseException e) {
            handleUIParseException(e);
        } catch (BikeShopException e) {
            handleBikeShopException(e);
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        }
        
        return true;
    }
    
    public void run() throws IOException {
        // Running breaks when parseLine returns false
        while (parseLine(getInputLine(), false));
    }
    
    //HELPER METHODS============================================================
    
    protected String getInputLine() throws IOException {
        System.out.print("> ");
        
        // This makes input work in netbeans
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
    
    protected String[] splitStringIntoParts(String input) {
        return input.split("\\s+");
    }
    
    protected void reset() {
        bikeShop = new BikeShop();
        System.out.println("Data records reset");
    }
    
    protected void handleBikeShopException(BikeShopException e) {
        if (e instanceof NullCustomerException) {
            NullCustomerException nce = (NullCustomerException) e;
            System.out.println("Invalid customer number: " + nce.getCustomerNumber());
        }
        if (e instanceof NullOrderException) {
            NullOrderException noe = (NullOrderException) e;
            System.out.println("Invalid order number: " + noe.getOrderNumber());
        }
        if (e instanceof NullPriceException) {
            NullPriceException npe = (NullPriceException) e;
            System.out.println("No price for brand: " + npe.getBrand() + ", tier: " + npe.getTier());
        } 
    }
    
    protected void handleUIParseException(UIParseException e) {
        System.out.println("Invalid " + e.getArgument() + ": \"" + e.getInputted() + "\" is not a valid " + e.getExpectedType());
    }
    
    //COMMANDS==================================================================

    protected void help() {
        System.out.println(helpMessage);
    }
    
    protected void addRepairPrice(String[] args) throws UIParseException {
        int price = Formatter.parseInt(args[2], "price");
        int days = Formatter.parseInt(args[3], "number of days");
        
        try {
            bikeShop.addRepairPrice(args[0], args[1], price, days);
        } catch (NullPriceException e) {
            System.out.println("Price already exists for brand: " + e.getBrand() + ", tier: " + e.getTier());
        }
    }
    
    protected void addCustomer(String[] args) {
        int customerNumber = bikeShop.addCustomer(args[0], args[1]);
        System.out.println("Customer " + args[0] + " " + args[1] + " given number: " + customerNumber);
    }
    
    protected void addOrder(String[] args) throws UIParseException, BikeShopException {
        int customerNumber = Formatter.parseInt(args[0], "customer number");
        Date date = Formatter.stringToDate(args[1]);
        String comment = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
        
        bikeShop.addOrder(customerNumber, date, args[2], args[3], comment);
    }
    
    protected void addPayment(String[] args) throws UIParseException, BikeShopException {
        int customerNumber = Formatter.parseInt(args[0], "customer number");
        Date date = Formatter.stringToDate(args[1]);
        int amount = Formatter.parseInt(args[2], "amount");
        
        bikeShop.addPayment(customerNumber, date, amount);
    }
    
    protected void markComplete(String[] args) throws UIParseException, BikeShopException {
        int orderNumber = Formatter.parseInt(args[0], "order number");
        Date date = Formatter.stringToDate(args[1]);
        
        bikeShop.markComplete(orderNumber, date);
    }
    
    protected void printRepairPrices(String[] args) {
        System.out.println("All Repair Prices: ");
        for (RepairPrice row : bikeShop.getRepairPrices()) {
            System.out.println(row);
        }
    }
    
    protected void printCustomersByName() {
        ArrayList<Customer> customers = bikeShop.getCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers");
            return;
        }
        
        Collections.sort(customers, 
            (Customer c1, Customer c2) -> 
                String.CASE_INSENSITIVE_ORDER.compare(c1.lastName, c2.lastName));
    
        for (Customer c : customers) 
            System.out.println(c);
        
    } 
    
    protected void printCustomersByNumber() {
       ArrayList<Customer> customers = bikeShop.getCustomers();
       if (customers.isEmpty()) {
           System.out.println("No customers");
           return;
       }
       
       Collections.sort(customers, 
            (Customer c1, Customer c2) -> c1.number - c2.number);

       for (Customer c : customers)
           System.out.println(c);
    }
    
    protected void printOrders(String[] args) {
        ArrayList<Pair<Order, Customer>> orders = bikeShop.getOrders();
        
        if (orders.isEmpty()) {
            System.out.println("No orders have been made yet");
            return;
        }
        
        String orderString = "";
        for (Pair<Order, Customer> pair : orders) {
            Order order = pair.getKey();
            Customer customer = pair.getValue();
            
            orderString += order.number + "\t" 
                    + customer.toString() + "\t\t" 
                    + order.brand + "\t" 
                    + order.tier + "\t" 
                    + order.price + "\t" 
                    + order.comment + "\t"
                    /*+ order.promiseDate + "\t"*/ 
                    /*+ order.completedDate */
                    + "\n";
        }
        
        System.out.println(orderString);
    }
    
    protected void printPayments(String[] args) {
        String output = "";
        for (Customer c : bikeShop.getCustomers()) {
            output += c.firstName + " " + c.lastName + ":\n";
            
            for (Payment p : c.payments)
                output += p.toString() + "\n";
            
        }
        if (output.equals(""))
            output = "No payments have been made yet";
        
        System.out.println(output);
    }
    
    protected void printTransactions(String[] args) {
        System.out.println("Printing All Transactions... ");
        String output = "";
        
        output += "All Orders: \n";
        int accumulatedPrice = 0;
        for (Pair<Order, Customer> pair : bikeShop.getOrders()) {
            Order order = pair.getKey();
            Customer customer = pair.getValue();
            
            output += customer.toString() + "\t\t" 
                    + order.brand + "\t" 
                    + order.tier + "\t" 
                    + order.price + "\t" 
                    + order.promiseDate + "\t" 
                    + order.completedDate + "\n";
            
            accumulatedPrice += order.price;
        }
        output += "\tTotal Owed: \t$" + accumulatedPrice + "\n";
        
        output += "All Payments: \n";
        int totalPayments = 0;
        ArrayList<Payment> allPayments = new ArrayList<>();
        
        for (Customer c : bikeShop.getCustomers())
            for (Payment p : c.payments)
                allPayments.add(p);
        
        Collections.sort(allPayments);
        for (Payment p : allPayments) {
            output += p.toString() + "\n";
            totalPayments += p.amount;
        }
        output += "\tTotal Payments: $" + totalPayments + "\n";
        output += "Total Revenue: \t$" + (accumulatedPrice - totalPayments);
        
        System.out.println(output);
    } 
    
    protected void printReceivables(String[] args) {
        String output = "";
        int totalDue = 0;
        for (Customer customer : bikeShop.getCustomers()) {
            output += customer.firstName + " " + customer.lastName + " owes: ";
            int amountDue = bikeShop.getCustomerDue(customer);
            totalDue += amountDue;
            //Assumed that balance is never greater than totalPrice ie. totalDue is never negative
            output += "$" + amountDue + "\n";
        }
        output += "\tTotal Accounts Receivable: $" + totalDue;
        System.out.println(output);
    }
    
    protected void printStatements(String[] args) {
        String output = "";
        
        for (Customer customer : bikeShop.getCustomers()) {
            output += customer.firstName + " " + customer.lastName + ": \n";
            
            int customerCost = 0;
            
            output += "Orders: \n";
            for (Order order : bikeShop.getOrdersOfCustomer(customer)) {
                customerCost += order.price;
                output += "\t" + order.startDate + " \t$" + order.price + " \n" ;
            }
            
            int customerPaid = customer.paid();
            
            int amountDue = customerCost - customerPaid;
            
            output += "Total Price: \t$" + amountDue;
            
            output += "Payments: \n";
            for (Payment payment: customer.payments)
                output += payment.toString() + "\n";
            
            output += "Total Payment: \t$" + customerPaid + "\n";
            output += "Total Amount Owed: \t$" + (amountDue);
        }
        
        if (output.equals("")) 
            System.out.println("No statements available");
        else 
            System.out.println(output);
        
    } 
    
    protected void readScript(String[] args, boolean isRestoring) throws IOException, UIParseException {
        String fileName = String.join(" ", args);
        
        if (readingFiles.search(fileName) > 0) {
            System.out.println("Recursive usage of readc is not allowed.");
            return;
        }
        readingFiles.push(fileName);
        System.out.println("Now reading " + fileName);
        
        File file = new File(fileName);
        
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
                
        String line;
        
        while ((line = reader.readLine()) != null) {
            if (!line.equals("")) {
                System.out.println(line);
                parseLine(line, isRestoring);
            }
        }
        
        readingFiles.pop();
        System.out.println("Done reading " + fileName);
    }
     
    protected void saveBikeShop(String[] args) throws IOException {
        File file = new File(args[0]);
        file.createNewFile();
        
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        
        writer.write(bikeShop.saveState());
        writer.close();
    }
    
    protected void restoreBikeShop(String[] args) throws IOException, UIParseException {
        reset();
        readScript(args, true);
    }
    
    protected void removeCustomer(String[] args) throws UIParseException {
        int customerNumber = Formatter.parseInt(args[0], "customer number");
    }
    
    protected void removeOrder(String[] args) throws UIParseException {
        int orderNumber = Formatter.parseInt(args[0], "order number");
    }
    
    protected void updateOrderCounter(String[] args) throws UIParseException {
        int orderCounter = Formatter.parseInt(args[0], "order counter");
        
        bikeShop.setOrderCounter(orderCounter);
    }
    
    protected void updateCustomerCounter(String[] args) throws UIParseException {
        int customerCounter = Formatter.parseInt(args[0], "customer counter");
        
        bikeShop.setCustomerCounter(customerCounter);
    }
}
