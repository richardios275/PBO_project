package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.character.Player;
import com.mygdx.game.pokemon.Pokedex;
import com.mygdx.game.pokemon.Pokemon;

import java.util.ArrayList;

/**
 * Class that represents the game screen where the gameplay is rendered.
 * This class includes methods for initializing and updating the player HUD,
 * pausing and resuming the game, starting battles, switching game zones,
 * handling screen resizing, and disposing resources when they are no longer needed.
 */
public class GameScreen implements Screen {
    private static final int MAP_SIZE = 48 * 16;
    private final GameClass game;
    private final Stage environmentStage;
    private final Player player;
    private final Pokedex pokedex;
    private final Skin skin;
    private final GameController gameController;
    private Table debugTable;
    private Stage hudStage;
    private Stage pauseStage;
    private boolean isPaused;
    private Zone zone;
    private String currentZone;
    private ArrayList<RectangleMapObject> collisionObjects;
    private ArrayList<RectangleMapObject> exitHitboxes;
    private ArrayList<RectangleMapObject> pokemonSpawnAreas;
    private TiledMapRenderer mapRenderer;

    public GameScreen(GameClass game, Skin skin, Player player, Pokedex pokedex) {
        this.game = game;
        this.skin = skin;
        this.player = player;
        this.pokedex = pokedex;
        this.gameController = new GameController(this.player, this);
        this.environmentStage = new Stage(new FitViewport(MAP_SIZE, MAP_SIZE));

        this.environmentStage.getCamera().position.set(MAP_SIZE / 2f, MAP_SIZE / 2f, 0);
        this.environmentStage.getCamera().update();
        this.environmentStage.addActor(this.player);

        this.zone = new Zone("starter_town", this.player, this.pokedex);

        this.currentZone = this.zone.getMapName();

        this.mapRenderer = new OrthogonalTiledMapRenderer(this.zone.getTiledMap(), this.environmentStage.getBatch());

        this.initZone();
        this.initHUD();
    }

    private void initHUD() {
        this.hudStage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.setFillParent(true);
        table.top().left();
        table.defaults().pad(5);

        if (Constants.DEBUG) {
            this.debugTable = new Table();
            this.debugTable.setFillParent(true);
            this.debugTable.left();
            this.debugTable.defaults().pad(5);
            for (Pokemon pokemon : this.player.getParty()) {
                Label label = new Label(pokemon.toString(), this.skin);
                label.setName(pokemon.getName());
                this.debugTable.add(label).row();
            }
            this.hudStage.addActor(this.debugTable);
        }

        Label playerLabel = new Label(this.player.toString(), this.skin);
        playerLabel.setName("player");

        table.add(playerLabel).row();
        this.hudStage.addActor(table);
    }

    private void updateHUD() {
        if (Constants.DEBUG) {
            for (Pokemon pokemon : this.player.getParty()) {
                Label label = this.debugTable.findActor(pokemon.getName());
                label.setText(pokemon.toString());
            }
        }

        Label playerLabel = this.hudStage.getRoot().findActor("player");
        playerLabel.setText(this.player.toString());
    }

    private void initZone() {
        if (this.collisionObjects != null) {
            this.collisionObjects.clear();
        }
        if (this.exitHitboxes != null) {
            this.exitHitboxes.clear();
        }
        if (this.pokemonSpawnAreas != null) {
            this.pokemonSpawnAreas.clear();
        }

        this.collisionObjects = (ArrayList<RectangleMapObject>) this.zone.getCollisionObjects();
        this.pokemonSpawnAreas = (ArrayList<RectangleMapObject>) this.zone.getPokemonSpawnAreas();
        this.exitHitboxes = (ArrayList<RectangleMapObject>) this.zone.getExitHitboxes();
    }

    /**
     * Switches the current game zone to a new one.
     *
     * @param nextZone the name of the zone to switch to
     */
    public void switchZone(String nextZone) {
        this.zone.getTiledMap().dispose();
        this.environmentStage.clear();
        this.environmentStage.addActor(this.player);

        String prevZone = this.currentZone;
        this.currentZone = nextZone;

        this.zone = new Zone(nextZone, this.player, this.pokedex);
        this.mapRenderer = new OrthogonalTiledMapRenderer(this.zone.getTiledMap(), this.environmentStage.getBatch());

        this.initZone();

        this.zone.getPokemons().keySet().forEach(this.environmentStage::addActor);

        //TODO refactor this to be more generic
        if (this.currentZone.equals("starter_forest") && prevZone.equals("starter_town")) {
            this.player.setPosition(55, 462);
        }
        if (this.currentZone.equals("starter_town") && prevZone.equals("starter_forest")) {
            this.player.setPosition(723, 610);
        }
    }

    /**
     * Pauses the game. This method sets the pause stage and its buttons,
     * such as Pokedex, Pokemon, Bag, Player, Save, Options, and Exit.
     */

    public void pauseGame() {
        this.isPaused = true;

        this.pauseStage = new Stage(new ScreenViewport());
        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.defaults().uniform().pad(2).size(200, 50);
        this.pauseStage.addActor(pauseTable);

        TextButton pokedexButton = new TextButton("Pokedex", this.skin);
        TextButton pokemonButton = new TextButton("Pokemon", this.skin);
        TextButton bagButton = new TextButton("Bag", this.skin);
        TextButton playerButton = new TextButton(this.player.getName(), this.skin);
        TextButton saveButton = new TextButton("Save", this.skin);
        TextButton optionsButton = new TextButton("Options", this.skin);
        TextButton exitButton = new TextButton("Exit", this.skin);

        pokedexButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO pokedex
            }
        });

        pokemonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO party tab
            }
        });

        bagButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO inventory
            }
        });

        playerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO player info
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO save the game
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO options
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        ButtonGroup<TextButton> buttonGroup = new ButtonGroup<>(pokedexButton, pokemonButton, bagButton, playerButton, saveButton, optionsButton, exitButton);

        for (TextButton button : buttonGroup.getButtons()) {
            pauseTable.add(button);
            pauseTable.row();
        }

        pauseTable.setPosition(0, Gdx.graphics.getHeight());
        pauseTable.addAction(Actions.moveTo(0, 0, 1, Interpolation.sine));

        Gdx.input.setInputProcessor(this.pauseStage);
    }

    /**
     * Resumes the game by setting isPaused to false.
     */
    public void resumeGame() {
        this.isPaused = false;
    }

    /**
     * Returns whether the game is paused or not.
     *
     * @return true if the game is paused, false otherwise
     */
    public boolean isPaused() {
        return this.isPaused;
    }

    /**
     * Starts a new battle by switching to a BattleScreen.
     *
     * @param enemyPokemon the Pokemon that the player will fight against
     */
    public void startBattle(Pokemon enemyPokemon) {
        this.game.setScreen(new BattleScreen(this.game, this, this.skin, this.player, enemyPokemon));
    }

    /**
     * Renders the game screen. This method clears the screen, handles player input,
     * checks for collisions, updates the player and the game stages, and draws
     * everything to the screen.
     *
     * @param delta the time interval for the game update
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.54f, 1, 0.61f, 1);

        this.gameController.handleInput();
        this.gameController.checkCollisions(delta, this.collisionObjects, this.exitHitboxes, this.zone.getPokemons());

        this.player.update(delta);

        this.environmentStage.getCamera().update();
        this.environmentStage.getViewport().apply();

        this.mapRenderer.setView((OrthographicCamera) this.environmentStage.getCamera());
        this.mapRenderer.render();

        this.environmentStage.act(delta);
        this.environmentStage.draw();

        this.updateHUD();
        this.hudStage.getViewport().apply();
        this.hudStage.act(delta);
        this.hudStage.draw();

        if (this.isPaused) {
            this.pauseStage.getViewport().apply();
            this.pauseStage.act(delta);
            this.pauseStage.draw();
        }

        if (Constants.DEBUG && Gdx.input.isKeyPressed(Input.Keys.Z)) {
            this.game.setScreen(new BattleScreen(this.game, this, this.skin, this.player, this.pokedex.getPokemon("bulbasaur")));
        }
    }

    /**
     * Shows the game screen by setting the input processor to the environment stage.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.environmentStage);
    }

    /**
     * Resizes the game screen. This method updates the viewports of the stages
     * to match the new screen dimensions.
     *
     * @param width  the new screen width
     * @param height the new screen height
     */
    @Override
    public void resize(int width, int height) {
        this.hudStage.getViewport().update(width, height, true);
        this.environmentStage.getViewport().update(width, height, true);
    }

    /**
     * Disposes resources when they are no longer needed.
     */
    @Override
    public void dispose() {
        this.environmentStage.dispose();
        this.hudStage.dispose();
        this.pauseStage.dispose();
    }

    // The following methods are not used in this class, but they are required by the Screen interface.
    @Override
    public void pause() {
        // unused
    }

    @Override
    public void resume() {
        // unused
    }

    @Override
    public void hide() {
        // unused
    }
}
