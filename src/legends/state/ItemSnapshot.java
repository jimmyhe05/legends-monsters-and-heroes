package legends.state;

/**
 * Lightweight item snapshot identified primarily by name and type.
 */
public class ItemSnapshot {
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
