package wheelswithinwheels;

class NullOrderException extends BikeShopException {
    protected int orderNumber;
    
    public NullOrderException (int orderNumber) {
        super();
        this.orderNumber = orderNumber;
    }
    
    public int getOrderNumber () {
        return orderNumber;
    }
}
