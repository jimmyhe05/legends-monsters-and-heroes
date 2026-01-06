package legends.items;

/**
 * Class representing an armor item.
 */
public class Armor extends Item {
    private final int damageReduction;

    /**
     * Constructor for an armor.
     * 
     * @param name the name of the armor
     * @param cost the cost of the armor
     * @param requiredLevel the required level to use the armor
     * @param damageReduction the amount of damage reduction provided by the armor
     */
    public Armor(String name, double cost, int requiredLevel, int damageReduction) {
        super(name, cost, requiredLevel);
        this.damageReduction = damageReduction;
        // Armors also wear down over time.
        this.remainingUses = 25;
    }

    /**
     * Getters for armor attributes.
     * 
     * @return damage reduction provided by the armor
     */
    public int getDamageReduction() {
        return damageReduction;
    }

    /**
     * Returns a string representation of the armor.
     * 
     * @return string representation of the armor
     */
    @Override
    public String toString() {
        return super.toString() +
                " [Armor reduction=" + damageReduction + "]";
    }
}