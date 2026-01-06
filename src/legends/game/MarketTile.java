package legends.game;

/**
 * Class representing a market tile on the game map.
 */
public class MarketTile extends Tile {

    private final Market market;

    public MarketTile() {
        this.market = new Market();
    }

    public MarketTile(Market market) {
        this.market = (market == null) ? new Market() : market;
    }

    /**
     * Check if the tile is accessible by heroes.
     * 
     * @return true if accessible, false otherwise
     */
    @Override
    public boolean isAccessible() {
        return true;
    }

    /**
     * Check if the tile has a market.
     * 
     * @return true if there is a market, false otherwise
     */
    @Override
    public boolean hasMarket() {
        return true;
    }

    /**
     * Get the market instance bound to this tile.
     * Each MarketTile owns its own inventory per the spec.
     */
    public Market getMarket() {
        return market;
    }

    /**
     * Get the symbol representing the tile.
     * 
     * @return character symbol of the tile
     */
    @Override
    public char getSymbol() {
        return 'M';
    }
}
