package com.mygdx.game.pokemon;

/**
 * Implementation of the Ice type of Pokemon.
 * Sets the effectiveness of the Ice type against other types.
 */
public class IcePokemon extends Pokemon {
    /**
     * Full constructor for the IcePokemon class.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolution tier of the Pokemon.
     * @param health      The health of the Pokemon.
     * @param attack      The attack of the Pokemon.
     * @param defense     The defense of the Pokemon.
     * @param speed       The speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    public IcePokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        super(name, tier, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the IcePokemon class.
     *
     * @param original The IcePokemon to copy.
     */
    public IcePokemon(IcePokemon original) {
        super(original);
    }

    @Override
    public String getType() {
        return "ice";
    }

    /**
     * Returns the effectiveness of the Ice type against the given Pokemon.
     * In decimal form 2 if effective 1 if neutral 0.5 if not very effective.
     *
     * @param otherPokemon The Pokemon to check the effectiveness against.
     * @return The effectiveness of the Ice type against the given Pokemon.
     */
    @Override
    public double getEffectiveness(Pokemon otherPokemon) {
        if (otherPokemon instanceof GrassPokemon) {
            return 2;
        } else if (otherPokemon instanceof FirePokemon
                || otherPokemon instanceof WaterPokemon
                || otherPokemon instanceof IcePokemon) {
            return 0.5;
        } else {
            return 1;
        }
    }

    /**
     * Returns a copy of the IcePokemon.
     *
     * @return A copy of the IcePokemon.
     */
    @Override
    public Pokemon copy() {
        return new IcePokemon(this);
    }
}
