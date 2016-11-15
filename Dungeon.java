package mcsally_borkv3;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
/**
 * The Dungeon class handles the map of the adventure land.
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class Dungeon {

    public static class IllegalDungeonFormatException extends Exception {
        public IllegalDungeonFormatException(String e) {
            super(e);
        }
    }

    // Variables relating to both dungeon file and game state storage.
    public static String TOP_LEVEL_DELIM = "===";
    public static String SECOND_LEVEL_DELIM = "---";

    // Variables relating to dungeon file (.bork) storage.
    public static String ITEMS_MARKER = "Items:";
    public static String ROOMS_MARKER = "Rooms:";
    public static String EXITS_MARKER = "Exits:";
    
    // Variables relating to game state (.sav) storage.
    static String FILENAME_LEADER = "Dungeon file: ";
    static String ROOM_STATES_MARKER = "Room states:";

    private String name;
    private Room entry;
    private Hashtable<String,Room> rooms;
    private Hashtable<String,Item> items;
    private String filename;

    /**
     * Constructs a new dungeon and sets Name and EntryRoom to the values passed in
     * 
     * @param entry The "entry" room where you always start
     * @param name The name of the dungeon
     */
    Dungeon(String name, Room entry) 
    {
        init();
        this.filename = null;    // null indicates not hydrated from file.
        this.name = name;
        this.entry = entry;
        init();
    }

    /**
     * Read from the .bork filename passed, and instantiate a Dungeon object
     * based on it.
     * 
     * @param filename String to read in from
     * @param initState Boolean to inititalize the dungeon or not
     */
    public Dungeon(String filename, Boolean initState) throws FileNotFoundException,IllegalDungeonFormatException 
    {
        init();
        this.filename = filename;
        Scanner s = new Scanner(new FileReader(filename));
        name = s.nextLine();

        s.nextLine();   // Throw away version indicator.

        // Throw away delimiter.
        if (!s.nextLine().equals(TOP_LEVEL_DELIM)) 
        {
            throw new IllegalDungeonFormatException("No '" +
                TOP_LEVEL_DELIM + "' after version indicator.");
        }

        // Throw away Items starter.
        if (!s.nextLine().equals(ITEMS_MARKER)) 
        {
            throw new IllegalDungeonFormatException("No '" +
                ITEMS_MARKER + "' line where expected.");
        }

        try 
        {
            // Instantiate and add items.
            while (true) 
            {
                add(new Item(s));
            }
        } catch (Item.NoItemException e) {  /* end of items */ }
        
        // Throw away Rooms starter.
        if (!s.nextLine().equals(ROOMS_MARKER)) 
        {
            throw new IllegalDungeonFormatException("No '" +
                ROOMS_MARKER + "' line where expected.");
        }

        try 
        {
            // Instantiate and add first room (the entry).
            entry = new Room(s, this, initState);
            add(entry);

            // Instantiate and add other rooms.
            while (true) 
            {
                add(new Room(s, this, initState));
            }
        } catch (Room.NoRoomException e) {  /* end of rooms */ }

        // Throw away Exits starter.
        if (!s.nextLine().equals(EXITS_MARKER)) 
        {
            throw new IllegalDungeonFormatException("No '" +
                EXITS_MARKER + "' line where expected.");
        }

        try 
        {
            // Instantiate exits.
            while (true) {
                Exit exit = new Exit(s, this);
            }
        } catch (Exit.NoExitException e) {  /* end of exits */ }

        s.close();
    }
    
    /**
     * Initialize the hashtables
     */
    private void init() {
        rooms = new Hashtable<String,Room>();
        items = new Hashtable<String,Item>();
    }

    /**
     * Creates a new save file with the passed in printWriter
     * 
     * @param printWriter to create a save file with the given printWriter
     */
    void storeState(PrintWriter w) throws IOException {
        w.println(FILENAME_LEADER + getFilename());
        w.println(ROOM_STATES_MARKER);
        for (Room room : rooms.values()) {
            room.storeState(w);
        }
        w.println(TOP_LEVEL_DELIM);
    }

    /**
     * Parses the save file with the give Scanner
     * 
     * @param sourceFile parses the savefile with the given scanner
     */
    void restoreState(Scanner s) throws GameState.IllegalSaveFormatException {

        // Note: the filename has already been read at this point.
        
        if (!s.nextLine().equals(ROOM_STATES_MARKER)) {
            throw new GameState.IllegalSaveFormatException("No '" +
                ROOM_STATES_MARKER + "' after dungeon filename in save file.");
        }

        String roomName = s.nextLine();
        while (!roomName.equals(TOP_LEVEL_DELIM)) {
            getRoom(roomName.substring(0,roomName.length()-1)).restoreState(s, this);
            roomName = s.nextLine();
        }
    }

    /**
     * Returns the entry room
     *
     * @return  the room you always enter in
     */
    public Room getEntry() { return entry; }
    
    /**
     * Returns the name of the dungeon
     *
     * @return  The name of the dungeon
     */
    public String getName() { return name; }
    
    /**
     * Returns the given filename
     * 
     * @return The given filename
     */
    public String getFilename() { return filename; }
    
    /**
     * Adds a room to the hashtable
     *
     * @param  room is a Room object
     */
    public void add(Room room) { rooms.put(room.getTitle(),room); }

    /**
     * GetRoom will return a Room object based on the title passed in
     *
     * @param  roomTitle   The title of the room you wish to return
     * @return the room associated with the title passed in
     */
    public Room getRoom(String roomTitle) {
        return rooms.get(roomTitle);
    }
    
    /**
     * Adds an item to the hashtable
     * 
     * @param item The item object that you want to add to the hashtable
     */
    public void add(Item item) { items.put(item.getPrimaryName(),item); }

    /**
     * Returns the item of a certain name
     * 
     * @param primaryName String name of the item you want
     * @return Item of the given primary name
     */
    public Item getItem(String primaryName) {
        return items.get(primaryName);
    }
}
