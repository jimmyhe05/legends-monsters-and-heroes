package legends.entities.monsters;

public abstract class Monster {

    // ----- Core attributes -----
    protected String name;
    protected int level;
    protected double hp;
    protected double baseDamage;
    protected double defense;
    protected double dodgeChance;

    /**
     * Constructor for a monster.
     * 
     * @param name the name of the monster
     * @param level the level of the monster
     * @param baseDamage the base damage the monster can deal
     * @param defense the defense value of the monster
     * @param dodgeChance the dodge chance percentage of the monster
     */
    public Monster(String name, int level, double baseDamage, double defense, double dodgeChance) {
        this.name = name;
        this.level = level;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;
        this.hp = level * 100; 
    }

    // ----- Combat Behavior -----

    /**
     * Basic attack method returning damage dealt.
     * 
     * @return damage dealt
     */
    public double attack() {
        return baseDamage;
    }

    /**
     * Apply damage to the monster, reducing HP.
     * 
     * @param dmg damage to apply
     */
    public void takeDamage(double dmg) {
        double reduced = dmg - defense;
        if (reduced < 0) {
            reduced = 0;
        }
        hp -= reduced;
        if (hp < 0) {
            hp = 0;
        }
    }

    /**
     * Check if the monster is dead.
     * 
     * @return true if dead, false otherwise
     */
    public boolean isDead() {
        return hp <= 0;
    }

    /**
     * Get the monster's dodge probability as a decimal.
     * 
     * @return dodge probability
     */
    public double getDodgeProbability() {
        return dodgeChance * 0.01;
    }

    // ----- Getters -----

    /**
     * Get the monster's name.
     * 
     * @return monster's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the monster's level.
     * 
     * @return monster's level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get the monster's current health points (HP).
     * 
     * @return monster's HP
     */
    public double getHp() {
        return hp;
    }

    /**
     * Get the monster's base damage.
     * 
     * @return monster's base damage
     */
    public double getBaseDamage() {
        return baseDamage;
    }

    /**
     * Get the monster's defense.
     * 
     * @return monster's defense
     */
    public double getDefense() {
        return defense;
    }

    /**
     * Get the monster's dodge chance as a percentage.
     * 
     * @return monster's dodge chance
     */
    public double getDodgeChance() {
        return dodgeChance;
    }

    /**
     * Returns a string representation of the monster.
     * 
     * @return string representation of the monster
     */
    @Override
    public String toString() {
        return name +
                " [Lvl " + level +
                ", HP=" + hp +
                ", DMG=" + baseDamage +
                ", DEF=" + defense +
                ", Dodge=" + dodgeChance + "%]";
    }
}
