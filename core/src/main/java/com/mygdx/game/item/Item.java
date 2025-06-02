package com.mygdx.game.item;

// TODO implement quest items

/**
 * The Item interface represents a generic item in the game.
 */
public interface Item {
    /**
     * Returns the name of the item.
     *
     * @return The name of the item.
     */
    String getName();

    /**
     * Returns a description of the item.
     *
     * @return The item description.
     */
    String getDescription();
}
