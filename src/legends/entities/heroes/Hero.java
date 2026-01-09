package legends.entities.heroes;

import legends.entities.Combatant;
import legends.entities.monsters.Monster;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;
import legends.utilities.Color;

/**
 * Abstract base class for all hero types in the game.
 */
public abstract class Hero implements Combatant {

    // ----- Core attributes -----
    protected String name;
    protected int level;
    protected double experience;
    protected double hp;
    protected double mp;
    protected double strength;
    protected double agility;
    protected double dexterity;
    protected double gold;


    // ----- Equipment / inventory -----
    protected Inventory inventory;
    protected Weapon equippedWeapon;
    protected Armor equippedArmor;
    // true if hero is gripping the equipped weapon with two hands (for bonus on one-hand weapons)
    protected boolean weaponTwoHandedGrip;

    /**
     * Constructor for a hero.
     * 
     * @param name the name of the hero
     * @param level the level of the hero
     * @param hp the health points of the hero
     * @param mp the mana points of the hero
     * @param strength the strength attribute of the hero
     * @param agility the agility attribute of the hero
     * @param dexterity the dexterity attribute of the hero
     * @param gold the gold amount the hero has
     */
    public Hero(String name,
                int level,
                double hp,
                double mp,
                double strength,
                double agility,
                double dexterity,
                double gold) {
        this.name = name;
        this.level = level;
        this.experience = 0.0;
        this.hp = hp;
        this.mp = mp;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.gold = gold;
        this.inventory = new Inventory();
        this.equippedWeapon = null;
        this.equippedArmor = null;
        this.weaponTwoHandedGrip = false;
    }

    // ----- Core combat API -----

    /**
     * Basic physical attack using the currently equipped weapon.
     */
    public abstract void attack(Monster m);

    /**
     * Cast a spell on a monster. Should check mana & inventory.
     */
    public abstract void castSpell(Spell s, Monster m);

    /**
     * Use a potion from inventory to buff this hero.
     */
    public void usePotion(Potion p) {
        if (p == null) {
            return;
        }

        if (!p.isUsable()) {
            System.out.println("That potion is empty and crumbles to dust.");
            inventory.removePotion(p);
            return;
        }

        p.applyTo(this);
        if (!p.isUsable()) {
            // Remove once it has no uses left
            inventory.removePotion(p);
        }
    }

    // ----- Equipment management -----

    /**
     * Equip a weapon from inventory.
     */
    public void equipWeapon(Weapon w) {
        if (w == null) {
            return;
        }
        if (!w.isUsable()) {
            System.out.println(w.getName() + " is broken and cannot be equipped until repaired.");
            return;
        }
        this.equippedWeapon = w;
        // default grip: two hands if weapon requires two, otherwise one hand until set
        this.weaponTwoHandedGrip = (w.getHandsRequired() == 2);
    }

    /**
     * Equip an armor from inventory.
     */
    public void equipArmor(Armor a) {
        if (a == null) {
            return;
        }
        if (!a.isUsable()) {
            System.out.println(a.getName() + " is broken and cannot be equipped until repaired.");
            return;
        }
        this.equippedArmor = a;
    }

    /**
     * Configure whether the current weapon is held with two hands (only meaningful for 1-hand weapons).
     */
    public void setWeaponTwoHandedGrip(boolean twoHanded) {
        if (equippedWeapon == null) {
            this.weaponTwoHandedGrip = false;
            return;
        }
        if (equippedWeapon.getHandsRequired() == 2) {
            this.weaponTwoHandedGrip = true; // forced two hands
        } else {
            this.weaponTwoHandedGrip = twoHanded;
        }
    }

    public boolean isWeaponTwoHandedGrip() {
        return weaponTwoHandedGrip;
    }

    // ----- Combat helpers -----

    /**
     * Called when this hero takes damage from a monster.
     */
    public void receiveDamage(double rawDamage) {
        double damage = rawDamage;
        if (equippedArmor != null) {
            damage -= equippedArmor.getDamageReduction();
            // consume armor durability once per hit
            if (equippedArmor.getRemainingUses() > 0) {
                equippedArmor.consumeUse();
                if (!equippedArmor.isUsable()) {
                    System.out.println(equippedArmor.getName() + " has broken!");
                    equippedArmor = null;
                }
            }
        }
        if (damage < 0) {
            damage = 0;
        }
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    /**
     * Check if the hero has fainted (HP <= 0).
     * 
     * @return true if fainted, false otherwise
     */
    public boolean isFainted() {
        return hp <= 0;
    }

    @Override
    public boolean isDefeated() {
        return isFainted();
    }

    /**
      * Revive the hero at half health and mana.
     */
    public void reviveAtHalf() {
          // when battle ends and hero fainted
          hp = (level * 100) / 2.0;
          mp = mp / 2.0;
    }

    /**
     * Regenerate some HP and MP at the end of a battle round
     * if the hero has not fainted.
     */
    public void regenAfterRound() {
        if (isFainted()) {
            return;
        }
        // Regenerate 10% of current HP/MP, but do not let HP exceed
        // the standard base value for this level (level * 100).
        hp *= 1.1;
        // Round to one decimal place to avoid floating-point artifacts
        hp = Math.round(hp * 10.0) / 10.0;
        double maxHp = level * 100.0;
        if (hp > maxHp) {
            hp = maxHp;
        }

        mp *= 1.1;
        // Round to one decimal place for consistency
        mp = Math.round(mp * 10.0) / 10.0;
    }


    // ----- Level / XP / Gold -----

    /**
     * Gain experience points and handle leveling up.
     */
    public void gainExperience(double amount) {
        experience += amount;
        while (experience >= experienceToLevelUp()) {
            experience -= experienceToLevelUp();
            levelUp();
        }
    }

    /**
     * Calculate experience needed to level up.
     * 
     * @return experience points needed for next level
     */
    protected int experienceToLevelUp() {
        return level * 10;
    }

    /**
     * Level up the hero, increasing stats.
     */
    protected abstract void levelUp(); 

    /**
     * Gain gold.
     */
    public void gainGold(double amount) {
        gold += amount;
    }

    /**
     * Directly set gold (used for loading saved games).
     */
    public void setGold(double gold) {
        this.gold = gold;
    }

    /**
     * Spend gold. Returns true if successful.
     * 
     * @param amount amount of gold to spend
     * @return true if enough gold, false otherwise
     */
    public boolean spendGold(double amount) {
        if (gold < amount) {
            return false;
        }
        gold -= amount;
        return true;
    }

    /**
     * Directly set experience points (used for loading saved games).
     */
    public void setExperience(double experience) {
        this.experience = experience;
    }

    /**
     * Directly set level (used for loading saved games). Does not auto-scale stats.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    // ----- Dodge chance -----

    /**
     * Calculate the hero's dodge chance based on agility.
     * 
     * @return dodge chance as a decimal (e.g., 0.1 for 10%)
     */
    public double getDodgeChance() {
    // Example rule from spec: dodge = agility * 0.002.
    // We halve it to make battles feel less dodge-heavy while
    // preserving the same scaling relationship.
    double prob = agility * 0.002 * 0.5; // half of the original chance
        if (prob < 0) {
            prob = 0;
        } else if (prob > 0.5) {
            // cap at 50% so heroes don't become unhittable at high agility
            prob = 0.5;
        }
        return prob;
    }

    // ----- Getters -----

    /**
     * Get the hero's name.
     * 
     * @return hero's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get a display-friendly version of the hero's name.
     * <p>
     * By default, this replaces underscores with spaces so that names like
     * "Gaerdal_Ironhand" show up as "Gaerdal Ironhand" in the terminal.
     * The underlying stored name (used for data loading, etc.) is unchanged.
     *
     * @return display-formatted hero name
     */
    @Override
    public String getDisplayName() {
        if (name == null) {
            return "";
        }
        return name.replace('_', ' ');
    }

    /**
     * Get the hero's level.
     * 
     * @return hero's level
     */
    @Override
    public int getLevel() {
        return level;
    }

    /**
     * Get the hero's current health points (HP).
     * 
     * @return hero's HP
     */
    @Override
    public double getHp() {
        return hp;
    }

    /**
     * Get the hero's current mana points (MP).
     * 
     * @return hero's MP
     */
    public double getMp() {
        return mp;
    }


    /**
     * Get the hero's strength attribute.
     * 
     * @return hero's strength
     */
    public double getStrength() {
        return strength;
    }

    /**
     * Get the hero's agility attribute.
     * 
     * @return hero's agility
     */
    public double getAgility() {
        return agility;
    }

    /**
     * Get the hero's dexterity attribute.
     * 
     * @return hero's dexterity
     */
    public double getDexterity() {
        return dexterity;
    }

    /**
     * Get the hero's gold amount.
     * 
     * @return hero's gold
     */
    public double getGold() {
        return gold;
    }

    /**
     * Get the hero's experience points.
     *
     * @return hero's experience
     */
    public double getExperience() {
        return experience;
    }

    /**
     * Get the hero's inventory.
     * 
     * @return hero's inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the hero's equipped weapon.
     * 
     * @return hero's equipped weapon
     */
    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    /**
     * Consume one use of the equipped weapon, unequipping it if it breaks.
     */
    public void consumeWeaponUse() {
        if (equippedWeapon == null) {
            return;
        }
        if (equippedWeapon.getRemainingUses() > 0) {
            equippedWeapon.consumeUse();
            if (!equippedWeapon.isUsable()) {
                System.out.println(equippedWeapon.getName() + " has broken and is unequipped.");
                equippedWeapon = null;
                weaponTwoHandedGrip = false;
            }
        }
    }

    /**
     * Get the hero's equipped armor.
     * 
     * @return hero's equipped armor
     */
    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    // ----- Setters (needed for Potions) -----

    /**
     * Set the hero's health points (HP).
     * @param hp the new HP value
     */
    public void setHp(double hp) {
        // HP is not capped per spec; setter is a simple assignment
        this.hp = hp;
    }

    /**
     * Set the hero's mana points (MP).
     * @param mp the new MP value
     */
    public void setMp(double mp) {
        // MP is not capped per spec; setter is a simple assignment
        this.mp = mp;
    }

    /**
     * Set the hero's strength attribute.
     * @param strength the new strength value
     */
    public void setStrength(double strength) {
        this.strength = strength;
    }

    /**
     * Set the hero's dexterity attribute.
     * @param dexterity the new dexterity value
     */
    public void setDexterity(double dexterity) {
        this.dexterity = dexterity;
    }

    /**
     * Set the hero's agility attribute.
     * @param agility the new agility value
     */
    public void setAgility(double agility) {
        this.agility = agility;
    }

    /**
     * Returns a string representation of the hero.
     * @return string representation of the hero
     */
    @Override
    public String toString() {
        String display = getDisplayName();
        return String.format(
                "%s [Lvl %d | HP=%.1f | MP=%.1f | STR=%.1f | DEX=%.1f | AGI=%.1f | Gold=%s]",
                Color.heroName(display),
                level,
                hp,
                mp,
                strength,
                dexterity,
                agility,
                Color.gold(gold)
        );
    }
}
