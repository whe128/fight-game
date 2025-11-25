package engine.level;

import data.DataManager;
import data.ISerializable;
import engine.IDisplayable;
import engine.entity.Entity;
import engine.item.Item;
import utils.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import static utils.ANSIColors.ANSI_RESET;
import static utils.ANSIColors.ANSI_YELLOW;

/**
 * Represents the game map, which consists of a grid of cells.
 * The map can be generated randomly or loaded from a configuration.
 */
public class GameMap implements ISerializable, IDisplayable {

    // Width of the game map
    private static final int WIDTH = 35;
    // Height of the game map
    private static final int HEIGHT = 12;

    // 2D array representing the grid of the
    private final Grid[][] grids;
    // Location of the exit on the map
    private Location exitLocation;
    // Starting location of the playe
    private Location startLocation;

    /**
     * Constructs a GameMap object and generates the map if not loading from a file.
     */
    public GameMap() {
        this.grids = new Grid[WIDTH][HEIGHT];

        if (!DataManager.READ_CONFIG_FROM_FILE) {
            generate();
        }
    }

    /**
     * Generates the game map with walls and random paths.
     */
    public void generate() {
        startLocation = new Location();
        exitLocation = new Location();

        // Initialize the grid with walls
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                grids[x][y] = new WallGrid();
            }
        }

        int startX = (int) (Math.random() * WIDTH);
        int startY = (int) (Math.random() * HEIGHT);

        generateMaze(startX, startY);
    }

    /**
     * Recursively generates the maze using a randomized depth-first search
     * algorithm.
     *
     * @param x The x-coordinate of the current cell.
     * @param y The y-coordinate of the current cell.
     */
    private void generateMaze(int x, int y) {
        // Set start location if it hasn't been set yet
        if (!startLocation.validLocation()) {
            startLocation.setLocationX(x);
            startLocation.setLocationY(y);
        }

        // Mark the current cell as em
        grids[x][y] = new EmptyGrid();

        // Possible directions to move (up, right, down, left)
        int[][] directions = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };

        // Shuffle the directions to randomize the path generation
        for (int i = 0; i < directions.length; i++) {
            int j = (int) (Math.random() * directions.length);
            int[] temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }

        // Explore each direction
        for (int[] dir : directions) {
            int newX = x + dir[0] * 2;
            int newY = y + dir[1] * 2;
            // Check if the new position is within bounds and is a wal
            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT && grids[newX][newY] instanceof WallGrid) {
                grids[x + dir[0]][y + dir[1]] = new EmptyGrid();
                generateMaze(newX, newY);
            }
        }

        // Set exit location if it hasn't been set yet
        if (!exitLocation.validLocation()) {
            exitLocation.setLocationX(x);
            exitLocation.setLocationY(y);
            grids[x][y] = new ExitGrid();
        }
    }

    /**
     * Places the specified entities on the map.
     *
     * @param entities The list of entities to place.
     */
    public void putOnEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            Location location = entity.getLocation();
            int X = location.getLocationX();
            int Y = location.getLocationY();

            grids[X][Y] = new EntityGrid(entity);
        }
    }

    /**
     * Places the specified items on the map.
     *
     * @param items The list of items to place.
     */
    public void putOnItems(List<Item> items) {
        for (Item item : items) {
            Location location = item.getLocation();
            int X = location.getLocationX();
            int Y = location.getLocationY();

            grids[X][Y] = new ItemGrid(item);
        }
    }

    /**
     * Checks if the specified coordinates are within the bounds of the map.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are within bounds; false otherwise.
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    /**
     * Checks if the target position is valid for movement.
     *
     * @param x The x-coordinate of the target position.
     * @param y The y-coordinate of the target position.
     * @return true if the position can be moved to; false otherwise.
     */
    public boolean isTargetPositionCanMove(int x, int y) {
        if (!isInBounds(x, y)) {
            return false;
        }
        return !(grids[x][y] instanceof WallGrid);
    }

    /**
     * Checks if the specified coordinates contain the exit.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are the exit; false otherwise.
     */
    public boolean isExit(int x, int y) {
        return grids[x][y] instanceof ExitGrid;
    }

    /**
     * Checks if the specified coordinates contain an enemy entity.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates contain an enemy; false otherwise.
     */
    public boolean isEnemy(int x, int y) {
        return grids[x][y] instanceof EntityGrid;
    }

    /**
     * Checks if the specified coordinates contain a bonus item.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates contain a bonus; false otherwise.
     */
    public boolean isBonus(int x, int y) {
        return grids[x][y] instanceof ItemGrid;
    }

    /**
     * Retrieves the entity at the specified coordinates.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return The entity at the specified coordinates, or null if there is none.
     */
    public Entity getEntity(int x, int y) {
        if (grids[x][y] instanceof EntityGrid) {
            return ((EntityGrid) grids[x][y]).getEntity();
        }
        return null;
    }

    /**
     * Retrieves the bonus item at the specified coordinates.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return The bonus item at the specified coordinates, or null if there is
     *         none.
     */
    public Item getBonus(int x, int y) {
        if (grids[x][y] instanceof ItemGrid) {
            return ((ItemGrid) grids[x][y]).getItem();
        }
        return null;
    }

    /**
     * Exchanges the grids at two specified coordinates.
     *
     * @param firstX  The x-coordinate of the first grid.
     * @param firstY  The y-coordinate of the first grid.
     * @param secondX The x-coordinate of the second grid.
     * @param secondY The y-coordinate of the second grid.
     */
    public void exchangeGrid(int firstX, int firstY, int secondX, int secondY) {
        if (!isInBounds(firstX, firstY) || !isInBounds(secondX, secondY)) {
            return;
        }

        Grid temp = grids[firstX][firstY];
        grids[firstX][firstY] = grids[secondX][secondY];
        grids[secondX][secondY] = temp;
    }

    /**
     * Sets the specified grid to an empty grid.
     *
     * @param X The x-coordinate of the grid to clear.
     * @param Y The y-coordinate of the grid to clear.
     */
    public void setGridEmpty(int X, int Y) {
        if (!isInBounds(X, Y)) {
            return;
        }
        grids[X][Y] = new EmptyGrid();
    }

    /**
     * Sets the specified grid.
     *
     * @param X    The x-coordinate of the grid to clear.
     * @param Y    The y-coordinate of the grid to clear.
     * @param grid the set grid
     */
    public void setGrid(int X, int Y, Grid grid) {
        if (!isInBounds(X, Y)) {
            return;
        }
        if (grid == null) {
            return;
        }
        grids[X][Y] = grid;
    }

    /**
     * Serializes the game grid into a JSON string format.
     *
     * @return A JSON string representation of the game grid, including the start
     *         and exit locations.
     */
    @Override
    public String serialize() {
        JsonObject jsonObject = new JsonObject();
        JsonArray gridsArray = new JsonArray();

        jsonObject.addProperty("start", this.startLocation.serialize());
        jsonObject.addProperty("exit", this.exitLocation.serialize());

        for (int x = 0; x < WIDTH; x++) {
            JsonArray column = new JsonArray();
            for (int y = 0; y < HEIGHT; y++) {
                JsonObject gridObject = new JsonObject();

                // entity and item will not save in the map--data from other source
                if (grids[x][y] instanceof EntityGrid || grids[x][y] instanceof ItemGrid
                        || grids[x][y] instanceof ExitGrid) {
                    gridObject.addProperty("type", EmptyGrid.class.getSimpleName());
                } else {
                    gridObject.addProperty("type", grids[x][y].getClass().getSimpleName());
                }

                column.add(gridObject);
            }
            gridsArray.add(column);
        }

        jsonObject.add("grids", gridsArray);
        return DataManager.GSON.toJson(jsonObject);
    }

    /**
     * Deserializes the given JSON string into the game grid, populating grids and
     * locations.
     *
     * @param data The JSON string representation of the game grid to be
     *             deserialized.
     * @throws IllegalArgumentException if an unknown grid type is encountered
     *                                  during deserialization.
     */
    @Override
    public void deserialize(String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();

        JsonArray gridsArray = jsonObject.getAsJsonArray("grids");

        for (int x = 0; x < WIDTH; x++) {
            JsonArray column = gridsArray.get(x).getAsJsonArray();
            for (int y = 0; y < HEIGHT; y++) {
                JsonObject gridObject = column.get(y).getAsJsonObject();
                String gridType = gridObject.get("type").getAsString();

                switch (gridType) {
                    case "EmptyGrid":
                        grids[x][y] = new EmptyGrid();
                        break;
                    case "WallGrid":
                        grids[x][y] = new WallGrid();
                        break;

                    // first for empty grid
                    case "OutGrid":
                    case "EntityGrid":
                    case "ItemGrid":
                        grids[x][y] = new EmptyGrid();
                        continue;

                    // other grid will use entity or item to put on the map
                    default:
                        throw new IllegalArgumentException("Unknown grid type: " + gridType);
                }
            }
        }

        // start Location will not show, just put the player on it
        this.startLocation = new Location();
        this.startLocation.deserialize(jsonObject.get("start").getAsString());

        this.exitLocation = new Location();
        this.exitLocation.deserialize(jsonObject.get("exit").getAsString());
        grids[exitLocation.getLocationX()][exitLocation.getLocationY()] = new ExitGrid();
    }

    /**
     * Displays the game instructions and the current state of the grid.
     */
    @Override
    public void display() {

        // Unicode box drawing characters
        final String TOP_LEFT = "+";
        final String TOP_RIGHT = "+";
        final String BOTTOM_LEFT = "+";
        final String BOTTOM_RIGHT = "+";
        final String HORIZONTAL = "-";
        final String VERTICAL = "|";

        // Top border
        System.out.print(ANSI_YELLOW + TOP_LEFT);
        for (int x = 0; x < WIDTH; x++) {
            System.out.print(HORIZONTAL);
        }
        System.out.println(TOP_RIGHT + ANSI_RESET);

        // Map content with side borders
        for (int y = 0; y < HEIGHT; y++) {
            System.out.print(ANSI_YELLOW + VERTICAL + ANSI_RESET);
            for (int x = 0; x < WIDTH; x++) {
                if (grids[x][y] != null) {
                    grids[x][y].display();
                } else {
                    System.out.print(" "); // Print space for null grids
                }
            }
            System.out.println(ANSI_YELLOW + VERTICAL + ANSI_RESET);
        }

        // Bottom border
        System.out.print(ANSI_YELLOW + BOTTOM_LEFT);
        for (int x = 0; x < WIDTH; x++) {
            System.out.print(HORIZONTAL);
        }
        System.out.println(BOTTOM_RIGHT + ANSI_RESET);
    }

    /**
     * Returns a string representation of the game grid, including start and exit
     * locations.
     *
     * @return A string representation of the game grid.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("start:").append(startLocation).append("\n");
        stringBuilder.append("exit:").append(exitLocation).append("\n");
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (grids[x][y] != null) {
                    stringBuilder.append(grids[x][y]);
                } else {
                    stringBuilder.append(" ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
