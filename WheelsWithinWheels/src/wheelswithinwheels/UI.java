package wheelswithinwheels;

import java.util.ArrayList;
import java.util.Arrays;

public class UI {
    protected final String helpMessage 
        = "quit - quit bike system\n"
        + "help - print this help message\n"
        + "addrp brand level price days - add repair price\n"
        + "[rest of help message here, follow this format]"; 
    
    protected PriceTable priceTable = new PriceTable();
    protected ArrayList<Order> orders = new ArrayList<Order>();
    protected ArrayList<Customer> customers = new ArrayList<Customer>();
    
    // Returns true if the program should continue, false on quit
    public boolean parseLine(String line) {
        String[] commandParts = splitStringIntoParts(line);
        String command = commandParts[0];
        String[] args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
        
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
                
            case "":
                break;
            
            default:
                System.out.println("Unknown command " + commandParts[0]);
            
        }
        
        return true;
    }
    
    public void run() {
        // Running breaks when parseLine returns false
        while (parseLine(getInputLine()));
    }
    
    protected String getInputLine() {
        System.out.print("> ");
        return System.console().readLine();
    }
    
    protected String[] splitStringIntoParts(String input) {
        return input.split("\\s+");
    }
   
    protected void reset() {
        priceTable = new PriceTable();
        orders = new ArrayList<Order>();
        customers = new ArrayList<Customer>();
        
        System.out.println("Data records reset");
    }
    
    // Command functions
    public void help() {
        System.out.println(helpMessage);
    }
    
    public void addRepairPrice(String[] args) {
        
    }
    
    public void addCustomer(String[] args) {
        
    }
    
    public void addOrder(String[] args) {
        
    }
    
    public void addPayment(String[] args) {
        
    }
}