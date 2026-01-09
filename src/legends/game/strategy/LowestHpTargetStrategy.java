package legends.game.strategy;

import java.util.List;
import java.util.Random;
import legends.entities.heroes.Hero;
import legends.game.HeroTargetStrategy;

/**
 * Targets the lowest-HP non-fainted hero; breaks ties randomly.
 */
public class LowestHpTargetStrategy implements HeroTargetStrategy {
    private final Random rng;

    public LowestHpTargetStrategy() {
        this(new Random());
    }

    public LowestHpTargetStrategy(Random rng) {
        this.rng = rng == null ? new Random() : rng;
    }

    @Override
    public Hero selectTarget(List<Hero> heroes) {
        if (heroes == null || heroes.isEmpty()) {
            return null;
        }
        Hero best = null;
        for (Hero h : heroes) {
            if (h == null || h.isFainted()) continue;
            if (best == null) {
                best = h;
                continue;
            }
            if (h.getHp() < best.getHp()) {
                best = h;
            } else if (Double.compare(h.getHp(), best.getHp()) == 0 && rng.nextBoolean()) {
                best = h; // tie-break randomly
            }
        }
        return best;
    }
}
