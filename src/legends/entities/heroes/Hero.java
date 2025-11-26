package legends.entities.heroes;

import legends.entities.monsters.Monster;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

/**
 * Abstract base class for all hero types in the game.
 */
public abstract class Hero {

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

        p.applyTo(this);          // e.g. method on Potion
        inventory.removePotion(p);
    }

    // ----- Equipment management -----

    /**
     * Equip a weapon from inventory.
     */
    public void equipWeapon(Weapon w) {
        if (w == null) {
            return;
        }
        // check level & inventory
        this.equippedWeapon = w;
    }

    /**
     * Equip an armor from inventory.
     */
    public void equipArmor(Armor a) {
        if (a == null) {
            return;
        }
        // check level & inventory
        this.equippedArmor = a;
    }

    // ----- Combat helpers -----

    /**
     * Called when this hero takes damage from a monster.
     */
    public void receiveDamage(double rawDamage) {
        double damage = rawDamage;
        if (equippedArmor != null) {
            damage -= equippedArmor.getDamageReduction();
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
        hp *= 1.1;
        mp *= 1.1;
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

    // ----- Dodge chance -----

    /**
     * Calculate the hero's dodge chance based on agility.
     * 
     * @return dodge chance as a decimal (e.g., 0.1 for 10%)
     */
    public double getDodgeChance() {
        // from spec: hero dodge chance
        return agility * 0.002;
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
     * Get the hero's level.
     * 
     * @return hero's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the hero's current health points (HP).
     * 
     * @return hero's HP
     */
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
     * Get the hero's equipped armor.
     * 
     * @return hero's equipped armor
     */
    public Armor getEquippedArmor() {
        return equippedArmor;
    }

    /**
     * Returns a string representation of the hero.
     * 
     * @return string representation of the hero
     */
    @Override
    public String toString() {
        return name +
                " [Lvl " + level +
                ", HP=" + hp +
                ", MP=" + mp +
                ", STR=" + strength +
                ", DEX=" + dexterity +
                ", AGI=" + agility +
                ", Gold=" + gold + "]";
    }
}
