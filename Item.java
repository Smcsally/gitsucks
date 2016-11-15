package mcsally_borkv3;
import java.util.*;
import java.io.*;
/**
 * Items found in certain rooms that can be picked up or dropped
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class Item
{
    class NoItemException extends Exception {}
    
    private String primaryName;
    private int weight;
    private Hashtable<String, String> messages;
    /**
     * Constructor for objects of class Item
     * 
     * @param s Scanner passed in for hydration
     * @throws IllegalDungeonFormationException
     * @throws NoItemException
     */
    public Item(Scanner s) throws Dungeon.IllegalDungeonFormatException, NoItemException
    {
        init();
        primaryName = s.nextLine();
        
        if (primaryName.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoItemException();
        }

        weight = Integer.parseInt(s.nextLine());

        String itemStr = s.nextLine();
        while (!itemStr.equals(Dungeon.SECOND_LEVEL_DELIM) &&
               !itemStr.equals(Dungeon.TOP_LEVEL_DELIM)) {
            
            //break the message read into verb and message
            String verb = itemStr.substring(0, itemStr.indexOf(':'));
            String message = itemStr.substring(itemStr.indexOf(':') + 1);
            messages.put(verb, message);
            
            itemStr = s.nextLine();
        }

        // throw away delimiter
        if (!itemStr.equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                Dungeon.SECOND_LEVEL_DELIM + "' after items.");
        }        
    }
    
    /**
     * initialize the hashtable
     */
    private void init() 
    {
        messages = new Hashtable<String,String>();
    }
    
    /**
     * Returns true if an item goes by the certain name given
     * 
     * @param name String to compare to
     * @return boolean return true if the item goes by the given name
     */
    public boolean goesBy(String name)
    {
        return name.equals(primaryName);
    }
    
    /**
     * returns the primary name of the item
     * 
     * @returns String primary name of the item
     */
    public String getPrimaryName()
    {
        return primaryName;
    }
    
    /**
     * Checks the hashtable for the given verb, if found, return that verb
     * 
     * @param verb The verb to look for
     * @return The string message associated with that verb
     */
    public String getMessageForVerb(String verb)
    {
        if ( messages.containsKey(verb)) {
            return messages.get(verb);
        }
        else {
            return "You can't " + verb + " the " + primaryName;
        }
    }
    
    public String toString()
    {
        return primaryName;
    }
}
