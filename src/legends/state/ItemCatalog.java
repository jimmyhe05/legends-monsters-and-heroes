package legends.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import legends.game.DataLoader;
import legends.items.Armor;
import legends.items.FireSpell;
import legends.items.IceSpell;
import legends.items.LightningSpell;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

/**
 * Simple registry of items keyed by name to support save/load reconstruction.
 * Items are cloned on retrieval to avoid mutating shared prototypes.
 */
public class ItemCatalog {
    private final Map<String, Weapon> weapons = new HashMap<>();
    private final Map<String, Armor> armors = new HashMap<>();
    private final Map<String, Potion> potions = new HashMap<>();
    private final Map<String, Spell> spells = new HashMap<>();

    public ItemCatalog() {
        loadAll();
    }

    private void loadAll() {
        String base = "data/items/";
        List<Weapon> allWeapons = DataLoader.loadWeapons(base + "Weaponry.txt");
        List<Armor> allArmors = DataLoader.loadArmors(base + "Armory.txt");
        List<Potion> allPotions = DataLoader.loadPotions(base + "Potions.txt");
        List<Spell> allSpells = DataLoader.loadFireSpells(base + "FireSpells.txt");
        allSpells.addAll(DataLoader.loadIceSpells(base + "IceSpells.txt"));
        allSpells.addAll(DataLoader.loadLightningSpells(base + "LightningSpells.txt"));

        for (Weapon w : allWeapons) {
            weapons.put(w.getName(), w);
        }
        for (Armor a : allArmors) {
            armors.put(a.getName(), a);
        }
        for (Potion p : allPotions) {
            potions.put(p.getName(), p);
        }
        for (Spell s : allSpells) {
            spells.put(s.getName(), s);
        }
    }

    public Weapon copyWeapon(String name) {
        Weapon proto = weapons.get(name);
        if (proto == null) return null;
        return new Weapon(proto.getName(), (int) proto.getCost(), proto.getRequiredLevel(), proto.getDamage(), proto.getHandsRequired());
    }

    public Armor copyArmor(String name) {
        Armor proto = armors.get(name);
        if (proto == null) return null;
        return new Armor(proto.getName(), proto.getCost(), proto.getRequiredLevel(), proto.getDamageReduction());
    }

    public Potion copyPotion(String name) {
        Potion proto = potions.get(name);
        if (proto == null) return null;
        return new Potion(proto.getName(), (int) proto.getCost(), proto.getRequiredLevel(), proto.getEffectAmount(), proto.getAffectedAttributes());
    }

    public Spell copySpell(String name) {
        Spell proto = spells.get(name);
        if (proto == null) return null;
        if (proto.getClass() == FireSpell.class) {
            FireSpell fs = (FireSpell) proto;
            return new FireSpell(fs.getName(), (int) fs.getCost(), fs.getRequiredLevel(), fs.getBaseDamage(), fs.getManaCost());
        } else if (proto.getClass() == IceSpell.class) {
            IceSpell is = (IceSpell) proto;
            return new IceSpell(is.getName(), (int) is.getCost(), is.getRequiredLevel(), is.getBaseDamage(), is.getManaCost());
        } else if (proto.getClass() == LightningSpell.class) {
            LightningSpell ls = (LightningSpell) proto;
            return new LightningSpell(ls.getName(), (int) ls.getCost(), ls.getRequiredLevel(), ls.getBaseDamage(), ls.getManaCost());
        }
        return null;
    }

    /**
     * Return a concrete item instance by snapshot type/name, or null if missing.
     */
    public Object copyBySnapshot(ItemSnapshot snap) {
        if (snap == null) return null;
        String type = snap.getType();
        String name = snap.getName();
        if ("Weapon".equalsIgnoreCase(type)) return copyWeapon(name);
        if ("Armor".equalsIgnoreCase(type)) return copyArmor(name);
        if ("Potion".equalsIgnoreCase(type)) return copyPotion(name);
        if ("Spell".equalsIgnoreCase(type)) return copySpell(name);
        return null;
    }
}
