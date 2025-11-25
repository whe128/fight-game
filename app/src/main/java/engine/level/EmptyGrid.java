package engine.level;

/**
 * Represents an empty grid cell in the game.
 * The EmptyGrid is non-solid, indicating that it can be passed through.
 */
public class EmptyGrid implements Grid {

    /**
     * Checks if the empty grid is solid.
     *
     * @return false, indicating that the empty grid is not solid and can be passed
     *         through.
     */
    @Override
    public boolean isSolid() {
        return false;
    }

    /**
     * Serializes the empty grid to a string representation.s
     *
     * @return An empty string as the serialization for an empty grid.
     */
    @Override
    public String serialize() {
        return "";
    }

    /**
     * Deserializes the given data into the empty grid.
     * This method does not require any data for an empty grid.
     *
     * @param data The data string to deserialize (not used in this case).
     */
    @Override
    public void deserialize(String data) {

    }

    /**
     * Displays the empty grid on the console.
     * It prints a space character to represent an empty cell.
     */
    @Override
    public void display() {
        System.out.print(" ");
    }

    /**
     * Returns a string representation of the empty grid.
     *
     * @return A space character representing the empty grid.
     */
    @Override
    public String toString() {
        return " ";
    }
}
