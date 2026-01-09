package legends.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import legends.entities.heroes.Hero;

/**
 * Random target selection strategy for monsters.
 */
public class RandomHeroTargetStrategy implements HeroTargetStrategy {
    private final Random rand = new Random();

    @Override
    public Hero selectTarget(List<Hero> heroes) {
        List<Hero> alive = new ArrayList<>();
        for (Hero h : heroes) {
            if (!h.isFainted()) {
                alive.add(h);
            }
        }
        if (alive.isEmpty()) {
            return null;
        }
        return alive.get(rand.nextInt(alive.size()));
    }
}
