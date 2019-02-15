package wheelswithinwheels;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

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
    public boolean parseLine(String line) {
        String[] commandParts = splitStringIntoParts(line),
                 args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
        
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
                
            case "":
                break;
            
            default:
                System.out.println("Unknown command " + commandParts[0]);
        }
        
        return true;
    }
    
    public void run() throws IOException {
        // Running breaks when parseLine returns false
        while (parseLine(getInputLine()));
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
    
    // Command functions
    public void help() {
        System.out.println(helpMessage);
    }
    
    public void addRepairPrice(String[] args) {
        //assuming no user error for now
        int price = Integer.parseInt(args[2]);
        int days = Integer.parseInt(args[3]);
        
        bikeShop.addRepairPrice(args[0], args[1], price, days);
    }
    
    public void addCustomer(String[] args) {
        //assuming no user error
        bikeShop.addCustomer(args[0], args[1]);
        
    }
    
    public void addOrder(String[] args) {
        //assuming no user error
        int customerNumber = Integer.parseInt(args[0]);
        Date date = Formatter.date(Integer.parseInt(args[1]));
        
        bikeShop.addOrder(customerNumber, date, args[2], args[3], "");
    }
    
    public void addPayment(String[] args) {
        //assuming no user error
        int customerNumber = Integer.parseInt(args[0]);
        Date date = Formatter.date(Integer.parseInt(args[1]));
        int amount = Integer.parseInt(args[2]);
        
        bikeShop.addPayment(customerNumber, date, amount);
    }
    
    public void markComplete(String[] args) {
        //assuming no user error
        int orderNumber = Integer.parseInt(args[0]);
        Date date = Formatter.date(Integer.parseInt(args[1]));
        
        bikeShop.markComplete(orderNumber, date);
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
        bikeShop = new BikeShop();
        readScript(args);
    }
}
