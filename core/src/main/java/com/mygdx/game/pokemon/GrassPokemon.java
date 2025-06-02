package com.mygdx.game.pokemon;

/**
 * Implementation of the Grass type of Pokemon.
 * Sets the effectiveness of the Grass type against other types.
 */
public class GrassPokemon extends Pokemon {
    /**
     * Full constructor for the GrassPokemon class.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolution tier of the Pokemon.
     * @param health      The health of the Pokemon.
     * @param attack      The attack of the Pokemon.
     * @param defense     The defense of the Pokemon.
     * @param speed       The speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    public GrassPokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        super(name, tier, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the GrassPokemon class.
     *
     * @param original The GrassPokemon to copy.
     */
    public GrassPokemon(GrassPokemon original) {
        super(original);
    }

    @Override
    public String getType() {
        return "grass";
    }

    /**
     * Returns the effectiveness of the Grass type against the given Pokemon.
     * In decimal form 2 if effective 1 if neutral 0.5 if not very effective.
     *
     * @param otherPokemon The Pokemon to check the effectiveness against.
     * @return The effectiveness of the Grass type against the given Pokemon.
     */
    @Override
    public double getEffectiveness(Pokemon otherPokemon) {
        if (otherPokemon instanceof RockPokemon) {
            return 2;
        } else if (otherPokemon instanceof FirePokemon
                || otherPokemon instanceof PoisonPokemon
                || otherPokemon instanceof GrassPokemon) {
            return 0.5;
        } else {
            return 1;
        }
    }

    /**
     * Returns a copy of the GrassPokemon.
     *
     * @return A copy of the GrassPokemon.
     */
    @Override
    public GrassPokemon copy() {
        return new GrassPokemon(this);
    }
}
