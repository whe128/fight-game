package engine.entity;

import data.DataManager;
import data.ISerializable;
import utils.Location;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Entity class is the base class for all entities in the game.
 * It provides properties and methods to manage an entity's state,
 * such as health, attack, and location.
 */
public class Entity implements ISerializable {

    /** The current health of the entity. */
    private int health;

    /** The maximum health of the entity. */
    private int maxHealth;

    /** The attack power of the entity. */
    private int attack;

    /** The current location of the entity in the game world. */
    private Location location;

    /** The type of the entity. */
    private EntityType type;

    /** The health of the entity before a fight. */
    private int preFightHealth;

    /**
     * Constructs a new Entity with the specified maximum health, attack, location,
     * and type.
     *
     * @param maxHealth the maximum health of the entity
     * @param attack    the attack power of the entity
     * @param location  the initial location of the entity
     * @param type      the type of the entity
     */
    public Entity(int maxHealth, int attack, Location location, EntityType type) {
        this.maxHealth = maxHealth;
        this.health = this.maxHealth;
        this.attack = attack;
        this.location = location;
        this.type = type;
    }

    /**
     * Sets the pre-fight health of the entity.
     */
    public void setPreFightHealth() {
        this.preFightHealth = health;
    }

    /**
     * Recovers the entity's health to its pre-fight health.
     */
    public void recover() {
        health = preFightHealth;
    }

    /**
     * Gets the current health of the entity.
     *
     * @return the current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets the maximum health of the entity.
     *
     * @return the maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Sets the current health of the entity.
     *
     * @param health the new health value
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Gets the attack power of the entity.
     *
     * @return the attack power
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Sets the attack power of the entity.
     *
     * @param attack the new attack power
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Damages the entity by the specified amount.
     *
     * @param damage the amount of damage to inflict
     */
    public void damage(int damage) {
        this.health = Math.max(0, this.health - damage);
    }

    /**
     * Heals the entity by the specified amount.
     *
     * @param heal the amount of health to recover
     */
    public void heal(int heal) {
        this.health = Math.min(this.maxHealth, this.health + heal);
    }

    /**
     * Gets the current location of the entity.
     *
     * @return the current location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Checks if the entity is dead (health is zero or below).
     *
     * @return true if the entity is dead, false otherwise
     */
    public boolean isDied() {
        return health <= 0;
    }

    /**
     * Gets the type of the entity.
     *
     * @return the entity type
     */
    public EntityType getType() {
        return type;
    }

    /**
     * Serializes the entity to a JSON string.
     *
     * @return the JSON representation of the entity
     */
    @Override
    public String serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", this.type.getName());
        jsonObject.addProperty("location", this.getLocation().serialize());
        jsonObject.addProperty("health", this.getHealth());
        jsonObject.addProperty("max_health", this.maxHealth);
        jsonObject.addProperty("attack", this.attack);
        return DataManager.GSON.toJson(jsonObject);
    }

    /**
     * Deserializes the entity from a JSON string.
     *
     * @param data the JSON string representing the entity
     */
    @Override
    public void deserialize(String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        String typeName = jsonObject.get("type").getAsString();
        this.type = EntityType.fromName(typeName);
        this.location = new Location();
        this.location.deserialize(jsonObject.get("location").getAsString());
        this.health = jsonObject.get("health").getAsInt();
        this.maxHealth = jsonObject.get("max_health").getAsInt();
        this.attack = jsonObject.get("attack").getAsInt();
    }

    /**
     * Returns a string representation of the entity, including its type, health,
     * and attack.
     *
     * @return a string representation of the entity
     */
    @Override
    public String toString() {
        return type + "  health:" + health + "(" + maxHealth + ") A:" + attack;
    }
}
