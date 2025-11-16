package legends.items;

import legends.entities.heroes.Hero;

/**
 * Class representing a potion item.
 */
public class Potion extends Item {
    private final int effectAmount;
    private final String affectedAttributes; // e.g. "Health", "Mana", "Strength", "Agility", etc.

    /**
     * Constructor for a potion.
     * 
     * @param name the name of the potion
     * @param cost the cost of the potion
     * @param requiredLevel the required level to use the potion
     * @param effectAmount the amount of effect the potion has
     * @param affectedAttributes the attributes affected by the potion
     */
    public Potion(String name, int cost, int requiredLevel, int effectAmount, String affectedAttributes) {
        super(name, cost, requiredLevel);
        this.effectAmount = effectAmount;
        this.affectedAttributes = affectedAttributes;
    }

    public int getEffectAmount() {
        return effectAmount;
    }

    public String getAffectedAttributes() {
        return affectedAttributes;
    }

    // TODO: implement actual stat updates later
    public void applyTo(Hero hero) {

    }

    @Override
    public String toString() {
        return super.toString() +
                " [Potion +" + effectAmount +
                " to " + affectedAttributes + "]";
    }
}
