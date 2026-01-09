package legends.items;

/**
 * Class representing a weapon item.
 */
public class Weapon extends Item {
    private final int damage;
    private final int handsRequired;

    /**
     * Constructor for a weapon.
     * 
     * @param name the name of the weapon
     * @param cost the cost of the weapon
     * @param requiredLevel the required level to use the weapon
     * @param damage the damage dealt by the weapon
     * @param handsRequired the number of hands required to wield the weapon
     */
    public Weapon(String name, int cost, int requiredLevel, int damage, int handsRequired) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.handsRequired = handsRequired;
        // Weapons are durable but not infinite; give a default use count so repair matters.
        this.remainingUses = 15;
    }

    /**
     * Getters for weapon attributes.
     * 
     * @return damage of the weapon
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Get the number of hands required to wield the weapon.
     * 
     * @return hands required
     */
    public int getHandsRequired() {
        return handsRequired;
    }

    /**
     * Returns a string representation of the weapon.
     * 
     * @return string representation of the weapon
     */
    @Override
    public String toString() {
        return super.toString() +
                " [Weapon dmg=" + damage +
                ", hands=" + handsRequired + "]";
    }
}