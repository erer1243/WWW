package wheelswithinwheels;

public class UI {
    public void parseline (String lineIn) {
        String[] args = split(lineIn);
        
        switch (args[0]) {
            case "help":
                help(args);
                break;
            
        }
    }
    
    public String[] split (String lineIn) {
        String[] args = lineIn.split("\\s+");
        while (args.length > 0 && args[0].equals("")) {
            String[] newArray = new String[args.length - 1];
            System.arraycopy(args, 1, newArray, 0, newArray.length);
            args = newArray;
        }
        return args;
    }
    
    public void help (String[] args) {
        
    }
}