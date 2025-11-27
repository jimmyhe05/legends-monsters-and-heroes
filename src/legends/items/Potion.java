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
        // Potions are multi-use: give them 3 uses by default.
        this.remainingUses = 3;

    }

    /**
     * Get the effect amount of the potion.
     * @return the effect amount
     */
    public int getEffectAmount() {
        return effectAmount;
    }

    /**
     * Get the affected attributes of the potion.
     * @return the affected attributes
     */
    public String getAffectedAttributes() {
        return affectedAttributes;
    }

    /**
     * Apply the potion's effect to a hero.
     * 
     * @param h the hero to apply the potion effect on
     */
    public void applyTo(Hero h) {
        if (h == null || affectedAttributes == null || affectedAttributes.isEmpty()) {
            return;
        }
        String[] attrs = affectedAttributes.split("[/, ]+");

        for (String rawAttr : attrs) {
            String attr = rawAttr.trim().toUpperCase();
            if (attr.isEmpty()) continue;

            switch (attr) {
                case "HEALTH":
                case "HP":
                    h.setHp(h.getHp() + effectAmount);
                    break;

                case "MANA":
                case "MP":
                    h.setMp(h.getMp() + effectAmount);
                    break;

                case "STRENGTH":
                    h.setStrength(h.getStrength() + effectAmount);
                    break;

                case "DEXTERITY":
                    h.setDexterity(h.getDexterity() + effectAmount);
                    break;

                case "AGILITY":
                    h.setAgility(h.getAgility() + effectAmount);
                    break;

                default:
                    System.out.println("Unknown attribute in potion: " + rawAttr);
                    break;
            }
        }

        if (!isUsable()) {
            System.out.println("This potion is empty and cannot be used anymore.");
            return;
        }

        // After successfully applying, consume one use.
        consumeUse();
    }

    /**
     * Returns a string representation of the potion.
     */
    @Override
    public String toString() {
        return super.toString() +
                " [Potion +" + effectAmount +
                " to " + affectedAttributes + "]";
    }
}
