package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.ability.Ability;
import com.mygdx.game.character.Player;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.Pokeball;
import com.mygdx.game.item.UsableItem;
import com.mygdx.game.pokemon.Pokemon;

/**
 * Screen class handling the rendering of the battle.
 */
public class BattleScreen implements Screen {
    private final GameScreen gameScreen;
    private final GameClass game;
    private final Skin skin;
    private final Stage pokemonStage;
    private final Player player;
    private final ButtonGroup<TextButton> buttonGroup;
    private final Stage uiStage;
    private final BattleController battleController;

    //TODO redesign battle screen

    /**
     * Constructs a new BattleScreen.
     *
     * @param game         the main game instance.
     * @param gameScreen   the screen of the game.
     * @param skin         the skin to be used for UI elements.
     * @param player       the player involved in the battle.
     * @param enemyPokemon the enemy Pokemon involved in the battle.
     */
    public BattleScreen(GameClass game, GameScreen gameScreen, Skin skin, Player player, Pokemon enemyPokemon) {
        this.gameScreen = gameScreen;
        this.game = game;
        this.skin = skin;
        this.player = player;

        this.buttonGroup = new ButtonGroup<>();

        this.battleController = new BattleController(this.player, enemyPokemon);

        this.pokemonStage = new Stage(new ScreenViewport());
        this.uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.uiStage);

        this.init();
    }

    private void updateUI() {
        Pokemon enemyPokemon = this.battleController.getEnemyPokemon();
        Pokemon selectedPokemon = this.battleController.getSelectedPokemon();

        Table enemyInfo = this.uiStage.getRoot().findActor("enemyInfo");
        Table playerInfo = this.uiStage.getRoot().findActor("playerInfo");
        ((Label) enemyInfo.findActor("enemyName")).setText(enemyPokemon.getName());
        ((Label) enemyInfo.findActor("enemyLevel")).setText("Lvl: " + enemyPokemon.getLevel());
        ((Label) enemyInfo.findActor("enemyHp")).setText("HP: " + enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth());

        ((Label) playerInfo.findActor("playerName")).setText(selectedPokemon.getName());
        ((Label) playerInfo.findActor("playerLevel")).setText("Lvl: " + selectedPokemon.getLevel());
        ((Label) playerInfo.findActor("playerHp")).setText("HP: " + selectedPokemon.getHealth() + "/" + selectedPokemon.getMaxHealth());

        if (enemyPokemon.hasFainted()) {
            selectedPokemon.gainExp(enemyPokemon.getLevel() * 5);
            this.player.gainGold(enemyPokemon.getLevel() * 5);
            this.game.setScreen(this.gameScreen);
        }
    }

    private void attackUI(Table buttonsTable) {
        buttonsTable.clearChildren();
        Pokemon selectedPokemon = this.battleController.getSelectedPokemon();

        for (Ability ability : selectedPokemon.getAbilities()) {
            TextButton abilityButton = new TextButton(ability.getName(), this.skin);
            abilityButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    BattleScreen.this.battleController.attack(ability);
                    BattleScreen.this.battleController.nextTurn();
                    BattleScreen.this.switchMainUI(buttonsTable);
                }
            });
            buttonsTable.add(abilityButton).row();
        }


        TextButton backButton = new TextButton("Back", this.skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.switchMainUI(buttonsTable);
            }
        });
        buttonsTable.add(backButton).row();
    }

    private void bagUI(Table buttonsTable) {
        buttonsTable.clearChildren();
        TextButton backButton = new TextButton("Back", this.skin);

        for (Item item : this.player.getInvetory()) {
            if (item instanceof Pokeball) {
                continue;
            }
            TextButton itemButton = new TextButton(item.getName(), this.skin);
            itemButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (item instanceof UsableItem) {
                        System.out.println("Using item " + item.getName());
                        BattleScreen.this.player.useItem((UsableItem) item, BattleScreen.this.battleController.getSelectedPokemon());
                        BattleScreen.this.switchMainUI(buttonsTable);
                    }
                }
            });
            buttonsTable.add(itemButton).row();
        }

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.switchMainUI(buttonsTable);
            }
        });
        buttonsTable.add(backButton).row();
    }

    private void switchMainUI(Table buttonsTable) {
        this.pokemonStage.clear();
        buttonsTable.clearChildren();
        buttonsTable.defaults().size(300, 100).pad(2);
        buttonsTable.bottom().right();
        Array<TextButton> buttons = this.buttonGroup.getButtons();
        for (int i = 0; i < buttons.size; i++) {
            buttonsTable.add(buttons.get(i));
            if (i % 2 == 1) {
                buttonsTable.row();
            }
        }

        this.setPositions();

        this.pokemonStage.addActor(this.battleController.getSelectedPokemon());
        this.pokemonStage.addActor(this.battleController.getEnemyPokemon());
    }

    private void switchPokemonUI(Table buttonsTable) {
        buttonsTable.clearChildren();
        for (Pokemon pokemon : this.player.getParty()) {
            TextButton button = new TextButton(pokemon.getName(), this.skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Switching to " + pokemon.getName());
                    BattleScreen.this.battleController.switchPokemon(pokemon);
                    BattleScreen.this.switchMainUI(buttonsTable);
                }
            });
            buttonsTable.add(button).row();
        }
        TextButton backButton = new TextButton("Back", this.skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.switchMainUI(buttonsTable);
            }
        });
        buttonsTable.add(backButton).row();
    }

    private void setPositions() {
        Pokemon selectedPokemon = this.battleController.getSelectedPokemon();
        Pokemon enemyPokemon = this.battleController.getEnemyPokemon();

        selectedPokemon.setPosition(300, 300);
        enemyPokemon.setPosition(Gdx.graphics.getWidth() - 400f, Gdx.graphics.getHeight() - 400f);
        selectedPokemon.setScale(20);
        enemyPokemon.setScale(20);
    }

    public void init() {
        Pokemon selectedPokemon = this.battleController.getSelectedPokemon();
        Pokemon enemyPokemon = this.battleController.getEnemyPokemon();

        Table buttonsTable = new Table();
        buttonsTable.setName("buttons");
        buttonsTable.setFillParent(true);

        Table enemyInfoTable = new Table();
        enemyInfoTable.setName("enemyInfo");
        Table playerInfoTable = new Table();
        playerInfoTable.setName("playerInfo");

        enemyInfoTable.setFillParent(true);
        playerInfoTable.setFillParent(true);

        this.uiStage.addActor(buttonsTable);
        this.uiStage.addActor(enemyInfoTable);
        this.uiStage.addActor(playerInfoTable);

        TextButton attack = new TextButton("Fight", this.skin);
        TextButton bag = new TextButton("Bag", this.skin);
        TextButton switchPokemon = new TextButton("Pokemon", this.skin);
        TextButton run = new TextButton("Run", this.skin);

        Label enemyPokemonName = new Label(enemyPokemon.getName(), this.skin);
        enemyPokemonName.setName("enemyName");

        Label enemyPokemonLevel = new Label("Lvl: " + enemyPokemon.getLevel(), this.skin);
        enemyPokemonLevel.setName("enemyLevel");

        Label enemyPokemonHP = new Label("HP: " + enemyPokemon.getHealth() + "/" + enemyPokemon.getMaxHealth(), this.skin);
        enemyPokemonHP.setName("enemyHp");

        Label playerPokemonName = new Label(selectedPokemon.getName(), this.skin);
        playerPokemonName.setName("playerName");

        Label playerPokemonLevel = new Label("Lvl: " + selectedPokemon.getLevel(), this.skin);
        playerPokemonLevel.setName("playerLevel");

        Label playerPokemonHP = new Label("HP: " + selectedPokemon.getHealth() + "/" + selectedPokemon.getMaxHealth(), this.skin);
        playerPokemonHP.setName("playerHp");

        enemyInfoTable.defaults().uniform().fill().pad(5);
        enemyInfoTable.top();
        enemyInfoTable.add(enemyPokemonName);
        enemyInfoTable.row();
        enemyInfoTable.add(enemyPokemonLevel);
        enemyInfoTable.add(enemyPokemonHP);

        playerInfoTable.defaults().uniform().fill().pad(5);
        playerInfoTable.bottom();
        playerInfoTable.add(playerPokemonName);
        playerInfoTable.row();
        playerInfoTable.add(playerPokemonLevel);
        playerInfoTable.add(playerPokemonHP);

        attack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.attackUI(buttonsTable);
            }
        });

        bag.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.bagUI(buttonsTable);
            }
        });

        switchPokemon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.switchPokemonUI(buttonsTable);
            }
        });

        run.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BattleScreen.this.game.setScreen(BattleScreen.this.gameScreen);
            }
        });

        this.buttonGroup.add(attack, bag, switchPokemon, run);

        this.switchMainUI(buttonsTable);
    }

    /**
     * Renders the screen every frame.
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.8f, 0.8f, 0.8f, 1);

        this.updateUI();

        this.pokemonStage.getViewport().apply();
        this.pokemonStage.act(delta);
        this.pokemonStage.draw();

        this.uiStage.getViewport().apply();
        this.uiStage.act(delta);
        this.uiStage.draw();

        if (this.battleController.checkBattleEnd()) {
            this.game.setScreen(this.gameScreen);
        }
    }

    /**
     * Triggers everytime the screen is resized.
     * Updates the viewport of the screen.
     *
     * @param width  the new width in pixels.
     * @param height the new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        this.uiStage.getViewport().update(width, height, true);
        this.pokemonStage.getViewport().update(width, height, true);
        this.setPositions();
    }

    /**
     * Called when this screen becomes the current screen for a game.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.uiStage);
    }

    /**
     * Called when the screen should release all resources.
     */
    @Override
    public void dispose() {
        this.uiStage.dispose();
        this.pokemonStage.dispose();
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
}
