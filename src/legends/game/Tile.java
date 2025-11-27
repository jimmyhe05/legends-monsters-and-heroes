package legends.game;

/**
 * Abstract base class for all tile types on the game map.
 */
public abstract class Tile {

    /**
     * Check if the tile is accessible by heroes.
     * 
     * @return true if accessible, false otherwise
     */
    public abstract boolean isAccessible();

    /**
     * Check if the tile has a market.
     * 
     * @return true if there is a market, false otherwise
     */
    public abstract boolean hasMarket();

    /**
     * Get the symbol representing the tile.
     * 
     * @return character symbol of the tile
     */
    public abstract char getSymbol();
}
