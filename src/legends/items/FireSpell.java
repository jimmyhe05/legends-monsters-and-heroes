package legends.items;

import legends.entities.monsters.Monster;

/**
 * Class representing a fire spell item.
 */
public class FireSpell extends Spell {

    /**
     * Constructor for a fire spell.
     * 
     * @param name the name of the spell
     * @param cost the cost of the spell
     * @param requiredLevel the required level to use the spell
     * @param baseDamage the base damage the spell can deal
     * @param manaCost the mana cost to cast the spell
     */
    public FireSpell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel, baseDamage, manaCost);
    }

    /**
     * Apply the fire spell's effect on a monster.
     * 
     * @param m the monster to apply the spell effect on
     */
    @Override
    public void applyEffect(Monster m) {
        if (m == null || m.isDead()) return;
        double newDefense = m.getDefense() * 0.9; // -10%
        m.setDefense(newDefense);
        System.out.println(m.getName() + "'s defense is reduced!");
    }
}
