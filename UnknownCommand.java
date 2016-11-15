package mcsally_borkv3;

/**
 * Handles all unknown commands in game
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class UnknownCommand extends Command
{
    private String bogusCommand;

    /**
     * Constructor for objects of class UnknownCommand
     * 
     * @param an unknown command passed in
     */
    public UnknownCommand(String bogusCommand)
    {
        this.bogusCommand = bogusCommand;
    }
    
    /**
     * Returns a string if an unknown command is given
     */
    public String execute()
    {
        return "Unknown command: " + this.bogusCommand + "\n";
    }
}
