package legends.items;

/**
 * Potion that boosts one or more attributes.
 */
public class AttributePotion extends Potion {
    public AttributePotion(String name, int cost, int requiredLevel, int effectAmount, String attributes) {
        super(name, cost, requiredLevel, effectAmount, attributes);
    }
}
