package legends.event;

import java.util.function.Consumer;

/**
 * Basic event bus contract for publishing and subscribing to game events.
 */
public interface EventBus {

    /**
     * Subscribe a listener to a specific event type.
     * @param type event type
     * @param listener listener to invoke
     */
    void register(GameEventType type, EventListener listener);

    /**
     * Subscribe a simple consumer that takes only the event payload object.
     * Convenience overload.
     */
    default void register(GameEventType type, Consumer<GameEvent> consumer) {
        register(type, (EventListener) consumer::accept);
    }

    /**
     * Remove a listener subscription.
     */
    void unregister(GameEventType type, EventListener listener);

    /**
     * Publish an event to all listeners of its type.
     */
    void publish(GameEvent event);

    /**
     * Publish an event with no payload.
     */
    default void publish(GameEventType type) {
        publish(new GameEvent(type));
    }
}
