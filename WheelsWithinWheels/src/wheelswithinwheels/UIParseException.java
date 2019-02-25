package wheelswithinwheels;

class UIParseException extends Exception {
    protected String inputted;
    protected String argument;
    protected String expectedType;
    
    public UIParseException (String inputted, String argument, String expectedType) {
        super();
        this.inputted = inputted;
        this.argument = argument;
        this.expectedType = expectedType;
    }
    
    public String getInputted () {
        return inputted;
    }
    
    public String getArgument () {
        return argument;
    }
    public String getExpectedType () {
        return expectedType;
    }
}
