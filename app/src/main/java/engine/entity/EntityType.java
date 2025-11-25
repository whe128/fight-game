package engine.entity;

/**
 * Enum representing different types of entities in the game.
 * The possible entity types are PLAYER, ENEMY, and NONE.
 */
public enum EntityType {
    PLAYER,
    ENEMY,
    NONE;

    /**
     * Gets the name of the entity type in lowercase.
     *
     * @return the lowercase name of the entity type
     */
    public String getName() {
        return name().toLowerCase();
    }

    /**
     * Gets the display name of the entity type.
     * The display name is the first letter of the entity type's name in uppercase.
     *
     * @return the display name of the entity type
     */
    public String getDisplayName() {
        return name().substring(0, 1).toUpperCase();
    }

    /**
     * Converts a string name to the corresponding EntityType.
     *
     * @param name the name of the entity type
     * @return the EntityType corresponding to the provided name
     * @throws IllegalArgumentException if the name does not correspond to any
     *                                  EntityType
     */
    public static EntityType fromName(String name) {
        return EntityType.valueOf(name.toUpperCase());
    }
}
