package legends.state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import legends.entities.heroes.Hero;
import legends.entities.heroes.Paladin;
import legends.entities.heroes.Sorcerer;
import legends.entities.heroes.Warrior;
import legends.game.Board;
import legends.game.DataLoader;
import legends.game.Difficulty;
import legends.game.Market;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

/**
 * Handles saving/loading of game state (memento style).
 */
public class SaveLoadManager {
    private final ItemCatalog itemCatalog = new ItemCatalog();
    private final Map<String, Hero> heroPrototypes = new HashMap<>();

    public SaveLoadManager() {
        loadHeroPrototypes();
    }

    /* --------------------------- Public API --------------------------- */

    public boolean save(GameState state, String filePath) {
        if (state == null || filePath == null || filePath.isEmpty()) {
            return false;
        }
        try {
            File target = new File(filePath);
            File parent = target.getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(target))) {
                oos.writeObject(state);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Failed to save game: " + e.getMessage());
            return false;
        }
    }

    public SaveResult load(String filePath) {
        if (filePath == null || filePath.isEmpty()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = ois.readObject();
            if (!(obj instanceof GameState state)) {
                return null;
            }
            Difficulty difficulty = state.getDifficulty() == null ? Difficulty.NORMAL : state.getDifficulty();
            Map<String, Market> markets = MarketSnapshot.toMarkets(state.getMarketSnapshots(), itemCatalog);
            Board board = new Board(state.getBoardLayout(), markets);
            List<Hero> heroes = new ArrayList<>();
            if (state.getParty() != null) {
                for (HeroSnapshot snap : state.getParty()) {
                    Hero h = rebuildHero(snap);
                    if (h != null) heroes.add(h);
                }
            }
            return new SaveResult(board, heroes, difficulty);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game: " + e.getMessage());
            return null;
        }
    }

    /* ------------------------ Reconstruction ------------------------- */

    private void loadHeroPrototypes() {
        String base = "data/heroes/";
        for (Warrior w : DataLoader.loadWarriors(base + "Warriors.txt")) {
            heroPrototypes.put(w.getName(), w);
        }
        for (Paladin p : DataLoader.loadPaladins(base + "Paladins.txt")) {
            heroPrototypes.put(p.getName(), p);
        }
        for (Sorcerer s : DataLoader.loadSorcerers(base + "Sorcerers.txt")) {
            heroPrototypes.put(s.getName(), s);
        }
    }

    private Hero rebuildHero(HeroSnapshot snap) {
        if (snap == null) return null;
        Hero proto = heroPrototypes.get(snap.getName());
        if (proto == null) return null;
        Hero hero = instantiateHero(proto);
        if (hero == null) return null;

        hero.setLevel(snap.getLevel());
        hero.setHp(snap.getHp());
        hero.setMp(snap.getMp());
        hero.setStrength(snap.getStrength());
        hero.setDexterity(snap.getDexterity());
        hero.setAgility(snap.getAgility());
        hero.setGold(snap.getGold());
        hero.setExperience(snap.getExperience());

        restoreInventoryAndEquipment(hero, snap);
        return hero;
    }

    private Hero instantiateHero(Hero proto) {
        if (proto instanceof Warrior w) {
            return new Warrior(w.getName(), w.getMp(), w.getStrength(), w.getAgility(), w.getDexterity(), w.getGold(), w.getExperience());
        }
        if (proto instanceof Paladin p) {
            return new Paladin(p.getName(), p.getMp(), p.getStrength(), p.getAgility(), p.getDexterity(), p.getGold(), p.getExperience());
        }
        if (proto instanceof Sorcerer s) {
            return new Sorcerer(s.getName(), s.getMp(), s.getStrength(), s.getAgility(), s.getDexterity(), s.getGold(), s.getExperience());
        }
        return null;
    }

    private void restoreInventoryAndEquipment(Hero hero, HeroSnapshot snap) {
        Inventory inv = hero.getInventory();
        if (snap.getInventoryItems() != null) {
            for (ItemSnapshot itemSnap : snap.getInventoryItems()) {
                String type = itemSnap.getType();
                String name = itemSnap.getName();
                if ("Weapon".equalsIgnoreCase(type)) {
                    Weapon w = itemCatalog.copyWeapon(name);
                    if (w != null) inv.addWeapon(w);
                } else if ("Armor".equalsIgnoreCase(type)) {
                    Armor a = itemCatalog.copyArmor(name);
                    if (a != null) inv.addArmor(a);
                } else if ("Potion".equalsIgnoreCase(type)) {
                    Potion p = itemCatalog.copyPotion(name);
                    if (p != null) inv.addPotion(p);
                } else if ("Spell".equalsIgnoreCase(type)) {
                    Spell s = itemCatalog.copySpell(name);
                    if (s != null) inv.addSpell(s);
                }
            }
        }

        // Equip saved weapon/armor if present in inventory
        if (snap.getWeaponName() != null) {
            for (Weapon w : inv.getWeapons()) {
                if (snap.getWeaponName().equals(w.getName())) {
                    hero.equipWeapon(w);
                    break;
                }
            }
        }
        if (snap.getArmorName() != null) {
            for (Armor a : inv.getArmors()) {
                if (snap.getArmorName().equals(a.getName())) {
                    hero.equipArmor(a);
                    break;
                }
            }
        }
    }

    /* --------------------------- DTO --------------------------- */
    public static class SaveResult {
        private final Board board;
        private final List<Hero> heroes;
        private final Difficulty difficulty;

        public SaveResult(Board board, List<Hero> heroes, Difficulty difficulty) {
            this.board = board;
            this.heroes = heroes;
            this.difficulty = difficulty;
        }

        public Board getBoard() { return board; }
        public List<Hero> getHeroes() { return heroes; }
        public Difficulty getDifficulty() { return difficulty; }
    }
}
