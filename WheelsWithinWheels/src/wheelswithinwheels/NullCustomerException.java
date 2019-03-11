package wheelswithinwheels;

class NullCustomerException extends BikeShopException {
    protected int customerNumber;
    
    public NullCustomerException(int customerNumber) {
        this.customerNumber = customerNumber;
    }
    
    public int getCustomerNumber() {
        return customerNumber;
    }
}
