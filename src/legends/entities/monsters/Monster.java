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
        // Apply a global scaling factor so monsters are not overly lethal,
        // especially at low hero levels. This keeps relative strengths
        // between monsters while making battles more survivable.
        double scalingFactor = 0.1; // 10% of the listed base damage
        return baseDamage * scalingFactor;
    }

    /**
     * Apply damage to the monster, reducing HP.
     * 
     * @param dmg damage to apply
     */
    public void takeDamage(double dmg) {
        // Treat defense as a softer percentage-based damage reduction.
        // Higher defense means more reduction, but damage is never fully negated.

        // Map defense into a reduction factor in [0, 0.8]. Larger denominator
        // makes defense grow more slowly, so early-game battles remain fair.
        double reductionFactor = defense / (defense + 3000.0);
        if (reductionFactor < 0) {
            reductionFactor = 0;
        } else if (reductionFactor > 0.8) {
            reductionFactor = 0.8; // cap at 80% damage reduction
        }

        double reduced = dmg * (1.0 - reductionFactor);
        // Always do at least 1 point of chip damage when an attack lands
        if (reduced < 1.0 && dmg > 0) {
            reduced = 1.0;
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
        // Example rule from spec: monster dodge = dodgeChance * 0.01.
        // We halve it to reduce excessive dodging while preserving scaling.
        double prob = (dodgeChance * 0.01) * 0.5;
        // Clamp to a reasonable range so monsters don't dodge almost everything
        if (prob < 0) {
            prob = 0;
        } else if (prob > 0.5) {
            prob = 0.5;
        }
        return prob;
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
     * Get a display-friendly version of the monster's name.
     * <p>
     * By default, this replaces underscores with spaces so that names like
     * "Ancient_Black_Dragon" show up as "Ancient Black Dragon" in the
     * terminal output. The stored name remains unchanged for data purposes.
     *
     * @return display-formatted monster name
     */
    public String getDisplayName() {
        if (name == null) {
            return "";
        }
        return name.replace('_', ' ');
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
     * Set the monster's base damage.
     * 
     * @param baseDamage the new base damage
     */
    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

    /**
     * Set the monster's defense.
     * 
     * @param defense the new defense value
     */
    public void setDefense(double defense) {
        this.defense = defense;
    }

    /**
     * Set the monster's dodge chance.
     * 
     * @param dodgeChance the new dodge chance percentage
     */
    public void setDodgeChance(double dodgeChance) {
        this.dodgeChance = dodgeChance;
    }

    /**
     * Returns a string representation of the monster.
     * 
     * @return string representation of the monster
     */
    @Override
    public String toString() {
    return String.format(
        "%s [Lvl %d, HP=%.1f, DMG=%.1f, DEF=%.1f, Dodge=%.1f%%]",
        getDisplayName(),
        level,
        hp,
        baseDamage,
        defense,
        dodgeChance
    );
    }
}
