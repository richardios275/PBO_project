package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.character.Player;
import com.mygdx.game.pokemon.Pokemon;

import java.util.List;
import java.util.Map;

/**
 * Class that handles the inputs and checks for collisions within the game.
 */
public class GameController {
    private final Player player;
    private final GameScreen gameScreen;

    /**
     * Constructs a new GameController with the specified player and game screen.
     *
     * @param player     the player of the game
     * @param gameScreen the screen where the game is rendered
     */
    public GameController(Player player, GameScreen gameScreen) {
        this.player = player;
        this.gameScreen = gameScreen;
    }

    /**
     * Handles player's inputs. This method processes keyboard inputs for
     * moving the player and pausing/resuming the game. It also prints out
     * the player's current coordinates when 'X' is pressed and the game is in debug mode.
     */
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.player.moveUp();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.player.moveDown();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.player.moveRight();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X) && Constants.DEBUG) {
            System.out.println("X: " + this.player.getX() + ", Y: " + this.player.getY());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!this.gameScreen.isPaused()) {
                this.gameScreen.pauseGame();
            } else {
                this.gameScreen.resumeGame();
            }
        }
    }

    /**
     * Checks if there's a collision between the player and other game objects,
     * including collision objects, exits and other pokemons. The player's movement is
     * adjusted based on these collisions.
     *
     * @param delta            the time interval for the game update
     * @param collisionObjects the list of objects in the game map that can collide with the player
     * @param exits            the list of exits in the game map
     * @param pokemons         the list of pokemons in the game map
     */
    public void checkCollisions(float delta, List<RectangleMapObject> collisionObjects, List<RectangleMapObject> exits, Map<Pokemon, Rectangle> pokemons) {
        Rectangle futureX = new Rectangle(this.player.getX() + this.player.getVelocity().x * delta, this.player.getY(), this.player.getWidth(), this.player.getHeight());
        Rectangle futureY = new Rectangle(this.player.getX(), this.player.getY() + this.player.getVelocity().y * delta, this.player.getWidth(), this.player.getHeight());
        Rectangle current = new Rectangle(this.player.getX(), this.player.getY(), this.player.getWidth(), this.player.getHeight());

        for (RectangleMapObject collisionObject : collisionObjects) {
            if (futureX.overlaps(collisionObject.getRectangle())) {
                this.player.getVelocity().x = 0;
                return;
            }
            if (futureY.overlaps(collisionObject.getRectangle())) {
                this.player.getVelocity().y = 0;
                return;
            }
        }

        for (RectangleMapObject exit : exits) {
            if (futureX.overlaps(exit.getRectangle()) || futureY.overlaps(exit.getRectangle())) {
                String nextZone = exit.getName();
                this.gameScreen.switchZone(nextZone);
                return;
            }
        }

        for (Map.Entry<Pokemon, Rectangle> pokemon : pokemons.entrySet()) {
            if (current.overlaps(pokemon.getValue())) {
                this.gameScreen.startBattle(pokemon.getKey());
                return;
            }
        }
    }
}
