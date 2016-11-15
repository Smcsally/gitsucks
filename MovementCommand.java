package mcsally_borkv3;

/**
 * Handles all movement commands given in game
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class MovementCommand extends Command
{
    private String direction;
    /**
     * Constructor for objects of class MovementCommand
     * 
     * @param A string direction to go in
     */
    public MovementCommand(String direction)
    {
        this.direction = direction;
    }

    /**
     * Returns a String based on the direction passed to the constructor
     * 
     * @return Either a string description of the room or an error
     */
    public String execute()
    {
        Room currentRoom = GameState.instance().getAdventurersCurrentRoom();
        Room nextRoom = currentRoom.leaveBy(direction);
        if (nextRoom != null) 
        {  
            GameState.instance().setAdventurersCurrentRoom(nextRoom);
            return "\n" + nextRoom.describe() + "\n";
        } 
        else 
        {
            return "You can't go " + direction + ".\n";
        }
    }
}
