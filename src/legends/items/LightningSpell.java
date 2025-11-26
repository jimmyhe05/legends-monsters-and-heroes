package legends.items;

import legends.entities.monsters.Monster;

/**
 * Class representing a lightning spell item.
 */
public class LightningSpell extends Spell {

    /**
     * Constructor for a lightning spell.
     * 
     * @param name the name of the spell
     * @param cost the cost of the spell
     * @param requiredLevel the required level to use the spell
     * @param baseDamage the base damage the spell can deal
     * @param manaCost the mana cost to cast the spell
     */
    public LightningSpell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel, baseDamage, manaCost);
    }

    /**
     * Apply the lightning spell's effect on a monster.
     * 
     * @param m the monster to apply the spell effect on
     */
    @Override
    public void applyEffect(Monster m) {
        if (m == null || m.isDead()) return;
        double newDodge = m.getDodgeChance() * 0.9; // still in %
        m.setDodgeChance(newDodge);
        System.out.println(m.getName() + "'s dodge chance is reduced!");
    }
}
