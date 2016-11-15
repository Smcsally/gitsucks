package mcsally_borkv3;

/**
 * Write a description of class ItemSpecificCommand here.
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class ItemSpecifiedCommand extends Command
{
    String verb = "";
    String noun = "";
    /**
     * Constructor for objects of class ItemSpecificCommand
     * 
     * @param verb An action for the item
     * @param noun A title for the item
     */
    public ItemSpecifiedCommand(String verb, String noun)
    {
        this.verb = verb;
        this.noun = noun;
    }
    
    /**
     * Returns a String based on the item passed into the constructor
     * 
     * @return Either info about the item or an exception
     */
    public String execute()
    {
       try {
           //Make sure item is at the room you are in
           Item item = GameState.instance().getItemInVicinityNamed(noun);
           //We should have an item now if we are here
           return item.getMessageForVerb(verb);
       }
       catch (GameState.NoItemException exception) {
           return exception.getMessage();
       }
    }
}
