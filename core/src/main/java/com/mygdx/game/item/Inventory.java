package com.mygdx.game.item;

import java.util.HashMap;
import java.util.Iterator;

/**
 * The Inventory class represents a player's inventory in the game, storing items.
 * The class contains functionality to add, remove and get an item. It also contains functionality to iterate over the items in the inventory.
 * The class is implemented as a HashMap, where the key is the item's name and the value is the item itself.
 */
public class Inventory implements Iterable<Item> {
    private final HashMap<String, Item> items;

    /**
     * Constructs a new empty Inventory.
     */
    public Inventory() {
        this.items = new HashMap<>();
    }

    /**
     * Adds a new item to the inventory.
     *
     * @param item The item to be added.
     */
    public void addItem(Item item) {
        this.items.put(item.getName(), item);
    }

    /**
     * Returns the item with the specified name from the inventory.
     *
     * @param name The name of the item to be retrieved.
     * @return The Item object, null if no such item exists.
     */
    public Item getItem(String name) {
        return this.items.get(name);
    }

    /**
     * Removes the item with the specified name from the inventory.
     *
     * @param name The name of the item to be removed.
     */
    public void removeItem(String name) {
        this.items.remove(name);
    }

    /**
     * Removes the specified item from the inventory.
     *
     * @param item The item to be removed.
     */
    public void removeItem(Item item) {
        this.items.remove(item.getName());
    }

    /**
     * Returns the size of the inventory, i.e., the number of items in the inventory.
     *
     * @return The number of items in the inventory.
     */
    public int getSize() {
        return this.items.size();
    }

    /**
     * Returns an iterator over the items in the inventory.
     *
     * @return An Iterator.
     */
    @Override
    public Iterator<Item> iterator() {
        return this.items.values().iterator();
    }
}
