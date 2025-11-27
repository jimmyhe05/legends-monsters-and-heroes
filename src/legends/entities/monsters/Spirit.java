package legends.entities.monsters;

/**
 * Class representing a Spirit monster.
 */
public class Spirit extends Monster {

    /**
     * Constructor for a Spirit monster.
     * 
     * @param name the name of the spirit
     * @param level the level of the spirit
     * @param baseDamage the base damage of the spirit
     * @param defense the defense of the spirit
     * @param dodgeChance the dodge chance of the spirit
     */
    public Spirit(String name, int level,
                  double baseDamage, double defense, double dodgeChance) {
        super(name, level, baseDamage, defense, dodgeChance);
        // Spirits have extra dodge chance
        this.dodgeChance *= 1.1;
    }
}
