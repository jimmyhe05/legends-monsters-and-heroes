# CS611-Assignment 4
## Legends, Monsters, and Heroes
---------------------------------------------------------------------------
- Name: Jimmy He
- Email: jimmyhe@bu.edu
- Student ID: U52815144

## Files
---------------------------------------------------------------------------
Below is a list of the main Java source files included in this submission and a short description for each one.

- `src/legends/Main.java`  
  Entry point. Creates a `Game` instance and starts a new Legends: Monsters & Heroes session.

- `src/legends/game/Game.java`  
  Core game controller:
  - Loads hero, monster, and item data from the `data/` directory.
  - Handles hero selection and party management.
  - Manages the board, movement commands, random encounters, and the main game loop.
  - Routes to `Battle` and `Market` when appropriate.

- `src/legends/game/Board.java`  
  Represents the world map:
  - Generates an `N x N` grid with common, market, and inaccessible tiles.
  - Tracks hero position and visited tiles.
  - Renders the map and movement controls.

- `src/legends/game/Tile.java` and subclasses  
  - `CommonTile.java`: normal walkable tile; may trigger battles.  
  - `MarketTile.java`: tile with a market.  
  - `InaccessibleTile.java`: blocked tile the hero cannot enter.

- `src/legends/game/Market.java`  
  Market system:
  - Lets the player choose which hero uses the market.
  - Supports buying, selling, and repairing items with level and gold checks.
  - Shows items in aligned tables (weapons, armors, potions, spells).
  - Immediately updates hero gold and inventory-related info.

- `src/legends/game/Battle.java`  
  Turn-based battle engine:
  - Runs hero and monster turns.
  - Supports basic attacks, spell casting, potion use, and running away.
  - Prints round-by-round status for all heroes and monsters.
  - Handles experience/gold rewards and level-ups after victories.

- `src/legends/entities/heroes/Hero.java` and subclasses  
  - `Hero.java`: abstract base for all heroes.
    - Holds stats (level, HP, MP, strength, dexterity, agility, gold).
    - Manages inventory and equipped weapon/armor.
    - Implements common logic such as taking damage, dodging, regenerating, and leveling.
  - `Warrior.java`, `Paladin.java`, `Sorcerer.java`:
    - Concrete hero types with different favored attributes and level-up rules.
    - Implement `attack` and `castSpell` behavior.

- `src/legends/entities/monsters/Monster.java` and subclasses  
  - `Monster.java`: base monster with HP, base damage, defense, and dodge chance.
  - `Dragon.java`, `Spirit.java`, `Exoskeleton.java`: different monster families with distinct stats.

- `src/legends/items/Item.java`  
  Base class for all items with common fields such as name, cost, required level, and remaining uses.

- `src/legends/items/Weapon.java`  
  Weapons:
  - Provide damage and number of hands required (one- or two-handed).
  - Affect the hero’s basic attack damage when equipped.

- `src/legends/items/Armor.java`  
  Armor:
  - Provides damage reduction.
  - Reduces incoming physical damage when equipped.

- `src/legends/items/Potion.java`  
  Potions:
  - Buff one or more hero attributes (health, mana, strength, dexterity, agility, etc.).
  - Have limited uses; are removed when they run out.

- `src/legends/items/Spell.java`, `FireSpell.java`, `IceSpell.java`, `LightningSpell.java`  
  Spells:
  - Store base damage, mana cost, and required level.
  - Different spell families (fire, ice, lightning) follow the project spec for extra effects.

- `src/legends/items/Inventory.java`  
  Per-hero inventory container:
  - Holds lists of weapons, armors, potions, and spells.
  - Used by the market, battle system, and hero equipment menus.

- `src/legends/utilities/Color.java`  
  ANSI color utility used to style console output:
  - Colored hero and monster names.
  - Highlighted titles, errors, and gold amounts.

- `src/legends/utilities/InputUtil.java`, `RandomUtil.java`  
  Small helpers for input parsing and random selection.

- `data/heroes/*.txt`  
  Hero data files (`Warriors.txt`, `Paladins.txt`, `Sorcerers.txt`) containing base stats and starting gold.

- `data/monsters/*.txt`  
  Monster data files (`Dragons.txt`, `Spirits.txt`, `Exoskeletons.txt`) with monster stats.

- `data/items/*.txt`  
  Item data files:
  - `Weaponry.txt`, `Armory.txt`, `Potions.txt`, `FireSpells.txt`, `IceSpells.txt`, `LightningSpells.txt`.


## Notes
---------------------------------------------------------------------------
Some highlights and design notes for this implementation:

1. **Hero selection and party management**  
  - The player can add 1–3 heroes of different types (Warrior, Sorcerer, Paladin).  
  - The same hero cannot be added twice; there is also an option to remove a hero from the party before starting the game.

2. **Turn-based combat with clear feedback**  
  - Battles are fully turn-based: each hero acts, then each monster acts.  
  - Combat messages show actual HP lost, dodges, and when monsters are defeated.  
  - Heroes regenerate a small percentage of HP and MP at the end of each round, rounded to one decimal place for readability.

3. **Market with table-style UI**  
  - Markets allow buying, selling, and repairing items, with level and gold checks.  
  - Items are displayed in aligned tables (Idx, Name, Lvl, Cost, etc.), and item names are cleaned up for display (underscores replaced with spaces).

4. **Equipment and inventory**  
  - Each hero has an inventory with weapons, armors, potions, and spells.  
  - Equipping a weapon or armor immediately affects battle damage and defense.  
  - Broken items can be repaired in the market for half of their original cost.

5. **Hero progression and revival**  
  - Heroes gain experience and automatically level up when they reach the XP threshold, increasing their stats according to their class.  
  - If a hero faints (HP reaches 0), they are revived at half HP and MP after the battle if the party wins.

6. **Running away and penalties**  
  - The party can attempt to flee from a battle with a fixed success chance.  
  - On a successful escape, non-fainted heroes lose a small percentage of their gold as a penalty.

7. **Console formatting and colors**  
  - Output is formatted into aligned columns for hero/monster stats and market lists to keep things readable in a monospaced terminal.  
  - ANSI colors are used to highlight heroes, monsters, titles, errors, and gold. The game still works in terminals without color support, just without styling.


## How to compile and run
---------------------------------------------------------------------------

1. Navigate to the directory "legends-monsters-and-heroes" after unzipping the files
2. Run the following instructions: 
<!-- javac -d out $(find src -name "*.java") && java -cp out legends.Main -->
javac --release 8 -d out $(find src -name "*.java") && java -cp out legends.Main

## Demo

You can watch a short demo run of the game here: [example-run.mov](./example-run.mov)

## Input/Output Example
---------------------------------------------------------------------------
===========================================
WELCOME TO LEGENDS: MONSTERS & HEROES
===========================================
Lead a party of heroes, explore the land, visit markets,
and fight terrifying monsters in turn-based battles.


Loaded heroes:
  Warriors:  6
  Paladins:  6
  Sorcerers: 6

Loaded monsters:
  Dragons:      12
  Spirits:      11
  Exoskeletons: 12


Choose 1–3 heroes for your party.

Hero types:
  Warrior  - Favored on strength and agility.
  Sorcerer - Favored on dexterity and agility.
  Paladin  - Favored on strength and dexterity.


1. Add Warrior
2. Add Paladin
3. Add Sorcerer
4. Finish selection
5. Remove hero from party

Enter choice: 1

Available heroes:

  Gaerdal Ironhand     [Lvl 1 | HP=100.0 | MP=100.0 | STR=700.0 | DEX=600.0 | AGI=500.0 | Gold=1354.0]
  Sehanine Monnbow     [Lvl 1 | HP=100.0 | MP=600.0 | STR=700.0 | DEX=500.0 | AGI=800.0 | Gold=2500.0]
  Muamman Duathall     [Lvl 1 | HP=100.0 | MP=300.0 | STR=900.0 | DEX=750.0 | AGI=500.0 | Gold=2546.0]
  Flandal Steelskin    [Lvl 1 | HP=100.0 | MP=200.0 | STR=750.0 | DEX=700.0 | AGI=650.0 | Gold=2500.0]
  Undefeated Yoj       [Lvl 1 | HP=100.0 | MP=400.0 | STR=800.0 | DEX=700.0 | AGI=400.0 | Gold=2500.0]
  Eunoia Cyn           [Lvl 1 | HP=100.0 | MP=400.0 | STR=700.0 | DEX=600.0 | AGI=800.0 | Gold=2500.0]

Choose hero index (or 0 to cancel): 1

Gaerdal Ironhand (Warrior) joined the party!

Your party (1 hero):
  Gaerdal Ironhand (Warrior)


1. Add Warrior
2. Add Paladin
3. Add Sorcerer
4. Finish selection
5. Remove hero from party

Enter choice: 2

Available heroes:

  Parzival             [Lvl 1 | HP=100.0 | MP=300.0 | STR=750.0 | DEX=700.0 | AGI=650.0 | Gold=2500.0]
  Sehanine Moonbow     [Lvl 1 | HP=100.0 | MP=300.0 | STR=750.0 | DEX=700.0 | AGI=700.0 | Gold=2500.0]
  Skoraeus Stonebones  [Lvl 1 | HP=100.0 | MP=250.0 | STR=650.0 | DEX=350.0 | AGI=600.0 | Gold=2500.0]
  Garl Glittergold     [Lvl 1 | HP=100.0 | MP=100.0 | STR=600.0 | DEX=400.0 | AGI=500.0 | Gold=2500.0]
  Amaryllis Astra      [Lvl 1 | HP=100.0 | MP=500.0 | STR=500.0 | DEX=500.0 | AGI=500.0 | Gold=2500.0]
  Caliber Heist        [Lvl 1 | HP=100.0 | MP=400.0 | STR=400.0 | DEX=400.0 | AGI=400.0 | Gold=2500.0]

Choose hero index (or 0 to cancel): 2

Sehanine Moonbow (Paladin) joined the party!

Your party (2 heroes):
  Gaerdal Ironhand (Warrior)
  Sehanine Moonbow (Paladin)


1. Add Warrior
2. Add Paladin
3. Add Sorcerer
4. Finish selection
5. Remove hero from party

Enter choice: 4

Your party (2 heroes):
  Gaerdal Ironhand (Warrior)
  Sehanine Moonbow (Paladin)


Choose board size (NxN).
Press ENTER for default 8x8.
Or enter a size between 5 and 12: 8


=== World Map ===
| M | M | . | X | M | . | . | M |
| X | M | X | . | . | . | M | . |
| M | . | . | . | . | . | . | . |
| . | . | X | M | M | . | . | . |
| . | . | . | . | X | . | . | X |
| M | . | X | M | . | . | M | . |
| M | X | M | X | M | . | H | . |
| . | . | M | . | M | X | X | . |

Controls: W/A/S/D to move | I: info | E: equip/use | M: market | Q: quit
Enter command: s
That tile is inaccessible!

=== World Map ===
... (map printouts omitted for brevity) ...


You stepped on a MARKET tile. Press 'M' to enter.

=== World Map ===
... (map with H on market) ...

Controls: W/A/S/D to move | I: info | E: equip/use | M: market | Q: quit
Enter command: m

Entering market...


=== Market ===
Choose a hero (or 0 to leave market):

  Idx  Hero                 Lvl      Gold
  1    Gaerdal Ironhand       1    1354.0
  2    Sehanine Moonbow       1    2500.0

Hero index (0=leave): 1


-- Market for Gaerdal Ironhand (Gold=1354.0) --

1. Buy items
2. Sell items
3. Show hero inventory
4. Repair items
5. Back to hero selection

Choose action: 1


What would you like to buy?

1. Weapons
2. Armors
3. Potions
4. Spells
5. Back

Choose category: 1

Available weapons:
  Idx  Name              Lvl      Cost     DMG  Hands
   1   Sword               1     500.0     800      1
   2   Bow                 2     300.0     500      2
   3   Scythe              6    1000.0    1100      2
   4   Axe                 5     550.0     850      1
   5   TSwords             8    1400.0    1600      2
   6   Dagger              1     200.0     250      1

Choose index to buy (0=cancel): 1

Purchased Sword for 500.0 gold.
Gaerdal Ironhand now has 854.0 gold left.
Updated hero info:
  Gaerdal Ironhand [Lvl 1 | HP=100.0 | MP=100.0 | STR=700.0 | DEX=600.0 | AGI=500.0 | Gold=854.0]


... (similar formatted sections for buying armor, potions, spells) ...


=== Party Info ===
  Gaerdal Ironhand  [Lvl 1 | HP=200.0 | MP=100.0 | STR=700.0 | DEX=600.0 | AGI=500.0 | Gold=104.0]
  Sehanine Moonbow  [Lvl 1 | HP=100.0 | MP=300.0 | STR=750.0 | DEX=700.0 | AGI=700.0 | Gold=2500.0]
==================


A group of monsters appears!

=== BATTLE START ===
Heroes vs Monsters!

--- Battle Status ---
Heroes:
  Gaerdal Ironhand  [Lvl 1 | HP=200.0 | MP=100.0 | STR=700.0 | DEX=600.0 | AGI=500.0 | Gold=104.0]
  Sehanine Moonbow  [Lvl 1 | HP=100.0 | MP=300.0 | STR=750.0 | DEX=700.0 | AGI=700.0 | Gold=2500.0]

Monsters:
  Desghidorrah      [Lvl 1 | HP=100.0 | DMG=363.0 | DEF=400.0 | Dodge=35.0%]
  Casper            [Lvl 1 | HP=100.0 | DMG=100.0 | DEF=100.0 | Dodge=60.5%]

--- Heroes' Turn ---

It's Gaerdal Ironhand's turn.

1. Attack
2. Cast Spell
3. Use Potion
4. Change Equipment
5. Run

Choose action: 1
Choose a monster to target:
  1. Desghidorrah [Lvl 1, HP=100.0, DMG=363.0, DEF=400.0, Dodge=35.0%]
  2. Casper       [Lvl 1, HP=100.0, DMG=100.0, DEF=100.0, Dodge=60.5%]

Index: 1
Gaerdal Ironhand attacked Desghidorrah for 66.2 damage!

... (rest of battle, similarly formatted) ...


Heroes win the battle!
Gaerdal_Ironhand leveled up to 2 (Warrior)!
Gaerdal_Ironhand gains 100.0 gold and 4.0 EXP.
Sehanine_Moonbow leveled up to 2 (Paladin)!
Sehanine_Moonbow gains 100.0 gold and 4.0 EXP.

=== BATTLE END ===


=== World Map ===
... (updated map) ...

=== Party Info ===
  Gaerdal Ironhand  [Lvl 2 | HP=200.0 | MP=133.1 | STR=771.8 | DEX=630.0 | AGI=551.3 | Gold=204.0]
  Sehanine Moonbow  [Lvl 2 | HP=200.0 | MP=399.3 | STR=826.9 | DEX=771.8 | AGI=735.0 | Gold=2600.0]
==================


Controls: W/A/S/D to move | I: info | E: equip/use | M: market | Q: quit
Enter command: q

Quitting game. Goodbye!
