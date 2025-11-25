package engine.item;

/**
 * Represents the types of items available in the game.
 */
public enum ItemType {
    RECOVER, WEAPON;

    /**
     * Converts a string name to its corresponding ItemType.
     *
     * @param name the name of the item type (case-insensitive)
     * @return the corresponding ItemType
     * @throws IllegalArgumentException if the name does not match any ItemType
     */
    public static ItemType fromName(String name) {
        return ItemType.valueOf(name.toUpperCase());
    }

    /**
     * Gets the name of the item type in lowercase.
     *
     * @return the lowercase name of the item type
     */
    public String getName() {
        return name().toLowerCase();
    }

    /**
     * Returns a string representation of the item type with the first letter
     * capitalized.
     *
     * @return a string representation of the item type
     */
    @Override
    public String toString() {
        return super.toString().substring(0, 1).toUpperCase();
    }
}
