package com.mygdx.game.game;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.character.Player;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Pokeball;
import com.mygdx.game.pokemon.Pokemon;

/**
 * Class managing combat between the player's Pokémon and the enemy's Pokémon in 2 vs 2 format.
 */
public class BattleController {
    private final Player player;
    private final Pokemon[] enemyPokemons;  // 2 musuh
    private final Pokemon[] allyPokemons;   // 2 sekutu
    private int currentPlayerPartySize;
    private int turnCounter = 0; // menentukan giliran

    public BattleController(Player player, Pokemon enemy1, Pokemon enemy2) {
        this.player = player;
        this.enemyPokemons = new Pokemon[] { enemy1, enemy2 };
        this.allyPokemons = new Pokemon[] { player.getPokemon(0), player.getPokemon(1) };
        this.currentPlayerPartySize = this.player.getPartySize();
    }

    public void nextTurn() {
        // Giliran musuh bergantian
        for (int i = 0; i < 2; i++) {
            if (!enemyPokemons[i].hasFainted()) {
                int targetIndex = MathUtils.random(0, 1);
                if (!allyPokemons[targetIndex].hasFainted()) {
                    Ability ability = enemyPokemons[i].getAbility(
                        MathUtils.random(0, enemyPokemons[i].getNumOfAbilities() - 1)
                    );
                    attack(ability, enemyPokemons[i], allyPokemons[targetIndex]);
                }
            }
        }
        turnCounter++;
    }

    public void attack(Ability ability, int attackerIndex, int targetIndex, boolean isAllyAttacker) {
        Pokemon attacker = isAllyAttacker ? allyPokemons[attackerIndex] : enemyPokemons[attackerIndex];
        Pokemon target = isAllyAttacker ? enemyPokemons[targetIndex] : allyPokemons[targetIndex];
        attack(ability, attacker, target);
    }

    public void attack(Ability ability, Pokemon source, Pokemon target) {
        if (!ability.isUnlocked()) {
            System.out.println("Ability is not unlocked!");
            return;
        }

        switch (ability.getEffect()) {
            case NONE -> {
                double effectiveness = source.getEffectiveness(target);
                target.decreaseHP((int) (ability.getAmount() * effectiveness));
            }
            case HEAL -> source.increaseHP(ability.getAmount());
            case BUFF_ATTACK -> source.increaseATT(ability.getAmount());
            case BUFF_DEFENSE -> source.increaseDEF(ability.getAmount());
            case BUFF_SPEED -> source.increaseSPD(ability.getAmount());
            default -> target.addStatusEffect(ability.getEffect());
        }
    }

    public void switchPokemon(int allyIndex, Pokemon newPokemon) {
        allyPokemons[allyIndex] = newPokemon;
    }

    public boolean checkBattleEnd() {
        boolean allEnemiesFainted = enemyPokemons[0].hasFainted() && enemyPokemons[1].hasFainted();
        boolean allAlliesFainted = allyPokemons[0].hasFainted() && allyPokemons[1].hasFainted();

        if (allEnemiesFainted) {
            for (Pokemon enemy : enemyPokemons) {
                if (!enemy.isCollected()) {
                    Item item = player.getItemFromInventory("Pokeball");
                    if (item instanceof Pokeball pokeball && pokeball.attemptToCatch(enemy)) {
                        player.collectPokemon(enemy);
                    }
                }
            }
            for (Pokemon ally : allyPokemons) {
                ally.gainExp(10); // EXP untuk kemenangan
            }
            player.gainGold(20);
            return true;
        }

        if (allAlliesFainted) {
            for (Pokemon ally : allyPokemons) {
                ally.setFainted(false);
                ally.heal();
            }
            return true;
        }

        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public Pokemon[] getEnemyPokemons() {
        return enemyPokemons;
    }

    public Pokemon[] getAllyPokemons() {
        return allyPokemons;
    }

    public int getTurnCounter() {
        return turnCounter;
    }
}
