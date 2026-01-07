package legends.event;

/**
 * Functional interface for event listeners.
 */
@FunctionalInterface
public interface EventListener {
    void onEvent(GameEvent event);
}
