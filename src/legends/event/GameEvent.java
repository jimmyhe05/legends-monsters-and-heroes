package legends.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple event envelope carrying a type and optional payload map.
 */
public class GameEvent {
    private final GameEventType type;
    private final Map<String, Object> payload;

    public GameEvent(GameEventType type) {
        this(type, Collections.<String, Object>emptyMap());
    }

    public GameEvent(GameEventType type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload == null ? Collections.<String, Object>emptyMap() : new HashMap<>(payload);
    }

    public GameEventType getType() {
        return type;
    }

    public Map<String, Object> getPayload() {
        return Collections.unmodifiableMap(payload);
    }

    public Object get(String key) {
        return payload.get(key);
    }
}
