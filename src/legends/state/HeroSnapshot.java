package legends.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import legends.entities.heroes.Hero;
import legends.items.Armor;
import legends.items.Inventory;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

/**
 * Snapshot of a hero's core stats, equipment, and inventory by item name.
 */
public class HeroSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String heroClass;
    private final int level;
    private final double hp;
    private final double mp;
    private final double strength;
    private final double dexterity;
    private final double agility;
    private final double gold;
    private final double experience;
    private final String weaponName;
    private final String armorName;
    private final List<ItemSnapshot> inventoryItems;

    public HeroSnapshot(String name, String heroClass, int level, double hp, double mp, double strength,
                        double dexterity, double agility, double gold, double experience, String weaponName,
                        String armorName, List<ItemSnapshot> inventoryItems) {
        this.name = name;
        this.heroClass = heroClass;
        this.level = level;
        this.hp = hp;
        this.mp = mp;
        this.strength = strength;
        this.dexterity = dexterity;
        this.agility = agility;
        this.gold = gold;
        this.experience = experience;
        this.weaponName = weaponName;
        this.armorName = armorName;
        this.inventoryItems = inventoryItems;
    }

    public String getName() {
        return name;
    }

    public String getHeroClass() {
        return heroClass;
    }

    public int getLevel() {
        return level;
    }

    public double getHp() {
        return hp;
    }

    public double getMp() {
        return mp;
    }

    public double getStrength() {
        return strength;
    }

    public double getDexterity() {
        return dexterity;
    }

    public double getAgility() {
        return agility;
    }

    public double getGold() {
        return gold;
    }

    public double getExperience() {
        return experience;
    }

    public String getWeaponName() {
        return weaponName;
    }

    public String getArmorName() {
        return armorName;
    }

    public List<ItemSnapshot> getInventoryItems() {
        return inventoryItems;
    }

    public static List<HeroSnapshot> fromHeroes(List<Hero> heroes) {
        List<HeroSnapshot> snapshots = new ArrayList<>();
        for (Hero hero : heroes) {
            Inventory inv = hero.getInventory();
            List<ItemSnapshot> items = new ArrayList<>();
            if (inv != null) {
                for (Weapon w : inv.getWeapons()) {
                    items.add(new ItemSnapshot(w.getName(), "Weapon"));
                }
                for (Armor a : inv.getArmors()) {
                    items.add(new ItemSnapshot(a.getName(), "Armor"));
                }
                for (Potion p : inv.getPotions()) {
                    items.add(new ItemSnapshot(p.getName(), "Potion"));
                }
                for (Spell s : inv.getSpells()) {
                    items.add(new ItemSnapshot(s.getName(), "Spell"));
                }
            }

            String weaponName = hero.getEquippedWeapon() != null ? hero.getEquippedWeapon().getName() : null;
            String armorName = hero.getEquippedArmor() != null ? hero.getEquippedArmor().getName() : null;

            snapshots.add(new HeroSnapshot(
                hero.getName(),
                hero.getClass().getSimpleName(),
                hero.getLevel(),
                hero.getHp(),
                hero.getMp(),
                hero.getStrength(),
                hero.getDexterity(),
                hero.getAgility(),
                hero.getGold(),
                hero.getExperience(),
                weaponName,
                armorName,
                items
            ));
        }
        return snapshots;
    }
}
