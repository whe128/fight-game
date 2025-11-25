import engine.GameState;
import engine.entity.EntityPlayer;
import engine.level.GameMap;
import utils.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains unit tests for player movement in the game. It tests
 * various movement directions
 * (up, down, left, right) as well as invalid movement inputs. The tests
 * simulate player movement and
 * verify if the player's position updates correctly based on the input.
 */
public class PlayerMovementTest {
    private GameState gameState;

    static int locationX = 7;
    static int locationY = 2;

    /**
     * Sets up the game state and places the player at a known location before each
     * test.
     * The player is placed in an empty grid, and the surrounding grids are marked
     * as empty.
     */
    @BeforeEach
    public void setUp() {

        gameState = new GameState();
        gameState.initialize();
        // Reset the player and place it at a known location
        EntityPlayer player = new EntityPlayer(new Location(locationX, locationY));
        gameState.spawnEntity(player);
        gameState.findPlayer(); // Initialize the player entity in the game state

        // set entity 4 direction be the empty grid
        GameMap gameMap = gameState.getMap();
        gameMap.setGridEmpty(locationX - 1, locationY);
        gameMap.setGridEmpty(locationX + 1, locationY);
        gameMap.setGridEmpty(locationX, locationY - 1);
        gameMap.setGridEmpty(locationX, locationY + 1);
    }

    /**
     * Simulates the player movement based on the input and updates the player's
     * location in the game.
     *
     * @param input     The input representing the direction of movement ("W", "A",
     *                  "S", "D")
     * @param gameState The current game state
     */
    private static void moveProcess(String input, GameState gameState) {
        switch (input) {
            case "w", "W":
                System.out.println("Moving up");
                gameState.movePlayer(0, -1);
                break;
            case "s", "S":
                System.out.println("Moving down");
                gameState.movePlayer(0, 1);
                break;
            case "a", "A":
                System.out.println("Moving left");
                gameState.movePlayer(-1, 0);
                break;
            case "d", "D":
                System.out.println("Moving right");
                gameState.movePlayer(1, 0);
                break;
            default:
                // Ignore other inputs
        }
    }

    /**
     * Tests the player's movement upwards when the "W" key is pressed.
     * The player's Y-coordinate should decrease by 1.
     */
    @Test
    public void testPlayerMovementW() {
        // Simulate pressing 'A'
        moveProcess("W", gameState);
        locationY -= 1;
        // Verify the player's new location
        Location newLocation = gameState.getEntityPlayer().getLocation();
        assertEquals(locationX, newLocation.getLocationX());
        assertEquals(locationY, newLocation.getLocationY());
    }

    /**
     * Tests the player's movement to the left when the "A" key is pressed.
     * The player's X-coordinate should decrease by 1.
     */
    @Test
    public void testPlayerMovementA() {
        // Simulate pressing 'A'
        moveProcess("A", gameState);
        locationX -= 1;
        // Verify the player's new location
        EntityPlayer player = gameState.getEntityPlayer();
        Location newLocation = player.getLocation();
        assertEquals(locationX, newLocation.getLocationX());
        assertEquals(locationY, newLocation.getLocationY());
    }

    /**
     * Tests the player's movement downwards when the "S" key is pressed.
     * The player's Y-coordinate should increase by 1.
     */
    @Test
    public void testPlayerMovementS() {
        // Simulate pressing 'S'
        moveProcess("S", gameState);
        locationY += 1;
        // Verify the player's new location
        EntityPlayer player = gameState.getEntityPlayer();
        Location newLocation = player.getLocation();
        assertEquals(locationX, newLocation.getLocationX());
        assertEquals(locationY, newLocation.getLocationY());
    }

    /**
     * Tests the player's movement downwards when the "D" key is pressed.
     * The player's X-coordinate should increase by 1.
     */
    @Test
    public void testPlayerMovementD() {
        // Simulate pressing 'D'
        moveProcess("D", gameState);
        locationX += 1;
        // Verify the player's new location
        EntityPlayer player = gameState.getEntityPlayer();
        Location newLocation = player.getLocation();
        assertEquals(locationX, newLocation.getLocationX());
        assertEquals(locationY, newLocation.getLocationY());
    }

    /**
     * Tests invalid player input by simulating pressing an invalid key (e.g., "X").
     * The player's position should not change.
     */
    @Test
    public void testPlayerMovementInvalidInput() {
        // Store the initial location
        EntityPlayer player = gameState.getEntityPlayer();
        Location initialLocation = player.getLocation();

        // Simulate invalid key press
        moveProcess("X", gameState); // Invalid input should not change position

        // Verify the player's location hasn't changed
        Location newLocation = gameState.getEntityPlayer().getLocation();
        assertEquals(initialLocation.getLocationX(), newLocation.getLocationX());
        assertEquals(initialLocation.getLocationY(), newLocation.getLocationY());
    }
}
