package legends.event;

/**
 * Enumeration of high-level game events. More specific detail can be carried
 * in the {@link GameEvent#payload} map.
 */
public enum GameEventType {
    GAME_STARTED,
    GAME_QUIT,
    GAME_SAVED,
    GAME_LOADED,

    MODE_EXPLORATION,
    MODE_MARKET,
    MODE_BATTLE,

    MARKET_ENTERED,
    MARKET_EXITED,
    ITEM_PURCHASED,
    ITEM_SOLD,
    ITEM_REPAIRED,

    BATTLE_STARTED,
    BATTLE_ENDED,
    ROUND_STARTED,
    ROUND_ENDED,
    HERO_ATTACK,
    MONSTER_ATTACK,
    HERO_CAST_SPELL,
    HERO_USE_POTION,
    HERO_FAINTED,
    MONSTER_DEFEATED,
    PARTY_FLED,
    PARTY_VICTORY,
    PARTY_DEFEAT
}
