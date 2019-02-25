package wheelswithinwheels;

class NullCustomerException extends Exception {
    
    protected int customerNumber;
    
    public NullCustomerException (int customerNumber) {
        super();
        this.customerNumber = customerNumber;
    }
    
    public int getCustomerNumber () {
        return customerNumber;
    }
}
