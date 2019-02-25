package wheelswithinwheels;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
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
    
    // Returns true if the program should continue, false on quit
    public boolean parseLine(String line) throws IOException {
        String[] commandParts = splitStringIntoParts(line),
                 args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
        
        try{
            switch (commandParts[0]) {
                case "help":
                    help();
                    break;

                case "quit":
                    System.out.println("Goodbye");
                    return false;

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

                case "readc":
                    readScript(args);
                    break;    

                case "savebs":
                    saveState(args);
                    break;  

                case "restorebs":
                    restoreState(args);
                    break;

                case "printrp":
                    printRepairPrices();
                    break;

                case "printo":
                    printOrders();
                    break;

                case "":
                    break;

                default:
                    System.out.println("Unknown command " + commandParts[0]);
            }
        } catch (UIParseException e) {
               handleUIException(e);
        }
        
        return true;
    }
    
    public void run() throws IOException {
        // Running breaks when parseLine returns false
        while (parseLine(getInputLine()));
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
    
    protected void handleBikeShopExeption (BikeShopException e) {
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
            System.out.println("Invalid price brand: " + npe.getBrand() + ", tier: " + npe.getTier());
        }
    }
    
    protected void handleUIException (UIParseException e) {
        System.out.println("Invalid " + e.getArgument() + ": \"" + e.getInputted() + "\" is not a valid " + e.getExpectedType());
    }
    
    //COMMANDS==================================================================

    public void help() {
        System.out.println(helpMessage);
    }
    
    public void addRepairPrice(String[] args) throws UIParseException {
        int price;
        int days;
        
        try {price = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {throw new UIParseException(args[2], "price", "number");}
        
        try {days = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {throw new UIParseException(args[2], "number of days", "number");}
        
        bikeShop.addRepairPrice(args[0], args[1], price, days);
    }
    
    public void addCustomer(String[] args) {
        
        bikeShop.addCustomer(args[0], args[1]);
        
    }
    
    public void addOrder(String[] args) throws UIParseException {
        int customerNumber;
        try {customerNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {throw new UIParseException(args[0], "customer number", "number");}
        
        Date date;
        try {date = Formatter.date(Integer.parseInt(args[1]));
        } catch (Exception e) {throw new UIParseException(args[1], "date", "date");}
        
        try {
            bikeShop.addOrder(customerNumber, date, args[2], args[3], "");
        } catch (BikeShopException e) {
            handleBikeShopExeption(e);
        }
    }
    
    public void addPayment(String[] args) throws UIParseException {
        int customerNumber;
        try {customerNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {throw new UIParseException(args[0], "customer number", "number");}
        
        Date date;
        try {date = Formatter.date(Integer.parseInt(args[1]));
        } catch (Exception e) {throw new UIParseException(args[2], "date", "date");}
        
        int amount;
        try {amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {throw new UIParseException(args[2], "amount", "number");}
        
        try {
            bikeShop.addPayment(customerNumber, date, amount);
        } catch (BikeShopException e) {
            handleBikeShopExeption(e);
        }
    }
    
    public void markComplete(String[] args) throws UIParseException {
        int orderNumber;
        try {orderNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {throw new UIParseException(args[2], "order number", "number");}
        
        Date date;
        try {date = Formatter.date(Integer.parseInt(args[1]));
        } catch (Exception e) {throw new UIParseException(args[2], "date", "date");}
        
        try {
            bikeShop.markComplete(orderNumber, date);
        } catch (BikeShopException e) {
            handleBikeShopExeption(e);
        }
    }
    
    public void printRepairPrices() {
        for (RepairPrice row : bikeShop.getRepairPrices()) {
            System.out.println(row);
        }
    }
    
    public void printOrders() {
        String orderString = "";
        for (Pair<Order, Customer> pair : bikeShop.getOrders()) {
            Order order = pair.getKey();
            Customer customer = pair.getValue();
            
            orderString += order.number + "\t" 
                    + customer.toString() + "\t\t" 
                    + order.brand + "\t" 
                    + order.tier + "\t" 
                    + order.price + "\t" 
                    /*+ order.promiseDate + "\t"*/ 
                    /*+ order.completedDate */
                    + "\n";
        }
        System.out.println(orderString);
    }
    
    public void readScript (String[] args) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader (new FileReader(args[0]));
        String line = null;
        while ((line = br.readLine()) != null) {
            parseLine(line);
        }
    }
    
    public void saveState (String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[0]), "utf-8"));
        bw.write(bikeShop.saveState());
    }
    
    public void restoreState (String[] args) throws IOException {
        reset();
        readScript(args);
    }
}
