package legends.entities.monsters;

public abstract class Monster {

    // ----- Core attributes -----
    protected String name;
    protected int level;
    protected double hp;
    protected double baseDamage;
    protected double defense;
    protected double dodgeChance;

    // ----- Constructor -----
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
     * Monster deals damage to a hero (raw base damage).
     * The Battle system will handle dodge and hero receiving damage.
     */
    public double attack() {
        return baseDamage;
    }

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

    public boolean isDead() {
        return hp <= 0;
    }

    /**
     * dodge chance in decimal form.
     */
    public double getDodgeProbability() {
        return dodgeChance * 0.01;
    }

    // ----- Getters -----

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getHp() {
        return hp;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public double getDefense() {
        return defense;
    }

    public double getDodgeChance() {
        return dodgeChance;
    }

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
