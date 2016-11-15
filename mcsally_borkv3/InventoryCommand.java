package mcsally_borkv3;
import java.util.*;
/**
 * Handles all commands dealing with the inventory
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class InventoryCommand  extends Command
{
    /**
     * Constructor for InventoryCommand
     */
    public InventoryCommand()
    {
    }

    /**
     * If there are items in the ArrayList, it prints out your inventory, if empty, print a message
     * 
     * @return String of your inventory
     */
    public String execute()
    {
        String inventory = "You are carrying: ";
        ArrayList<String> names = GameState.instance().getInventoryNames();
        if (names.size() > 0) 
        {
            for (String s : names)
            {
                inventory += "A " + s + " ";
            }
        }
        else 
        {
            inventory = "You are empty handed.";
        }
        return inventory;
    }

    
}
