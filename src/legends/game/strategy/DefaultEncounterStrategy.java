package legends.game.strategy;

import java.util.Random;
import legends.game.Board;

/**
 * Default encounter strategy: moderate encounter chance (e.g., 30%).
 */
public class DefaultEncounterStrategy implements EncounterStrategy {
    private static final double CHANCE = 0.30; // 30%

    @Override
    public boolean shouldTrigger(Board board, Random rng) {
        if (board == null || rng == null) {
            return false;
        }
        return rng.nextDouble() < CHANCE;
    }
}
