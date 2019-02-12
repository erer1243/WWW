package wheelswithinwheels;
import java.util.ArrayList;

public class Customer {
    final int number;
    String firstName;
    String lastName;
    
    ArrayList<Payment> payments;
    ArrayList<Integer> orderNumbers;
    
    public Customer(int number, String firstName, String lastName) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
