package engine;

import data.DataManager;
import data.ISerializable;
import engine.entity.Entity;
import engine.entity.EntityNPC;
import engine.entity.EntityPlayer;
import engine.entity.EntityType;
import engine.item.*;
import engine.level.GameMap;
import utils.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static utils.ANSIColors.*;

/**
 * Represents the state of the game, including the player, NPCs, items, and the
 * game map.
 * Implements the {@link ISerializable} and {@link IDisplayable} interfaces for
 * serialization
 * and display functionality.
 */
public class GameState implements ISerializable, IDisplayable {

    /**
     * singleton instance
     */
    private static GameState INSTANCE;

    /**
     * get the singleton instance
     * if the instance is null, create a new instance
     *
     * @return the singleton instance
     */
    public static GameState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameState();
        }
        return INSTANCE;
    }

    // Fields
    private final List<Entity> entities = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final GameMap map = new GameMap();
    private final Inventory inventory = new Inventory();
    private int life;
    private EntityPlayer entityPlayer;
    private GameStatus gameStatus;
    private Item findBonus;
    private EntityNPC meetEntityNPC;

    /**
     * Initializes a new GameState instance.
     * Generates entities and items if configuration is not read from a file.
     */
    public GameState() {
        INSTANCE = this;
        if (!DataManager.READ_CONFIG_FROM_FILE) {
            generateEntity();
            generateItem();
            life = 2;
            initialize();
        }
    }

    /**
     * Initializes the game state by putting entities and items on the map,
     * finding the player, and setting the game status to ready to move.
     */
    public void initialize() {
        map.putOnEntities(entities);
        map.putOnItems(items);
        findPlayer();
        gameStatus = GameStatus.READY_MOVE;
    }

    /**
     * Returns the list of entities in the game.
     *
     * @return The list of entities.
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * Returns the player entity.
     *
     * @return The player entity.
     */
    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    /**
     * Spawns a new entity in the game.
     *
     * @param entity The entity to be spawned.
     */
    public void spawnEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Finds and sets the player entity from the list of entities.
     * If no player is found, prints an error message.
     */
    public void findPlayer() {
        for (Entity entity : entities) {
            if (entity.getType() == EntityType.PLAYER) {
                this.entityPlayer = (EntityPlayer) entity;
                return;
            }
        }
        System.out.println(ANSI_RED + "no player" + ANSI_RESET);
    }

    /**
     * Moves the player to a new position based on the specified deltas.
     * Checks for valid move conditions such as map boundaries, enemy encounters,
     * bonus items, and exit conditions.
     *
     * @param deltaX The change in the X coordinate.
     * @param deltaY The change in the Y coordinate.
     */
    public void movePlayer(int deltaX, int deltaY) {
        int currentX = entityPlayer.getLocation().getLocationX();
        int currentY = entityPlayer.getLocation().getLocationY();
        int nextX = currentX + deltaX;
        int nextY = currentY + deltaY;

        // out of range
        if (!map.isTargetPositionCanMove(nextX, nextY)) {
            System.out.println(ANSI_RED + "invalid move" + ANSI_RESET);
            return;
        }

        // enemy can not overlap
        if (map.isEnemy(nextX, nextY)) {
            gameStatus = GameStatus.MEET_NPC;
            meetEntityNPC = (EntityNPC) map.getEntity(nextX, nextY);

            System.out.println(ANSI_RED + "Meet NPC!");
            System.out.println(ANSI_RED + "NPC: I am very powerful, do you want to fight with me? " + ANSI_RESET);
            return;
        } else if (map.isExit(nextX, nextY)) {
            gameStatus = GameStatus.WIN;
            // then clear the bonus grid and let it become empty
            map.setGridEmpty(nextX, nextY);

            System.out.println(ANSI_GREEN + "Arrive the exit!" + ANSI_RESET);

        } else if (map.isBonus(nextX, nextY)) {
            // bonus can overlap
            gameStatus = GameStatus.FIND_BONUS;
            // first get the bonus
            findBonus = map.getBonus(nextX, nextY);
            // then clear the bonus grid and let it become empty
            map.setGridEmpty(nextX, nextY);
            System.out.println(ANSI_RED + "Find Bonus! -- " + findBonus + ANSI_RESET);
        }

        map.exchangeGrid(currentX, currentY, nextX, nextY);
        entityPlayer.getLocation().setLocationX(nextX);
        entityPlayer.getLocation().setLocationY(nextY);
    }

    /**
     * Fetches the bonus item found at the player's current location and adds it to
     * the inventory.
     * Updates the game status accordingly.
     */
    public void fetchBonus() {
        if (findBonus == null) {
            return;
        }
        boolean isAddSuccessful = false;

        if (findBonus instanceof ItemWeapon) {
            isAddSuccessful = inventory.addWeapon((ItemWeapon) findBonus);
        } else if (findBonus instanceof ItemRecover) {
            isAddSuccessful = inventory.addRecover((ItemRecover) findBonus);
        }

        if (isAddSuccessful) {
            gameStatus = GameStatus.READY_MOVE;
        }
    }

    /**
     * Fetches the bonus item found at the player's current location and adds it to
     * the inventory.
     * Updates the game status accordingly.
     */
    public void dropBonus() {
        gameStatus = GameStatus.READY_MOVE;
    }

    /**
     * Initiates the fight with the NPC, setting the pre-fight health for both the
     * player and the NPC.
     */
    public void chooseToFightNPC() {
        entityPlayer.setPreFightHealth();
        meetEntityNPC.setPreFightHealth();
        gameStatus = GameStatus.FIGHTING;
    }

    /**
     * Chooses to run away from the NPC encounter.
     * Resets the game status to ready to move.
     */
    public void chooseToRunAwayNPC() {
        gameStatus = GameStatus.READY_MOVE;
    }

    /**
     * Handles the fighting logic between the player and the NPC.
     * Calculates damage based on the weapon used and checks for death conditions.
     *
     * @param weaponIndex The index of the weapon in the inventory used for the
     *                    fight.
     */
    public void fightNPC(int weaponIndex) {
        ItemWeapon itemWeapon = inventory.getWeapon(weaponIndex);

        if (itemWeapon == null) {
            return;
        }

        meetEntityNPC.damage(itemWeapon.getAttributes());

        if (meetEntityNPC.isDied()) {
            System.out.println(ANSI_RED + "NPC: I can't believe you can beat me!" + ANSI_RESET);
            Location location = meetEntityNPC.getLocation();
            // clear the NPC grid
            map.setGridEmpty(location.getLocationX(), location.getLocationY());

            gameStatus = GameStatus.READY_MOVE;
            return;
        }

        // first we fight NPC first, if NPC died we will not get the attack
        entityPlayer.damage(meetEntityNPC.getAttack());
        if (entityPlayer.isDied()) {
            System.out.println(ANSI_RED + "NPC: You can't beat me, you are a looser!" + ANSI_RESET);
            life--;
            if (life == 0) {
                System.out.println(ANSI_RED + "You have no life to resurrect, you lose the game!" + ANSI_RESET);
                gameStatus = GameStatus.LOSS;
            } else {
                System.out.println(ANSI_BLUE + "You resurrected!");
                meetEntityNPC.recover();
                entityPlayer.recover();
                gameStatus = GameStatus.READY_MOVE;
            }
        }
    }

    /**
     * Removes an item from the inventory based on its type and index.
     *
     * @param type  The type of item to be removed (weapon or recover).
     * @param index The index of the item to be removed.
     */
    public void removeInventory(ItemType type, int index) {
        switch (type) {
            case WEAPON -> inventory.removeWeapon(index);
            case RECOVER -> inventory.removeRecover(index);
        }
    }

    /**
     * set the life of the player
     *
     * @param life input the life of the player
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Heals the player using a recover item from the inventory.
     *
     * @param index The index of the recover item in the inventory.
     */
    public void userRecover(int index) {
        ItemRecover itemRecover = inventory.removeRecover(index);
        if (itemRecover != null) {
            entityPlayer.heal(itemRecover.getAttributes());
        }
    }

    /**
     * Returns the current game status.
     *
     * @return The current game status.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Returns the game map.
     *
     * @return The game map.
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Returns the inventory.
     *
     * @return The inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Serializes the current game state into a format suitable for saving.
     *
     * @return The serialized game state.
     */
    @Override
    public String serialize() {
        JsonObject jsonObject = new JsonObject();
        // serialize life
        jsonObject.addProperty("life", 10);

        // serialize entities
        JsonArray entitiesArray = new JsonArray();
        for (Entity entity : this.entities) {
            entitiesArray.add(JsonParser.parseString(entity.serialize()));
        }
        jsonObject.add("entities", entitiesArray);

        JsonArray itemsArray = new JsonArray();
        for (Item item : this.items) {
            itemsArray.add(JsonParser.parseString(item.serialize()));
        }
        jsonObject.add("items", itemsArray);

        // serialize inventory
        jsonObject.add("inventory", JsonParser.parseString(inventory.serialize()));

        // serialize map
        jsonObject.add("map", JsonParser.parseString(map.serialize()));

        return jsonObject.toString();
    }

    /**
     * Deserializes the game state from the given JSON data and updates the current
     * state.
     *
     * @param data The JSON string representing the saved game state.
     */
    @Override
    public void deserialize(String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
        // deserialize life
        life = jsonObject.get("life").getAsInt();

        // deserialize entities
        JsonArray entitiesArray = jsonObject.getAsJsonArray("entities");
        for (JsonElement entityElement : entitiesArray) {
            JsonObject entityObject = entityElement.getAsJsonObject();
            if (entityObject.get("type").getAsString().equals(EntityType.PLAYER.getName())) {
                EntityPlayer entityPlayer = new EntityPlayer(new Location());
                entityPlayer.deserialize(entityElement.toString());
                entities.add(entityPlayer);
            } else if (entityObject.get("type").getAsString().equals(EntityType.ENEMY.getName())) {
                EntityNPC enemy = new EntityNPC(0, 0, new Location());
                enemy.deserialize(entityElement.toString());
                entities.add(enemy);
            } else {
                Entity entity = new Entity(0, 0, new Location(), EntityType.NONE);
                entity.deserialize(entityElement.toString());
                entities.add(entity);
            }
        }

        // deserialize items
        JsonArray itemsArray = jsonObject.getAsJsonArray("items");
        for (JsonElement itemElement : itemsArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            if (itemObject.get("type").getAsString().equals(ItemType.WEAPON.getName())) {
                ItemWeapon itemWeapon = new ItemWeapon(new Location(), 0);
                itemWeapon.deserialize(itemElement.toString());
                items.add(itemWeapon);
            } else if (itemObject.get("type").getAsString().equals(ItemType.RECOVER.getName())) {
                ItemRecover itemRecover = new ItemRecover(new Location(), 0);
                itemRecover.deserialize(itemElement.toString());
                items.add(itemRecover);
            }
        }

        // deserialize map
        map.deserialize(jsonObject.get("map").toString());

        // deserialize inventory
        inventory.deserialize(jsonObject.get("inventory").toString());
    }

    /**
     * Displays the current game state to the user, including the map, entities,
     * player life, and inventory.
     */
    @Override
    public void display() {
        if (gameStatus == GameStatus.READY_MOVE || gameStatus == GameStatus.WIN) {
            this.map.display();
        }
        if (gameStatus == GameStatus.FIGHTING) {
            meetEntityNPC.display();
        }

        if (gameStatus == GameStatus.FIND_BONUS) {
            findBonus.display();
        }

        // debug
        // print current work directory
        // System.out.println(ANSI_BLUE + "Current work directory: " +
        // System.getProperty("user.dir") + ANSI_RESET);

        entityPlayer.display();
        System.out.println(ANSI_BLUE + "   life: " + life + ANSI_RESET);

        this.inventory.display();
    }

    /**
     * Generates entities and adds them to the game state.
     */
    void generateEntity() {
        entities.add(new EntityNPC(0, 0, new Location(3, 1)));
        entities.add(new EntityNPC(0, 0, new Location(8, 2)));
        entities.add(new EntityPlayer(new Location(20, 6)));
    }

    /**
     * Generates items and adds them to the game state.
     */
    void generateItem() {
        items.add(new ItemWeapon(new Location(10, 10), 10));
        items.add(new ItemRecover(new Location(15, 4), 220));
    }

    /**
     * Returns a string representation of the game state, including life, map,
     * entities, items, and inventory.
     *
     * @return A string representation of the game state.
     */
    @Override
    public String toString() {
        return "GameState{" +
                "\nlife: " + life +
                "\nmap:\n" + map +
                "\nentities:" + entities +
                "\nitems:" + items +
                "\ninventory:" + inventory;
    }
}
