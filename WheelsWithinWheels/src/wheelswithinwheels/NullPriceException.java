package wheelswithinwheels;

class NullPriceException extends BikeShopException {
    protected String brand;
    protected String tier;
    
    public NullPriceException(String brand, String tier) {
        this.brand = brand;
        this.tier = tier;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getTier() {
        return tier;
    }
}
