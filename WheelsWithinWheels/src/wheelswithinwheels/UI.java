package wheelswithinwheels;

import java.io.*;
import java.time.LocalDate;
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
        
        if (commandParts.length == 0)
            return true;
        
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
                    if (checkNumberArgs(4, args))
                        addRepairPrice(args);
                    break;

                case "addc":
                    if (checkNumberArgs(2, args))
                        addCustomer(args);
                    break;

                case "addo":
                    /* not using checkNumberArgs because addo is a special case 
                       in that it has an optional comment of indefinite length */
                    if (args.length < 4)
                        System.out.println("Expected at least 4 arguments but got " + args.length);
                    else
                        addOrder(args);
                    break;    

                case "addp":
                    if (checkNumberArgs(3, args))
                        addPayment(args);
                    break;

                case "comp":
                    if (checkNumberArgs(2, args))
                        markComplete(args);
                    break;    
                
                case "printrp":
                    printRepairPrices();
                    break;

                case "printo":
                    printOrders();
                    break;
                
                case "printp":
                    printPayments();
                    break;
                
                case "printt":
                    printTransactions();
                    break;
                
                case "printr":
                    printReceivables();
                    break;
                    
                case "prints":
                    printStatements();
                    break;
                
                case "readc":
                    readScript(args, false); //filenames can have spaces
                    break;
                    
                case "printcname":
                    printCustomersByName();
                    break;
                    
                case "printcnum":
                    printCustomersByNumber();
                    break;
                    
                case "savebs":
                    saveBikeShop(args); //filenames can have spaces
                    break;  

                case "restorebs":
                    restoreBikeShop(args); //filenames can have spaces
                    break;
                
                case "rncn":
                    if (isRestoring)
                        updateCustomerCounter(args);
                    else
                        System.out.println("rncn is only allowed in restorebs files");
                    break;
                    
                case "rnon":
                    if (isRestoring)   
                        updateOrderCounter(args);
                    else
                        System.out.println("rnon is only allowed in restorebs files");
                    break;
                
                case "remc":
                    removeCustomer(args);
                    break;
                    
                case "remo":
                    removeOrder(args);
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
    
    protected boolean checkNumberArgs(int targetArgs, String[] args) {
        if (targetArgs != args.length) {
            System.out.println("Expected " + targetArgs + " argument(s) but got " + args.length);
            return false;
        } else
            return true;
    }
    
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
    
    protected String fit(String s, int size, boolean right) {
        String result = "";
        int sSize = s.length();
        if (sSize == size) return s;
        if (size < sSize) return s.substring(0, size);
        result = s;
        String addon = "";
        int num = size - sSize;
        for (int i = 0; i < num; i++) {
            addon += " ";
        }
        if (right) {
            return result + addon;
        }
        return addon + result;
    }
    
    // for printcnum/printCustomersByNumber and printcname/printCustomersByName
    protected void printCustomerList(ArrayList<Customer> customers) {
        for (Customer c : customers)
            System.out.println(
                      fit(Integer.toString(c.number), 3, true)
                    + " "
                    + fit(c.lastName + ",", 10, false)
                    + " "
                    + fit(c.firstName, 10, true)
            );
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
        LocalDate date = Formatter.stringToDate(args[1]);
        String comment = String.join(" ", Arrays.copyOfRange(args, 4, args.length));
        
        bikeShop.addOrder(customerNumber, date, args[2], args[3], comment);
    }
    
    protected void addPayment(String[] args) throws UIParseException, BikeShopException {
        int customerNumber = Formatter.parseInt(args[0], "customer number");
        LocalDate date = Formatter.stringToDate(args[1]);
        int amount = Formatter.parseInt(args[2], "amount");
        
        bikeShop.addPayment(customerNumber, date, amount);
    }
    
    protected void markComplete(String[] args) throws UIParseException, BikeShopException {
        int orderNumber = Formatter.parseInt(args[0], "order number");
        LocalDate date = Formatter.stringToDate(args[1]);
        
        bikeShop.markComplete(orderNumber, date);
    }
    
    protected void printRepairPrices() {
        System.out.println("All Repair Prices: ");
        ArrayList<String> allRPs = new ArrayList<>();
        for (RepairPrice row : bikeShop.getRepairPrices()) {
            String thisRP = "\t";
            thisRP += fit(row.brand, 15, true) 
                    + fit(row.tier, 15, true)
                    + row.price + "\t"
                    + row.days;
            allRPs.add(thisRP);
        }
        Collections.sort(allRPs); 
        for (String s : allRPs){
            System.out.println(s);
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
    
        printCustomerList(customers);
        
    } 
    
    protected void printCustomersByNumber() {
       ArrayList<Customer> customers = bikeShop.getCustomers();
       if (customers.isEmpty()) {
           System.out.println("No customers");
           return;
       }
       
       Collections.sort(customers, 
            (Customer c1, Customer c2) -> c1.number - c2.number);

       printCustomerList(customers);
    }
    
    protected void printOrders() {
        ArrayList<Pair<Order, Customer>> orders = bikeShop.getOrders();
        
        if (orders.isEmpty()) {
            System.out.println("No orders have been made yet");
            return;
        } else {
            String orderString = "Order#\t" 
                    + fit("Customer", 20, true) + "\t"
                    + fit("Brand", 15, true)
                    + fit("Tier", 15, true)
                    + "Price\tPromise Date\tCompleted Date\tComment\n";
                    
            for (Pair<Order, Customer> pair : orders) {
                Order order = pair.getKey();
                Customer customer = pair.getValue();

                orderString += order.number + "\t" 
                        + fit(customer.firstName + " " + customer.lastName, 20, true)
                        + customer.number + "\t"
                        + fit(order.brand, 15, true)
                        + fit(order.tier, 15, true)
                        + "$" + order.price + "\t" 
                        + order.promiseDate + "\t";
                if (order.completedDate == null) {
                    orderString += "Incomplete\t";
                } else {
                    orderString += order.completedDate + "\t";
                }
                orderString += order.comment + "\n";
                        
            }

            System.out.println(orderString);
        }
    }
    
    protected void printPayments() {
        String output = "";
        int totalPayments = 0;
        for (Customer c : bikeShop.getCustomers()) {
            output += c.firstName + " " + c.lastName + ":\n";
            
            for (Payment p : c.payments) {
                output += "\t" + p.date + "\t\t$" + p.amount + "\n";
                totalPayments += p.amount;
            }
        }
        if (output.equals("")) {
            output = "No payments have been made yet";
        } else {
            output += "Total Payments: $" + totalPayments;
        }
        
        System.out.println(output);
    }
    
    protected void printTransactions() {
        System.out.println("Printing All Transactions... ");
        String output = "";
        
        output += "All Orders: \n";
        int accumulatedPrice = 0;
        int completedPrice = 0;
        for (Pair<Order, Customer> pair : bikeShop.getOrders()) {
            Order order = pair.getKey();
            Customer customer = pair.getValue();
            
            output += "\t" + fit(customer.firstName + " " + customer.lastName, 20, true)
                    + fit(order.brand, 15, true)
                    + fit(order.tier, 15, true);
            if (order.completedDate == null) {
                output += "Incomplete\t";
            } else {
                output += order.completedDate + "\t";
            }
            output += "$" + order.price + "\n" ;
            
            accumulatedPrice += order.price;
            if (order.completedDate != null) {
                completedPrice += order.price;
            }
        }
        output += "Total Price: $" + accumulatedPrice + "\n";
        output += "Total Price of Completed Orders: $" + completedPrice + "\n";
        
        output += "All Payments: \n";
        int totalPayments = 0;
        ArrayList<String> allPayments = new ArrayList<>();
        
        for (Customer c : bikeShop.getCustomers()){
            for (Payment p : c.payments) {
                String thisPayment = "\t" + p.date
                        + "\t" + fit(c.firstName + " " + c.lastName, 20, true)
                        + "$" + p.amount;
                allPayments.add(thisPayment);
                totalPayments += p.amount;
            }
        }
        
        Collections.sort(allPayments);
        for (String s : allPayments) {
            output += s + "\n";
        }
        output += "Total Payments: $" + totalPayments + "\n";
        output += "Total Amount Owed: $" + (accumulatedPrice - totalPayments);
        
        System.out.println(output);
    } 
    
    protected void printReceivables() {
        String output = "";
        int totalDue = 0;
        for (Customer customer : bikeShop.getCustomers()) {
            output += "\t" + fit(customer.firstName + " " + customer.lastName + " owes: ", 24, true) ;
            int amountDue = bikeShop.getCustomerDue(customer);
            totalDue += amountDue;
            //Assumed that balance is never greater than totalPrice ie. totalDue is never negative
            output += "$" + amountDue + "\n";
        }
        output += "Total Accounts Receivable: $" + totalDue;
        System.out.println(output);
    }
    
    protected void printStatements() {
        String output = "Printing Statements for All Customers... \n";
        
        for (Customer customer : bikeShop.getCustomers()) {
            output += customer.firstName + " " + customer.lastName + ": \n";
            
            int customerCost = 0;
            output += "\tOrders: \n";
            for (Order order : bikeShop.getOrdersOfCustomer(customer)) {
                customerCost += order.price;
                output += "\t\t" + order.startDate + " \t$" + order.price + " \n" ;
            }
            output += "\t  Total Price: \t\t$" + customerCost + "\n";
            
            output += "\tPayments: \n";
            for (Payment payment: customer.payments) {
                output += "\t\t" + payment.date + " \t$" + payment.amount + "\n";
            }
            
            int customerPaid = customer.paid();
            int amountDue = customerCost - customerPaid;
            output += "\t  Total Payment: \t$" + customerPaid + "\n";
            //output += "\tTotal Amount Owed: \t$" + customerCost + " - $" + customerPaid + " = $" + amountDue + "\n\n";
            output += "\tTotal Amount Owed: \t$" + amountDue + "\n\n";
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
