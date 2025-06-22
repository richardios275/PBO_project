package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.character.Player;
import com.mygdx.game.item.Effect;
import com.mygdx.game.item.Pokeball;
import com.mygdx.game.item.Potion;
import com.mygdx.game.pokemon.Pokedex;

/**
 * The MainMenuScreen class represents the main menu of the game.
 * It provides options to play the game, access settings and exit the game.
 * Upon choosing to play the game, it prompts the player to enter their name and initializes a new game.
 */
public class MainMenuScreen implements Screen {
    private final GameClass game;
    private final Skin skin;
    private final Pokedex pokedex;
    private final SettingsScreen settingsScreen;
    private final Stage menu;
    private final Table table;
    private ButtonGroup<TextButton> buttons;
    private boolean isPlayClicked;
    private Music menuMusic;

    /**
     * Constructs a new MainMenuScreen.
     *
     * @param game    The main game class
     * @param skin    The skin for styling
     * @param pokedex The game's Pokedex
     */
    public MainMenuScreen(GameClass game, Skin skin, Pokedex pokedex) {
        this.game = game;
        this.skin = skin;
        this.pokedex = pokedex;
        this.isPlayClicked = false;
        this.settingsScreen = new SettingsScreen(this.game, this.skin, this);

        this.table = new Table();
        this.table.defaults().uniform().fill().pad(2);
        this.createButtons();
        this.menu = new Stage(new ScreenViewport());
        this.menu.addActor(this.table);
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
        menuMusic.setLooping(true);
        menuMusic.setVolume(0.5f);
        menuMusic.play();

        Gdx.input.setInputProcessor(this.menu);
    }

    private void createButtons() {
        this.table.setFillParent(true);

        TextButton playButton = new TextButton("Play", this.skin);
        TextButton settingsButton = new TextButton("Settings", this.skin);
        TextButton exitButton = new TextButton("Exit", this.skin);
        this.buttons = new ButtonGroup<>(playButton, settingsButton, exitButton);

        for (TextButton button : this.buttons.getButtons()) {
            this.table.add(button).size(250, 100).pad(2);
            this.table.row();
        }
    }

    private void starterItemsPlayer(Player player) {
        player.collectPokemon(this.pokedex.getPokemon("charmander"));
        player.takeItem(new Potion("Attack potion", "Raises 20 ATT", 20, Effect.BUFF_ATTACK));
        player.takeItem(new Potion("Heal potion", "Heals 20 HP", 20, Effect.HEAL));
        player.takeItem(new Pokeball());
    }

    private void createEnterNameDialog() {
        Label welcomeLabel = new Label("Hello, welcome to the world of Johto, please enter your name: ", this.skin);

        this.table.add(welcomeLabel);
        this.table.row();

        TextField nameField = new TextField("", this.skin);
        nameField.setMessageText("Enter your name");
        this.table.add(nameField).width(500);
        this.table.row();

        TextButton continueButton = new TextButton("Continue", this.skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Player player = new Player(nameField.getText(), MainMenuScreen.this.pokedex);
                MainMenuScreen.this.starterItemsPlayer(player);
                MainMenuScreen.this.game.setScreen(new GameScreen(MainMenuScreen.this.game, MainMenuScreen.this.skin, player, MainMenuScreen.this.pokedex));
            }
        });
        this.table.add(continueButton).width(300).padTop(10);
    }

    /**
     * Renders the main menu screen.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.53f, 0.88f, 1, 1);
        this.menu.getViewport().apply();
        this.menu.act(delta);
        this.menu.draw();

        // 0 - play, 1 - settings, 2 - exit
        if (!this.isPlayClicked) {
            if (this.buttons.getButtons().get(0).isPressed()) {
                this.isPlayClicked = true;
                this.buttons.clear();
                this.table.clear();
                this.createEnterNameDialog();
            } else if (this.buttons.getButtons().get(1).isPressed()) {
                this.game.setScreen(new SettingsScreen(this.game, this.skin, this));
            } else if (this.buttons.getButtons().get(2).isPressed()) {
                Gdx.app.exit();
            }
        }
    }

    /**
     * Calls when the screen gets resized.
     *
     * @param width  The new width
     * @param height The new height
     */
    @Override
    public void resize(int width, int height) {
        this.menu.getViewport().update(width, height, true);
    }

    /**
     * Disposes of all objects in the MainMenuScreen.
     * This is called when the MainMenuScreen is no longer needed.
     */
    @Override
    public void dispose() {
        this.menu.dispose();
    }

    @Override
    public void pause() {
        //unused
    }

    @Override
    public void resume() {
        //unused
    }

    @Override
    public void hide() {
        //unused
    }

    /**
     * Called when the screen is shown.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.menu);
    }
}
