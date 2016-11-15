package mcsally_borkv3;
import java.util.Scanner;
/**
 * The Interpreter class is the main class for Bork.
 * 
 * @author Shane McSally
 * @version Version 3 10/23/2016
 */
public class Interpreter {
    private static GameState state; // not strictly necessary; GameState is 
                                    // singleton
    public static String USAGE_MSG = "Usage: Interpreter borkFile.bork|saveFile.sav.";
    /**
     * Main method that loops the "UI" of the game
     * 
     * @param args[] A string array of args
     */
    public static void main(String args[]) {
        if (args.length < 1) {
            System.err.println(USAGE_MSG);
            System.exit(1);
        }

        String command;
        Scanner commandLine = new Scanner(System.in);

        try {
            state = GameState.instance();
            if (args[0].endsWith(".bork")) {
                state.initialize(new Dungeon(args[0], true));
                System.out.println("\nWelcome to " + 
                    state.getDungeon().getName() + "!");
            } else if (args[0].endsWith(".sav")) {
                state.restore(args[0]);
                System.out.println("\nWelcome back to " + 
                    state.getDungeon().getName() + "!");
            } else {
                System.err.println(USAGE_MSG);
                System.exit(2);
            }

            System.out.print("\n" + 
                state.getAdventurersCurrentRoom().describe() + "\n");

            command = promptUser(commandLine);

            while (!command.equals("q")) {

                System.out.print(CommandFactory.instance().parse(command).execute());

                command = promptUser(commandLine);
            }

            System.out.println("Bye!");

        } catch(Exception e) { 
            e.printStackTrace(); 
        }
    
    }

    /**
     * Prompts the user to enter a line of text
     * 
     * @param commandLine Scanner to get user input
     * @param output allows for outputs other than >
     */
    private static String promptUser(Scanner commandLine) {

        System.out.print("> ");
        return commandLine.nextLine();
    }

}
