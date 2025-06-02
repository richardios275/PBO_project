package com.mygdx.game.item;

import com.mygdx.game.pokemon.Pokemon;

/**
 * Interface for items that can be used on Pokemon.
 */
public interface UsableItem extends Item {
    /**
     * Applies the effect of the item to the given Pokemon.
     *
     * @param pokemon The Pokemon to apply the effect to.
     */
    void use(Pokemon pokemon);
}
