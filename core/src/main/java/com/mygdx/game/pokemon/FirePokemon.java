package com.mygdx.game.pokemon;

/**
 * Implementation of the Fire type of Pokemon.
 * Sets the effectiveness of the Fire type against other types.
 */
public class FirePokemon extends Pokemon {
    /**
     * Full constructor for the FirePokemon class.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolution tier of the Pokemon.
     * @param health      The health of the Pokemon.
     * @param attack      The attack of the Pokemon.
     * @param defense     The defense of the Pokemon.
     * @param speed       The speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    public FirePokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        super(name, tier, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the FirePokemon class.
     *
     * @param original The FirePokemon to copy.
     */
    public FirePokemon(FirePokemon original) {
        super(original);
    }

    @Override
    public String getType() {
        return "fire";
    }

    /**
     * Returns the effectiveness of the Fire type against the given Pokemon.
     * In decimal form 2 if effective 1 if neutral 0.5 if not very effective.
     *
     * @param otherPokemon The Pokemon to check the effectiveness against.
     * @return The effectiveness of the Fire type against the given Pokemon.
     */
    @Override
    public double getEffectiveness(Pokemon otherPokemon) {
        if (otherPokemon instanceof WaterPokemon || otherPokemon instanceof FirePokemon) {
            return 0.5;
        } else if (otherPokemon instanceof GrassPokemon || otherPokemon instanceof IcePokemon) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Returns a copy of the FirePokemon.
     *
     * @return A copy of the FirePokemon.
     */
    @Override
    public Pokemon copy() {
        return new FirePokemon(this);
    }

}
