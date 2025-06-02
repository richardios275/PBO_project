package com.mygdx.game.game;

import com.badlogic.gdx.utils.Null;
import com.mygdx.game.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * The Party class represents a group of Pokemon. The class contains functionality to add, remove
 * and get a Pokemon. It also contains functionality to iterate over the Pokemon in the party.
 */
public class Party implements Iterable<Pokemon> {
    private static final int MAX_PARTY_SIZE = 6;
    private final ArrayList<Pokemon> pokemons;
    private int powerPoints;
    private int size;

    /**
     * Constructs a new Party with no Pokemon.
     */
    public Party() {
        this.powerPoints = 0;
        this.pokemons = new ArrayList<>();
    }

    /**
     * Adds a Pokemon to the party, while ensuring the party size doesn't exceed the maximum limit.
     * The Pokemon are sorted by their speed in descending order. The total power points of the party are updated.
     *
     * @param pokemon The Pokemon to be added to the party
     */
    public void addPokemon(Pokemon pokemon) {
        if (this.size >= MAX_PARTY_SIZE) {
            System.out.println("Party is full");
        }
        this.pokemons.add(pokemon);
        this.size++;
        this.powerPoints += pokemon.getPowerPoints();

        this.pokemons.sort(Comparator.comparingInt(Pokemon::getSpeed).reversed());
    }

    /**
     * Returns the maximum level among all Pokemon in the party.
     *
     * @return The maximum level among all Pokemon in the party
     */
    public int getMaxLevel() {
        int maxLevel = 0;
        for (Pokemon pokemon : this.pokemons) {
            if (pokemon.getLevel() > maxLevel) {
                maxLevel = pokemon.getLevel();
            }
        }
        return maxLevel;
    }

    /**
     * Removes a Pokemon from the party. Throws an exception if the Pokemon isn't in the party or
     * if the party has none Pokemon. Updates the total power points of the party.
     *
     * @param pokemon The Pokemon to be removed from the party
     */
    public void removePokemon(Pokemon pokemon) {
        if (!this.pokemons.contains(pokemon)) {
            throw new IllegalStateException("Pokemon is not in party");
        } else if (this.getSize() < 1) {
            throw new IllegalStateException("Party must have at least one pokemon");
        }

        this.pokemons.remove(pokemon);
        this.size--;
        this.powerPoints -= pokemon.getPowerPoints();
    }

    public int getPowerPoints() {
        return this.powerPoints;
    }

    public int getSize() {
        return this.size;
    }

    /**
     * Returns a Pokemon from the party.
     *
     * @param pokemon The Pokemon to get from the party
     * @return The Pokemon from the party
     */
    public Pokemon getPokemon(Pokemon pokemon) {
        return this.pokemons.get(this.pokemons.indexOf(pokemon));
    }

    /**
     * Returns the first Pokemon in the party.
     * Returns null if the party is empty.
     * The first pokemon is the one with the highest speed.
     *
     * @return The first Pokemon in the party, or null if the party is empty
     */
    @Null
    public Pokemon getFirstPokemon() {
        if (this.pokemons.isEmpty()) {
            return null;
        } else {
            return this.pokemons.get(0);
        }
    }

    /**
     * Returns an iterator over the Pokemon in the party. To use the iterator, use a for-each loop.
     *
     * @return An iterator over the Pokemon in the party
     */
    @Override
    public Iterator<Pokemon> iterator() {
        return this.pokemons.iterator();
    }
}
