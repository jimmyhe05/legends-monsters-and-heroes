package legends.game;

import java.util.Random;

import legends.utilities.Color;

/**
 * Class representing the game board.
 */
public class Board {

    private Tile[][] grid;
    private int size;

    // Party position (row, col)
    private int partyRow;
    private int partyCol;

    // Tile type distribution
    private double inaccessiblePercent;
    private double marketPercent;
    // the rest is common

    private Random rand;

    // track tiles the hero has visited (for preventing repeat encounters)
    private boolean[][] visited;

    // was the last successful move onto a tile where this is the first visit?
    private boolean lastMoveFirstVisit = false;

    /**
     * Create a board with default distribution:
     * 20% inaccessible, 30% market, 50% common.
     */
    public Board(int size) {
        this(size, 0.20, 0.30);
    }

    /**
     * Create a board with specified tile type distribution.
     * 
     * @param size               the size of the board (size x size)
     * @param inaccessiblePercent percentage of inaccessible tiles (0.0 to 1.0)
     * @param marketPercent      percentage of market tiles (0.0 to 1.0)
     */
    public Board(int size, double inaccessiblePercent, double marketPercent) {
        this.size = size;
        this.inaccessiblePercent = inaccessiblePercent;
        this.marketPercent = marketPercent;
        this.grid = new Tile[size][size];
        this.rand = new Random();
        this.visited = new boolean[size][size];

        generateRandomLayout();
        placePartyRandomly();
    }

    /**
     * Generate a random layout of tiles based on the specified distribution.
     */
    private void generateRandomLayout() {
        int total = size * size;
        int targetInaccessible = (int) (total * inaccessiblePercent);
        int targetMarket = (int) (total * marketPercent);

        int countInaccessible = 0;
        int countMarket = 0;

        // Assign tiles based on random numbers.
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double r = rand.nextDouble();

                if (countInaccessible < targetInaccessible && r < inaccessiblePercent) {
                    grid[i][j] = new InaccessibleTile();
                    countInaccessible++;
                } else if (countMarket < targetMarket && r < inaccessiblePercent + marketPercent) {
                    grid[i][j] = new MarketTile();
                    countMarket++;
                } else {
                    grid[i][j] = new CommonTile();
                }
            }
        }
    }

    /**
     * Place the party on a random accessible tile that has at least
     * one accessible neighbor, so the hero is never completely stuck.
     */
    private void placePartyRandomly() {
        // First, try a bunch of random positions
        for (int attempts = 0; attempts < size * size * 2; attempts++) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            if (grid[r][c].isAccessible() && hasAccessibleNeighbor(r, c)) {
                partyRow = r;
                partyCol = c;
                // mark initial tile as visited so starting tile won't trigger encounters
                visited[partyRow][partyCol] = true;
                lastMoveFirstVisit = false;
                return;
            }
        }

        // Fallback: scan board for any accessible tile with accessible neighbor
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].isAccessible() && hasAccessibleNeighbor(r, c)) {
                    partyRow = r;
                    partyCol = c;
                    visited[partyRow][partyCol] = true;
                    lastMoveFirstVisit = false;
                    return;
                }
            }
        }

        // Final fallback: pick any accessible tile
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (grid[r][c].isAccessible()) {
                    partyRow = r;
                    partyCol = c;
                    visited[partyRow][partyCol] = true;
                    lastMoveFirstVisit = false;
                    return;
                }
            }
        }
        // No accessible tiles at all.
        partyRow = 0;
        partyCol = 0;
    }

    /**
     * Check if the tile at (r, c) has at least one accessible neighbor.
     * 
     * @param r row index
     * @param c column index
     * @return true if at least one neighbor is accessible, false otherwise
     */
    private boolean hasAccessibleNeighbor(int r, int c) {
        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        for (int[] d : dirs) {
            int nr = r + d[0];
            int nc = c + d[1];
            if (isInside(nr, nc) && grid[nr][nc].isAccessible()) {
                return true;
            }
        }
        return false;
    }

    /* ===================== Movement ===================== */

    /**
     * Move the party up by one tile.
     * 
     * @return true if move was successful, false otherwise
     */
    public boolean moveUp() {
        return moveTo(partyRow - 1, partyCol);
    }

    /**
     * Move the party down by one tile.
     * 
     * @return true if move was successful, false otherwise
     */
    public boolean moveDown() {
        return moveTo(partyRow + 1, partyCol);
    }

    /**
     * Move the party left by one tile.
     * 
     * @return true if move was successful, false otherwise
     */
    public boolean moveLeft() {
        return moveTo(partyRow, partyCol - 1);
    }

    /**
     * Move the party right by one tile.
     * 
     * @return true if move was successful, false otherwise
     */
    public boolean moveRight() {
        return moveTo(partyRow, partyCol + 1);
    }

    /**
     * Move the party to the specified tile if possible.
     * 
     * @param newRow the row index of the target tile
     * @param newCol the column index of the target tile
     * @return true if move was successful, false otherwise
     */
    private boolean moveTo(int newRow, int newCol) {
        if (!isInside(newRow, newCol)) {
            System.out.println("You can't move outside the map!");
            return false;
        }
        if (!grid[newRow][newCol].isAccessible()) {
            System.out.println("That tile is inaccessible!");
            return false;
        }
        // determine if this tile is a first visit
        boolean firstVisit = !visited[newRow][newCol];

        partyRow = newRow;
        partyCol = newCol;

        // mark visited now
        visited[partyRow][partyCol] = true;
        lastMoveFirstVisit = firstVisit;

        return true;
    }

    /**
     * Check if the given position is inside the board boundaries.
     * 
     * @param r row index
     * @param c column index
     * @return true if inside the board, false otherwise
     */
    private boolean isInside(int r, int c) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }

    /* ===================== Accessors ===================== */

    /**
     * Get the size of the board.
     * 
     * @return size of the board
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the row index of the party's current position.
     * 
     * @return row index of the party
     */
    public int getPartyRow() {
        return partyRow;
    }

    /**
     * Get the column index of the party's current position.
     * 
     * @return column index of the party
     */
    public int getPartyCol() {
        return partyCol;
    }

    /**
     * Get the tile where the party is currently located.
     * 
     * @return current Tile of the party
     */
    public Tile getCurrentTile() {
        return grid[partyRow][partyCol];
    }

    /**
     * Get the tile at the specified position.
     * 
     * @param r row index
     * @param c column index
     * @return Tile at (r, c) or null if out of bounds
     */
    public Tile getTile(int r, int c) {
        if (!isInside(r, c)) {
            return null;
        }
        return grid[r][c];
    }

    /**
     * Return whether a tile was visited previously.
     * 
     * @param r row index
     * @param c column index
     * @return true if visited before, false otherwise
     */
    public boolean isVisited(int r, int c) {
        if (!isInside(r, c)) return false;
        return visited[r][c];
    }

    /**
     * Return whether the last successful move landed on a previously unvisited tile.
     * 
     * @return true if last move was first visit, false otherwise
     */
    public boolean wasLastMoveFirstVisit() {
        return lastMoveFirstVisit;
    }

    /**
     * Create a simple character grid representing the current board layout and visit state.
     * 'H' for hero position, 'M' for market, 'X' for inaccessible, '*' for visited common,
     * '.' for unvisited common.
     *
     * @return copy of the current layout as chars
     */
    public char[][] copyLayout() {
        char[][] layout = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == partyRow && j == partyCol) {
                    layout[i][j] = 'H';
                } else {
                    Tile t = grid[i][j];
                    if (t instanceof MarketTile) {
                        layout[i][j] = 'M';
                    } else if (t instanceof InaccessibleTile) {
                        layout[i][j] = 'X';
                    } else {
                        layout[i][j] = visited[i][j] ? '*' : '.';
                    }
                }
            }
        }
        return layout;
    }


    /* ===================== Display ===================== */

    /**
     * Display the board in the console with color coding.
     * 'H' for hero position (yellow)
     * 'M' for market tiles (green)
     * 'X' for inaccessible tiles (red)
     * '*' for visited common tiles (cyan)
     * '.' for unvisited common tiles (white)
     */
    public void display() {
        System.out.println(Color.title("=== World Map ==="));
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String cell;
                if (i == partyRow && j == partyCol) {
                    cell = Color.YELLOW + "H" + Color.RESET;
                } else {
                    Tile t = grid[i][j];
                    if (t instanceof MarketTile) {
                        cell = Color.GREEN + "M" + Color.RESET;
                    } else if (t instanceof InaccessibleTile) {
                        cell = Color.RED + "X" + Color.RESET;
                    } else {
                        // common tile
                        boolean tileVisited = isVisited(i, j); // from the new visited-tracking
                        char symbol = tileVisited ? '*' : '.';
                        cell = (tileVisited ? Color.CYAN : Color.WHITE) + symbol + Color.RESET;
                    }
                }
                System.out.print("| " + cell + " ");
            }
            System.out.println("|");
        }
        System.out.println(Color.title("================="));
    }
}