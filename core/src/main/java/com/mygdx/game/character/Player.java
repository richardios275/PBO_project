package com.mygdx.game.character;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.game.Party;
import com.mygdx.game.item.Inventory;
import com.mygdx.game.item.Item;
import com.mygdx.game.item.UsableItem;
import com.mygdx.game.pokemon.Pokedex;
import com.mygdx.game.pokemon.Pokemon;

/**
 * Class managing the player's party, inventory, gold and sprite.
 */
public class Player extends Actor {
    private final Party party;
    private final Inventory inventory;
    private final Vector2 velocity;
    private final Pokedex pokedex;
    private final float speed;
    private final TextureAtlas textureAtlas;
    private int gold;
    private int collectedPokemons;

    /**
     * Constructor for the player.
     *
     * @param name    Name of the player
     * @param pokedex Pokedex of the player
     */
    public Player(String name, Pokedex pokedex) {
        super();
        this.pokedex = pokedex;
        this.textureAtlas = new TextureAtlas("Atlas/Trainer.atlas");
        this.inventory = new Inventory();
        this.party = new Party();
        this.velocity = new Vector2();
        this.speed = 250;
        this.gold = 0;
        this.collectedPokemons = 0;

        this.setPosition(415, 198);
        this.setWidth(16);
        this.setHeight(16);
        this.setName(name);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(this.textureAtlas.findRegion("player"), this.getX(), this.getY());
    }

    public int getMaxLvlOfParty() {
        return this.party.getMaxLevel();
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public void moveUp() {
        this.velocity.y += this.speed;
    }

    public void moveDown() {
        this.velocity.y -= this.speed;
    }

    public void moveLeft() {
        this.velocity.x -= this.speed;
    }

    public void moveRight() {
        this.velocity.x += this.speed;
    }

    public Item getItemFromInventory(String item) {
        return this.inventory.getItem(item);
    }

    public void update(float deltaTime) {
        this.setPosition(this.getX() + this.velocity.x * deltaTime, this.getY());
        this.velocity.x = 0;

        this.setPosition(this.getX(), this.getY() + this.velocity.y * deltaTime);
        this.velocity.y = 0;
    }

    // TODO party management, inventory system, trading system
    public void addPokemonToParty(Pokemon pokemon) {
        this.party.addPokemon(pokemon);
    }

    public void removePokemonFromParty(Pokemon pokemon) {
        this.party.removePokemon(pokemon);
    }

    public int getPartyStrength() {
        return this.party.getPowerPoints();
    }

    public void takeItem(Item item) {
        this.inventory.addItem(item);
    }

    public int getInvetorySize() {
        return this.inventory.getSize();
    }

    public void useItem(UsableItem item, Pokemon pokemon) {
        item.use(this.party.getPokemon(pokemon));
        this.inventory.removeItem(item);
    }

    /**
     * Collects the pokemon and adds it to the party.
     *
     * @param pokemon Pokemon to be collected
     */
    public void collectPokemon(Pokemon pokemon) {
        this.pokedex.getPokemon(pokemon.getName()).setCollected(true);
        this.addPokemonToParty(pokemon);
        this.collectedPokemons++;
    }

    /**
     * Returns the first pokemon in the party.
     *
     * @return First pokemon in the party
     */
    public Pokemon getFirstPokemon() {
        return this.party.getFirstPokemon();
    }

    public int getGold() {
        return this.gold;
    }

    public void gainGold(int gold) {
        this.gold += gold;
    }

    public Iterable<Pokemon> getParty() {
        return this.party;
    }

    public Iterable<Item> getInvetory() {
        return this.inventory;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player: ").append(this.getName())
                .append(System.lineSeparator())
                .append("Gold: ").append(this.gold)
                .append(" Collected Pokemons: ").append(this.collectedPokemons)
                .append(System.lineSeparator())
                .append("Party = [ ");
        for (Pokemon pokemon : this.party) {
            sb.append(pokemon.getName()).append(", ");
        }
        sb.append(" ] ").append(System.lineSeparator());
        sb.append("Inventory = [ ");
        for (Item item : this.inventory) {
            sb.append(item.getName()).append(", ");
        }
        sb.append(" ] ").append(System.lineSeparator());
        return sb.toString();
    }

    public int getPartySize() {
        return this.party.getSize();
    }
}
