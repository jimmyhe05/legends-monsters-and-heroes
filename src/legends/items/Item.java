package legends.items;

/**
 * Abstract base class for all items in the game.
 */
public abstract class Item {
    protected String name;
    protected double cost;
    protected int requiredLevel;

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
     * Returns a string representation of the item.
     * 
     * @return string representation of the item
     */
    @Override
    public String toString() {
        return name + " (Lvl " + requiredLevel + ", Cost " + cost + ")";
    }
}
