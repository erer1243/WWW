package wheelswithinwheels;

import java.util.ArrayList;

public class PriceTable {
    protected ArrayList<RepairPrice> priceList = new ArrayList<>();
    
    public RepairPrice getPrice(String brand, String tier) {
        for (RepairPrice price : priceList)
            if (price.brand.equals(brand) && price.tier.equals(tier))
                return price;
                   
        return null;
    }
    
    public void addPrice(String brand, String tier, int price, int days) {
        RepairPrice currentPrice = getPrice(brand, tier);
        if (currentPrice != null) {
            return;
        } else 
            priceList.add(new RepairPrice(brand, tier, price, days));
    }
    
    public ArrayList<RepairPrice> getAll() {
        return priceList;
    }
    
}
