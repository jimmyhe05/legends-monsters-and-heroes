package legends.entities.monsters;

/**
 * Class representing a Dragon monster.
 */
public class Dragon extends Monster {
    
    /**
     * Constructor for a Dragon monster.
     * 
     * @param name the name of the dragon
     * @param level the level of the dragon
     * @param baseDamage the base damage of the dragon
     * @param defense the defense of the dragon
     * @param dodgeChance the dodge chance of the dragon
     */
    public Dragon(String name, int level,
                  double baseDamage, double defense, double dodgeChance) {
        super(name, level, baseDamage, defense, dodgeChance);
        // Dragons have extra damage
        this.baseDamage *= 1.1;
    }
}
