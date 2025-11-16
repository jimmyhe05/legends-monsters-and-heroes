package legends.items;

import legends.entities.heroes.Hero;

public class Potion extends Item {

    public Potion() {
        super();
    }

    /**
     * Apply this potion's effect to the given hero.
     */
    public void applyTo(Hero hero) {
        if (hero == null) {
            return;
        }
        // TODO: implement real buff logic (e.g., increase health/mana/stats)
    }
}