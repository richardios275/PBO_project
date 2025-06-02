package com.mygdx.game.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.ability.AbilityLoader;
import com.mygdx.game.character.Player;
import com.mygdx.game.item.Effect;
import com.mygdx.game.item.Pokeball;
import com.mygdx.game.item.Potion;
import com.mygdx.game.pokemon.FirePokemon;
import com.mygdx.game.pokemon.Pokedex;
import com.mygdx.game.pokemon.Pokemon;

/**
 * Entry point of the game. This class is responsible for creating the game
 */
public class GameClass extends Game {
    private Skin skin;
    private Pokedex pokedex;
    private MainMenuScreen mainMenuScreen;
    private SettingsScreen settingsScreen;
    private Player player;

    private void testGame() {
        this.player = new Player("Player", this.pokedex);
        Pokemon debugFirePokemon = new FirePokemon("charizard", 3, 300, 10, 10, 10, null);
        Pokemon debugWaterPokemon = this.pokedex.getPokemon("squirtle");
        this.player.collectPokemon(debugFirePokemon);
        this.player.collectPokemon(debugWaterPokemon);
        for (int i = 0; i < 5; i++) {
            debugFirePokemon.levelUp();
        }
        Pokeball debugPokeball = new Pokeball();
        Potion debugHPPotion = new Potion("Heal potion", "Heals 20 HP", 20, Effect.HEAL);
        Potion debugATTPotion = new Potion("Attack potion", "Raises 20 ATT", 20, Effect.BUFF_ATTACK);
        this.player.takeItem(debugHPPotion);
        this.player.takeItem(debugATTPotion);
        this.player.takeItem(debugPokeball);
    }

    /**
     * Creates a new game session. This method initializes the skin, Pokedex,
     * the main menu screen, and settings screen. It also sets the current screen.
     */
    @Override
    public void create() {
        AbilityLoader abilityLoader = new AbilityLoader();
        this.pokedex = new Pokedex(abilityLoader);
        this.skin = new Skin(Gdx.files.internal("skin\\skin.json"), new TextureAtlas("skin\\skin.atlas"));
        this.mainMenuScreen = new MainMenuScreen(this, this.skin, this.pokedex);
        this.settingsScreen = new SettingsScreen(this, this.skin, this.mainMenuScreen);
        if (Constants.DEBUG) {
            this.testGame();
            this.setScreen(new GameScreen(this, this.skin, this.player, this.pokedex));
        } else {
            this.setScreen(this.mainMenuScreen);
        }
    }

    /**
     * Called when this game is being disposed of. This method disposes
     * of the main menu screen, settings screen, and skin.
     */
    @Override
    public void dispose() {
        super.dispose();
        this.mainMenuScreen.dispose();
        this.settingsScreen.dispose();
        this.skin.dispose();
    }
}
