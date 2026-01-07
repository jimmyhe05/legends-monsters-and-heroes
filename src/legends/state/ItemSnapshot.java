package legends.state;

import java.io.Serializable;

/**
 * Lightweight item snapshot identified primarily by name and type.
 */
public class ItemSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String type;

    public ItemSnapshot(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
