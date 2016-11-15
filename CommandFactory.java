package mcsally_borkv3;
import java.util.*;
/**
 * The Command Factory parses all commands given by user.
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class CommandFactory {
    private static CommandFactory theInstance;
    public static List<String> MOVEMENT_COMMANDS = Arrays.asList("n","w","e","s","u","d" );
    public static String SAVE_COMMAND = "save";
    public static String TAKE_COMMAND = "take";
    public static String DROP_COMMAND = "drop";
    public static List<String> INVENTORY_COMMANDS = Arrays.asList("i","inventory" );

    /**
     * Creates a single instance of the CommandFactory
     * 
     * @return a CommandFactory object
     */
    public static synchronized CommandFactory instance() {
        if (theInstance == null) {
            theInstance = new CommandFactory();
        }
        return theInstance;
    }

    private CommandFactory() {}

    /**
     * Parses through all fields and checks for the string passed in
     * 
     * @param command the command to be searched for
     */
    public Command parse(String command) {
        String[] words = command.trim().split(" ");

        if (words.length > 0) 
        {
            if (MOVEMENT_COMMANDS.contains(words[0])) 
            {
                return new MovementCommand(words[0]);
            } 
            else if (words[0].equals(SAVE_COMMAND)) 
            {
                return new SaveCommand(words[0]);
            }
            else if (INVENTORY_COMMANDS.contains(words[0])) 
            {
                return new InventoryCommand();
            }
            else if (words[0].equals(TAKE_COMMAND)) 
            {
                if (words.length == 1)
                {
                    return new TakeCommand("");
                }
                return new TakeCommand(words[1].trim());
            }
            else if (words[0].equals(DROP_COMMAND)) 
            {
                if (words.length == 1)
                {
                    return new DropCommand("");
                }
                return new DropCommand(words[1].trim());
            }
            
            if (words.length > 1) 
            {
                ArrayList<String> items = GameState.instance().getInventoryNames();
                if (items.contains(words[1])) 
                {
                    return new ItemSpecifiedCommand(words[0], words[1]);
                }
            }
        }
        return new UnknownCommand(command);
    }

}
