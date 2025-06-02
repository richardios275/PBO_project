package com.mygdx.game.pokemon;

/**
 * Implementation of the Water type of Pokemon.
 * Sets the effectiveness of the Water type against other types.
 */
public class WaterPokemon extends Pokemon {
    /**
     * Full constructor for the WaterPokemon class.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolution tier of the Pokemon.
     * @param health      The health of the Pokemon.
     * @param attack      The attack of the Pokemon.
     * @param defense     The defense of the Pokemon.
     * @param speed       The speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    public WaterPokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        super(name, tier, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the WaterPokemon class.
     *
     * @param original The WaterPokemon to copy.
     */
    public WaterPokemon(WaterPokemon original) {
        super(original);
    }

    @Override
    public String getType() {
        return "water";
    }

    /**
     * Returns the effectiveness of the Water type against the given Pokemon.
     * In decimal form 2 if effective 1 if neutral 0.5 if not very effective.
     *
     * @param otherPokemon The Pokemon to check the effectiveness against.
     * @return The effectiveness of the Water type against the given Pokemon.
     */
    @Override
    public double getEffectiveness(Pokemon otherPokemon) {
        if (otherPokemon instanceof FirePokemon
                || otherPokemon instanceof RockPokemon) {
            return 2;
        } else if (otherPokemon instanceof GrassPokemon
                || otherPokemon instanceof WaterPokemon) {
            return 0.5;
        } else {
            return 1;
        }
    }

    /**
     * Returns a copy of the WaterPokemon.
     *
     * @return A copy of the WaterPokemon.
     */
    @Override
    public Pokemon copy() {
        return new WaterPokemon(this);
    }
}
