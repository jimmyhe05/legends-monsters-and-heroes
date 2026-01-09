package legends.game.strategy;

import java.util.Random;
import legends.game.Board;

/**
 * Decides whether a battle should trigger based on context (e.g., board, RNG).
 */
public interface EncounterStrategy {
    /**
     * @param board current board
     * @param rng random generator
     * @return true if an encounter should start
     */
    boolean shouldTrigger(Board board, Random rng);
}
