package legends.game;

import java.util.List;
import legends.entities.heroes.Hero;

/**
 * Strategy interface for selecting which hero a monster targets.
 */
public interface HeroTargetStrategy {
    Hero selectTarget(List<Hero> heroes);
}
