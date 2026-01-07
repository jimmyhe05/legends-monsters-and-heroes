package legends.state;

import java.util.List;
import java.util.Map;

import legends.entities.heroes.Hero;
import legends.game.Board;
import legends.game.Difficulty;
import legends.game.Market;

/**
 * Snapshot of the game world for save/load. Keep fields simple/serializable.
 */
public class GameState {
    private final Difficulty difficulty;
    private final int boardSize;
    private final char[][] boardLayout;
    private final int heroRow;
    private final int heroCol;
    private final List<HeroSnapshot> party;
    private final Map<String, MarketSnapshot> marketSnapshots;

    public GameState(Difficulty difficulty,
                     int boardSize,
                     char[][] boardLayout,
                     int heroRow,
                     int heroCol,
                     List<HeroSnapshot> party,
                     Map<String, MarketSnapshot> marketSnapshots) {
        this.difficulty = difficulty;
        this.boardSize = boardSize;
        this.boardLayout = boardLayout;
        this.heroRow = heroRow;
        this.heroCol = heroCol;
        this.party = party;
        this.marketSnapshots = marketSnapshots;
    }

    public Difficulty getDifficulty() { return difficulty; }
    public int getBoardSize() { return boardSize; }
    public char[][] getBoardLayout() { return boardLayout; }
    public int getHeroRow() { return heroRow; }
    public int getHeroCol() { return heroCol; }
    public List<HeroSnapshot> getParty() { return party; }
    public Map<String, MarketSnapshot> getMarketSnapshots() { return marketSnapshots; }

    /**
     * Create a snapshot from live objects.
     */
    public static GameState from(Board board, List<Hero> heroes, Difficulty difficulty, Map<String, Market> markets) {
        if (board == null || heroes == null) return null;
        char[][] layout = board.copyLayout();
        int size = board.getSize();
    int row = board.getPartyRow();
    int col = board.getPartyCol();
        List<HeroSnapshot> party = HeroSnapshot.fromHeroes(heroes);
        Map<String, MarketSnapshot> marketSnaps = MarketSnapshot.fromMarkets(markets);
        return new GameState(difficulty, size, layout, row, col, party, marketSnaps);
    }
}
