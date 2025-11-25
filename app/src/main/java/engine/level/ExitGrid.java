package engine.level;

import static utils.ANSIColors.ANSI_RED;
import static utils.ANSIColors.ANSI_RESET;

/**
 * Represents an exit grid in the game.
 * The exit grid is a solid grid that indicates the exit point in the game.
 */
public class ExitGrid implements Grid {
    /**
     * Checks if the exit grid is solid.
     *
     * @return true, indicating that the exit grid is solid and cannot be passed
     *         through.
     */
    @Override
    public boolean isSolid() {
        return true;
    }

    /**
     * Serializes the exit grid into a string representation.
     *
     * @return A string representation of the exit grid, which is "@".
     */
    @Override
    public String serialize() {
        return "@";
    }

    /**
     * Deserializes the given data into the exit grid.
     * This method is currently empty, as there is no data to deserialize for the
     * exit grid.
     *
     * @param data The string representation of the exit grid (not used).
     */
    @Override
    public void deserialize(String data) {

    }

    /**
     * Displays the exit grid on the console.
     * The exit grid is displayed using a red "@" character.
     */
    @Override
    public void display() {
        String WALL_CHAR = "@";
        System.out.print(ANSI_RED + WALL_CHAR + ANSI_RESET);
    }

    /**
     * Returns a string representation of the exit grid.
     *
     * @return A string representation of the exit grid, which is "@".
     */
    @Override
    public String toString() {
        return "@";
    }
}
