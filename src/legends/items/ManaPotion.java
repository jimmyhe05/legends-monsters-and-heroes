package legends.items;

/**
 * Potion that restores mana.
 */
public class ManaPotion extends Potion {
    public ManaPotion(String name, int cost, int requiredLevel, int effectAmount) {
        super(name, cost, requiredLevel, effectAmount, "Mana");
    }
}
