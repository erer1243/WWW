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
public class PriceTable {
    
    public PriceTable() {}
    
    protected ArrayList<RepairPrice> priceList;
    
    public RepairPrice getPrice (String brand, String tier) {
        for (RepairPrice row : priceList) {
            if (row.brand.equals(brand) && row.tier.equals(tier)) {
                return row;
            }
        }
        return null;
    }
    
    public void setPrice (String brand, String tier, int price, int days) {
        RepairPrice line = getPrice(brand, tier);
        if (line != null) {
            line.price = price;
            line.days = days;
        } else {
            priceList.add(new RepairPrice(brand, tier, price, days));
        }
    }
    
    public ArrayList<RepairPrice> getArray () {
        return priceList;
    }
}
