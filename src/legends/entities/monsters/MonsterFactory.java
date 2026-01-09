package legends.entities.monsters;

/**
 * Factory for creating monsters from prototypes at a desired level.
 */
public class MonsterFactory {
    public static Monster cloneAtLevel(Dragon proto, int level) {
        if (proto == null) return null;
        return new Dragon(proto.getName(), level, proto.getBaseDamage(), proto.getDefense(), proto.getDodgeChance());
    }

    public static Monster cloneAtLevel(Spirit proto, int level) {
        if (proto == null) return null;
        return new Spirit(proto.getName(), level, proto.getBaseDamage(), proto.getDefense(), proto.getDodgeChance());
    }

    public static Monster cloneAtLevel(Exoskeleton proto, int level) {
        if (proto == null) return null;
        return new Exoskeleton(proto.getName(), level, proto.getBaseDamage(), proto.getDefense(), proto.getDodgeChance());
    }
}
