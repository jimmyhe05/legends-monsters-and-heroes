  # Legends: Monsters & Heroes

  Console-based RPG implemented in Java, emphasizing object-oriented design, clean architecture, and extensibility. Built as a software engineering project and presented as a portfolio artifact for evaluating design quality, maintainability, and system structure.

  ## Table of Contents
- [Legends: Monsters \& Heroes](#legends-monsters--heroes)
  - [Table of Contents](#table-of-contents)
  - [Project Overview](#project-overview)
  - [Why This Project](#why-this-project)
  - [Key Features](#key-features)
  - [Architecture Overview](#architecture-overview)
  - [Object-Oriented Design](#object-oriented-design)
  - [Design Patterns Used](#design-patterns-used)
  - [Game Flow](#game-flow)
  - [Data-Driven Design](#data-driven-design)
  - [Sound \& ASCII Art System](#sound--ascii-art-system)
  - [Save / Load System](#save--load-system)
  - [Extensibility \& Future Enhancements](#extensibility--future-enhancements)
  - [How to Run](#how-to-run)
  - [Project Structure](#project-structure)
  - [Technologies Used](#technologies-used)
  - [Screenshots / Demo](#screenshots--demo)
  - [License / Academic Disclaimer](#license--academic-disclaimer)

  ## Project Overview
  - Grid-based exploration, turn-based combat, and a market economy rendered with ASCII UI.
  - Data-driven configuration (heroes, monsters, items, spells, potions) from external text files.
  - Sound effects and music via the Java Sound API with runtime toggles.
  - Save/load support to persist party, board state, and inventory snapshots.
  - Designed for clarity, separation of concerns, and extension beyond the initial assignment scope.

  ## Why This Project
  This project was built to demonstrate real-world object-oriented software design in an interactive system, emphasizing clean architecture, extensibility, and maintainability over graphical complexity.

  ## Key Features
  - Turn-based battle system with physical and spell attacks, potions, flee logic, and level-up flow.
  - Grid/world map with common, market, and inaccessible tiles; random encounters on common tiles.
  - Market system for buying, selling, and repairing equipment with level/gold validation.
  - Multiple hero and monster families using inheritance and polymorphism for behaviors and stat scaling.
  - Inventory and equipment management that immediately affects combat calculations.
  - Spell and potion subsystems with typed effects (fire/ice/lightning spells, multi-attribute potions).
  - ASCII presentation for maps, menus, and colored highlights; sound cues for key events.
  - Save/load commands to persist and resume sessions.

  ## Architecture Overview
  - **Game loop and orchestration**: `Game` coordinates input, board navigation, encounters, battles, markets, and persistence.
  - **Domain model**: Heroes, monsters, items, spells, potions, and tiles are rich domain objects with encapsulated behavior.
  - **Board and world**: `Board` owns tile generation and rendering, separating world state from game flow.
  - **Battle engine**: `Battle` executes deterministic turn order, regeneration between rounds, and outcome handling.
  - **Services**: `SoundService` for audio playback/toggling; `SaveLoadManager` for serialization; `EventBus` for decoupled notifications.
  - **State snapshots**: `GameState`, `HeroSnapshot`, `ItemSnapshot`, and `MarketSnapshot` isolate serialization concerns from live objects.

  ## Object-Oriented Design
  - **Inheritance & polymorphism**: Hero subclasses (Warrior/Paladin/Sorcerer) and monster families (Dragon/Spirit/Exoskeleton) override stat scaling and combat behaviors; spell families add distinct secondary effects.
  - **Encapsulation**: Combat calculations, regeneration, dodge, and damage mitigation are confined to entity classes; board movement rules are encapsulated in tile/board classes.
  - **Composition**: Heroes own inventories, equipment, and stats; markets operate on shared `ItemCatalog` data without leaking UI concerns.
  - **Interfaces/abstractions**: Event listener interfaces and item/hero abstractions make it straightforward to add new behaviors without editing consumers.

  ## Design Patterns Used
  - **Factory/Data Loader**: `DataLoader` and `ItemCatalog` parse hero/monster/item files into typed objects, isolating file formats from gameplay code.
  - **Strategy/Polymorphic behavior**: Hero types, monster families, and spell classes encapsulate specialized calculations without condition-heavy branching.
  - **Observer (lightweight)**: `EventBus` with `GameEvent`/`GameEventType` enables decoupled notifications (e.g., save events, audio hooks) without coupling to the game loop.
  - **Service layer**: Cross-cutting concerns like sound (`SoundService`) and persistence (`SaveLoadManager`) live in dedicated services for testability and future swaps.
  - **Template-like flows**: Battle sequencing (heroes then monsters with regeneration) and market transactions follow structured steps with overridable per-entity logic.

  ## Game Flow
  1. Launch the game and configure board size (default 8x8).
  2. Build a party of 1–3 heroes from distinct classes.
  3. Explore the grid (W/A/S/D), triggering markets or random encounters on common tiles.
  4. In battle, select per-hero actions (attack, cast, potion, flee, change equipment). Monsters act after all heroes.
  5. Post-battle, heroes level up, revive if victorious, and receive gold/experience.
  6. Use markets to buy/sell/repair equipment and restock potions/spells.
  7. Save (`P`) and load (`O`) to persist or restore progress.

  ## Data-Driven Design
  - All hero, monster, and item definitions live in `data/` as human-readable text files.
  - `DataLoader` and `ItemCatalog` convert rows into typed domain objects, enabling balance changes or content additions without recompiling.
  - Board generation and monster selection leverage data-driven parameters (levels, dodge, damage) rather than hard-coded constants.

  ## Sound & ASCII Art System
  - ASCII rendering for boards, menus, and stats keeps the UI terminal-friendly and deterministic.
  - Sound cues (intro, movement, battle start, victory/defeat, flee, hero down) are played via `SoundService`, with runtime toggle (`V`/`B`) and graceful fallback when assets are missing.
  - Background and battle music are looped/paused to avoid clashes with outcome SFX.

  ## Save / Load System
  - `SaveLoadManager` serializes `GameState` snapshots (party, board position, inventory/equipment, market map) to disk (`saves/latest.dat` by default).
  - Load flow rebuilds the board, heroes, items, and markets from snapshots, decoupled from live objects to prevent partial state.
  - Simple file paths allow multiple save slots; errors are surfaced without crashing the session.

  ## Extensibility & Future Enhancements
  - Add new hero/monster families or rebalance stats by extending subclasses and updating data files.
  - Introduce additional item/spell types by subclassing `Item`/`Spell` and adding rows to data files.
  - Swap the UI layer (e.g., graphical front-end) while retaining the domain, battle, and data layers.
  - Enrich the event system with analytics/logging subscribers for debugging or telemetry.
  - Add automated tests around battle math, market validation, and save/load to guard future changes.

  ## How to Run
  Prerequisites:
  - JDK 17+ (tested on macOS terminal; also works on Linux/Windows shells).
  - Optional: place WAV files under `assets/sounds/` to enable audio cues.

  Build and run:
  ```sh
  javac -d out $(find src -name "*.java")
  java -cp out legends.Main
  ```

  ## Project Structure
  ```text
  legends-monsters-and-heroes/
  ├─ src/
  │  ├─ legends/Main.java
  │  ├─ legends/game/        # Game loop, board, battle, market, data loading
  │  ├─ legends/entities/    # Heroes, monsters, stats
  │  ├─ legends/items/       # Weapons, armor, potions, spells, inventories
  │  ├─ legends/state/       # Snapshots and SaveLoadManager
  │  ├─ legends/event/       # Event bus and event types
  │  └─ legends/utilities/   # Color, input helpers, sound service
  ├─ data/
  │  ├─ heroes/              # Warrior/Paladin/Sorcerer definitions
  │  ├─ monsters/            # Dragon/Spirit/Exoskeleton definitions
  │  └─ items/               # Weapons, armors, potions, spells
  ├─ assets/sounds/          # Optional WAV assets (not tracked)
  └─ saves/                  # Save files (created at runtime)
  ```

  ## Technologies Used
  - Java (console application)
  - Java Sound API for audio playback
  - Plain-text data configuration files
  - ANSI escape codes for terminal styling

  ## Screenshots / Demo
  - Placeholder: capture gameplay, market, and battle screens and link here for reviewers.

  ## License / Academic Disclaimer
  - Built as part of an academic assignment; provided for educational and portfolio purposes.
  - No warranty or production support implied. Verify academic integrity policies before reuse.
