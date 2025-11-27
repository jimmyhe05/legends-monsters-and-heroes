package legends.items;

/**
 * Abstract base class for all items in the game.
 */
public abstract class Item {
    protected String name;
    protected double cost;
    protected int requiredLevel;
    // How many uses remain before the item becomes unusable (0 = broken/empty).
    protected int remainingUses;

    /**
     * Constructor for an item.
     * 
     * @param name the name of the item
     * @param cost the cost of the item
     * @param requiredLevel the required level to use the item
     */
    public Item(String name, double cost, int requiredLevel) {
        this.name = name;
        this.cost = cost;
        this.requiredLevel = requiredLevel;
        this.remainingUses = -1; // -1 means "infinite" or not tracked (e.g., base items)
    }

    /**
     * Getters for item attributes.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the cost of the item.
     * 
     * @return cost of the item
     */
    public double getCost() {
        return cost;
    }

    /**
     * Get the required level to use the item.
     * 
     * @return required level of the item
     */
    public int getRequiredLevel() {
        return requiredLevel;
    }

    /**
     * Get how many uses remain for this item.
     * A negative value (e.g. -1) means the item does not track uses
     * and can be treated as having infinite durability.
     */
    public int getRemainingUses() {
        return remainingUses;
    }

    /**
     * Set the remaining uses of this item.
     */
    public void setRemainingUses(int uses) {
        this.remainingUses = uses;
    }

    /**
     * Returns true if this item is still usable (has uses left or is infinite-use).
     */
    public boolean isUsable() {
        return remainingUses != 0;
    }

    /**
     * Decrement remaining uses if they are being tracked (> 0).
     * When remainingUses goes to 0, the item is considered broken/empty.
     */
    public void consumeUse() {
        if (remainingUses > 0) {
            remainingUses--;
        }
    }
    
    /**
     * Returns a string representation of the item.
     * 
     * @return string representation of the item
     */
    @Override
    public String toString() {
        return name + " (Lvl " + requiredLevel + ", Cost " + cost + ")";
    }
}
