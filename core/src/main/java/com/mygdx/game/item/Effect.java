package com.mygdx.game.item;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.ability.Ability;

/**
 * The Effect enum represents different status effects that can be applied to game entities.
 */

public enum Effect {
    BUFF_ATTACK(),
    BUFF_DEFENSE(),
    BUFF_SPEED(),
    HEAL(),
    POISON(),
    BURN(),
    FREEZE(),
    SHOCK(),
    WET(),
    ENTANGLE(),
    NONE();

    /**
     * Calculates the amount of the effect when applied through an ability.
     * The amount is calculated as the ability's power +/- 50% of the ability's power.
     * The amount is then rounded to the nearest integer.
     *
     * @param ability The ability through which the effect is being applied.
     * @return The calculated amount.
     */
    public int getAmount(Ability ability) {
        return (int) (ability.getPower() + MathUtils.random(-0.5f, 0.5f) * ability.getPower() / 2);
    }

    /**
     * Retrieves the duration in turns of the effect when applied through an ability.
     *
     * @param ability The ability through which the effect is being applied.
     * @return The duration of the effect.
     */
    public int getDuration(Ability ability) {
        return ability.getDuration();
    }

    /**
     * Retrieves the amount of the effect when applied through a potion.
     *
     * @param potion The potion through which the effect is being applied.
     * @return The amount of the effect.
     */
    public int getAmount(Potion potion) {
        return potion.getAmount();
    }

    /**
     * Retrieves the duration int turns  of the effect when applied through a potion.
     *
     * @param potion The potion through which the effect is being applied.
     * @return The duration of the effect.
     */
    public int getDuration(Potion potion) {
        return potion.getDuration();
    }
}
