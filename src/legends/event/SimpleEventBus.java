package legends.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * A lightweight, in-memory event bus. Not thread-safe, intended for single-threaded game loop use.
 */
public class SimpleEventBus implements EventBus {

    private final Map<GameEventType, List<EventListener>> listeners = new EnumMap<>(GameEventType.class);

    @Override
    public void register(GameEventType type, EventListener listener) {
        if (type == null || listener == null) {
            return;
        }
        listeners.computeIfAbsent(type, t -> new ArrayList<EventListener>()).add(listener);
    }

    @Override
    public void unregister(GameEventType type, EventListener listener) {
        if (type == null || listener == null) {
            return;
        }
        List<EventListener> list = listeners.get(type);
        if (list != null) {
            list.remove(listener);
            if (list.isEmpty()) {
                listeners.remove(type);
            }
        }
    }

    @Override
    public void publish(GameEvent event) {
        if (event == null || event.getType() == null) {
            return;
        }
        List<EventListener> list = listeners.get(event.getType());
        if (list == null || list.isEmpty()) {
            return;
        }
        // copy to avoid concurrent modification if listeners adjust subscriptions
        List<EventListener> snapshot = new ArrayList<>(list);
        for (EventListener l : snapshot) {
            try {
                l.onEvent(event);
            } catch (Exception e) {
                // swallow exceptions to avoid breaking the loop; log minimally
                System.err.println("[EventBus] Listener error for " + event.getType() + ": " + e.getMessage());
            }
        }
    }
}
