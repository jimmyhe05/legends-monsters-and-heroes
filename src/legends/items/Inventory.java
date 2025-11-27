package legends.items;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing an inventory holding various items.
 */
public class Inventory {

    private final List<Weapon> weapons;
    private final List<Armor> armors;
    private final List<Potion> potions;
    private final List<Spell> spells;

    /**
     * Constructor for an inventory.
     * Initializes empty lists for weapons, armors, potions, and spells.
     */
    public Inventory() {
        this.weapons = new ArrayList<Weapon>();
        this.armors = new ArrayList<Armor>();
        this.potions = new ArrayList<Potion>();
        this.spells = new ArrayList<Spell>();
    }

    // ---- Adders ----
    /**
     * Adds a weapon to the inventory.
     * 
     * @param w the weapon to add
     */
    public void addWeapon(Weapon w) {
        weapons.add(w);
    }

    /**
     * Adds an armor to the inventory.
     * 
     * @param a the armor to add
     */
    public void addArmor(Armor a) {
        armors.add(a);
    }

    /**
     * Adds a potion to the inventory.
     * 
     * @param p the potion to add
     */
    public void addPotion(Potion p) {
        potions.add(p);
    }

    /**
     * Adds a spell to the inventory.
     * 
     * @param s the spell to add
     */
    public void addSpell(Spell s) {
        spells.add(s);
    }

    // ---- Removers ----
    /**
     * Removes a weapon from the inventory.
     * 
     * @param w the weapon to remove
     */
    public void removeWeapon(Weapon w) {
        weapons.remove(w);
    }

    /**
     * Removes an armor from the inventory.
     * 
     * @param a the armor to remove
     */
    public void removeArmor(Armor a) {
        armors.remove(a);
    }

    /**
     * Removes a potion from the inventory.
     * 
     * @param p the potion to remove
     */
    public void removePotion(Potion p) {
        potions.remove(p);
    }

    /**
     * Removes a spell from the inventory.
     * 
     * @param s the spell to remove
     */
    public void removeSpell(Spell s) {
        spells.remove(s);
    }

    // ---- Getters ----

    /**
     * Gets the list of weapons in the inventory.
     * 
     * @return the list of weapons
     */
    public List<Weapon> getWeapons() {
        return weapons;
    }

    /**
     * Gets the list of armors in the inventory.
     * 
     * @return the list of armors
     */
    public List<Armor> getArmors() {
        return armors;
    }

    /**
     * Gets the list of potions in the inventory.
     * 
     * @return the list of potions
     */
    public List<Potion> getPotions() {
        return potions;
    }

    /**
     * Gets the list of spells in the inventory.
     * 
     * @return the list of spells
     */
    public List<Spell> getSpells() {
        return spells;
    }
}
