package mcsally_borkv3;


/**
 * Handles all drop commands in the game
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class DropCommand extends Command
{
    private String itemName;

    /**
     * Constructor for objects of class TakeCommand
     * 
     * @param itemName String name of the item you want to drop
     */
    public DropCommand(String itemName)
    {
        this.itemName = itemName;
    }

    /**
     * If you are given no item name, return a string. If you are given a valid name, return a different string
     * 
     * @return String depending on situation
     */
    public String execute()
    {
        String message = "Item '" + itemName + "' dropped.";
        if(itemName.equals(""))
        {
            message = "Drop what?";
        }
        else
        {
            try {
                Item item = GameState.instance().getItemFromInventoryNamed(itemName);
                GameState.instance().removeFromInventory(item);
                GameState.instance().getAdventurersCurrentRoom().add(item);
            }
            catch (GameState.NoItemException exception) {
                return exception.getMessage();
            }
        }
        return message;
    }
}
