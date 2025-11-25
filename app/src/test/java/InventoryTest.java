import engine.item.Inventory;
import engine.item.ItemRecover;
import engine.item.ItemWeapon;
import utils.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Inventory class in the game engine.
 * It validates the functionality of the Inventory class including
 * adding, removing weapons and recovers, and serialization/deserialization.
 */
public class InventoryTest {
    private Inventory inventory;

    /**
     * Set up a new Inventory instance before each test case.
     * for all test run, first create the instance of the inventory
     */
    @BeforeEach
    void setUp() {
        inventory = new Inventory();
    }

    /**
     * Test the addition of a weapon to the inventory.
     * Ensures the weapon is added successfully and can be retrieved.
     */
    @Test
    void testAddWeapon() {
        ItemWeapon weapon = new ItemWeapon(new Location(), 10);
        Assertions.assertTrue(inventory.addWeapon(weapon));
        Assertions.assertEquals(weapon, inventory.getWeapon(1));
    }

    /**
     * Test adding weapons beyond the maximum limit.
     * Ensures the method returns false when the inventory is full.
     */
    @Test
    void testAddWeaponExceedLimit() {
        for (int i = 0; i < Inventory.maxWeaponNum; i++) {
            inventory.addWeapon(new ItemWeapon(new Location(), i + 1));
        }
        // Should return false
        Assertions.assertFalse(inventory.addWeapon(new ItemWeapon(new Location(), 10)));
    }

    /**
     * Test removing a weapon from the inventory.
     * Ensures the weapon is removed and the slot is null after removal.
     */
    @Test
    void testRemoveWeapon() {
        ItemWeapon weapon = new ItemWeapon(new Location(), 10);
        inventory.addWeapon(weapon);
        inventory.removeWeapon(1);
        Assertions.assertNull(inventory.getWeapon(1));
    }

    /**
     * Test the addition of a recover item to the inventory.
     * Ensures the recover item is added successfully and can be retrieved.
     */
    @Test
    void testAddRecover() {
        ItemRecover recover = new ItemRecover(new Location(), 15);
        Assertions.assertTrue(inventory.addRecover(recover));
        Assertions.assertNotNull(inventory.removeRecover(1));
    }

    /**
     * Test adding recover items beyond the maximum limit.
     * Ensures the method returns false when the inventory is full.
     */
    @Test
    void testAddRecoverExceedLimit() {
        for (int i = 0; i < Inventory.maxRecoverNum; i++) {
            inventory.addRecover(new ItemRecover(new Location(), i + 1));
        }
        // Should return false, because the inventory is full
        Assertions.assertFalse(inventory.addRecover(new ItemRecover(new Location(), 10)));
    }

    /**
     * Test removing a recover item from the inventory.
     * Ensures the recover item is removed and the slot is null after removal.
     */
    @Test
    void testRemoveRecover() {
        ItemRecover recover = new ItemRecover(new Location(), 15);
        inventory.addRecover(recover);
        inventory.removeRecover(1);
        Assertions.assertNull(inventory.removeRecover(1));
    }

    /**
     * Test serialization and deserialization of the inventory.
     * Ensures the inventory state is correctly serialized and deserialized.
     */
    @Test
    void testSerializeAndDeserialize() {
        inventory.addWeapon(new ItemWeapon(new Location(), 20));
        inventory.addRecover(new ItemRecover(new Location(), 25));

        String serializedData = inventory.serialize();
        Inventory newInventory = new Inventory();
        // from the instance to json string, then comeback to the inventory instance
        newInventory.deserialize(serializedData);

        // inventory index from 1
        Assertions.assertEquals(20, newInventory.getWeapon(1).getAttributes());
        Assertions.assertEquals(25, newInventory.getRecover(1).getAttributes());
    }

}
