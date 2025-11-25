package engine.item;

import data.DataManager;
import utils.Location;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static utils.ANSIColors.ANSI_BLUE;
import static utils.ANSIColors.ANSI_RESET;

/**
 * Represents a weapon item in the game.
 */
public class ItemWeapon extends Item {

    int attack;

    /**
     * Constructs a new ItemWeapon with the specified location and attack value.
     *
     * @param location the location of the weapon
     * @param attack   the attack value of the weapon
     */
    public ItemWeapon(Location location, int attack) {
        super(location, ItemType.WEAPON);
        this.attack = attack;
    }

    /**
     * Gets the attack value of the weapon.
     *
     * @return the attack value
     */
    @Override
    public int getAttributes() {
        return attack;
    }

    /**
     * Serializes the weapon's data to a JSON string.
     *
     * @return a JSON representation of the weapon
     */
    @Override
    public String serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type.getName());
        jsonObject.addProperty("location", location.serialize());
        jsonObject.addProperty("attack", this.attack);
        return DataManager.GSON.toJson(jsonObject);
    }

    /**
     * Deserializes the weapon's data from a JSON string.
     *
     * @param data the JSON string containing the weapon data
     */
    @Override
    public void deserialize(String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        String typeName = jsonObject.get("type").getAsString();
        this.type = ItemType.fromName(typeName);
        this.location = new Location();
        this.location.deserialize(jsonObject.get("location").getAsString());
        this.attack = jsonObject.get("attack").getAsInt();
    }

    /**
     * Displays the weapon's information in a formatted output.
     */
    @Override
    public void display() {
        String out = "Bonus: Weapon" + "  attack:" + attack;
        System.out.println(ANSI_BLUE + out + ANSI_RESET);
    }
}
