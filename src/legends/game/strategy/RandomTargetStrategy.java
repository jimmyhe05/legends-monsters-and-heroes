package legends.game.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import legends.entities.heroes.Hero;
import legends.game.HeroTargetStrategy;

/**
 * Randomly selects any non-fainted hero.
 */
public class RandomTargetStrategy implements HeroTargetStrategy {
    private final Random rng;

    public RandomTargetStrategy() {
        this(new Random());
    }

    public RandomTargetStrategy(Random rng) {
        this.rng = rng == null ? new Random() : rng;
    }

    @Override
    public Hero selectTarget(List<Hero> heroes) {
        if (heroes == null || heroes.isEmpty()) {
            return null;
        }
        List<Hero> alive = new ArrayList<>();
        for (Hero h : heroes) {
            if (h != null && !h.isFainted()) {
                alive.add(h);
            }
        }
        if (alive.isEmpty()) {
            return null;
        }
        int idx = rng.nextInt(alive.size());
        return alive.get(idx);
    }
}
