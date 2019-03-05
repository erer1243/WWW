package wheelswithinwheels;
import java.util.ArrayList;

public class Customer {
    final int number;
    String firstName, lastName;
    
    ArrayList<Payment> payments = new ArrayList<>();
    ArrayList<Integer> orderNumbers = new ArrayList<>();
    
    public Customer (int number, String firstName, String lastName) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public int paid() {
        int sum = 0;
        for (Payment payment : payments) {
            sum += payment.amount;
        }
        return sum;
    }
    
    public String toString() {
        return lastName + ", " + firstName + " " + number;
    }
}
