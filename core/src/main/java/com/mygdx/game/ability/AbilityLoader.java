package com.mygdx.game.ability;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.game.item.Effect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class that loads abilities from JSON file
 */
public class AbilityLoader {
    private final HashMap<String, ArrayList<Ability>> abilities;

    /**
     * Constructor which adds all abilities from a specific JSON file to the list of abilities
     */
    public AbilityLoader() {
        this.abilities = new HashMap<>();
        JsonReader json = new JsonReader();
        JsonValue entries = json.parse(Gdx.files.internal("abilities.json"));
        for (int i = 0; i < entries.size; i++) {
            JsonValue entry = entries.get(i);
            String name = entry.getString("name");
            String description = entry.getString("description");
            String type = entry.getString("type");
            int cooldown = entry.getInt("cooldown");
            int duration = entry.getInt("duration");
            int power = entry.getInt("power");
            int levelRequirement = entry.getInt("level");
            String effect = entry.getString("effect");

            Ability ability = new Ability(name, description, type, cooldown, duration, power, levelRequirement, this.parseEffect(effect));
            this.addAbility(type, ability);
        }

    }

    private Effect parseEffect(String string) {
        switch (string) {
            case "none": {
                return Effect.NONE;
            }
            case "heal": {
                return Effect.HEAL;
            }
            case "buff_attack": {
                return Effect.BUFF_ATTACK;
            }
            case "buff_defense": {
                return Effect.BUFF_DEFENSE;
            }
            case "buff_speed": {
                return Effect.BUFF_SPEED;
            }
            case "poison": {
                return Effect.POISON;
            }
            case "burn": {
                return Effect.BURN;
            }
            case "freeze": {
                return Effect.FREEZE;
            }
            case "shock": {
                return Effect.SHOCK;
            }
            case "wet": {
                return Effect.WET;
            }
            case "entangle": {
                return Effect.ENTANGLE;
            }
            default: {
                System.out.println("Unknown effect: " + string);
                return null;
            }
        }
    }

    private void addAbility(String type, Ability ability) {
        if (this.abilities.get(type) == null) {
            this.abilities.put(type, new ArrayList<>());
            this.abilities.get(type).add(ability);
        } else {
            this.abilities.get(type).add(ability);
        }
    }

    /**
     * Returns a copy of list of abilities of a specific type
     *
     * @param type Type of abilities
     * @return List of abilities
     */

    public List<Ability> getAbilities(String type) {
        ArrayList<Ability> copyAbilities = new ArrayList<>();

        if (this.abilities.get(type) != null) {
            for (Ability ability : this.abilities.get(type)) {
                copyAbilities.add(ability.copy());
            }
        } else if (this.abilities.get("normal") != null) {
            for (Ability ability : this.abilities.get("normal")) {
                copyAbilities.add(ability.copy());
            }
        }

        return copyAbilities;
    }
}
