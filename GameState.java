package mcsally_borkv3;
import java.util.*;
import java.io.*;
/**
 * The GameState class keeps track of the adventurer.
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016 
 */
public class GameState {
    
    public static class IllegalSaveFormatException extends Exception {
        public IllegalSaveFormatException(String e) {
            super(e);
        }
    }
    public static class NoItemException extends Exception {
        public NoItemException(String e) {
            super(e);
        }
    }
    
    static String DEFAULT_SAVE_FILE = "trinklev3";
    static String SAVE_FILE_EXTENSION = ".sav";
    static String SAVE_FILE_VERSION = "Bork v3.0 save data";

    static String CURRENT_ROOM_LEADER = "Current room: ";
    static String SAVE_FILE_ADVENTURER = "Adventurer:";
    static String SAVE_FILE_INVENTORY = "Inventory: ";

    private static GameState theInstance;
    private Dungeon dungeon;
    private Room adventurersCurrentRoom;
    private ArrayList<Item> items;

    /**
     * Creates a single instance of a gamestate
     */
    static synchronized GameState instance() {
        if (theInstance == null) {
            theInstance = new GameState();
        }
        return theInstance;
    }

    private GameState() {
    }

    /**
     * Restores from the given filename
     * 
     * @param filename the filename to restore the game from
     * @throws IllegalSaveFormatException, IllegalDungeonFormatException
     */
    void restore(String filename) throws FileNotFoundException,
        IllegalSaveFormatException, Dungeon.IllegalDungeonFormatException {
        Scanner s = new Scanner(new FileReader(filename));
        items = new ArrayList<Item>();

        if (!s.nextLine().equals(SAVE_FILE_VERSION)) 
        {
            throw new IllegalSaveFormatException("Save file not compatible.");
        }

        String dungeonFileLine = s.nextLine();

        if (!dungeonFileLine.startsWith(Dungeon.FILENAME_LEADER)) 
        {
            throw new IllegalSaveFormatException("No '" +
                Dungeon.FILENAME_LEADER + 
                "' after version indicator.");
        }

        dungeon = new Dungeon(dungeonFileLine.substring(Dungeon.FILENAME_LEADER.length()), false);
        dungeon.restoreState(s);

        String adventurer = s.nextLine();
        if (!adventurer.startsWith(SAVE_FILE_ADVENTURER)) 
        {
            throw new IllegalSaveFormatException("No '" +
                SAVE_FILE_ADVENTURER + 
                "' after rooms terminator.");
        }
        
        String currentRoomLine = s.nextLine();
        adventurersCurrentRoom = dungeon.getRoom(
            currentRoomLine.substring(CURRENT_ROOM_LEADER.length()));
            
        //Now deal with the Inventory, make sure there is a line!
        if (s.hasNextLine()) 
        {
            String inventory = s.nextLine();
            if (!inventory.startsWith(SAVE_FILE_INVENTORY)) 
            {
                throw new IllegalSaveFormatException("No '" +
                    SAVE_FILE_INVENTORY + 
                    "' after current room indicator.");
            }
            
            String[] parts = inventory.substring(SAVE_FILE_INVENTORY.length()).trim().split(",");
            for(int index=0; index < parts.length; index++) 
            {
                Item item = dungeon.getItem (parts[index]);
                if (item != null) 
                {
                    addToInventory(item);
                }
            }
        }
    }

    void store() throws IOException {
        store(DEFAULT_SAVE_FILE);
    }

    /**
     * Writes to a save file with the formatted save data
     * 
     * @param filename the filename to write to a new save file
     * @throws IllegalSaveFormatException
     */
    void store(String saveName) throws IOException {
        String filename = saveName + SAVE_FILE_EXTENSION;
        int counter = 0;
        
        PrintWriter w = new PrintWriter(new FileWriter(filename));
        w.println(SAVE_FILE_VERSION);
        dungeon.storeState(w);
        w.println(SAVE_FILE_ADVENTURER);
        w.println(CURRENT_ROOM_LEADER + getAdventurersCurrentRoom().getTitle());
        
        //Print out the inventory
        if (items.size() > 0) {
            w.print(SAVE_FILE_INVENTORY);
            for (Item item : items) {
                w.print(item.getPrimaryName());
                counter ++;
                if (counter < items.size()) {
                    w.print(",");
                }
            }
            w.println("");
        }
        w.close();
    }

    void initialize(Dungeon dungeon) {
        this.dungeon = dungeon;
        adventurersCurrentRoom = dungeon.getEntry();
        items = new ArrayList<Item>();
    }

    /**
     * Returns the room that the user is curently in
     * 
     * @return the current room that the user is in
     */
    Room getAdventurersCurrentRoom() {
        return adventurersCurrentRoom;
    }

    /**
     * Sets the user's current room
     * 
     * @param room the room to be set as the user's current room
     */
    void setAdventurersCurrentRoom(Room room) {
        adventurersCurrentRoom = room;
    }

    /**
     * Returns the dungeon that the user is in
     * 
     * @return the current Dungeon
     */
    Dungeon getDungeon() {
        return dungeon;
    }
    
    /**
     * Creates and returns an ArrayList if item names
     * 
     * @return ArrayList of String item names
     */
    ArrayList<String> getInventoryNames() {
        ArrayList<String> list= new ArrayList<String>();
        for (Item item : items) {
            list.add(item.getPrimaryName());
        }
        return list;
    }
    
    /**
     * Adds an Item to the inventory
     * 
     * @param item Item object to add to the inventory
     */
    void addToInventory(Item item) {
        items.add(item);    
    }

    /**
     * Removes an Item from the inventory
     * 
     * @param item Item object to remove from the inventory
     */
    void removeFromInventory(Item item) {
        items.remove(item);    
    }
    
    /**
     * Find an item in your current room based on the name given
     * 
     * @param name String name of the object
     * @return Item object based on the name given
     */
    Item getItemInVicinityNamed(String name) throws NoItemException {
        Item item = null;
        
        try {
            //See if you already have it first
            item = getItemFromInventoryNamed(name);
        }
        catch (NoItemException exception) {
            //Did not find it so keep going
        }

        if (item == null) {
            //Make sure item is at the room you are in
            item = getAdventurersCurrentRoom().getItemNamed(name);
        }
        
        if (item == null) {
            throw new NoItemException("There's no '" + name + "' found.");
        }
        return item;
    }

    /**
     * Return an item from your inventory given a name
     * 
     * @param name String name of the object
     * @return Item object based on name given
     */
    Item getItemFromInventoryNamed(String name) throws NoItemException {
        for (Item item : items) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        throw new NoItemException("You don't have a '" + name + "'.");
    }
}
