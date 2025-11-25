import engine.GameState;
import engine.GameStatus;
import engine.entity.EntityNPC;
import engine.entity.EntityPlayer;
import engine.item.ItemRecover;
import engine.item.ItemWeapon;
import engine.level.EntityGrid;
import engine.level.ExitGrid;
import engine.level.GameMap;
import engine.level.ItemGrid;
import utils.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains unit tests for the GameState in the game.
 */
public class GameStateTest {

    private GameState gameState;

    /**
     * Sets up the game state before each test.
     * Initializes the GameState instance and spawns the player at the origin (0,
     * 0).
     */
    @BeforeEach
    public void setUp() {
        gameState = new GameState();
        gameState.initialize();
        // Reset the player and place it at a known location
        EntityPlayer player = new EntityPlayer(new Location(0, 0));
        gameState.spawnEntity(player);
        gameState.findPlayer(); // Initialize the player entity in the game state
    }

    /**
     * Tests the player's movement to a valid location.
     * Moves the player to the coordinates (1, 0) and verifies the new location.
     */
    @Test
    public void testPlayerMovementValid() {

        // Move player to (1, 0)
        gameState.movePlayer(1, 0);

        // Verify the player's new location
        Location newLocation = gameState.getEntities().get(0).getLocation();
        assertEquals(1, newLocation.getLocationX());
        assertEquals(0, newLocation.getLocationY());
    }

    /**
     * Tests the player's movement to an invalid location.
     * Attempts to move the player out of bounds and verifies that the player's
     * location remains unchanged.
     */
    @Test
    public void testPlayerMovementInvalid() {
        // Store the initial location
        Location initialLocation = gameState.getEntities().get(0).getLocation();

        // Attempt to move player out of bounds (invalid move)
        gameState.movePlayer(-1, 0); // Assuming this is invalid

        // Verify the player's location hasn't changed
        Location newLocation = gameState.getEntities().get(0).getLocation();
        assertEquals(initialLocation.getLocationX(), newLocation.getLocationX());
        assertEquals(initialLocation.getLocationY(), newLocation.getLocationY());
    }

    /**
     * Tests the condition for winning the game.
     * Moves the player to an exit grid and verifies that the game status is set to
     * WIN.
     */
    @Test
    public void testWin() {
        assertEquals(GameStatus.READY_MOVE, gameState.getGameStatus(), "beginning should be Ready move");
        GameMap gameMap = gameState.getMap();
        gameMap.setGrid(1, 0, new ExitGrid());

        gameState.movePlayer(1, 0);

        assertEquals(GameStatus.WIN, gameState.getGameStatus(), "win when go to the exit");
        gameState.movePlayer(1, 0);
    }

    /**
     * Tests the condition for losing the game.
     * Sets up a scenario where the player encounters an NPC that they cannot
     * defeat, resulting in a loss.
     */
    @Test
    public void testLoss() {
        assertEquals(GameStatus.READY_MOVE, gameState.getGameStatus(), "beginning should be Ready move");
        GameMap gameMap = gameState.getMap();
        // only one life
        gameState.setLife(1);

        EntityNPC entityNPC = new EntityNPC(100, 100, new Location(1, 0));
        gameMap.setGrid(1, 0, new EntityGrid(entityNPC));

        // add a weapon that cannot kill the NPC, player will die
        gameState.getInventory().removeWeapon(1);
        gameState.getInventory().addWeapon(new ItemWeapon(new Location(), 10));

        // fight NPC
        gameState.movePlayer(1, 0);
        assertEquals(GameStatus.MEET_NPC, gameState.getGameStatus(), "should meet NPC");
        gameState.chooseToFightNPC();
        gameState.fightNPC(1);

        assertEquals(GameStatus.LOSS, gameState.getGameStatus(), "loss when player has no life");
        gameState.movePlayer(1, 0);
    }

    /**
     * Tests the acquisition of bonus items.
     * Moves the player to locations containing bonus items and verifies the game
     * status changes accordingly.
     */
    @Test
    public void testGetBonus() {
        assertEquals(GameStatus.READY_MOVE, gameState.getGameStatus(), "beginning should be Ready move");
        GameMap gameMap = gameState.getMap();

        ItemWeapon weapon = new ItemWeapon(new Location(1, 0), 10);
        gameMap.setGrid(1, 0, new ItemGrid(weapon));

        ItemRecover itemRecover = new ItemRecover(new Location(2, 0), 30);
        gameMap.setGrid(2, 0, new ItemGrid(itemRecover));

        // begin to move
        gameState.movePlayer(1, 0);
        assertEquals(GameStatus.FIND_BONUS, gameState.getGameStatus(), "find the bonus");

        gameState.dropBonus();

        // return to ready move
        assertEquals(GameStatus.READY_MOVE, gameState.getGameStatus(), "beginning should be Ready move");
        gameState.movePlayer(1, 0);
        assertEquals(GameStatus.FIND_BONUS, gameState.getGameStatus(), "find the bonus");
    }
}
