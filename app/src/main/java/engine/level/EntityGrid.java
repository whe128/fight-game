package engine.level;

import data.DataManager;
import engine.entity.Entity;
import engine.entity.EntityType;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

import static utils.ANSIColors.ANSI_RESET;
import static utils.ANSIColors.ANSI_YELLOW;

/**
 * Represents a grid cell that contains an entity in the game.
 * The EntityGrid is solid and can hold entities like players and enemies.
 */
public class EntityGrid implements Grid {

    private final Entity entity;

    /**
     * Constructs an EntityGrid with the specified entity.
     *
     * @param entity The entity to be placed in this grid.
     */
    public EntityGrid(Entity entity) {
        this.entity = entity;
    }

    /**
     * Returns the entity contained in this grid.
     *
     * @return The entity present in this EntityGrid.
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Checks if the entity grid is solid.
     *
     * @return true, indicating that the entity grid is solid and cannot be passed
     *         through.
     */
    @Override
    public boolean isSolid() {
        return true;
    }

    /**
     * Serializes the entity grid to a JSON string representation.
     *
     * @return A JSON string representing the entity and its type.
     */
    @Override
    public String serialize() {
        Map<String, String> data = new HashMap<>();
        data.put("entity", entity.serialize());
        data.put("type", entity.getType().getName());
        return DataManager.GSON.toJson(data);
    }

    /**
     * Deserializes the given JSON string into the entity grid.
     *
     * @param data The JSON string representation of the entity grid.
     */
    @Override
    public void deserialize(String data) {
        Map<String, String> map = DataManager.GSON.fromJson(data, new TypeToken<Map<String, String>>() {
        }.getType());
        entity.deserialize(map.get("entity"));
    }

    /**
     * Displays the entity contained in the grid on the console.
     * The display color depends on the type of the entity.
     */
    @Override
    public void display() {

        if (entity.getType() == EntityType.ENEMY) {
            System.out.print(ANSI_YELLOW);
        }
        System.out.print(entity.getType().getDisplayName());
        // reset the color
        System.out.print(ANSI_RESET);
    }

    /**
     * Returns a string representation of the entity grid.
     * Different entity types are represented by specific characters.
     *
     * @return A character representing the type of the entity:
     *         "E" for enemies, "P" for players, and a space for other types.
     */
    @Override
    public String toString() {
        EntityType type = entity.getType();
        switch (type) {
            case ENEMY -> {
                return "E";
            }
            case PLAYER -> {
                return "P";
            }
            default -> {
                return " ";
            }
        }
    }
}
