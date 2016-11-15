package mcsally_borkv3;
/**
 * Handles all save commands given in game
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2106
 */
public class SaveCommand extends Command
{
    private String saveFilename;

    /**
     * Constructor for objects of class SaveCommand
     * 
     * @param filename String of given "save" command
     */
    public SaveCommand(String filename)
    {
        this.saveFilename = filename;
    }
    
    /**
     * If given filename is "save", then save. Else, throw an error
     * 
     * @return String with either successful save or error
     */
    public String execute()
    {
        try {
            GameState.instance().store();
            return "Data saved to " + GameState.DEFAULT_SAVE_FILE + 
            GameState.SAVE_FILE_EXTENSION + ".\n";
        } catch (Exception e) {
            System.err.println("Couldn't save!");
            e.printStackTrace();
            return "";
        }
    }
}
