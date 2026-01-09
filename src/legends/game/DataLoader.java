package legends.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import legends.entities.heroes.Paladin;
import legends.entities.heroes.Sorcerer;
import legends.entities.heroes.Warrior;
import legends.entities.monsters.Dragon;
import legends.entities.monsters.Exoskeleton;
import legends.entities.monsters.Spirit;
import legends.items.Armor;
import legends.items.AttributePotion;
import legends.items.FireSpell;
import legends.items.HealthPotion;
import legends.items.IceSpell;
import legends.items.LightningSpell;
import legends.items.ManaPotion;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

public class DataLoader {
    /**
     * Utility class for loading heroes, monsters, and items from the assignment data files.
     * Each loader parses rows from text tables into domain objects.
     */
    
    /* ====================== HEROES ====================== */

    /**
     * Load warriors from a given file path.
     * 
     * @param path the file path to load warriors from
     * @return list of Warrior objects
     */
    public static List<Warrior> loadWarriors(String path) {
    List<Warrior> warriors = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // split by whitespace (name has no spaces, uses _)
                String[] parts = line.split("\\s+");
                if (parts.length < 7) {
                    continue; // malformed line
                }

                String name = parts[0];
                double mana = Double.parseDouble(parts[1]);
                double strength = Double.parseDouble(parts[2]);
                double agility = Double.parseDouble(parts[3]);
                double dexterity = Double.parseDouble(parts[4]);
                double gold = Double.parseDouble(parts[5]);
                double experience = Double.parseDouble(parts[6]);

                Warrior w = new Warrior(name, mana, strength, agility, dexterity, gold, experience);
                warriors.add(w);
            }
        } catch (IOException e) {
            System.err.println("Error loading warriors from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return warriors;
    }

    /**
     * Load paladins from a given file path.
     * 
     * @param path the file path to load paladins from
     * @return list of Paladin objects
     */
    public static List<Paladin> loadPaladins(String path) {
    List<Paladin> paladins = new ArrayList<>();
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length < 7) {
                    continue;
                }

                String name = parts[0];
                double mana = Double.parseDouble(parts[1]);
                double strength = Double.parseDouble(parts[2]);
                double agility = Double.parseDouble(parts[3]);
                double dexterity = Double.parseDouble(parts[4]);
                double gold = Double.parseDouble(parts[5]);
                double experience = Double.parseDouble(parts[6]);

                Paladin p = new Paladin(name, mana, strength, agility, dexterity, gold, experience);
                paladins.add(p);
            }
        } catch (IOException e) {
            System.err.println("Error loading paladins from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return paladins;
    }

    /**
     * Load sorcerers from a given file path.
     * 
     * @param path the file path to load sorcerers from
     * @return list of Sorcerer objects
     */
    public static List<Sorcerer> loadSorcerers(String path) {
    List<Sorcerer> sorcerers = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length < 7) {
                    continue;
                }

                String name = parts[0];
                double mana = Double.parseDouble(parts[1]);
                double strength = Double.parseDouble(parts[2]);
                double agility = Double.parseDouble(parts[3]);
                double dexterity = Double.parseDouble(parts[4]);
                double gold = Double.parseDouble(parts[5]);
                double experience = Double.parseDouble(parts[6]);

                Sorcerer s = new Sorcerer(name, mana, strength, agility, dexterity, gold, experience);
                sorcerers.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error loading sorcerers from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return sorcerers;
    }

    /* ====================== MONSTERS ====================== */

    /**
     * Load dragons from a given file path.
     * 
     * @param path the file path to load dragons from
     * @return list of Dragon objects
     */
    public static List<Dragon> loadDragons(String path) {
    List<Dragon> dragons = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/level/damage/defense/dodge chance
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                double damage = Double.parseDouble(parts[2]);
                double defense = Double.parseDouble(parts[3]);
                double dodgeChance = Double.parseDouble(parts[4]);

                Dragon d = new Dragon(name, level, damage, defense, dodgeChance);
                dragons.add(d);
            }
        } catch (IOException e) {
            System.err.println("Error loading dragons from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return dragons;
    }

    /**
     * Load spirits from a given file path.
     * 
     * @param path the file path to load spirits from
     * @return list of Spirit objects
     */
    public static List<Spirit> loadSpirits(String path) {
    List<Spirit> spirits = new ArrayList<>();
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/level/damage/defense/dodge chance
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                double damage = Double.parseDouble(parts[2]);
                double defense = Double.parseDouble(parts[3]);
                double dodgeChance = Double.parseDouble(parts[4]);

                Spirit s = new Spirit(name, level, damage, defense, dodgeChance);
                spirits.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error loading spirits from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return spirits;
    }

    /**
     * Load exoskeletons from a given file path.
     * 
     * @param path the file path to load exoskeletons from
     * @return list of Exoskeleton objects
     */
    public static List<Exoskeleton> loadExoskeletons(String path) {
    List<Exoskeleton> exos = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/level/damage/defense/dodge chance
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int level = Integer.parseInt(parts[1]);
                double damage = Double.parseDouble(parts[2]);
                double defense = Double.parseDouble(parts[3]);
                double dodgeChance = Double.parseDouble(parts[4]);

                Exoskeleton e = new Exoskeleton(name, level, damage, defense, dodgeChance);
                exos.add(e);
            }
        } catch (IOException e) {
            System.err.println("Error loading exoskeletons from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return exos;
    }

    /* ====================== ITEMS ====================== */

    /**
     * Load weapons from a given file path.
     * 
     * @param path the file path to load weapons from
     * @return list of Weapon objects
     */
    public static List<Weapon> loadWeapons(String path) {
    List<Weapon> weapons = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/cost/level/damage/required hands
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int hands = Integer.parseInt(parts[4]);

                Weapon w = new Weapon(name, cost, level, damage, hands);
                weapons.add(w);
            }
        } catch (IOException e) {
            System.err.println("Error loading weapons from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return weapons;
    }

    /**
     * Load armors from a given file path.
     * 
     * @param path the file path to load armors from
     * @return list of Armor objects
     */
    public static List<Armor> loadArmors(String path) {
    List<Armor> armors = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/cost/required level/damage reduction
                String[] parts = line.split("\\s+");
                if (parts.length < 4) {
                    continue;
                }

                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int reduction = Integer.parseInt(parts[3]);

                Armor a = new Armor(name, cost, level, reduction);
                armors.add(a);
            }
        } catch (IOException e) {
            System.err.println("Error loading armors from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return armors;
    }

    /**
     * Load potions from a given file path.
     * 
     * @param path the file path to load potions from
     * @return list of Potion objects
     */
    public static List<Potion> loadPotions(String path) {
    List<Potion> potions = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/cost/required level/attribute increase/attribute affected
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int increase = Integer.parseInt(parts[3]);

                // attribute affected might have slashes/spaces, join the rest
                StringBuilder attr = new StringBuilder();
                for (int i = 4; i < parts.length; i++) {
                    if (i > 4) {
                        attr.append(" ");
                    }
                    attr.append(parts[i]);
                }

                String attrs = attr.toString();
                String key = attrs.toUpperCase().trim();
                Potion p;
                switch (key) {
                    case "HEALTH" -> p = new HealthPotion(name, cost, level, increase);
                    case "MANA" -> p = new ManaPotion(name, cost, level, increase);
                    default -> p = new AttributePotion(name, cost, level, increase, attrs);
                }
                potions.add(p);
            }
        } catch (IOException e) {
            System.err.println("Error loading potions from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return potions;
    }

    /**
     * Load fire spells from a given file path.
     * 
     * @param path the file path to load fire spells from
     * @return list of FireSpell objects
     */
    public static List<Spell> loadFireSpells(String path) {
    List<Spell> spells = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                // Name/cost/required level/damage/mana cost
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int manaCost = Integer.parseInt(parts[4]);

                Spell s = new FireSpell(name, cost, level, damage, manaCost);
                spells.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error loading fire spells from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return spells;
    }

    /**
     * Load ice spells from a given file path.
     * 
     * @param path the file path to load ice spells from
     * @return list of IceSpell objects
     */
    public static List<Spell> loadIceSpells(String path) {
    List<Spell> spells = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int manaCost = Integer.parseInt(parts[4]);

                Spell s = new IceSpell(name, cost, level, damage, manaCost);
                spells.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error loading ice spells from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return spells;
    }

    /**
     * Load lightning spells from a given file path.
     * 
     * @param path the file path to load lightning spells from
     * @return list of LightningSpell objects
     */
    public static List<Spell> loadLightningSpells(String path) {
    List<Spell> spells = new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            // skip header
            br.readLine();

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+");
                if (parts.length < 5) {
                    continue;
                }

                String name = parts[0];
                int cost = Integer.parseInt(parts[1]);
                int level = Integer.parseInt(parts[2]);
                int damage = Integer.parseInt(parts[3]);
                int manaCost = Integer.parseInt(parts[4]);

                Spell s = new LightningSpell(name, cost, level, damage, manaCost);
                spells.add(s);
            }
        } catch (IOException e) {
            System.err.println("Error loading lightning spells from " + path + ": " + e.getMessage());
        } finally {
            closeQuietly(br);
        }
        return spells;
    }

    /* ====================== UTIL ====================== */

    private static void closeQuietly(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
