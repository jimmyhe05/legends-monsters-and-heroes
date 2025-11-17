package legends.game;

import legends.entities.heroes.Hero;
import legends.entities.heroes.Paladin;
import legends.entities.heroes.Sorcerer;
import legends.entities.heroes.Warrior;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class representing the Legends game.
 */
public class Game {

    private Board board;
    private static final int DEFAULT_BOARD_SIZE = 8;
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 12;
    private final List<Hero> party;
    private boolean running;
    private final Scanner in;

    private List<Warrior> allWarriors;
    private List<Paladin> allPaladins;
    private List<Sorcerer> allSorcerers;

    public Game() {
        this.party = new ArrayList<Hero>();
        this.in = new Scanner(System.in);
        this.running = false;
    }

    /**
     * Called by Main to start a new game.
     */
    public void startNewGame() {
        setup();
        run();
    }

    /**
     * Setup phase:
     * - load data
     * - hero selection
     * - create the board
     */
    private void setup() {
        System.out.println("===========================================");
        System.out.println("   WELCOME TO LEGENDS: MONSTERS & HEROES   ");
        System.out.println("===========================================");
        System.out.println("Lead a party of heroes, explore the land, visit markets,");
        System.out.println("and fight terrifying monsters in turn-based battles.\n");

        loadHeroData();
        chooseHeroes();

        int size = askBoardSize();
        board = new Board(size); 
        running = true;
    }

    /**
     * Ask the user for the desired board size.
     * 
     * @return the chosen board size
     */
    private int askBoardSize() {
        System.out.println("Choose board size (NxN).");
        System.out.println("Press ENTER for default " + DEFAULT_BOARD_SIZE + "x" + DEFAULT_BOARD_SIZE + ".");
        System.out.print("Or enter a size between " + MIN_BOARD_SIZE + " and " + MAX_BOARD_SIZE + ": ");

        String line = in.nextLine().trim();
        if (line.isEmpty()) {
            return DEFAULT_BOARD_SIZE;
        }

        try {
            int value = Integer.parseInt(line);
            if (value < MIN_BOARD_SIZE || value > MAX_BOARD_SIZE) {
                System.out.println("Out of range, using default " + DEFAULT_BOARD_SIZE + ".");
                return DEFAULT_BOARD_SIZE;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, using default " + DEFAULT_BOARD_SIZE + ".");
            return DEFAULT_BOARD_SIZE;
        }
    }

    /**
     * Load hero data from files.
     */
    private void loadHeroData() {
        String base = "data/"; 

        allWarriors = DataLoader.loadWarriors(base + "heroes/Warriors.txt");
        allPaladins = DataLoader.loadPaladins(base + "heroes/Paladins.txt");
        allSorcerers = DataLoader.loadSorcerers(base + "heroes/Sorcerers.txt");

        System.out.println("Loaded heroes:");
        System.out.println("  Warriors: " + allWarriors.size());
        System.out.println("  Paladins: " + allPaladins.size());
        System.out.println("  Sorcerers: " + allSorcerers.size());
        System.out.println();
    }

    /**
     * Let the user choose 1–3 heroes for their party.
     */
    private void chooseHeroes() {
        System.out.println("Choose 1–3 heroes for your party.");
        System.out.println("---------------------------------");

        System.out.println("Hero types:");
        System.out.println("  Warrior  - Favored on strength and agility.");
        System.out.println("  Sorcerer - Favored on dexterity and agility.");
        System.out.println("  Paladin  - Favored on strength and dexterity.\n");

        while (true) {
            if (party.size() == 3) {
                System.out.println("You already have 3 heroes. Type 4 to finish.");
            }

            System.out.println("1. Add Warrior");
            System.out.println("2. Add Paladin");
            System.out.println("3. Add Sorcerer");
            System.out.println("4. Finish selection");
            System.out.println("5. Remove hero from party");
            System.out.print("Enter choice: ");

            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number 1–5.");
                continue;
            }

            if (choice == 4) {
                if (party.size() == 0) {
                    System.out.println("You must select at least 1 hero.");
                } else {
                    break;
                }
            } else if (choice == 5) {
                removeHeroFromParty();
            } else if (party.size() >= 3) {
                System.out.println("Party is full, type 4 to finish.");
            } else {
                switch (choice) {
                    case 1:
                        pickHeroFromList(allWarriors);
                        break;
                    case 2:
                        pickHeroFromList(allPaladins);
                        break;
                    case 3:
                        pickHeroFromList(allSorcerers);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }

        printPartySummary();
    }

    /**
     * Let the user pick a hero from the given list to add to their party.
     * 
     * @param list the list of heroes to choose from
     */
    private <T extends Hero> void pickHeroFromList(List<T> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No heroes available of this type.");
            return;
        }

        while (true) {
            System.out.println("\nAvailable heroes:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
            System.out.print("Choose hero index (or 0 to cancel): ");

            String line = in.nextLine().trim();
            int idx;
            try {
                idx = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid index. Please enter a number.");
                continue;
            }

            if (idx == 0) {
                System.out.println("Cancelled hero selection.");
                return;
            }

            if (idx < 1 || idx > list.size()) {
                System.out.println("Index out of range. Try again.");
                continue;
            }

            Hero chosen = list.get(idx - 1);

            // Ensure we don't add the same hero twice
            if (party.contains(chosen)) {
                System.out.println("That hero is already in your party. Choose a different hero.");
                continue;
            }

            party.add(chosen);
            System.out.println(chosen.getName() + " (" + chosen.getClass().getSimpleName() + ") joined the party!\n");
            printPartySummary();
            return;
        }
    }

    /**
     * Remove a hero from the current party by index.
     */
    private void removeHeroFromParty() {
        if (party.isEmpty()) {
            System.out.println("Your party is currently empty.");
            return;
        }

        System.out.println("\nCurrent party:");
        for (int i = 0; i < party.size(); i++) {
            Hero h = party.get(i);
            System.out.println((i + 1) + ". " + h.getName() + " (" + h.getClass().getSimpleName() + ")");
        }
        System.out.print("Enter index of hero to remove (or 0 to cancel): ");

        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }

        if (idx == 0) {
            System.out.println("Removal cancelled.");
            return;
        }

        if (idx < 1 || idx > party.size()) {
            System.out.println("Index out of range.");
            return;
        }

        Hero removed = party.remove(idx - 1);
        System.out.println(removed.getName() + " was removed from the party.");
        printPartySummary();
    }

    /**
     * Print a concise summary of the current party.
     */
    private void printPartySummary() {
        System.out.println("\nYour party (" + party.size() + " hero" + (party.size() == 1 ? "" : "es") + "):");
        if (party.isEmpty()) {
            System.out.println("  [empty]");
        } else {
            for (Hero h : party) {
                System.out.println("  - " + h.getName() + " (" + h.getClass().getSimpleName() + ")");
            }
        }
        System.out.println();
    }

    /**
     * Main game loop:
     * - display map
     * - handle WASD / I / M / Q
     */
    private void run() {
        if (party.isEmpty()) {
            System.out.println("No party selected. Exiting game.");
            return;
        }

        while (running) {
            board.display();
            printControls();
            System.out.print("Enter command: ");

            String line = in.nextLine().trim().toUpperCase();
            if (line.isEmpty()) {
                continue;
            }
            char cmd = line.charAt(0);

            switch (cmd) {
                case 'W':
                    handleMove('W');
                    break;
                case 'A':
                    handleMove('A');
                    break;
                case 'S':
                    handleMove('S');
                    break;
                case 'D':
                    handleMove('D');
                    break;
                case 'I':
                    showPartyInfo();
                    break;
                case 'M':
                    enterMarketIfPossible();
                    break;
                case 'Q':
                    running = false;
                    System.out.println("Quitting game. Goodbye!");
                    break;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    /**
     * Print available controls to the user.
     */
    private void printControls() {
        System.out.println("Controls: W/A/S/D to move | I: info | M: market | Q: quit");
    }

    /**
     * Handle movement commands.
     * 
     * @param direction the direction character ('W', 'A', 'S', 'D')
     */
    private void handleMove(char direction) {
        boolean moved = false;
        switch (direction) {
            case 'W':
                moved = board.moveUp();
                break;
            case 'A':
                moved = board.moveLeft();
                break;
            case 'S':
                moved = board.moveDown();
                break;
            case 'D':
                moved = board.moveRight();
                break;
            default:
                return;
        }

        if (!moved) {
            return;
        }

        Tile tile = board.getCurrentTile();
        if (tile.hasMarket()) {
            System.out.println("You stepped on a MARKET tile. Press 'M' to enter.");
        } else {
            System.out.println("You are on a COMMON tile.");
            maybeTriggerBattle();
        }
    }

    /**
     * Show detailed info about the current party.
     */
    private void showPartyInfo() {
        System.out.println("=== Party Info ===");
        for (Hero h : party) {
            System.out.println(h);
        }
        System.out.println("==================");
    }

    /**
     * Enter the market if the current tile has one.
     */
    private void enterMarketIfPossible() {
        Tile tile = board.getCurrentTile();
        if (!tile.hasMarket()) {
            System.out.println("You are not on a market tile.");
            return;
        }

        System.out.println("Entering market...");
        // TODO: call Market system when it’s implemented
    }

    /**
     * Maybe trigger a battle when on a common tile.
     */
    private void maybeTriggerBattle() {
        double encounterChance = 0.3; // 30%
        if (Math.random() < encounterChance) {
            System.out.println("A group of monsters appears!");
            // TODO: create monsters and call Battle class
        }
    }
}
