package legends.items;

public class Armor extends Item {

    // Simple example: flat damage reduction value.
    private int damageReduction;

    public Armor() {
        this(0);
    }

    public Armor(int damageReduction) {
        this.damageReduction = Math.max(0, damageReduction);
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    public void setDamageReduction(int damageReduction) {
        this.damageReduction = Math.max(0, damageReduction);
    }

    @Override
    public String toString() {
        return "Armor{" +
                "damageReduction=" + damageReduction +
                '}';
    }
}