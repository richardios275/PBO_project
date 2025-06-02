package com.mygdx.game.ability;

import com.mygdx.game.item.Effect;

/**
 * Represents an Ability in the game.
 * An Ability has a name, description, effect, power, duration, level requirement, cooldown, type, and unlock status.
 */
public class Ability {
    private final String name;
    private final String description;
    private final Effect effect;
    private final int power;
    private final int duration;
    private final int levelRequirement;
    private final int cooldown;
    private final String type;
    private boolean isUnlocked;


    /**
     * Convenient constructor for an Ability with no effect. isUnlocked is set to false by default.
     *
     * @param name             the name of the ability
     * @param description      the description of the ability
     * @param type             the type of the ability
     * @param cooldown         the cooldown of the ability
     * @param duration         the duration of the ability
     * @param power            the power of the ability
     * @param levelRequirement the level requirement of the ability
     * @param effect           the effect of the ability
     */
    public Ability(String name, String description, String type, int cooldown, int duration, int power, int levelRequirement, Effect effect) {
        this(name, description, type, cooldown, duration, power, levelRequirement, effect, false);
    }

    /**
     * Constructs a new Ability with the specified characteristics.
     *
     * @param name             the name of the ability
     * @param description      the description of the ability
     * @param type             the type of the ability
     * @param cooldown         the cooldown of the ability
     * @param duration         the duration of the ability
     * @param power            the power of the ability
     * @param levelRequirement the level requirement of the ability
     * @param effect           the effect of the ability
     * @param isUnlocked       the unlock status of the ability
     */
    public Ability(String name, String description, String type, int cooldown, int duration, int power, int levelRequirement, Effect effect, boolean isUnlocked) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.cooldown = cooldown;
        this.duration = duration;
        this.power = power;
        this.levelRequirement = levelRequirement;
        this.effect = effect;
        this.isUnlocked = isUnlocked;
    }

    /**
     * Copy Constructor for an Ability.
     */
    public Ability(Ability original) {
        this(original.name, original.description, original.type, original.cooldown, original.duration, original.power, original.levelRequirement, original.effect, original.isUnlocked);
    }

    public boolean isUnlocked() {
        return this.isUnlocked;
    }

    public void unlock() {
        this.isUnlocked = true;
    }

    public String getName() {
        return this.name;
    }

    public Effect getEffect() {
        return this.effect;
    }

    public int getAmount() {
        return this.effect.getAmount(this);
    }

    public int getLvlReq() {
        return this.levelRequirement;
    }

    public Ability copy() {
        return new Ability(this);
    }

    public int getPower() {
        return this.power;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getCooldown() {
        return this.cooldown;
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "Skill: " + this.name + " " + this.description + " LVLRQ: " + this.levelRequirement + " UNLCKD: " + this.isUnlocked;
    }
}
