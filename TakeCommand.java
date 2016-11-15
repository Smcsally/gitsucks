package mcsally_borkv3;
import java.util.*;

/**
 * Handles all take commands in the game
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class TakeCommand extends Command
{
    private String itemName;

    /**
     * Constructor for objects of class TakeCommand
     * 
     * @param itemName Name of the item you want to take
     */
    public TakeCommand(String itemName)
    {
        this.itemName = itemName;
    }

    /**
     * If you are given no item name, return a string. If you are given a valid name, return a different string
     * 
     * @return String based on the situation
     */
    public String execute()
    {
        Item item = null;
        String message = "Item '" + itemName + "' taken.";
        if(itemName.equals(""))
        {
            message = "Take what?";
        }
        else{

            try {
                //See if the item is in the room or your inventory
                item = GameState.instance().getItemInVicinityNamed(itemName);

                //See if its in your inventory
                ArrayList<String> items = GameState.instance().getInventoryNames();
                if (items.contains(itemName)) {
                    message = "You already have the " + itemName + ".";
                }
                else {
                    GameState.instance().addToInventory(item);
                    GameState.instance().getAdventurersCurrentRoom().remove(item);
                }
            }
            catch (GameState.NoItemException exception) {
                message = exception.getMessage();
            }
        }

        return message;
    }  
}
