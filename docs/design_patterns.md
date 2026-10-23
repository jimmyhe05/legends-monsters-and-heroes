# Design Patterns in Legends: Monsters & Heroes

Each entry lists the problem, where the pattern lives, and its current status.

## Strategy: monster targeting (in use)
- **Problem:** how a monster picks which hero to attack should be changeable without editing the battle loop.
- **Participants:** `HeroTargetStrategy` (interface), `RandomHeroTargetStrategy`, `LowestHpTargetStrategy`, `Battle`.
- **How it works:** `Battle` receives a strategy through its constructor and calls `selectTarget(heroes)` on each monster's turn. `Game` picks random targeting by default and lowest-HP targeting on hard difficulty. `LowestHpTargetStrategy` accepts a `Random` so tests can use a seeded generator.
- **Extend:** implement `HeroTargetStrategy` and return it from `Game.targetStrategyFor`.

## Template Method: leveling up (in use)
- **Problem:** every hero levels up the same way, but each class favors different stats.
- **Participants:** `Hero.gainExperience` (fixed algorithm), abstract `Hero.levelUp` (step), `Warrior`, `Paladin`, `Sorcerer`.

## Factory: loading content (in use)
- **Problem:** game code should not parse files or decide which concrete class to build.
- **Participants:** `DataLoader` (builds heroes, monsters, and items from `data/`, choosing `HealthPotion`, `ManaPotion`, or `AttributePotion` by attribute), `MonsterFactory` (builds a fresh monster from a loaded template at the party's level).

## Memento: save and load (in use)
- **Problem:** save and restore a session without tying the save format to every live class.
- **Participants:** `GameState`, `HeroSnapshot`, `ItemSnapshot`, `MarketSnapshot`, `ItemCatalog`, `SaveLoadManager`.
- **How it works:** snapshots hold only the needed state in final fields; items are saved by name and rebuilt from the catalog on load. Saves use Java serialization to `saves/latest.dat`.

## Observer: event bus (groundwork, not wired in)
- **Participants:** `EventBus`, `SimpleEventBus` (an `EnumMap` of listener lists, iterates over a copy, isolates listener exceptions), `EventListener`, `GameEvent`, `GameEventType`.
- **Status:** nothing publishes or subscribes yet. Planned first use: sound effects as listeners on battle and market events.

## Strategy: encounters and board generation (groundwork, not wired in)
- **Participants:** `EncounterStrategy` with default, low-risk, and high-risk versions; `BoardGenerationStrategy`.
- **Status:** defined for future difficulty modes; not yet used by `Game` or `Board`.
