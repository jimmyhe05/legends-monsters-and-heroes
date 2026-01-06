package legends.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import legends.entities.heroes.Hero;
import legends.entities.monsters.Monster;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;
import legends.utilities.Color;

/**
 * Class representing a battle between heroes and monsters.
 */
public class Battle {

    private final List<Hero> heroes;
    private final List<Monster> monsters;
    private final HeroTargetStrategy heroTargetStrategy;
    private final Scanner in;
    // Flag indicating the party has successfully fled this battle.
    private boolean fleeing = false;

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
        this.heroTargetStrategy = new RandomHeroTargetStrategy();
    }

        // start the battle
        public void start() {
		System.out.println(Color.title("=== BATTLE START ==="));
		System.out.println(Color.title("Heroes vs Monsters!"));

        // Main battle loop
        while (!allHeroesFainted() && !allMonstersDead() && !fleeing) {
            printStatus();

            heroesTurn();
            // If the party successfully fled during the heroes' turn,
            // end the battle immediately before monsters can act.
            if (fleeing) {
                System.out.println(Color.warning("The party has escaped from the battle."));
                break;
            }

            if (allMonstersDead()) {
                break;
            }

            monstersTurn();
            endOfRoundRegen();
        }

        if (allMonstersDead()) {
            System.out.println(Color.success("Heroes win the battle!"));
            handleVictoryRewards();
        } else if (fleeing) {
            System.out.println(Color.warning("The party successfully fled the battle."));
        } else {
            System.out.println(Color.error("All heroes have fainted... Game Over."));
        }

        System.out.println(Color.title("=== BATTLE END ==="));
    }

    /* ================= HERO & MONSTER TURN ================= */

        // Handles all heroes' turns
        private void heroesTurn() {
		System.out.println(Color.title("\n--- Heroes' Turn ---"));
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
            System.out.println("\nIt's " + hero.getDisplayName() + "'s turn.");
            System.out.println("1. Attack");
            System.out.println("2. Cast Spell");
            System.out.println("3. Use Potion");
            System.out.println("4. Change Equipment");
            System.out.println("5. Inspect (free)");
            System.out.println("6. Run");
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
                case 1 -> {
                    // Regular attack consumes the turn only when a valid target was chosen
                    if (handleAttack(hero)) {
                        return;
                    }
                }
                case 2 -> {
                    // Only consume the turn if a spell was actually cast
                    if (handleCastSpell(hero)) {
                        return;
                    }
                    // otherwise, re-show the action menu
                }
                case 3 -> {
                    // Only consume the turn if a potion was actually used
                    if (handleUsePotion(hero)) {
                        // Show updated hero stats after potion effects
                        System.out.println("Updated hero stats:");
                        System.out.println("  " + hero);
                        return;
                    }
                    // otherwise, re-show the action menu
                }
                case 4 -> {
                    // Changing equipment does NOT consume the turn; after
                    // equipping, let the player choose another action.
                    handleChangeEquipment(hero);
                    System.out.println("Updated equipment:");
                    System.out.println("  " + hero);
                }
                case 5 ->
                    // Free inspect action
                    printStatus();
                case 6 -> {
                    // Attempt to flee the battle for the whole party.
                    if (attemptRunAway()) {
                        // On successful run, end the battle loop via flag.
                        fleeing = true;
                    }
                    // Whether we succeed or fail, this hero's turn is done.
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // Handles all monsters' turns
    private void monstersTurn() {
		System.out.println(Color.title("\n--- Monsters' Turn ---"));

        for (Monster m : monsters) {
            if (m.isDead()) {
                continue;
            }
            Hero target = heroTargetStrategy.selectTarget(heroes);
            if (target == null) {
                return;
            }

            System.out.println(Color.monsterName(m.getDisplayName()) + " attacks " + Color.heroName(target.getDisplayName()) + "!" );

            // hero dodge
            double dodgeProb = target.getDodgeChance();
            if (Math.random() < dodgeProb) {
                System.out.println(Color.heroName(target.getDisplayName()) + " dodged the attack!");
                continue;
            }

            double damage = m.attack(); // monster's base damage

            double beforeHp = target.getHp();
            target.receiveDamage(damage);
            double afterHp = target.getHp();
            double lost = beforeHp - afterHp;
            if (lost < 0) {
                lost = 0;
            }
            lost = Math.round(lost * 10.0) / 10.0;

            System.out.println(Color.heroName(target.getDisplayName()) + " took " + lost + " damage.");

            if (isHeroFainted(target)) {
                System.out.println(Color.error(target.getDisplayName() + " has fainted!"));
            }
        }
    }

    /**
     * Attempt to flee from battle for the entire party.
     * Returns true on success, false on failure.
     * On success, applies a small gold penalty to each non-fainted hero
     * and sets the fleeing flag so the main loop ends.
     */
    private boolean attemptRunAway() {
        double chance = 0.6; // 60% success chance
        if (Math.random() < chance) {
            System.out.println("You successfully fled from battle, but lost some gold in the chaos!");

            for (Hero h : heroes) {
                if (!h.isFainted() && h.getGold() > 0) {
                    double loss = h.getGold() * 0.1; // lose 10% of current gold
                    h.spendGold(loss);
                }
            }

            fleeing = true;
            return true;
        } else {
            System.out.println("You failed to escape!");
            return false;
        }
    }

    /* ===================== HERO ACTION HELPERS ===================== */

    /**
     * Handles the attack action for a hero.
     * 
     * @param hero The hero performing the attack.
     */
    private boolean handleAttack(Hero hero) {
        Monster target = chooseMonsterTarget();
        if (target == null) {
            System.out.println("No valid target selected. Action cancelled.");
            return false;
        }

        double beforeHp = target.getHp();
    hero.attack(target);
    hero.consumeWeaponUse();
        double afterHp = target.getHp();
        double lost = beforeHp - afterHp;
        if (lost <= 0) {
            // Treat as a dodge or fully negated hit
            System.out.println(
                    Color.heroName(hero.getDisplayName()) +
                    " attacked " +
                    Color.monsterName(target.getDisplayName()) +
                    ", but it dodged!"
            );
        } else {
            // Round to one decimal place for a clean message
            lost = Math.round(lost * 10.0) / 10.0;

            System.out.println(
                    Color.heroName(hero.getDisplayName()) +
                    " attacked " +
                    Color.monsterName(target.getDisplayName()) +
                    " for " + lost + " damage!"
            );

            if (target.isDead()) {
                System.out.println(Color.success(Color.monsterName(target.getDisplayName()) + " is defeated!"));
            }
        }
        return true;
    }

    /**
     * Handles the cast spell action for a hero.
     * 
     * @param hero The hero performing the spell cast.
     * @return true if a spell was successfully cast, false otherwise (no spells or invalid choice).
     */
    private boolean handleCastSpell(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Spell> spells = inv.getSpells();
        if (spells.isEmpty()) {
            System.out.println("No spells in inventory.");
            return false;
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
            return false;
        }

        if (idx < 1 || idx > spells.size()) {
            System.out.println("Index out of range.");
            return false;
        }

        Spell s = spells.get(idx - 1);
        Monster target = chooseMonsterTarget();
        if (target == null) {
            System.out.println("No valid target.");
            return false;
        }

        hero.castSpell(s, target);
        // Spells are consumable; remove once uses are gone
        s.consumeUse();
        if (!s.isUsable()) {
            inv.removeSpell(s);
            System.out.println(s.getName() + " has been consumed.");
        }
        if (target.isDead()) {
            System.out.println(target.getName() + " is defeated!");
        }

        return true;
    }

    /**
     * Handles the use potion action for a hero.
     * 
     * @param hero The hero using the potion.
     * @return true if a potion was successfully used, false otherwise
     */
    private boolean handleUsePotion(Hero hero) {
        Inventory inv = hero.getInventory();
        List<Potion> potions = inv.getPotions();
        if (potions.isEmpty()) {
            System.out.println("No potions in inventory.");
            return false;
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
            return false;
        }

        if (idx < 1 || idx > potions.size()) {
            System.out.println("Index out of range.");
            return false;
        }

        Potion p = potions.get(idx - 1);
        hero.usePotion(p);
        System.out.println(hero.getName() + " used " + p.getName() + ".");

        return true;
    }

    /**
     * Handles the change equipment action for a hero.
     * 
     * @param hero The hero changing equipment.
     */
    private void handleChangeEquipment(Hero hero) {
        Inventory inv = hero.getInventory();

        while (true) {
            System.out.println("Change Equipment:");
            System.out.println("1. Equip Weapon");
            System.out.println("2. Equip Armor");
            System.out.println("0. Cancel");
            System.out.print("Choose: ");

            String line = in.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            if (choice == 0) {
                return;
            }

            switch (choice) {
                case 1 -> {
                    List<Weapon> weapons = inv.getWeapons();
                    if (weapons.isEmpty()) {
                        System.out.println("No weapons available.");
                        continue;
                    }
                    while (true) {
                        System.out.println("Weapons:");
                        for (int i = 0; i < weapons.size(); i++) {
                            System.out.println((i + 1) + ". " + weapons.get(i));
                        }
                        System.out.print("Choose weapon (0=cancel): ");
                        line = in.nextLine().trim();
                        int wIdx;
                        try {
                            wIdx = Integer.parseInt(line);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid index.");
                            continue;
                        }
                        if (wIdx == 0) {
                            break;
                        }
                        if (wIdx < 1 || wIdx > weapons.size()) {
                            System.out.println("Index out of range.");
                            continue;
                        }
                        Weapon chosen = weapons.get(wIdx - 1);
                        hero.equipWeapon(chosen);
                        if (chosen.getHandsRequired() == 1) {
                            System.out.print("Use two hands for extra damage? (y/n): ");
                            String grip = in.nextLine().trim().toLowerCase();
                            hero.setWeaponTwoHandedGrip(grip.startsWith("y"));
                        }
                        System.out.println(hero.getName() + " equipped " + chosen.getName());
                        return;
                    }
                }
                case 2 -> {
                    List<Armor> armors = inv.getArmors();
                    if (armors.isEmpty()) {
                        System.out.println("No armor available.");
                        continue;
                    }
                    while (true) {
                        System.out.println("Armors:");
                        for (int i = 0; i < armors.size(); i++) {
                            System.out.println((i + 1) + ". " + armors.get(i));
                        }
                        System.out.print("Choose armor (0=cancel): ");
                        line = in.nextLine().trim();
                        int aIdx;
                        try {
                            aIdx = Integer.parseInt(line);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid index.");
                            continue;
                        }
                        if (aIdx == 0) {
                            break;
                        }
                        if (aIdx < 1 || aIdx > armors.size()) {
                            System.out.println("Index out of range.");
                            continue;
                        }
                        hero.equipArmor(armors.get(aIdx - 1));
                        System.out.println(hero.getName() + " equipped " + armors.get(aIdx - 1).getName());
                        return;
                    }
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    /* ===================== STATUS & HELPERS ===================== */

    // Print current status of heroes and monsters (heroes via Hero.toString for simplicity)
    private void printStatus() {
        System.out.println("\n--- Battle Status ---");
        System.out.println("Heroes:");
        for (Hero h : heroes) {
            if (!h.isFainted()) {
                System.out.println("  " + h);
            }
        }
        System.out.println("Monsters:");
        for (Monster m : monsters) {
            if (!m.isDead()) {
                String stats = String.format("[Lvl %d | HP=%.1f | DMG=%.1f | DEF=%.1f | Dodge=%.1f%%]",
                        m.getLevel(), m.getHp(), m.getBaseDamage(), m.getDefense(), m.getDodgeChance());
                System.out.println("  " + Color.monsterName(m.getDisplayName()) + " " + stats);
            }
        }
        System.out.println("---------------------");
    }

    // Let the user choose a monster target
    private Monster chooseMonsterTarget() {
    List<Monster> alive = new ArrayList<>();
        for (Monster m : monsters) {
            if (!m.isDead()) {
                alive.add(m);
            }
        }
        if (alive.isEmpty()) {
            return null;
        }

        while (true) {
            System.out.println("Choose a monster to target:");
            for (int i = 0; i < alive.size(); i++) {
                Monster m = alive.get(i);
                String stats = String.format("[Lvl %d, HP=%.1f, DMG=%.1f, DEF=%.1f, Dodge=%.1f%%]",
                        m.getLevel(), m.getHp(), m.getBaseDamage(), m.getDefense(), m.getDodgeChance());
                System.out.println((i + 1) + ". " + Color.monsterName(m.getDisplayName()) + " " + stats);
            }
            System.out.print("Index (0=cancel): ");

            String line = in.nextLine().trim();
            int idx;
            try {
                idx = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid index.");
                continue;
            }

            if (idx == 0) {
                return null;
            }
            if (idx < 1 || idx > alive.size()) {
                System.out.println("Index out of range.");
                continue;
            }
            return alive.get(idx - 1);
        }
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
