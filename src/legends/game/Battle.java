package legends.game;

import legends.entities.heroes.Hero;
import legends.entities.monsters.Monster;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class representing a battle between heroes and monsters.
 */
public class Battle {

    private List<Hero> heroes;
    private List<Monster> monsters;
    private Scanner in;

    /**
     * Constructor for Battle
     * 
     * @param heroes   List of heroes participating in the battle
     * @param monsters List of monsters participating in the battle
     */
    public Battle(List<Hero> heroes, List<Monster> monsters) {
        // assume both lists are non-null
        this.heroes = heroes;
        this.monsters = monsters;
        this.in = new Scanner(System.in);
    }

    // start the battle
    public void start() {
        System.out.println("=== BATTLE START ===");
        System.out.println("Heroes vs Monsters!");

        // Main battle loop
        while (!allHeroesFainted() && !allMonstersDead()) {
            printStatus();

            heroesTurn();
            if (allMonstersDead()) {
                break;
            }

            monstersTurn();
            endOfRoundRegen();
        }

        if (allMonstersDead()) {
            System.out.println("Heroes win the battle!");
            handleVictoryRewards();
        } else {
            System.out.println("All heroes have fainted... Game Over.");
        }

        System.out.println("=== BATTLE END ===");
    }

    /* ================= HERO & MONSTER TURN ================= */

    // Handles all heroes' turns
    private void heroesTurn() {
        System.out.println("\n--- Heroes' Turn ---");
        for (Hero h : heroes) {
            if (isHeroFainted(h)) {
                continue;
            }
            heroAction(h);
            if (allMonstersDead()) {
                break;
            }
        }
    }

    /**
     * Handles a single hero's action during their turn.
     * 
     * @param hero The hero whose turn it is.
     */
    private void heroAction(Hero hero) {
        while (true) {
            System.out.println("\nIt's " + hero.getName() + "'s turn.");
            System.out.println("1. Attack");
            System.out.println("2. Cast Spell");
            System.out.println("3. Use Potion");
            System.out.println("4. Change Equipment");
            System.out.println("5. Show Info");
            System.out.println("6. Skip Turn");
            System.out.print("Choose action: ");

            String line = in.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    handleAttack(hero);
                    return;
                case 2:
                    handleCastSpell(hero);
                    return;
                case 3:
                    handleUsePotion(hero);
                    return;
                case 4:
                    handleChangeEquipment(hero);
                    return;
                case 5:
                    printStatus();
                    break; // stay in this hero's menu
                case 6:
                    System.out.println(hero.getName() + " skips the turn.");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    // Handles all monsters' turns
    private void monstersTurn() {
        System.out.println("\n--- Monsters' Turn ---");

        for (Monster m : monsters) {
            if (m.isDead()) {
                continue;
            }
            Hero target = chooseRandomAliveHero();
            if (target == null) {
                return;
            }

            System.out.println(m.getName() + " attacks " + target.getName() + "!");

            // hero dodge
            double dodgeProb = target.getDodgeChance();
            if (Math.random() < dodgeProb) {
                System.out.println(target.getName() + " dodged the attack!");
                continue;
            }

            double damage = m.attack(); // monster's base damage
            // hero.receiveDamage handles armor reduction etc.
            target.receiveDamage(damage);

            System.out.println(target.getName() + " took " + damage + " damage.");

            if (isHeroFainted(target)) {
                System.out.println(target.getName() + " has fainted!");
            }
        }
    }

    /* ===================== HERO ACTION HELPERS ===================== */

    /**
     * Handles the attack action for a hero.
     * 
     * @param hero The hero performing the attack.
     */
    private void handleAttack(Hero hero) {
        Monster target = chooseMonsterTarget();
        if (target == null) {
            System.out.println("No valid target.");
            return;
        }
        hero.attack(target);
        if (target.isDead()) {
            System.out.println(target.getName() + " is defeated!");
        }
    }

    /**
     * Handles the cast spell action for a hero.
     * @param hero The hero performing the spell cast.
     */
    private void handleCastSpell(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Spell> spells = inv.getSpells();
        if (spells.isEmpty()) {
            System.out.println("No spells in inventory.");
            return;
        }

        System.out.println("Spells:");
        for (int i = 0; i < spells.size(); i++) {
            System.out.println((i + 1) + ". " + spells.get(i));
        }
        System.out.print("Choose spell index: ");

        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }

        if (idx < 1 || idx > spells.size()) {
            System.out.println("Index out of range.");
            return;
        }

        Spell s = spells.get(idx - 1);
        Monster target = chooseMonsterTarget();
        if (target == null) {
            System.out.println("No valid target.");
            return;
        }

        hero.castSpell(s, target);
        if (target.isDead()) {
            System.out.println(target.getName() + " is defeated!");
        }
    }

    /**
     * Handles the use potion action for a hero.
     * 
     * @param hero The hero using the potion.
     */
    private void handleUsePotion(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Potion> potions = inv.getPotions();
        if (potions.isEmpty()) {
            System.out.println("No potions in inventory.");
            return;
        }

        System.out.println("Potions:");
        for (int i = 0; i < potions.size(); i++) {
            System.out.println((i + 1) + ". " + potions.get(i));
        }
        System.out.print("Choose potion index: ");

        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return;
        }

        if (idx < 1 || idx > potions.size()) {
            System.out.println("Index out of range.");
            return;
        }

        Potion p = potions.get(idx - 1);
        hero.usePotion(p);
        System.out.println(hero.getName() + " used " + p.getName() + ".");
    }

    /**
     * Handles the change equipment action for a hero.
     * 
     * @param hero The hero changing equipment.
     */
    private void handleChangeEquipment(Hero hero) {
        Inventory inv = hero.getInventory();

        System.out.println("Change Equipment:");
        System.out.println("1. Equip Weapon");
        System.out.println("2. Equip Armor");
        System.out.print("Choose: ");

        String line = in.nextLine().trim();
        int choice;
        try {
            choice = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        switch (choice) {
            case 1:
                List<Weapon> weapons = inv.getWeapons();
                if (weapons.isEmpty()) {
                    System.out.println("No weapons available.");
                    return;
                }
                System.out.println("Weapons:");
                for (int i = 0; i < weapons.size(); i++) {
                    System.out.println((i + 1) + ". " + weapons.get(i));
                }
                System.out.print("Choose weapon: ");
                line = in.nextLine().trim();
                int wIdx;
                try {
                    wIdx = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid index.");
                    return;
                }
                if (wIdx < 1 || wIdx > weapons.size()) {
                    System.out.println("Index out of range.");
                    return;
                }
                hero.equipWeapon(weapons.get(wIdx - 1));
                System.out.println(hero.getName() + " equipped " + weapons.get(wIdx - 1).getName());
                break;

            case 2:
                List<Armor> armors = inv.getArmors();
                if (armors.isEmpty()) {
                    System.out.println("No armor available.");
                    return;
                }
                System.out.println("Armors:");
                for (int i = 0; i < armors.size(); i++) {
                    System.out.println((i + 1) + ". " + armors.get(i));
                }
                System.out.print("Choose armor: ");
                line = in.nextLine().trim();
                int aIdx;
                try {
                    aIdx = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid index.");
                    return;
                }
                if (aIdx < 1 || aIdx > armors.size()) {
                    System.out.println("Index out of range.");
                    return;
                }
                hero.equipArmor(armors.get(aIdx - 1));
                System.out.println(hero.getName() + " equipped " + armors.get(aIdx - 1).getName());
                break;

            default:
                System.out.println("Invalid choice.");
        }
    }

    /* ===================== STATUS & HELPERS ===================== */

    // Print current status of heroes and monsters
    private void printStatus() {
        System.out.println("\n--- Battle Status ---");
        System.out.println("Heroes:");
        for (Hero h : heroes) {
            System.out.println("  " + h);
        }
        System.out.println("Monsters:");
        for (Monster m : monsters) {
            System.out.println("  " + m);
        }
        System.out.println("---------------------");
    }

    // Let the user choose a monster target
    private Monster chooseMonsterTarget() {
        List<Monster> alive = new ArrayList<Monster>();
        for (Monster m : monsters) {
            if (!m.isDead()) {
                alive.add(m);
            }
        }
        if (alive.isEmpty()) {
            return null;
        }

        System.out.println("Choose a monster to target:");
        for (int i = 0; i < alive.size(); i++) {
            System.out.println((i + 1) + ". " + alive.get(i));
        }
        System.out.print("Index: ");

        String line = in.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println("Invalid index.");
            return null;
        }

        if (idx < 1 || idx > alive.size()) {
            System.out.println("Index out of range.");
            return null;
        }
        return alive.get(idx - 1);
    }

    // Choose a random alive hero for monster attacks
    private Hero chooseRandomAliveHero() {
        List<Hero> alive = new ArrayList<Hero>();
        for (Hero h : heroes) {
            if (!isHeroFainted(h)) {
                alive.add(h);
            }
        }
        if (alive.isEmpty()) {
            return null;
        }
        int idx = (int) (Math.random() * alive.size());
        return alive.get(idx);
    }

    // Check if all heroes have fainted
    private boolean allHeroesFainted() {
        for (Hero h : heroes) {
            if (!isHeroFainted(h)) {
                return false;
            }
        }
        return true;
    }

    // Check if all monsters are dead
    private boolean allMonstersDead() {
        for (Monster m : monsters) {
            if (!m.isDead()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a hero has fainted.
     * 
     * @param h The hero to check.
     * @return true if the hero has fainted, false otherwise.
     */
    private boolean isHeroFainted(Hero h) {
        return h.isFainted();
    }

    // Regenerate some HP/MP for heroes at end of round
    private void endOfRoundRegen() {
        System.out.println("\nEnd of round: heroes regain some HP and MP.");
        for (Hero h : heroes) {
            if (!isHeroFainted(h)) {
                h.regenAfterRound(); 
            }
        }
    }

    // Handle rewards for heroes after victory
    private void handleVictoryRewards() {
        if (monsters.isEmpty()) {
            return;
        }
        int numMonsters = monsters.size();
        int monsterLevel = monsters.get(0).getLevel(); // all monsters should be at same level
        double goldPerHero = monsterLevel * 100.0;
        double expGain = numMonsters * 2.0;

        for (Hero h : heroes) {
            if (isHeroFainted(h)) {
                System.out.println(h.getName() + " was fainted and gains no rewards.");
                h.reviveAtHalf(); // revive fainted heroes at half HP/MP
            } else {
                h.gainGold(goldPerHero);
                h.gainExperience(expGain);
                System.out.println(h.getName() + " gains " + goldPerHero + " gold and " + expGain + " EXP.");
            }
        }
    }
}
