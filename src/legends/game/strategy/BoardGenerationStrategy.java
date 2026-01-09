package legends.game.strategy;

import legends.game.Board;

/**
 * Strategy to generate a board layout. Implementations can vary density of markets/inaccessible tiles.
 */
public interface BoardGenerationStrategy {
    Board generate(int size);
}
