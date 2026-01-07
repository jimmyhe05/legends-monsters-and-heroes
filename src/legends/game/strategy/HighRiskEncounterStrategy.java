package legends.game.strategy;

import java.util.Random;
import legends.game.Board;

/**
 * Higher encounter chance for harder difficulties (e.g., 55%).
 */
public class HighRiskEncounterStrategy implements EncounterStrategy {
    private static final double CHANCE = 0.55; // 55%

    @Override
    public boolean shouldTrigger(Board board, Random rng) {
        if (board == null || rng == null) {
            return false;
        }
        return rng.nextDouble() < CHANCE;
    }
}
