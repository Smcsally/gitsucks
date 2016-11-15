package mcsally_borkv3;
import java.util.Scanner;
/**
 * The Exit class contains all information about an exit within Bork.
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class Exit {

    class NoExitException extends Exception {}

    private String dir;
    private Room src, dest;

    /**
     * Constructs an exit for a room
     * 
     * @param  dir  direction the exit is in from center of room
     * @param  src  room you are leaving
     * @param  dest  room you are going to
     */
    Exit(String dir, Room src, Room dest) {
        init();
        this.dir = dir;
        this.src = src;
        this.dest = dest;
        src.addExit(this);
    }

    /** Given a Scanner object positioned at the beginning of an "exit" file
        entry, read and return an Exit object representing it. 
        @param d The dungeon that contains this exit (so that Room objects 
        may be obtained.)
        @throws NoExitException The reader object is not positioned at the
        start of an exit entry. A side effect of this is the reader's cursor
        is now positioned one line past where it was.
        @throws IllegalDungeonFormatException A structural problem with the
        dungeon file itself, detected when trying to read this room.
     */
    Exit(Scanner s, Dungeon d) throws NoExitException,
        Dungeon.IllegalDungeonFormatException {

        init();
        String srcTitle = s.nextLine();
        if (srcTitle.equals(Dungeon.TOP_LEVEL_DELIM)) {
            throw new NoExitException();
        }
        src = d.getRoom(srcTitle);
        dir = s.nextLine();
        dest = d.getRoom(s.nextLine());
        
        // I'm an Exit object. Great. Add me as an exit to my source Room too,
        // though.
        src.addExit(this);

        // throw away delimiter
        if (!s.nextLine().equals(Dungeon.SECOND_LEVEL_DELIM)) {
            throw new Dungeon.IllegalDungeonFormatException("No '" +
                Dungeon.SECOND_LEVEL_DELIM + "' after exit.");
        }
    }

    // Common object initialization tasks.
    private void init() {
    }

    /**
     * Returns a string with a description of exits
     * 
     * @return String with directions to exits
     */
    String describe() {
        return "You can go " + dir + " to " + dest.getTitle() + ".";
    }

    /**
     * Returns a String of which direction the exit is in
     * 
     * @return String with direction to exit
     */
    String getDir() { return dir; }
    
    /**
     * Returns a Room associated with the exit
     * 
     * @return the Room which the exit is in
     */
    Room getSrc() { return src; }
    
    /**
     * Returns a Room which the exit is going to
     * 
     * @return the Room which the exit is leading to
     */
    Room getDest() { return dest; }
}
