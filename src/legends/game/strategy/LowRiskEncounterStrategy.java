package legends.game.strategy;

import java.util.Random;
import legends.game.Board;

/**
 * Lower encounter chance for easier difficulties (e.g., 15%).
 */
public class LowRiskEncounterStrategy implements EncounterStrategy {
    private static final double CHANCE = 0.15; // 15%

    @Override
    public boolean shouldTrigger(Board board, Random rng) {
        if (board == null || rng == null) {
            return false;
        }
        return rng.nextDouble() < CHANCE;
    }
}
