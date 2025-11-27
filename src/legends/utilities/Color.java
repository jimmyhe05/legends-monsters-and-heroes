package legends.utilities;

/**
 * ANSI color codes and helper methods for styling console output.
 * Works on most UNIX-like terminals (including macOS Terminal).
 */
public final class Color {

    // Reset
    public static final String RESET  = "\u001B[0m";

    // Regular colors
    public static final String BLACK  = "\u001B[30m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN   = "\u001B[36m";
    public static final String WHITE  = "\u001B[37m";

    // Styles
    public static final String BOLD      = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";

    private Color() { }

    // Convenience helpers for common UI elements
    public static String heroName(String name) {
        return BOLD + CYAN + name + RESET;
    }

    public static String monsterName(String name) {
        return BOLD + RED + name + RESET;
    }

    public static String gold(double amount) {
        return YELLOW + amount + RESET;
    }

    public static String title(String msg) {
        return BOLD + PURPLE + msg + RESET;
    }

    public static String success(String msg) {
        return GREEN + msg + RESET;
    }

    public static String warning(String msg) {
        return YELLOW + msg + RESET;
    }

    public static String error(String msg) {
        return RED + msg + RESET;
    }
}
