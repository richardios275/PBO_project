package com.mygdx.game.item;

import com.mygdx.game.pokemon.Pokemon;

/**
 * Potion is a specific item in the game that can be used to provide a certain effect to a Pokemon.
 */
public class Potion implements UsableItem {
    private final String name;
    private final String description;
    private final Effect effect;
    private final int amount;
    private final int duration;

    /**
     * Full constructor for the Potion class.
     *
     * @param name        The name of the potion.
     * @param description A brief description of what the potion does.
     * @param duration    How long the potion's effect lasts.
     * @param amount      The magnitude of the potion's effect.
     * @param effect      The type of effect the potion provides.
     */
    public Potion(String name, String description, int duration, int amount, Effect effect) {
        this.name = name;
        this.duration = duration;
        this.amount = amount;
        this.description = description;
        this.effect = effect;
    }

    /**
     * Convenience constructor for the Potion class for potions with no duration.
     *
     * @param name        The name of the potion.
     * @param description A brief description of what the potion does.
     * @param amount      The magnitude of the potion's effect.
     * @param effect      The type of effect the potion provides.
     */
    public Potion(String name, String description, int amount, Effect effect) {
        this(name, description, 0, amount, effect);
    }


    public int getAmount() {
        return this.amount;
    }

    public int getDuration() {
        return this.duration;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Uses the potion on a specific Pokemon. Depending on the effect of the potion,
     * it might heal the Pokemon, or increase its defense or attack.
     *
     * @param pokemon The Pokemon on which to use the potion.
     */
    @Override
    public void use(Pokemon pokemon) {
        switch (this.effect) {
            case HEAL: {
                pokemon.increaseHP(this.effect.getAmount(this));
                break;
            }
            case BUFF_DEFENSE: {
                pokemon.increaseDEF(this.effect.getAmount(this));
                break;
            }
            case BUFF_ATTACK: {
                pokemon.increaseATT(this.effect.getAmount(this));
                break;
            }
            default: {
                break;
            }
        }
    }
}
