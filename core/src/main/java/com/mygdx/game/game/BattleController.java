package com.mygdx.game.game;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.character.Player;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Pokeball;
import com.mygdx.game.pokemon.Pokemon;

/**
 * Class managing combat between the player pokemon and the enemy pokemon.
 */
public class BattleController {
    private final Player player;
    private final Pokemon enemyPokemon;
    private Pokemon selectedPokemon;
    private int turnCounter;
    private int currentPlayerPartySize;
    private String attackMessage;

    /**
     * Constructs a BattleController object to manage a battle between a Player and an Enemy Pokemon.
     *
     * @param player       The player involved in the battle.
     * @param enemyPokemon The enemy Pokemon involved in the battle.
     */
    public BattleController(Player player, Pokemon enemyPokemon) {
        this.player = player;
        this.enemyPokemon = enemyPokemon;
        this.currentPlayerPartySize = this.player.getPartySize();
        this.selectedPokemon = player.getFirstPokemon();
    }

    /**
     * Processes the next turn of the battle, including the enemy's turn.
     */
    public void nextTurn() {
        this.enemyTurn();
        this.turnCounter++;
    }

    private void enemyTurn() {
        int size = this.enemyPokemon.getNumOfAbilities();
        Ability ability = this.enemyPokemon.getAbility(MathUtils.random(0, size - 1));
        this.attack(ability, this.enemyPokemon, this.selectedPokemon);
    }

    /**
     * Player's Pokemon attacks using the provided Ability.
     *
     * @param ability The ability that the player's Pokemon will use to attack.
     */
    public void attack(Ability ability) {
        this.attack(ability, this.selectedPokemon, this.enemyPokemon);
    }

    /**
     * Pokemon attacks another Pokemon using a specified Ability.
     *
     * @param ability The Ability being used to attack.
     * @param source  The Pokemon that is using the Ability.
     * @param target  The Pokemon that is being attacked.
     */
    public void attack(Ability ability, Pokemon source, Pokemon target) {
        if (!ability.isUnlocked()) {
            System.out.println("Ability is not unlocked!");
            return;
        }
        switch (ability.getEffect()) {
            case NONE: {
                double effectiveness = source.getEffectiveness(target);
                target.decreaseHP((int) (ability.getAmount() * effectiveness));
                break;
            }
            case HEAL: {
                source.increaseHP(ability.getAmount());
                break;
            }
            case BUFF_ATTACK: {
                source.increaseATT(ability.getAmount());
                break;
            }
            case BUFF_DEFENSE: {
                source.increaseDEF(ability.getAmount());
                break;
            }
            case BUFF_SPEED: {
                source.increaseSPD(ability.getAmount());
                break;
            }
            default: {
                this.enemyPokemon.addStatusEffect(ability.getEffect());
                break;
            }
        }
    }

    /**
     * Switches the player's currently selected Pokemon with a new one.
     *
     * @param newPokemon The new Pokemon to be selected.
     */
    public void switchPokemon(Pokemon newPokemon) {
        this.selectedPokemon = newPokemon;
    }

    /**
     * Checks if the battle has ended. A battle ends if either the enemy Pokemon has fainted, or all the player's Pokemon have fainted.
     *
     * @return True if the battle has ended, false otherwise.
     */
    public boolean checkBattleEnd() {
        if (this.enemyPokemon.hasFainted()) {
            this.selectedPokemon.gainExp(this.enemyPokemon.getLevel() * 5);
            this.player.gainGold(this.enemyPokemon.getLevel() * 5);

            Item item = this.player.getItemFromInventory("Pokeball");
            if (item instanceof Pokeball && !this.enemyPokemon.isCollected()) {
                Pokeball pokeball = (Pokeball) item;
                if (pokeball.attemptToCatch(this.enemyPokemon)) {
                    this.player.collectPokemon(this.enemyPokemon);
                }
            }

            return true;
        }

        if (this.selectedPokemon.hasFainted()) {
            this.currentPlayerPartySize--;
            if (this.currentPlayerPartySize > 0) {
                this.selectedPokemon = this.player.getFirstPokemon();
            } else {
                this.selectedPokemon.setFainted(false);
                this.selectedPokemon.heal();
                return true;
            }
        }
        return false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Pokemon getEnemyPokemon() {
        return this.enemyPokemon;
    }

    public Pokemon getSelectedPokemon() {
        return this.selectedPokemon;
    }
}
