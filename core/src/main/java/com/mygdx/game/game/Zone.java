package com.mygdx.game.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.character.Player;
import com.mygdx.game.pokemon.Pokedex;
import com.mygdx.game.pokemon.Pokemon;

import java.util.*;

/**
 * The Zone class represents a map or location in the game with various features like Pokemon spawn areas, collision objects, and exit points.
 */
public class Zone {
    private final TiledMap tiledMap;
    private final String mapName;
    private final ArrayList<RectangleMapObject> collisionObjects;
    private final ArrayList<RectangleMapObject> exitHitboxes;
    private final ArrayList<RectangleMapObject> pokemonSpawnAreas;
    private final ArrayList<Pokemon> pokemons;

    /**
     * Constructs a new Zone with the given parameters.
     *
     * @param mapName The name of the map
     * @param player  The player who is in this zone
     * @param pokedex The Pokedex containing information about Pokemon
     */
    public Zone(String mapName, Player player, Pokedex pokedex) {
        this.mapName = mapName;
        this.tiledMap = new TmxMapLoader().load("tmx/" + this.mapName + ".tmx");
        this.pokemons = new ArrayList<>();
        this.collisionObjects = new ArrayList<>();
        this.exitHitboxes = new ArrayList<>();
        this.pokemonSpawnAreas = new ArrayList<>();

        MapLayer collisionLayer = this.tiledMap.getLayers().get("Collision");
        for (MapObject object : collisionLayer.getObjects()) {
            this.collisionObjects.add((RectangleMapObject) object);
        }
//        this.collisionObjects = collisionLayer.getObjects().getByType(RectangleMapObject.class);

        MapLayer exits = this.tiledMap.getLayers().get("Exits");
        for (MapObject object : exits.getObjects()) {
            this.exitHitboxes.add((RectangleMapObject) object);
        }
//        this.exitHitboxes = exits.getObjects().getByType(RectangleMapObject.class);

        if (this.tiledMap.getLayers().get("Pokemons") != null) {
            MapLayer pokemonLayer = this.tiledMap.getLayers().get("Pokemons");
            for (MapObject object : pokemonLayer.getObjects()) {
                this.pokemonSpawnAreas.add((RectangleMapObject) object);
            }
//            this.pokemonSpawnAreas = pokemonLayer.getObjects().getByType(RectangleMapObject.class);
            this.spawnPokemons(player, pokedex);
        }
    }

    private void spawnPokemons(Player player, Pokedex pokedex) {
        Array<Vector2> spawnPoints = new Array<>();
        final float POKEMON_WIDTH = 32f;
        final float POKEMON_HEIGHT = 32f;

        for (RectangleMapObject area : this.pokemonSpawnAreas) {
            Rectangle rectangle = area.getRectangle();

            for (int i = 0; i < 10; i++) {
                float x = MathUtils.random(rectangle.getX(), rectangle.getX() + rectangle.getWidth());
                float y = MathUtils.random(rectangle.getY(), rectangle.getY() + rectangle.getHeight());

                Pokemon pokemon = this.randomizePokemonStats(player, pokedex);

                // Set a fixed size
                pokemon.setSize(POKEMON_WIDTH, POKEMON_HEIGHT);

                // Center the position based on fixed size
                x += POKEMON_WIDTH / 2;
                y += POKEMON_HEIGHT / 2;

                Vector2 spawnPoint = new Vector2(x, y);

                if (this.isFarEnough(spawnPoint, spawnPoints)) {
                    pokemon.setPosition(x, y);
                    spawnPoints.add(spawnPoint);
                    this.pokemons.add(pokemon);
                }
            }
        }
    }

    private boolean isFarEnough(Vector2 spawnPoint, Array<Vector2> spawnPoints) {
        for (Vector2 point : spawnPoints) {
            if (spawnPoint.dst(point) < 120) {
                return false;
            }
        }
        return true;
    }


    private Pokemon randomizePokemonStats(Player player, Pokedex pokedex) {
        int playerPartyPower = player.getPartyStrength();
        Pokemon pokemon = pokedex.getRandomPokemon();
        int[] pokemonStats = pokemon.getStats();


        // first stat is always level
        for (int i = 0; i < pokemonStats.length; i++) {
            if (i == 0) {
                pokemonStats[i] = MathUtils.random(player.getMaxLvlOfParty() - 2, player.getMaxLvlOfParty() + 2);
                continue;
            }
            pokemonStats[i] += MathUtils.random(playerPartyPower / 5 - 2, playerPartyPower / 5 + 2);
        }
        pokemon.setStats(pokemonStats);

        return pokemon;
    }

    /**
     * Retrieves the spawn areas for Pokemon in the zone.
     *
     * @return A list of RectangleMapObject representing the spawn areas
     */
    public List<RectangleMapObject> getPokemonSpawnAreas() {
        return new ArrayList<>(this.pokemonSpawnAreas);
    }

    /**
     * Retrieves the objects in the zone that the player can collide with.
     *
     * @return A list of RectangleMapObject representing the collision objects
     */
    public List<RectangleMapObject> getCollisionObjects() {
        return new ArrayList<>(this.collisionObjects);
    }

    /**
     * Retrieves the exit points in the zone.
     *
     * @return A copy of a list of RectangleMapObject representing the exit hitboxes
     */
    public List<RectangleMapObject> getExitHitboxes() {
        return new ArrayList<>(this.exitHitboxes);
    }

    /**
     * Retrieves the Pokemon present in the zone along with their hitboxes.
     *
     * @return A unmodifiable Map with Pokemon as keys and their corresponding hitboxes as values
     */
    public Map<Pokemon, Rectangle> getPokemons() {
        HashMap<Pokemon, Rectangle> pokemonHitbox = new HashMap<>();

        for (Pokemon pokemon : this.pokemons) {
            pokemonHitbox.put(pokemon, new Rectangle(pokemon.getX(), pokemon.getY(), pokemon.getWidth(), pokemon.getHeight()));
        }

        return Collections.unmodifiableMap(pokemonHitbox);
    }

    /**
     * Retrieves the name of the map for this zone.
     *
     * @return A string representing the map name
     */
    public String getMapName() {
        return this.mapName;
    }

    /**
     * Retrieves the TiledMap object associated with this zone.
     *
     * @return The TiledMap object
     */
    public TiledMap getTiledMap() {
        return this.tiledMap;
    }
}
