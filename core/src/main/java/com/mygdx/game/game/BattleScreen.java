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

public class BattleScreen implements Screen {
    private final GameScreen gameScreen;
    private final GameClass game;
    private final Skin skin;
    private final Stage pokemonStage;
    private final Player player;
    private final ButtonGroup<TextButton> buttonGroup;
    private final Stage uiStage;
    private final BattleController battleController;

    // Track PokÃ©mon ally yang sedang aktif melakukan aksi (index 0 atau 1)
    private int currentAllyIndex = 0;

    public BattleScreen(GameClass game, GameScreen gameScreen, Skin skin, Player player, Pokemon enemy1, Pokemon enemy2) {
        this.gameScreen = gameScreen;
        this.game = game;
        this.skin = skin;
        this.player = player;
        this.buttonGroup = new ButtonGroup<>();
        this.battleController = new BattleController(this.player, enemy1, enemy2);
        this.pokemonStage = new Stage(new ScreenViewport());
        this.uiStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this.uiStage);
        this.init();
    }

    private void updateUI() {
        Pokemon[] enemies = this.battleController.getEnemyPokemons();
        Pokemon[] allies = this.battleController.getAllyPokemons();

        for (int i = 0; i < 2; i++) {
            Table enemyInfo = this.uiStage.getRoot().findActor("enemyInfo" + i);
            ((Label) enemyInfo.findActor("enemyName" + i)).setText(enemies[i].getName());
            ((Label) enemyInfo.findActor("enemyLevel" + i)).setText("Lvl: " + enemies[i].getLevel());
            ((Label) enemyInfo.findActor("enemyHp" + i)).setText("HP: " + enemies[i].getHealth() + "/" + enemies[i].getMaxHealth());

            Table playerInfo = this.uiStage.getRoot().findActor("playerInfo" + i);
            ((Label) playerInfo.findActor("playerName" + i)).setText(allies[i].getName());
            ((Label) playerInfo.findActor("playerLevel" + i)).setText("Lvl: " + allies[i].getLevel());
            ((Label) playerInfo.findActor("playerHp" + i)).setText("HP: " + allies[i].getHealth() + "/" + allies[i].getMaxHealth());
        }
    }

    private void attackUI(Table buttonsTable) {
        buttonsTable.clearChildren();
        Pokemon current = battleController.getAllyPokemons()[currentAllyIndex]; // pakai currentAllyIndex

        for (Ability ability : current.getAbilities()) {
            TextButton abilityButton = new TextButton(ability.getName(), skin);
            abilityButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // lakukan serangan dengan ability dari current ally ke musuh secara random (target musuh random)
                    Pokemon[] enemies = battleController.getEnemyPokemons();

                    // Cari musuh yang belum faint, pilih random satu
                    int targetIndex = -1;
                    for (int i = 0; i < enemies.length; i++) {
                        if (!enemies[i].hasFainted()) {
                            targetIndex = i;
                            break;
                        }
                    }
                    if (targetIndex == -1) targetIndex = 0; // fallback

                    battleController.attack(ability, currentAllyIndex, targetIndex, true);
                    battleController.nextTurn();
                    // pindah ke ally berikutnya yang belum faint
                    currentAllyIndex = (currentAllyIndex + 1) % 2;
                    switchMainUI(buttonsTable);
                }
            });
            buttonsTable.add(abilityButton).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchMainUI(buttonsTable);
            }
        });
        buttonsTable.add(backButton).row();
    }

    private void bagUI(Table buttonsTable) {
        buttonsTable.clearChildren();
        for (Item item : player.getInvetory()) {
            if (item instanceof Pokeball) continue;
            TextButton itemButton = new TextButton(item.getName(), skin);
            itemButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (item instanceof UsableItem) {
                        player.useItem((UsableItem) item, battleController.getAllyPokemons()[currentAllyIndex]);
                        switchMainUI(buttonsTable);
                    }
                }
            });
            buttonsTable.add(itemButton).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchMainUI(buttonsTable);
            }
        });
        buttonsTable.add(backButton).row();
    }

    private void switchPokemonUI(Table buttonsTable) {
        buttonsTable.clearChildren();
        for (Pokemon pokemon : player.getParty()) {
            TextButton button = new TextButton(pokemon.getName(), skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    battleController.switchPokemon(currentAllyIndex, pokemon);
                    switchMainUI(buttonsTable);
                }
            });
            buttonsTable.add(button).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switchMainUI(buttonsTable);
            }
        });
        buttonsTable.add(backButton).row();
    }

    private void switchMainUI(Table buttonsTable) {
        pokemonStage.clear();
        buttonsTable.clearChildren();
        buttonsTable.defaults().size(300, 100).pad(2);
        buttonsTable.bottom().right();
        Array<TextButton> buttons = buttonGroup.getButtons();
        for (int i = 0; i < buttons.size; i++) {
            buttonsTable.add(buttons.get(i));
            if (i % 2 == 1) buttonsTable.row();
        }

        setPositions();

        for (Pokemon p : battleController.getAllyPokemons()) pokemonStage.addActor(p);
        for (Pokemon e : battleController.getEnemyPokemons()) pokemonStage.addActor(e);
    }

    private void setPositions() {
        Pokemon[] allies = battleController.getAllyPokemons();
        Pokemon[] enemies = battleController.getEnemyPokemons();

        allies[0].setPosition(250, 200);
        allies[1].setPosition(350, 250);
        enemies[0].setPosition(900, 500);
        enemies[1].setPosition(1000, 550);

        for (Pokemon p : allies) p.setScale(7);
        for (Pokemon p : enemies) p.setScale(7);
    }

    public void init() {
        Table buttonsTable = new Table();
        buttonsTable.setName("buttons");
        buttonsTable.setFillParent(true);
        uiStage.addActor(buttonsTable);

        for (int i = 0; i < 2; i++) {
            Table enemyInfo = new Table();
            enemyInfo.setName("enemyInfo" + i);
            enemyInfo.setFillParent(true);
            uiStage.addActor(enemyInfo);

            Label name = new Label("", skin); name.setName("enemyName" + i);
            Label lvl = new Label("", skin); lvl.setName("enemyLevel" + i);
            Label hp = new Label("", skin); hp.setName("enemyHp" + i);

            enemyInfo.top(); enemyInfo.add(name).row(); enemyInfo.add(lvl); enemyInfo.add(hp);

            Table playerInfo = new Table();
            playerInfo.setName("playerInfo" + i);
            playerInfo.setFillParent(true);
            uiStage.addActor(playerInfo);

            Label pname = new Label("", skin); pname.setName("playerName" + i);
            Label plvl = new Label("", skin); plvl.setName("playerLevel" + i);
            Label php = new Label("", skin); php.setName("playerHp" + i);

            playerInfo.bottom(); playerInfo.add(pname).row(); playerInfo.add(plvl); playerInfo.add(php);
        }

        TextButton attack = new TextButton("Fight", skin);
        TextButton bag = new TextButton("Bag", skin);
        TextButton switchPokemon = new TextButton("Pokemon", skin);
        TextButton run = new TextButton("Run", skin);

        attack.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { attackUI(buttonsTable); }
        });
        bag.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { bagUI(buttonsTable); }
        });
        switchPokemon.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { switchPokemonUI(buttonsTable); }
        });
        run.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) { game.setScreen(gameScreen); }
        });

        buttonGroup.add(attack, bag, switchPokemon, run);

        switchMainUI(buttonsTable);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.8f, 0.8f, 0.8f, 1);
        updateUI();
        pokemonStage.act(delta);
        pokemonStage.draw();
        uiStage.act(delta);
        uiStage.draw();

        if (battleController.checkBattleEnd()) {
            game.setScreen(gameScreen);
        }
    }

    @Override public void resize(int w, int h) {
        uiStage.getViewport().update(w, h, true);
        pokemonStage.getViewport().update(w, h, true);
        setPositions();
    }

    @Override public void show() { Gdx.input.setInputProcessor(uiStage); }
    @Override public void dispose() { uiStage.dispose(); pokemonStage.dispose(); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
