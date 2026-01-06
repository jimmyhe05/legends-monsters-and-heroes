package legends.entities;

/**
 * Common abstraction for all battle participants (heroes and monsters).
 */
public interface Combatant {
    String getDisplayName();
    double getHp();
    int getLevel();
    boolean isDefeated();
}
