/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wheelswithinwheels;

/**
 *
 * @author Gideon_mac
 */
public class RepairPrice {
    final String brand;
    final String tier;
    int price;
    int days;
    
    public RepairPrice (String brand, String tier, int price, int days) {
        this.brand = brand;
        this.tier = tier;
        this.price = price;
        this.days = days;
    }
}
