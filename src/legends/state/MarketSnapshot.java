package legends.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import legends.game.Market;
import legends.items.Armor;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

/**
 * Snapshot of a market's inventory by item names.
 */
public class MarketSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<ItemSnapshot> weapons;
    private final List<ItemSnapshot> armors;
    private final List<ItemSnapshot> potions;
    private final List<ItemSnapshot> spells;

    public MarketSnapshot(List<ItemSnapshot> weapons, List<ItemSnapshot> armors,
                          List<ItemSnapshot> potions, List<ItemSnapshot> spells) {
        this.weapons = weapons;
        this.armors = armors;
        this.potions = potions;
        this.spells = spells;
    }

    public List<ItemSnapshot> getWeapons() { return weapons; }
    public List<ItemSnapshot> getArmors() { return armors; }
    public List<ItemSnapshot> getPotions() { return potions; }
    public List<ItemSnapshot> getSpells() { return spells; }

    public static MarketSnapshot fromMarket(Market market) {
        if (market == null) return new MarketSnapshot(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        List<ItemSnapshot> weaponSnaps = new ArrayList<>();
        for (Weapon w : market.getWeapons()) {
            weaponSnaps.add(new ItemSnapshot(w.getName(), "Weapon"));
        }
        List<ItemSnapshot> armorSnaps = new ArrayList<>();
        for (Armor a : market.getArmors()) {
            armorSnaps.add(new ItemSnapshot(a.getName(), "Armor"));
        }
        List<ItemSnapshot> potionSnaps = new ArrayList<>();
        for (Potion p : market.getPotions()) {
            potionSnaps.add(new ItemSnapshot(p.getName(), "Potion"));
        }
        List<ItemSnapshot> spellSnaps = new ArrayList<>();
        for (Spell s : market.getSpells()) {
            spellSnaps.add(new ItemSnapshot(s.getName(), "Spell"));
        }
        return new MarketSnapshot(weaponSnaps, armorSnaps, potionSnaps, spellSnaps);
    }

    public Market toMarket(ItemCatalog catalog) {
        List<Weapon> ws = new ArrayList<>();
        List<Armor> as = new ArrayList<>();
        List<Potion> ps = new ArrayList<>();
        List<Spell> ss = new ArrayList<>();

        for (ItemSnapshot snap : weapons) {
            Object obj = catalog.copyBySnapshot(snap);
            if (obj instanceof Weapon w) ws.add(w);
        }
        for (ItemSnapshot snap : armors) {
            Object obj = catalog.copyBySnapshot(snap);
            if (obj instanceof Armor a) as.add(a);
        }
        for (ItemSnapshot snap : potions) {
            Object obj = catalog.copyBySnapshot(snap);
            if (obj instanceof Potion p) ps.add(p);
        }
        for (ItemSnapshot snap : spells) {
            Object obj = catalog.copyBySnapshot(snap);
            if (obj instanceof Spell s) ss.add(s);
        }

        return new Market(ws, as, ps, ss);
    }

    public static Map<String, MarketSnapshot> fromMarkets(Map<String, Market> markets) {
        Map<String, MarketSnapshot> result = new HashMap<>();
        if (markets == null) {
            return result;
        }
        for (Map.Entry<String, Market> entry : markets.entrySet()) {
            result.put(entry.getKey(), fromMarket(entry.getValue()));
        }
        return result;
    }

    public static Map<String, Market> toMarkets(Map<String, MarketSnapshot> snapshots, ItemCatalog catalog) {
        Map<String, Market> map = new HashMap<>();
        if (snapshots == null) return map;
        for (Map.Entry<String, MarketSnapshot> e : snapshots.entrySet()) {
            map.put(e.getKey(), e.getValue().toMarket(catalog));
        }
        return map;
    }
}
