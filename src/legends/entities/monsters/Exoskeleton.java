package legends.entities.monsters;

/**
 * Class representing an Exoskeleton monster.
 */
public class Exoskeleton extends Monster {

    /**
     * Constructor for an Exoskeleton monster.
     * 
     * @param name the name of the exoskeleton
     * @param level the level of the exoskeleton
     * @param baseDamage the base damage of the exoskeleton
     * @param defense the defense of the exoskeleton
     * @param dodgeChance the dodge chance of the exoskeleton
     */
    public Exoskeleton(String name, int level,
                       double baseDamage, double defense, double dodgeChance) {
        super(name, level, baseDamage, defense, dodgeChance);
        // Exoskeletons have extra defense
        this.defense *= 1.1;
    }
}
