package legends.items;

/**
 * Potion that restores health.
 */
public class HealthPotion extends Potion {
    public HealthPotion(String name, int cost, int requiredLevel, int effectAmount) {
        super(name, cost, requiredLevel, effectAmount, "Health");
    }
}
