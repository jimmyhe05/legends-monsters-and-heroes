package legends.entities.heroes;

import legends.entities.monsters.Monster;
import legends.items.Spell;

/**
 * Class representing a Paladin hero.
 */
public class Paladin extends Hero {

    /**
     * Constructor for a Paladin hero.
     * 
     * @param name the name of the paladin
     * @param mana the starting mana points
     * @param strength the strength attribute
     * @param agility the agility attribute
     * @param dexterity the dexterity attribute
     * @param startingGold the starting gold amount
     * @param startingExperience the starting experience points
     */
    public Paladin(String name,
                   double mana,
                   double strength,
                   double agility,
                   double dexterity,
                   double startingGold,
                   double startingExperience) {

        super(name,
              1,                         // level
              1 * 100.0,                 // hp
              mana,                      // mp
              strength,
              agility,
              dexterity,
              startingGold);

        this.experience = startingExperience;
    }

    /**
     * Paladin's attack implementation.
     * 
     * @param m the monster to attack
     */
    @Override
    public void attack(Monster m) {
        if (isFainted() || m == null || m.isDead()) {
            return;
        }

        double weaponDamage = 0;
        if (equippedWeapon != null) {
            weaponDamage = equippedWeapon.getDamage();
        }

        double rawDamage = (strength + weaponDamage) * 0.05;

        double dodgeProb = m.getDodgeProbability();
        if (Math.random() < dodgeProb) {
            System.out.println(name + " attacked " + m.getName() + " but it dodged!");
            return;
        }

        m.takeDamage(rawDamage);
        System.out.println(name + " attacked " + m.getName() + " for " + rawDamage + " damage!");
    }

    /**
     * Paladin's spell casting implementation.
     * 
     * @param s the spell to cast
     * @param m the monster to cast the spell on
     */
    @Override
    public void castSpell(Spell s, Monster m) {
        if (isFainted() || s == null || m == null || m.isDead()) {
            return;
        }

        if (mp < s.getManaCost()) {
            System.out.println(name + " does not have enough mana to cast " + s.getName() + "!");
            return;
        }

        double base = s.getBaseDamage();
        double spellDamage = base + (dexterity / 10000.0) * base;

        double dodgeProb = m.getDodgeProbability();
        if (Math.random() < dodgeProb) {
            System.out.println(name + " cast " + s.getName() + " on " + m.getName() + " but it dodged!");
            mp -= s.getManaCost();
            return;
        }

        m.takeDamage(spellDamage);
        s.applyEffect(m);
        mp -= s.getManaCost();

        System.out.println(name + " cast " + s.getName() + " on " + m.getName() +
                           " for " + spellDamage + " damage!");
    }

    /**
     * Level up the Paladin hero, increasing stats accordingly.
     */
    @Override
    protected void levelUp() {
        level++;
        hp = level * 100.0;
        mp *= 1.1;

        // Paladin favored: strength & dexterity
        // Base +5% to all:
        strength *= 1.05;
        agility *= 1.05;
        dexterity *= 1.05;

        // Extra +5% to favored:
        strength *= 1.05;
        dexterity *= 1.05;

        System.out.println(name + " leveled up to " + level + " (Paladin)!");
    }
}
