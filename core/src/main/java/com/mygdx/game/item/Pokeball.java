package com.mygdx.game.item;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.pokemon.Pokemon;

/**
 * Pokeball is a specific item in the game that can be used to catch defeated Pokemon.
 * The chance of catching a Pokemon is 50%.
 * If the catch is successful, the Pokemon is marked as collected.
 */
public class Pokeball implements Item {
    /**
     * Returns the name of the Pokeball item.
     *
     * @return The name of the item, in this case "Pokeball".
     */
    @Override
    public String getName() {
        return "Pokeball";
    }

    /**
     * Returns a description of the Pokeball item.
     *
     * @return The item description.
     */
    @Override
    public String getDescription() {
        return "A device for catching wild Pokemon.";
    }

    /**
     * Attempts to catch a wild Pokemon. The success is determined by a random chance.
     * Specifically, the chance of catching a Pokemon is 50%.
     *
     * @param pokemon The Pokemon to attempt to catch.
     * @return True if the Pokemon was successfully caught, false otherwise.
     */
    public boolean attemptToCatch(Pokemon pokemon) {
        int catchChance = (int) (MathUtils.random() * 100);
        if (catchChance < 50) {
            pokemon.setCollected(true);
            System.out.println("You caught " + pokemon.getName() + "!");
            return true;
        } else {
            System.out.println("You failed to catch " + pokemon.getName() + "!");
            return false;
        }
    }
}
