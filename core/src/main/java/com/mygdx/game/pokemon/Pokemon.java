package com.mygdx.game.pokemon;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.game.Constants;
import com.mygdx.game.item.Effect;

import java.util.ArrayList;

/**
 * Pokemon is an abstract class representing a Pokemon in the game.
 * It contains basic properties and behaviors that all Pokemon should have,
 * such as health, attack, defense, and speed attributes, as well as abilities
 * and status effects. The Pokemon can also gain experience, level up, and evolve.
 */
public abstract class Pokemon extends Actor {

    private static final int LVL_EVOLUTION_TO_TIER_2 = 16;
    private static final int LVL_EVOLUTION_TO_TIER_3 = 36;
    private final String evolvesInto;
    private final ArrayList<Effect> statusEffects;
    private final ArrayList<Ability> abilities;
    private int tier;
    private int maxHealth;
    private int defense;
    private int speed;
    private TextureAtlas textureAtlas;
    private int attack;
    private int health;
    private boolean fainted;
    private int level;
    private int experience;
    private boolean isCollected;

    /**
     * Full constructor for the Pokemon class.
     * Initializes a new instance of a Pokemon with specific attributes.
     *
     * @param name        The name of the Pokemon.
     * @param tier        The evolutionary tier of the Pokemon.
     * @param level       The current level of the Pokemon.
     * @param health      The current health of the Pokemon.
     * @param attack      The current attack power of the Pokemon.
     * @param defense     The current defense power of the Pokemon.
     * @param speed       The current speed of the Pokemon.
     * @param evolvesInto The name of the Pokemon this Pokemon evolves into.
     */
    protected Pokemon(String name, int tier, int level, int health, int attack, int defense, int speed, String evolvesInto) {
        this.tier = tier;
        this.level = level;
        this.health = health;
        this.maxHealth = health;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.evolvesInto = evolvesInto;
        this.abilities = new ArrayList<>();
        this.statusEffects = new ArrayList<>();
        this.experience = 0;
        this.fainted = false;
        this.isCollected = false;

        this.setName(name);
        this.initSprite();
    }

    protected Pokemon(String name, int tier, int health, int attack, int defense, int speed, String evolvesInto) {
        this(name, tier, 1, health, attack, defense, speed, evolvesInto);
    }

    /**
     * Copy constructor for the Pokemon class.
     *
     * @param original The original Pokemon to copy.
     */
    protected Pokemon(Pokemon original) {
        this(original.getName(), original.getTier(), original.getLevel(), original.getHealth(), original.getAttack(), original.getDefense(), original.getSpeed(), original.getEvolvesInto());
        this.textureAtlas = original.textureAtlas;
        for (Ability ability : original.getAbilities()) {
            this.abilities.add(ability.copy());
        }
    }

    public abstract String getType();

    /**
     * Calculates and returns the effectiveness of this Pokemon against another Pokemon.
     *
     * @param otherPokemon The other Pokemon.
     * @return A double representing the effectiveness of this Pokemon against the other Pokemon.
     */

    public abstract double getEffectiveness(Pokemon otherPokemon);

    /**
     * Creates and returns a copy of this Pokemon.
     *
     * @return A new instance of Pokemon that is a copy of this Pokemon.
     */
    public abstract Pokemon copy();

    private String getEvolvesInto() {
        return this.evolvesInto;
    }

    private int getTier() {
        return this.tier;
    }

    /**
     * Initialises the sprite of the Pokemon.
     * This method should be called after the Pokemon has been created.
     * It sets the sprite of the Pokemon to the corresponding texture in the Atlas.
     * If the texture is not found, an error message is printed.
     * This method automatically sets the texture for all potential subclasses of Pokemon.
     */
    public void initSprite() {
        this.textureAtlas = new TextureAtlas("Atlas/Pokemons.atlas");
        if (this.textureAtlas.findRegion(this.getName()) == null) {
            System.out.println("Texture for " + this.getName() + " not found!");
        } else {
            TextureRegion textureRegion = this.textureAtlas.findRegion(this.getName());
            this.setWidth(textureRegion.getRegionWidth());
            this.setHeight(textureRegion.getRegionHeight());
            this.setOrigin(this.getX() + this.getWidth() / 2f, this.getY() + this.getHeight() / 2f);
        }
    }

    /**
     * Increases experience points of the Pokemon.
     * If the Pokemon has enough experience points, it levels up.
     *
     * @param exp The amount of experience points to increase by.
     */
    public void gainExp(int exp) {
        this.experience += exp;
        if (this.experience >= 100) {
            this.levelUp();
        }
    }

    /**
     * Causes the Pokemon to level up, increasing its stats and potentially causing it to evolve.
     */
    public void levelUp() {
        this.level++;
        if (this.level >= LVL_EVOLUTION_TO_TIER_2 && this.tier == 1 && this.evolvesInto != null) {
            this.evolve();
        }
        if (this.level >= LVL_EVOLUTION_TO_TIER_3 && this.tier == 2 && this.evolvesInto != null) {
            this.evolve();
        }

        for (Ability ability : this.abilities) {
            if (this.getLevel() >= ability.getLvlReq()) {
                ability.unlock();
            }
        }

        this.experience = 0;
        this.setMaxHealth((int) (this.getMaxHealth() + 1 / 50f * this.getMaxHealth()));
        this.attack += 1 / 50f * this.attack;
        this.defense += 1 / 50f * this.defense;
        this.speed += 1 / 50f * this.speed;
    }

    public int getLevel() {
        return this.level;
    }


    public void evolve() {
        this.tier++;
    }

    public void addAbility(Ability ability) {
        if (!this.abilities.contains(ability)) {
            if (this.getLevel() >= ability.getLvlReq()) {
                ability.unlock();
            }
            this.abilities.add(ability);
        }
    }

    /**
     * Increases the health of the Pokemon by the given amount.
     * If the health of the Pokemon is greater than the maximum health, the health is set to the maximum health.
     *
     * @param hp The amount of health to increase by.
     */
    public void increaseHP(int hp) {
        if (this.health + hp <= this.maxHealth) {
            this.health += hp;
        } else {
            this.health = this.maxHealth;
        }
    }

    public void increaseATT(int attack) {
        this.attack += attack;
    }

    public void increaseDEF(int defense) {
        this.defense += defense;
    }

    public void increaseSPD(int speed) {
        this.speed += speed;
    }

    /**
     * Attempts to decrease the health of the Pokemon by the given amount.
     * If the health of the Pokemon is less than or equal to 0, the Pokemon is considered fainted.
     * If the health of the Pokemon is less than 0, the health is set to 0.
     *
     * @param hp The amount of health to decrease by.
     */

    public void decreaseHP(int hp) {
        this.health -= hp;
        if (this.health <= 0) {
            this.fainted = true;
            this.health = 0;
        }
    }

    //TODO implement
    public void decreaseATT(int attack) {
        this.attack -= attack;
    }

    public void decreaseDEF(int defense) {
        this.defense -= defense;
    }

    public void decreaseSPD(int speed) {
        this.speed -= speed;
    }

    public int getAttack() {
        return this.attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = health;
        this.health = health;
    }

    public boolean hasFainted() {
        return this.fainted;
    }

    public void setFainted(boolean fainted) {
        this.fainted = fainted;
    }

    public int getDefense() {
        return this.defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Returns the power points of the Pokemon, which is calculated by the sum of the Pokemon's stats.
     */
    public int getPowerPoints() {
        int powerPoints = 0;
        powerPoints += 1 / 5f * this.health;
        powerPoints += 1 / 5f * this.attack;
        powerPoints += 1 / 5f * this.defense;
        powerPoints += 1 / 5f * this.speed;
        powerPoints += this.level;
        return powerPoints;
    }

    public Iterable<Effect> getStatusEffects() {
        return this.statusEffects;
    }

    public void addStatusEffect(Effect effect) {
        this.statusEffects.add(effect);
    }

    public Ability getAbility(int index) {
        return this.abilities.get(index);
    }

    public Iterable<Ability> getAbilities() {
        return this.abilities;
    }

    /**
     * Heals the Pokemon to full health
     */
    public void heal() {
        this.health = this.maxHealth;
    }

    /**
     * Returns all stats of the Pokemon in an array
     *
     * @return an array of 5 integers, representing the level, health, attack, defense and speed of the Pokemon
     */
    public int[] getStats() {
        int[] stats = new int[5];
        stats[0] = this.level;
        stats[1] = this.health;
        stats[2] = this.attack;
        stats[3] = this.defense;
        stats[4] = this.speed;
        return stats;
    }

    /**
     * Sets stats in bulk
     *
     * @param pokemonStats an array of 5 integers, representing the level, health, attack, defense and speed of the Pokemon
     */
    public void setStats(int[] pokemonStats) {
        if (pokemonStats.length != 5) {
            throw new IllegalArgumentException("Pokemon stats must be 5!");
        }
        if (pokemonStats[0] >= 1) {
            for (int i = 0; i < pokemonStats[0]; i++) {
                this.levelUp();
            }
            this.setMaxHealth(pokemonStats[1]);
            this.setAttack(pokemonStats[2]);
            this.setDefense(pokemonStats[3]);
            this.setSpeed(pokemonStats[4]);
        }
    }

    public boolean isCollected() {
        return this.isCollected;
    }

    public void setCollected(boolean collected) {
        this.isCollected = collected;
    }

    public int getNumOfAbilities() {
        return this.abilities.size();
    }

    /**
     * Draw method for usage in the rendering classes of the game.
     * For example in the render method of the Screen class.
     *
     * @param batch       The Batch used for drawing.
     * @param parentAlpha The parent alpha, which influences the transparency.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.textureAtlas.findRegion(this.getName()), this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());

        if (Constants.DEBUG) {
            BitmapFont font = new BitmapFont();
            font.draw(batch, this.toString(), this.getX() - this.getWidth() - 100, this.getY());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NAME: ").append(this.getName())
                .append(" LVL : ").append(this.getLevel())
                .append(" EXP: ").append(this.experience)
                .append(" HP: ").append(this.getHealth())
                .append(" ATT: ").append(this.getAttack())
                .append(" DEF: ").append(this.getDefense())
                .append(" SPD: ").append(this.getSpeed())
                .append(" PP: ").append(this.getPowerPoints()).append(System.lineSeparator())
                .append("FAINTED: ").append(this.hasFainted())
                .append(" COLLECTED: ").append(this.isCollected)
                .append(" ABILITIES: ").append(this.getAbilities().spliterator().getExactSizeIfKnown())
                .append(" STATUS EFFECTS: ").append(this.getStatusEffects().spliterator().getExactSizeIfKnown())
                .append(" EVOLVES INTO: ").append(this.evolvesInto)
                .append(" TIER: ").append(this.tier)
                .append(System.lineSeparator());

        for (Ability ability : this.getAbilities()) {
            sb.append(ability).append(System.lineSeparator());
        }

        for (Effect statusEffect : this.getStatusEffects()) {
            sb.append(statusEffect).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
