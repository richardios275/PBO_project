package com.mygdx.game.pokemon;

/**
 * Implementation of the Rock type of Pokemon.
 * Sets the effectiveness of the Poison type against other types.
 */
public class RockPokemon extends Pokemon {
    /**
     * Full constructor for the RockPokemon class.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolution tier of the Pokemon.
     * @param health      The health of the Pokemon.
     * @param attack      The attack of the Pokemon.
     * @param defense     The defense of the Pokemon.
     * @param speed       The speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    public RockPokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        super(name, tier, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the RockPokemon class.
     *
     * @param original The RockPokemon to copy.
     */
    public RockPokemon(RockPokemon original) {
        super(original);
    }

    @Override
    public String getType() {
        return "rock";
    }

    /**
     * Returns the effectiveness of the Rock type against the given Pokemon.
     * In decimal form 2 if effective 1 if neutral 0.5 if not very effective.
     *
     * @param otherPokemon The Pokemon to check the effectiveness against.
     * @return The effectiveness of the Rock type against the given Pokemon.
     */
    @Override
    public double getEffectiveness(Pokemon otherPokemon) {
        if (otherPokemon instanceof FirePokemon
                || otherPokemon instanceof IcePokemon) {
            return 2;
        } else if (otherPokemon instanceof RockPokemon) {
            return 0.5;
        } else {
            return 1;
        }
    }

    /**
     * Returns a copy of the RockPokemon.
     *
     * @return A copy of the RockPokemon.
     */
    @Override
    public Pokemon copy() {
        return new RockPokemon(this);
    }
}
