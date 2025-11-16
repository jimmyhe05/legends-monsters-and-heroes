package legends.items;

import legends.entities.monsters.Monster;

/**
 * Abstract base class for all spell items.
 */
public abstract class Spell extends Item {
    protected int baseDamage;
    protected int manaCost;

    /**
     * Constructor for a spell.
     * 
     * @param name the name of the spell
     * @param cost the cost of the spell
     * @param requiredLevel the required level to use the spell
     * @param baseDamage the base damage the spell can deal
     * @param manaCost the mana cost to cast the spell
     */
    public Spell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel);
        this.baseDamage = baseDamage;
        this.manaCost = manaCost;
    }

    /**
     * Getters for spell attributes.
     * 
     * @return base damage of the spell
     */
    public int getBaseDamage() {
        return baseDamage;
    }

    /**
     * Get the mana cost of the spell.
     * 
     * @return mana cost of the spell
     */
    public int getManaCost() {
        return manaCost;
    }

    /**
     * Apply the spell's effect on a monster.
     * 
     * @param m the monster to apply the spell effect on
     */
    public abstract void applyEffect(Monster m);

    /**
     * Returns a string representation of the spell.
     * 
     * @return string representation of the spell
     */
    @Override
    public String toString() {
        return super.toString() +
                " [Spell dmg=" + baseDamage +
                ", manaCost=" + manaCost + "]";
    }
}
