package wheelswithinwheels;

class NullOrderException extends Exception {
    
    protected int orderNumber;
    
    public NullOrderException (int orderNumber) {
        super();
        this.orderNumber = orderNumber;
    }
    
    public int getOrderNumber () {
        return orderNumber;
    }
}
