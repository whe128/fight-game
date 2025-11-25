package engine.level;

import static utils.ANSIColors.ANSI_BLUE;
import static utils.ANSIColors.ANSI_RESET;

/**
 * Represents a wall grid in the game, which is a solid obstacle.
 */
public class WallGrid implements Grid {

    /**
     * Checks if the grid is solid.
     *
     * @return true since a WallGrid is solid.
     */
    @Override
    public boolean isSolid() {
        return true;
    }

    /**
     * Serializes the WallGrid to a string representation.
     *
     * @return a string representation of the WallGrid, which is "W".
     */
    @Override
    public String serialize() {
        return "W";
    }

    /**
     * Deserializes the WallGrid from a string representation.
     * This implementation does not change state from the provided data.
     *
     * @param data the string data to deserialize.
     */
    @Override
    public void deserialize(String data) {

    }

    /**
     * Displays the WallGrid on the console using a blue color.
     */
    @Override
    public void display() {
        // Unicode character for a solid block
        String WALL_CHAR = "#";
        System.out.print(ANSI_BLUE + WALL_CHAR + ANSI_RESET);
    }

    /**
     * Returns a string representation of the WallGrid.
     *
     * @return a string representing the WallGrid, which is "#".
     */
    @Override
    public String toString() {
        return "#";
    }
}
