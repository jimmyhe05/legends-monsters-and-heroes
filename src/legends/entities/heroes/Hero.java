package legends.entities.heroes;

import legends.entities.monsters.Monster;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

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

    // ----- Constructor -----
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

    public void equipWeapon(Weapon w) {
        if (w == null) {
            return;
        }
        // check level & inventory
        this.equippedWeapon = w;
    }

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

    public boolean isFainted() {
        return hp <= 0;
    }

    public void reviveAtHalf() {
        // when battle ends and hero fainted
        hp = (level * 100) / 2.0; 
        mp = mp / 2.0;
    }

    // ----- Level / XP / Gold -----

    public void gainExperience(double amount) {
        experience += amount;
        while (experience >= experienceToLevelUp()) {
            experience -= experienceToLevelUp();
            levelUp();
        }
    }

    protected int experienceToLevelUp() {
        return level * 10;
    }

    protected abstract void levelUp(); 

    public void gainGold(double amount) {
        gold += amount;
    }

    public boolean spendGold(double amount) {
        if (gold < amount) {
            return false;
        }
        gold -= amount;
        return true;
    }

    // ----- Dodge chance -----

    public double getDodgeChance() {
        // from spec: hero dodge chance
        return agility * 0.002;
    }

    // ----- Getters -----

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getHp() {
        return hp;
    }

    public double getMp() {
        return mp;
    }

    public double getStrength() {
        return strength;
    }

    public double getAgility() {
        return agility;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getGold() {
        return gold;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public Armor getEquippedArmor() {
        return equippedArmor;
    }

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
