/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelswithinwheels;
import java.util.ArrayList;

/**
 *
 * @author Gideon_mac
 */
public class WheelsWithinWheels {
    public PriceTable priceTable = new PriceTable();
    public ArrayList<Order> orders = new ArrayList<Order>();
    public ArrayList<Customer> customers = new ArrayList<Customer>();
    
    /*protected String saveToStulin () {
        String output = "";
        for (RepairPrice row : priceTable.getArray()) {
            output += "addrp " + row.brand + " " + row.tier + " " + row.price + " " + row.days + "\n";
        }
        for (Order order: orders) {
            
        }
    }*/
    
    protected void reset () {
        bikeShop = new WheelsWithinWheels();
    }
    
    protected static WheelsWithinWheels bikeShop = new WheelsWithinWheels();
    
    public static void main(String[] args) {
        
    }
    
}
