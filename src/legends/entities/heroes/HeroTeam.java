package legends.entities.heroes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Aggregate class representing a team of heroes.
 * Encapsulates party operations instead of passing raw lists around.
 */
public class HeroTeam implements Iterable<Hero> {

    private final List<Hero> heroes = new ArrayList<>();

    public void add(Hero hero) {
        if (hero != null && !heroes.contains(hero)) {
            heroes.add(hero);
        }
    }

    public boolean contains(Hero hero) {
        return heroes.contains(hero);
    }

    public Hero remove(int index) {
        return heroes.remove(index);
    }

    public void remove(Hero hero) {
        heroes.remove(hero);
    }

    public Hero get(int index) {
        return heroes.get(index);
    }

    public int size() {
        return heroes.size();
    }

    public boolean isEmpty() {
        return heroes.isEmpty();
    }

    public boolean allFainted() {
        for (Hero h : heroes) {
            if (!h.isFainted()) {
                return false;
            }
        }
        return true;
    }

    public List<Hero> asList() {
        return Collections.unmodifiableList(heroes);
    }

    public void clear() {
        heroes.clear();
    }

    public void replaceWith(List<Hero> newHeroes) {
        heroes.clear();
        if (newHeroes != null) {
            heroes.addAll(newHeroes);
        }
    }

    @Override
    public Iterator<Hero> iterator() {
        return heroes.iterator();
    }
}
