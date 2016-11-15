package mcsally_borkv3;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * Rooms are individual areas that the player can enter
 *  
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class Room {
    class NoRoomException extends Exception {}
    private String title;
    private String desc;
    private boolean beenHere;
    private ArrayList<Exit> exits;
    private ArrayList<Item> items;
    
    /**
     * Constructs a room with a name passed in
     * 
     * @param title Name of the room
     */
    Room(String title) {
        init();
        this.title = title;
    }
    
    /** Given a Scanner object positioned at the beginning of a "room" file
        entry, read and return a Room object representing it. 
        @throws NoRoomException The reader object is not positioned at the
        start of a room entry. A side effect of this is the reader's cursor
        is now positioned one line past where it was.
        @throws IllegalDungeonFormatException A structural problem with the
        dungeon file itself, detected when trying to read this room.
     */
    Room(Scanner s, Dungeon dungeon, Boolean initState) throws NoRoomException,
        Dungeon.IllegalDungeonFormatException {

        init();
        title = s.nextLine();
        desc = "";
        if (title.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoRoomException();
        }
        
        String lineOfDesc = s.nextLine();
        //Find the contents now
        if (lineOfDesc.startsWith("Contents:")) {
            if (initState) {
                String[] parts = lineOfDesc.substring(9).trim().split(",");
                for(int index=0; index < parts.length; index++) {
                    Item item = dungeon.getItem (parts[index]);
                    add(item);
                }
            }

            lineOfDesc = s.nextLine();
        }
        
        while (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM) &&
               !lineOfDesc.equals(Dungeon.TOP_LEVEL_DELIM)) {
            desc += lineOfDesc + "\n";
            lineOfDesc = s.nextLine();
        }

        // throw away delimiter
        if (!lineOfDesc.equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                Dungeon.SECOND_LEVEL_DELIM + "' after room.");
        }
    }

    /**
     * Initialize the hashtable
     */
    private void init() {
        exits = new ArrayList<Exit>();
        items = new ArrayList<Item>();
        beenHere = false;
    }

    /**
     * Returns the name of a given room
     * 
     * @return the name of the room
     */
    String getTitle() { return title; }

    /**
     * Sets the description of the room
     * 
     * @param desc the String description of the room
     */
    void setDesc(String desc) { this.desc = desc; }

    /*
     * Store the current (changeable) state of this room to the writer
     * passed.
     */
    void storeState(PrintWriter w) throws IOException {
        // At this point, nothing to save for this room if the user hasn't
        // visited it.
        int counter = 0;
        w.println(title + ":");
        w.println("beenHere=" + beenHere);

        //Print out the contents
        w.print("Contents: ");
        for (Item item : items) {
            w.print(item.getPrimaryName());
            counter ++;
            if (counter < items.size()) {
                w.print(",");
            }
        }  
        w.println("");
        w.println(Dungeon.SECOND_LEVEL_DELIM);
    }

    void restoreState(Scanner s, Dungeon dungeon) throws GameState.IllegalSaveFormatException {

        String line = s.nextLine();
        if (!line.startsWith("beenHere")) {
            throw new GameState.IllegalSaveFormatException("No beenHere.");
        }
        beenHere = Boolean.valueOf(line.substring(line.indexOf("=")+1));

        line = s.nextLine();
        //Find the contents now
        if (line.startsWith("Contents:")) {
            String[] parts = line.substring(9).trim().split(",");
            for(int index=0; index < parts.length; index++) {
                Item item = dungeon.getItem (parts[index]);
                if (item != null) {
                    add(item);
                }
            }
        }
        
        s.nextLine();   // consume end-of-room delimiter
    }

    /**
     * Prints the description of the room if it is the first time visiting that room
     * 
     * @return If first time visited, returns a String description of the room
     */
    public String describe() {
        String description;
        if (beenHere) {
            description = title;
        } else {
            description = title + "\n" + desc;
        }
        for (Exit exit : exits) {
            description += "\n" + exit.describe();
        }

        
        for (Item item : items) {
            description += "\nThere is a " + item.getPrimaryName() + " here.  " ;
        }
        beenHere = true;
        return description.trim();
    }
    
    /**
     * Returns the room that is reachable in given direction
     * 
     * @return Room that is in provided direction
     */
    public Room leaveBy(String dir) {
        for (Exit exit : exits) {
            if (exit.getDir().equals(dir)) {
                return exit.getDest();
            }
        }
        return null;
    }

    /**
     * Adds exits to the list of existing exits
     */
    public void addExit(Exit exit) {
        exits.add(exit);
    }
    
    /**
     * Add the given item to the ArrayList
     * 
     * @param item Item given to add to the ArrayList
     */
    public void add(Item item) {
        items.add(item);
    }

    /**
     * Remove the given item from the ArrayList
     * 
     * @param item Item given to be removed from the ArrayList
     */
    public void remove(Item item) {
        items.remove(item);
    }
    
    /**
     * Returns an item that matches the name given
     * 
     * @param name String name to match to an item
     * @return Item that matches given name
     */
    public Item getItemNamed(String name) {
        for (Item item : items) {
            if (item.goesBy(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * returns the ArrayList of Items
     * 
     * @return An ArrayList of Items
     */
    public ArrayList<Item> getContents () {
        return items;
    }
}
