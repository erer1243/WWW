package wheelswithinwheels;

class NullPriceException extends Exception {
    protected String brand;
    protected String tier;
    
    public NullPriceException (String brand, String tier) {
        super();
        this.brand = brand;
        this.tier = tier;
    }
    
    public String getBrand () {
        return brand;
    }
    
    public String getTier () {
        return tier;
    }
}
