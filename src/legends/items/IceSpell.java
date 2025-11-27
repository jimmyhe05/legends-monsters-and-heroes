package legends.items;

import legends.entities.monsters.Monster;

/**
 * Class representing an ice spell item.
 */
public class IceSpell extends Spell {

    /**
     * Constructor for an ice spell.
     * 
     * @param name the name of the spell
     * @param cost the cost of the spell
     * @param requiredLevel the required level to use the spell
     * @param baseDamage the base damage the spell can deal
     * @param manaCost the mana cost to cast the spell
     */
    public IceSpell(String name, int cost, int requiredLevel, int baseDamage, int manaCost) {
        super(name, cost, requiredLevel, baseDamage, manaCost);
    }

    /**
     * Apply the ice spell's effect on a monster.
     * 
     * @param m the monster to apply the spell effect on
     */
    @Override
    public void applyEffect(Monster m) {
        if (m == null || m.isDead()) return;
        double newDamage = m.getBaseDamage() * 0.9;
        m.setBaseDamage(newDamage);
        System.out.println(m.getName() + "'s damage is reduced!");
    }
}
