package legends.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import legends.entities.heroes.Hero;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Item;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;
import legends.utilities.Color;

/**
 * Market system where heroes can buy and sell items.
 *
 * Follows the assignment's spirit:
 * - Heroes can only buy items if they have enough gold and meet the required level.
 * - Items are loaded from the data/items text files via DataLoader.
 * - Selling returns half the item's original cost.
 */
public class Market {

	private final List<Weapon> weapons;
	private final List<Armor> armors;
	private final List<Potion> potions;
	private final List<Spell> spells;
	private final Random rand = new Random();

	public Market() {
		String base = "data/items/";
		List<Weapon> allWeapons = DataLoader.loadWeapons(base + "Weaponry.txt");
		List<Armor> allArmors = DataLoader.loadArmors(base + "Armory.txt");
		List<Potion> allPotions = DataLoader.loadPotions(base + "Potions.txt");

		// Collect all spell types into one list
		List<Spell> allSpells = new ArrayList<>();
		allSpells.addAll(DataLoader.loadFireSpells(base + "FireSpells.txt"));
		allSpells.addAll(DataLoader.loadIceSpells(base + "IceSpells.txt"));
		allSpells.addAll(DataLoader.loadLightningSpells(base + "LightningSpells.txt"));

		// Create per-market subsets to make inventories unique per tile
		this.weapons = pickSubset(allWeapons, 6);
		this.armors = pickSubset(allArmors, 6);
		this.potions = pickSubset(allPotions, 6);
		this.spells = pickSubset(allSpells, 6);
	}

	/**
	 * Main market loop. Lets the user pick a hero and then buy/sell items
	 * until they choose to leave.
     * 
     * @param party the list of heroes in the player's party
     * @param in the Scanner object for user input
	 */
	public void run(List<Hero> party, Scanner in) {
		if (party == null || party.isEmpty()) {
			System.out.println(Color.error("No heroes in party to use the market."));
			return;
		}

		while (true) {
			System.out.println(Color.title("\n=== Market ==="));
			System.out.println("Choose a hero (or 0 to leave market):");
			// Header row for hero table (match widths with row format)
            System.out.println(String.format("  %-3s %-30s %5s %10s", "Idx", "Hero", "Lvl", "Gold"));

            for (int i = 0; i < party.size(); i++) {
                Hero h = party.get(i);

                // colored name (with ANSI codes)
                String coloredName = Color.heroName(h.getDisplayName());
                // plain name (no ANSI) used for alignment
                String plainName = stripAnsi(coloredName);

                String row = String.format(
                    "  %-3d %-30s %5d %10.1f",
                    i + 1,
                    plainName,
                    h.getLevel(),
                    h.getGold());

                // put the colored name back into the aligned row
                row = row.replaceFirst(plainName, coloredName);

                System.out.println(row);
            }
			System.out.print("Hero index (0=leave): ");

			int choice = readInt(in);
			if (choice == 0) {
				break;
			}
			if (choice < 1 || choice > party.size()) {
				System.out.println(Color.error("Invalid hero index."));
				continue;
			}

			Hero hero = party.get(choice - 1);
			heroMenu(hero, in);
		}
	}

	/**
	 * Menu for actions with a specific hero inside the market.
     * 
     * @param hero the hero using the market
     * @param in the Scanner object for user input
	 */
	private void heroMenu(Hero hero, Scanner in) {
		boolean back = false;
		while (!back) {
			System.out.println(Color.title("\n-- Market for " + hero.getDisplayName() + " (Gold=" + Color.gold(hero.getGold()) + ") --"));
			System.out.println("1. " + Color.CYAN + "Buy items" + Color.RESET);
			System.out.println("2. " + Color.CYAN + "Sell items" + Color.RESET);
			System.out.println("3. " + Color.CYAN + "Show hero inventory" + Color.RESET);
			System.out.println("4. " + Color.CYAN + "Repair items" + Color.RESET);
			System.out.println("5. " + Color.CYAN + "Back to hero selection" + Color.RESET);
			System.out.print("Choose action: ");

			int choice = readInt(in);
			switch (choice) {
				case 1 -> handleBuy(hero, in);
				case 2 -> handleSell(hero, in);
				case 3 -> printHeroInventory(hero);
				case 4 -> handleRepair(hero, in);
				case 5 -> back = true;
				default -> System.out.println(Color.error("Invalid choice."));
			}
		}
	}

	/**
	 * Allow a hero to repair broken items for half their original cost.
	 * Only items with remainingUses == 0 are listed.
     * 
     * @param hero the hero repairing items
     * @param in the Scanner object for user input
	 */
	private void handleRepair(Hero hero, Scanner in) {
		Inventory inv = hero.getInventory();
		List<Item> repairables = new ArrayList<>();

		for (Weapon w : inv.getWeapons()) {
			if (w.getRemainingUses() == 0) {
				repairables.add(w);
			}
		}
		for (Armor a : inv.getArmors()) {
			if (a.getRemainingUses() == 0) {
				repairables.add(a);
			}
		}
		for (Potion p : inv.getPotions()) {
			if (p.getRemainingUses() == 0) {
				repairables.add(p);
			}
		}
		for (Spell s : inv.getSpells()) {
			if (s.getRemainingUses() == 0) {
				repairables.add(s);
			}
		}

		if (repairables.isEmpty()) {
			System.out.println("No items need repair.");
			return;
		}

		System.out.println("\nItems that can be repaired:");
		for (int i = 0; i < repairables.size(); i++) {
			Item item = repairables.get(i);
			double repairCost = item.getCost() / 2.0;
			System.out.println((i + 1) + ". " + item + " [repair cost=" + repairCost + "]");
		}
		System.out.print("Choose index to repair (0=cancel): ");

		int idx = readInt(in);
		if (idx == 0) {
			return;
		}
		if (idx < 1 || idx > repairables.size()) {
			System.out.println("Index out of range.");
			return;
		}

		Item chosen = repairables.get(idx - 1);
		double repairCost = chosen.getCost() / 2.0;
		if (!hero.spendGold(repairCost)) {
			System.out.println("You don't have enough gold to repair this item.");
			return;
		}

		// Reset remainingUses depending on item type
		if (chosen instanceof Potion) {
			chosen.setRemainingUses(3);
		} else if (chosen instanceof Spell) {
			chosen.setRemainingUses(1);
		} else if (chosen instanceof Weapon) {
			chosen.setRemainingUses(15);
		} else if (chosen instanceof Armor) {
			chosen.setRemainingUses(25);
		}

		System.out.println("Repaired " + chosen.getName() + " for " + repairCost + " gold.");
		System.out.println("Updated hero info:");
		System.out.println("  " + hero);
	}

	/* ===================== BUY ===================== */

    /**
     * Handle buying items for a hero.
     * 
     * @param hero the hero buying items
     * @param in the Scanner object for user input
     */
	private void handleBuy(Hero hero, Scanner in) {
		boolean back = false;
		while (!back) {
			System.out.println("\nWhat would you like to buy?");
			System.out.println("1. Weapons");
			System.out.println("2. Armors");
			System.out.println("3. Potions");
			System.out.println("4. Spells");
			System.out.println("5. Back");
			System.out.print("Choose category: ");

			int choice = readInt(in);
			switch (choice) {
				case 1 -> {
					buyFromList(hero, in, weapons, "weapon");
					System.out.println("Updated hero info:");
					System.out.println("  " + hero);
				}
				case 2 -> {
					buyFromList(hero, in, armors, "armor");
					System.out.println("Updated hero info:");
					System.out.println("  " + hero);
				}
				case 3 -> {
					buyFromList(hero, in, potions, "potion");
					System.out.println("Updated hero info:");
					System.out.println("  " + hero);
				}
				case 4 -> {
					buyFromList(hero, in, spells, "spell");
					System.out.println("Updated hero info:");
					System.out.println("  " + hero);
				}
				case 5 -> back = true;
				default -> System.out.println("Invalid choice.");
			}
		}
	}

    /**
     * Generic method to buy an item from a given list.
     * 
     * @param <T>   the type of item
     * @param hero  the hero buying the item
     * @param in    the Scanner object for user input
     * @param items the list of items to choose from
     * @param label the label for the item type (e.g., "weapon", "armor")
     */
	private <T extends Item> void buyFromList(Hero hero, Scanner in, List<T> items, String label) {
		if (items == null || items.isEmpty()) {
			System.out.println("No " + label + "s available to buy.");
			return;
		}

		System.out.println("\nAvailable " + label + "s:");
		// Format lists in aligned columns depending on item type
		if (!items.isEmpty()) {
			Item first = items.get(0);
			if (first instanceof Weapon) {
				System.out.println(String.format(
					"  %-3s %-24s %5s %10s %8s %7s",
					"Idx", "Name", "Lvl", "Cost", "DMG", "Hands"));
				for (int i = 0; i < items.size(); i++) {
					Weapon w = (Weapon) items.get(i);
					String row = String.format(
						"  %-3d %-24s %5d %10.1f %8d %7d",
						i + 1,
						w.getName().replace('_', ' '),
						w.getRequiredLevel(),
						w.getCost(),
						w.getDamage(),
						w.getHandsRequired());
					System.out.println(row);
				}
			} else if (first instanceof Armor) {
				System.out.println(String.format(
					"  %-3s %-24s %5s %10s %8s",
					"Idx", "Name", "Lvl", "Cost", "DEF"));
				for (int i = 0; i < items.size(); i++) {
					Armor a = (Armor) items.get(i);
					String row = String.format(
						"  %-3d %-24s %5d %10.1f %8d",
						i + 1,
						 a.getName().replace('_', ' '),
						 a.getRequiredLevel(),
						 a.getCost(),
						 a.getDamageReduction());
					System.out.println(row);
				}
			} else if (first instanceof Potion) {
				System.out.println(String.format(
					"  %-3s %-24s %5s %10s %10s",
					"Idx", "Name", "Lvl", "Cost", "Attributes"));
				for (int i = 0; i < items.size(); i++) {
					Potion p = (Potion) items.get(i);
					String row = String.format(
						"  %-3d %-24s %5d %10.1f %10s",
						i + 1,
						p.getName().replace('_', ' '),
						p.getRequiredLevel(),
						p.getCost(),
						p.getAffectedAttributes());
					System.out.println(row);
				}
			} else if (first instanceof Spell) {
				System.out.println(String.format(
					"  %-3s %-24s %5s %10s %8s %8s",
					"Idx", "Name", "Lvl", "Cost", "DMG", "MP Cost"));
				for (int i = 0; i < items.size(); i++) {
					Spell s = (Spell) items.get(i);
					String row = String.format(
						"  %-3d %-24s %5d %10.1f %8d %8d",
						i + 1,
						s.getName().replace('_', ' '),
						s.getRequiredLevel(),
						s.getCost(),
						s.getBaseDamage(),
						s.getManaCost());
					System.out.println(row);
				}
			} else {
				// Fallback: basic list using toString
				for (int i = 0; i < items.size(); i++) {
					System.out.println((i + 1) + ". " + items.get(i));
				}
			}
		}
		System.out.print("Choose index to buy (0=cancel): ");

		int idx = readInt(in);
		if (idx == 0) {
			return;
		}
		if (idx < 1 || idx > items.size()) {
			System.out.println("Index out of range.");
			return;
		}

		T item = items.get(idx - 1);
		if (hero.getLevel() < item.getRequiredLevel()) {
			System.out.println("Your level is too low to buy this " + label + ".");
			return;
		}
		double cost = item.getCost();
		if (!hero.spendGold(cost)) {
			System.out.println("You don't have enough gold.");
			return;
		}

		Inventory inv = hero.getInventory();
		switch (item) {
			case Weapon weapon -> inv.addWeapon(weapon);
			case Armor armor -> inv.addArmor(armor);
			case Potion potion -> inv.addPotion(potion);
			case Spell spell -> inv.addSpell(spell);
			default -> {
			}
		}

		System.out.println("Purchased " + item.getName() + " for " + cost + " gold.");
		// Use display name so underscores do not appear in hero names
		System.out.println(hero.getDisplayName() + " now has " + hero.getGold() + " gold left.");
	}

	/* ===================== SELL ===================== */

    /**
     * Handle selling items for a hero.
     * 
     * @param hero the hero selling items
     * @param in the Scanner object for user input
     */
	private void handleSell(Hero hero, Scanner in) {
		Inventory inv = hero.getInventory();
		List<SellEntry> entries = new ArrayList<>();

		// Flatten hero's inventory into a single list with type info
		for (Weapon w : inv.getWeapons()) {
			entries.add(new SellEntry(w, SellType.WEAPON));
		}
		for (Armor a : inv.getArmors()) {
			entries.add(new SellEntry(a, SellType.ARMOR));
		}
		for (Potion p : inv.getPotions()) {
			entries.add(new SellEntry(p, SellType.POTION));
		}
		for (Spell s : inv.getSpells()) {
			entries.add(new SellEntry(s, SellType.SPELL));
		}

		if (entries.isEmpty()) {
			System.out.println("You have no items to sell.");
			return;
		}

		System.out.println("\nItems in inventory:");
		for (int i = 0; i < entries.size(); i++) {
			SellEntry se = entries.get(i);
			System.out.println((i + 1) + ". [" + se.type + "] " + se.item);
		}
		System.out.print("Choose index to sell (0=cancel): ");

		int idx = readInt(in);
		if (idx == 0) {
			return;
		}
		if (idx < 1 || idx > entries.size()) {
			System.out.println("Index out of range.");
			return;
		}

		SellEntry selected = entries.get(idx - 1);
		Item item = selected.item;
		double refund = item.getCost() / 2.0;

		switch (selected.type) {
			case WEAPON -> {
				Weapon weapon = (Weapon) item;
				inv.removeWeapon(weapon);
				weapons.add(weapon);
			}
			case ARMOR -> {
				Armor armor = (Armor) item;
				inv.removeArmor(armor);
				armors.add(armor);
			}
			case POTION -> {
				Potion potion = (Potion) item;
				inv.removePotion(potion);
				potions.add(potion);
			}
			case SPELL -> {
				Spell spell = (Spell) item;
				inv.removeSpell(spell);
				spells.add(spell);
			}
		}

		hero.gainGold(refund);
		System.out.println("Sold " + item.getName() + " for " + refund + " gold.");
		System.out.println("Updated hero info:");
		System.out.println("  " + hero);
	}

    /**
     * Print the inventory of a hero.
     * @param hero the hero whose inventory to print
     */
	private void printHeroInventory(Hero hero) {
		Inventory inv = hero.getInventory();
		System.out.println("\nInventory of " + hero.getName() + ":");

		System.out.println("Weapons:");
		if (inv.getWeapons().isEmpty()) {
			System.out.println("  [none]");
		} else {
			for (Weapon w : inv.getWeapons()) {
				System.out.println("  - " + w);
			}
		}

		System.out.println("Armors:");
		if (inv.getArmors().isEmpty()) {
			System.out.println("  [none]");
		} else {
			for (Armor a : inv.getArmors()) {
				System.out.println("  - " + a);
			}
		}

		System.out.println("Potions:");
		if (inv.getPotions().isEmpty()) {
			System.out.println("  [none]");
		} else {
			for (Potion p : inv.getPotions()) {
				System.out.println("  - " + p);
			}
		}

		System.out.println("Spells:");
		if (inv.getSpells().isEmpty()) {
			System.out.println("  [none]");
		} else {
			for (Spell s : inv.getSpells()) {
				System.out.println("  - " + s);
			}
		}
	}

	/* ===================== Helpers ===================== */

    /**
     * Read an integer from the scanner, handling invalid input.
     * Returns -1 on invalid input.
     * 
     * @param in the Scanner object for user input
     * @return the integer read, or -1 if invalid
     */
	private int readInt(Scanner in) {
		String line = in.nextLine().trim();
		if (line.isEmpty()) {
			return -1;
		}
		try {
			return Integer.parseInt(line);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private static enum SellType { WEAPON, ARMOR, POTION, SPELL }

	/**
	 * Randomly select up to maxCount items from the source list to stock this market.
	 */
	private <T> List<T> pickSubset(List<T> source, int maxCount) {
		if (source == null || source.isEmpty()) {
			return new ArrayList<>();
		}
		List<T> copy = new ArrayList<>(source);
		Collections.shuffle(copy, rand);
		if (copy.size() <= maxCount) {
			return copy;
		}
		return new ArrayList<>(copy.subList(0, maxCount));
	}

    /**
     * Helper class to hold an item and its type for selling.
     */
	private static class SellEntry {
		final Item item;
		final SellType type;

		SellEntry(Item item, SellType type) {
			this.item = item;
			this.type = type;
		}
	}

    private static final String ANSI_REGEX = "\\u001B\\[[;\\d]*m";

    /**
     * Strip ANSI color codes from a string.
     * 
     * @param s the string to strip
     * @return the string without ANSI codes
     */
    private static String stripAnsi(String s) {
        if (s == null) {
            return "";
        }
        return s.replaceAll(ANSI_REGEX, "");
    }
}
