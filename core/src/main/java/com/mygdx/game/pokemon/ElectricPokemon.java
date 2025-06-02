package com.mygdx.game.pokemon;

/**
 * Implementation of the Electric type of Pokemon.
 * Sets the effectiveness of the Electric type against other types.
 */
public class ElectricPokemon extends Pokemon {

    /**
     * Full constructor for the ElectricPokemon class.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolution tier of the Pokemon.
     * @param health      The health of the Pokemon.
     * @param attack      The attack of the Pokemon.
     * @param defense     The defense of the Pokemon.
     * @param speed       The speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    public ElectricPokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        super(name, tier, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the ElectricPokemon class.
     *
     * @param original The ElectricPokemon to copy.
     */
    public ElectricPokemon(ElectricPokemon original) {
        super(original);
    }

    @Override
    public String getType() {
        return "electric";
    }

    /**
     * Returns the effectiveness of the Electric type against the given Pokemon.
     * In decimal form 2 if effective 1 if neutral 0.5 if not very effective.
     *
     * @param otherPokemon The Pokemon to check the effectiveness against.
     * @return The effectiveness of the Electric type against the given Pokemon.
     */
    @Override
    public double getEffectiveness(Pokemon otherPokemon) {
        if (otherPokemon instanceof WaterPokemon) {
            return 2;
        } else if (otherPokemon instanceof GrassPokemon
                || otherPokemon instanceof ElectricPokemon) {
            return 0.5;
        } else {
            return 1;
        }
    }

    /**
     * Returns a copy of the ElectricPokemon.
     *
     * @return A copy of the ElectricPokemon.
     */
    @Override
    public Pokemon copy() {
        return new ElectricPokemon(this);
    }
}
