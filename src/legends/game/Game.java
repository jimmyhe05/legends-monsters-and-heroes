package legends.game;

import legends.entities.heroes.Hero;
import legends.entities.heroes.Paladin;
import legends.entities.heroes.Sorcerer;
import legends.entities.heroes.Warrior;
import legends.entities.monsters.Monster;
import legends.entities.monsters.Dragon;
import legends.entities.monsters.Spirit;
import legends.entities.monsters.Exoskeleton;
import legends.game.DataLoader;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Weapon;
import legends.utilities.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    private final Random rand;
    private Market market;

    private List<Warrior> allWarriors;
    private List<Paladin> allPaladins;
    private List<Sorcerer> allSorcerers;

    private List<Dragon> allDragons;
    private List<Spirit> allSpirits;
    private List<Exoskeleton> allExoskeletons;

    public Game() {
        this.party = new ArrayList<Hero>();
        this.in = new Scanner(System.in);
        this.running = false;
        this.rand = new Random();
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
        System.out.println(Color.title("==========================================="));
        System.out.println(Color.title("   WELCOME TO LEGENDS: MONSTERS & HEROES   "));
        System.out.println(Color.title("==========================================="));
        System.out.println(Color.warning("Lead a party of heroes, explore the land, visit markets,"));
        System.out.println(Color.warning("and fight terrifying monsters in turn-based battles.\n"));

        loadHeroData();
        loadMonsterData();
        chooseHeroes();

        // Initialize market (loads item data once)
        market = new Market();

        int size = askBoardSize();
        board = new Board(size);
        running = true;
    }

    /**
     * Ask the user for the desired board size.
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
     * Load monster data from files.
     */
    private void loadMonsterData() {
        String base = "data/";

        allDragons = DataLoader.loadDragons(base + "monsters/Dragons.txt");
        allSpirits = DataLoader.loadSpirits(base + "monsters/Spirits.txt");
        allExoskeletons = DataLoader.loadExoskeletons(base + "monsters/Exoskeletons.txt");

        System.out.println("Loaded monsters:");
        System.out.println("  Dragons: " + allDragons.size());
        System.out.println("  Spirits: " + allSpirits.size());
        System.out.println("  Exoskeletons: " + allExoskeletons.size());
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
            System.out.println(chosen.getDisplayName() + " (" + chosen.getClass().getSimpleName() + ") joined the party!\n");
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
            System.out.println((i + 1) + ". " + h.getDisplayName() + " (" + h.getClass().getSimpleName() + ")");
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
        System.out.println(removed.getDisplayName() + " was removed from the party.");
            printPartySummary();
    }

    /**
     * Print a concise summary of the current party.
     */
    private void printPartySummary() {
        System.out.println(Color.title("\nYour party (" + party.size() + " hero" + (party.size() == 1 ? "" : "es") + "):"));
        if (party.isEmpty()) {
            System.out.println(Color.warning("  [empty]"));
        } else {
            for (Hero h : party) {
                System.out.println("  - " + h.getDisplayName() + " (" + h.getClass().getSimpleName() + ")");
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
			System.out.println(Color.error("No party selected. Exiting game."));
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
                case 'E':
                    openPartyManagementMenu();
                    break;
                case 'M':
                    enterMarketIfPossible();
                    break;
                case 'Q':
                    running = false;
					System.out.println(Color.warning("Quitting game. Goodbye!"));
                    break;
                default:
					System.out.println(Color.error("Unknown command."));
            }
        }
    }

    /**
     * Print available controls to the user.
     */
    private void printControls() {
		System.out.println(Color.title("Controls: ") +
				Color.CYAN + "W/A/S/D" + Color.RESET + " to move | " +
				"I: info | E: equip/use | M: market | Q: quit");
    }

    /**
     * Open an out-of-battle party management menu for equipment and potions.
     */
    private void openPartyManagementMenu() {
        while (true) {
            System.out.println(Color.title("\n=== Party Management ==="));
            for (int i = 0; i < party.size(); i++) {
                Hero h = party.get(i);
                System.out.println("" + (i + 1) + ". " + h.getDisplayName());
            }
            System.out.println("0. Back to game");
            System.out.print("Choose hero: ");

            String line = in.nextLine().trim();
            int idx;
            try {
                idx = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(Color.error("Invalid index."));
                continue;
            }

            if (idx == 0) {
                return;
            }
            if (idx < 1 || idx > party.size()) {
                System.out.println(Color.error("Index out of range."));
                continue;
            }

            Hero chosen = party.get(idx - 1);
            manageHeroOutsideBattle(chosen);
        }
    }

    /**
     * Menu to equip items or use potions for a single hero.
     * 
     * @param hero the hero to manage
     */
    private void manageHeroOutsideBattle(Hero hero) {
        while (true) {
            System.out.println(Color.title("\n-- Manage " + hero.getDisplayName() + " --"));
            System.out.println("1. Equip weapon");
            System.out.println("2. Equip armor");
            System.out.println("3. Use potion");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            String line = in.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            char choice = line.charAt(0);
            switch (choice) {
                case '1':
                    equipWeaponOutsideBattle(hero);
                    break;
                case '2':
                    equipArmorOutsideBattle(hero);
                    break;
                case '3':
                    usePotionOutsideBattle(hero);
                    break;
                case '0':
                    return;
                default:
                    System.out.println(Color.error("Invalid choice."));
            }
        }
    }

    /**
     * Equip a weapon for the given hero outside of battle.
     * 
     * @param hero the hero to equip a weapon
     */
    private void equipWeaponOutsideBattle(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Weapon> weapons = inv.getWeapons();
        if (weapons.isEmpty()) {
            System.out.println(Color.warning("No weapons in inventory."));
            return;
        }
        System.out.println(Color.title("Available weapons:"));
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.println("" + (i + 1) + ". " + w.getName() + " (DMG=" + w.getDamage() + ")");
        }
        System.out.print("Choose weapon index (0 to cancel): ");
        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println(Color.error("Invalid index."));
            return;
        }
        if (idx == 0) {
            return;
        }
        if (idx < 1 || idx > weapons.size()) {
            System.out.println(Color.error("Index out of range."));
            return;
        }
        Weapon chosen = weapons.get(idx - 1);
        hero.equipWeapon(chosen);
        System.out.println(Color.success(hero.getDisplayName() + " equipped " + chosen.getName() + "."));
    }

    /**
     * Equip armor for the given hero outside of battle.
     * 
     * @param hero the hero to equip armor
     */
    private void equipArmorOutsideBattle(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Armor> armors = inv.getArmors();
        if (armors.isEmpty()) {
            System.out.println(Color.warning("No armor in inventory."));
            return;
        }
        System.out.println(Color.title("Available armors:"));
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.println("" + (i + 1) + ". " + a.getName() + " (DEF=" + a.getDamageReduction() + ")");
        }
        System.out.print("Choose armor index (0 to cancel): ");
        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println(Color.error("Invalid index."));
            return;
        }
        if (idx == 0) {
            return;
        }
        if (idx < 1 || idx > armors.size()) {
            System.out.println(Color.error("Index out of range."));
            return;
        }
        Armor chosen = armors.get(idx - 1);
        hero.equipArmor(chosen);
        System.out.println(Color.success(hero.getDisplayName() + " equipped " + chosen.getName() + "."));
    }

    /**
     * Use a potion for the given hero outside of battle.
     * 
     * @param hero the hero to use a potion
     */
    private void usePotionOutsideBattle(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Potion> potions = inv.getPotions();
        if (potions.isEmpty()) {
            System.out.println(Color.warning("No potions in inventory."));
            return;
        }
        System.out.println(Color.title("Available potions:"));
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.println("" + (i + 1) + ". " + p.getName());
        }
        System.out.print("Choose potion index (0 to cancel): ");
        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println(Color.error("Invalid index."));
            return;
        }
        if (idx == 0) {
            return;
        }
        if (idx < 1 || idx > potions.size()) {
            System.out.println(Color.error("Index out of range."));
            return;
        }
        Potion chosen = potions.get(idx - 1);
        hero.usePotion(chosen);
        System.out.println(Color.success(hero.getDisplayName() + " used " + chosen.getName() + "."));
    }

    /**
     * Handle movement commands.
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
			System.out.println(Color.success("You stepped on a MARKET tile. Press 'M' to enter."));
        } else {
			System.out.println(Color.warning("You are on a COMMON tile."));
            maybeTriggerBattle();
        }
    }

    /**
     * Show detailed info about the current party.
     */
    private void showPartyInfo() {
		System.out.println(Color.title("=== Party Info ==="));
        for (Hero h : party) {
            System.out.println(h);
        }
		System.out.println(Color.title("=================="));
    }

    /**
     * Enter the market if the current tile has one.
     */
    private void enterMarketIfPossible() {
        Tile tile = board.getCurrentTile();
        if (!tile.hasMarket()) {
			System.out.println(Color.error("You are not on a market tile."));
            return;
        }
		System.out.println(Color.success("Entering market..."));
        if (market == null) {
            market = new Market();
        }
        market.run(party, in);
    }

    /**
     * Maybe trigger a battle when on a common tile.
     */
    private void maybeTriggerBattle() {
        double encounterChance = 0.3; // 30%
        if (Math.random() >= encounterChance) {
            return;
        }

		System.out.println(Color.warning("A group of monsters appears!"));

        List<Monster> encounter = createEncounter();
        if (encounter.isEmpty()) {
            System.out.println("No monsters available to fight.");
            return;
        }

        Battle battle = new Battle(party, encounter);
        battle.start();

        // If all heroes fainted, end the game.
        if (allHeroesFainted()) {
			System.out.println(Color.error("Your entire party has fallen..."));
            running = false;
        }
    }

    /**
     * Create a monster encounter scaled to the party.
     * Simple rule: one monster per hero, at max hero level.
     */
    private List<Monster> createEncounter() {
        List<Monster> result = new ArrayList<Monster>();

        if ((allDragons == null || allDragons.isEmpty()) &&
            (allSpirits == null || allSpirits.isEmpty()) &&
            (allExoskeletons == null || allExoskeletons.isEmpty())) {
            return result;
        }

        int maxLevel = getMaxHeroLevel();
        int count = party.size();

        for (int i = 0; i < count; i++) {
            Monster m = createRandomMonsterForLevel(maxLevel);
            if (m != null) {
                result.add(m);
            }
        }

        return result;
    }

    /**
     * Get the maximum level among all heroes in the party.
     * @return the highest hero level
     */
    private int getMaxHeroLevel() {
        int max = 1;
        for (Hero h : party) {
            if (h.getLevel() > max) {
                max = h.getLevel();
            }
        }
        return max;
    }

    /**
     * Create a single random monster for a given level
     * by sampling from loaded monsters and building a fresh instance.
     * @param level the desired monster level
     * @return a new Monster instance, or null if none could be created
     */
    private Monster createRandomMonsterForLevel(int level) {
        // 0 = Dragon, 1 = Spirit, 2 = Exoskeleton
        for (int attempts = 0; attempts < 10; attempts++) {
            int type = rand.nextInt(3);

            switch (type) {
                case 0:
                    if (allDragons != null && !allDragons.isEmpty()) {
                        Dragon protoD = allDragons.get(rand.nextInt(allDragons.size()));
                        // For very low-level parties, avoid the most extreme dragons
                        if (level <= 2 && (protoD.getBaseDamage() > 500 || protoD.getDefense() > 600)) {
                            break; // pick another monster type
                        }
                        return new Dragon(
                                protoD.getName(),
                                level,
                                protoD.getBaseDamage(),
                                protoD.getDefense(),
                                protoD.getDodgeChance()
                        );
                    }
                    break;
                case 1:
                    if (allSpirits != null && !allSpirits.isEmpty()) {
                        Spirit protoS = allSpirits.get(rand.nextInt(allSpirits.size()));
                        if (level <= 2 && (protoS.getBaseDamage() > 500 || protoS.getDefense() > 600)) {
                            break;
                        }
                        return new Spirit(
                                protoS.getName(),
                                level,
                                protoS.getBaseDamage(),
                                protoS.getDefense(),
                                protoS.getDodgeChance()
                        );
                    }
                    break;
                default:
                    if (allExoskeletons != null && !allExoskeletons.isEmpty()) {
                        Exoskeleton protoE = allExoskeletons.get(rand.nextInt(allExoskeletons.size()));
                        if (level <= 2 && (protoE.getBaseDamage() > 500 || protoE.getDefense() > 600)) {
                            break;
                        }
                        return new Exoskeleton(
                                protoE.getName(),
                                level,
                                protoE.getBaseDamage(),
                                protoE.getDefense(),
                                protoE.getDodgeChance()
                        );
                    }
                    break;
            }
        }

        // Fallback: no monsters available of selected types
        return null;
    }

    /**
     * Check if all heroes have fainted.
     */
    private boolean allHeroesFainted() {
        for (Hero h : party) {
            if (!h.isFainted()) {
                return false;
            }
        }
        return true;
    }
}