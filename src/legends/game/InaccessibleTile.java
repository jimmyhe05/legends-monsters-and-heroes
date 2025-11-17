package legends.game;

/**
 * Class representing an inaccessible tile on the game map.
 */
public class InaccessibleTile extends Tile {

    /**
     * Check if the tile is accessible by heroes.
     * 
     * @return true if accessible, false otherwise
     */
    @Override
    public boolean isAccessible() {
        return false;
    }

    /**
     * Check if the tile has a market.
     * 
     * @return true if there is a market, false otherwise
     */
    @Override
    public boolean hasMarket() {
        return false;
    }

    /**
     * Get the symbol representing the tile.
     * 
     * @return character symbol of the tile
     */
    @Override
    public char getSymbol() {
        return 'X';
    }
}
