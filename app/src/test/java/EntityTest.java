import engine.entity.Entity;
import engine.entity.EntityType;
import utils.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Entity class in the game engine.
 */
public class EntityTest {

    private Entity entityPlayer;
    private Entity entityNPC;

    /**
     * Set up the test environment before each test.
     * Initializes a player and an NPC entity.
     */
    @BeforeEach
    void setUp() {
        // Initialize an entity before each test
        entityPlayer = new Entity(100, 10, new Location(0, 0), EntityType.PLAYER);
        entityNPC = new Entity(50, 5, new Location(0, 0), EntityType.ENEMY);
    }

    /**
     * Test for correct initialization of Entity objects.
     * Ensures that the attributes (health, attack, type, location) are correctly
     * set during initialization.
     */
    @Test
    void testEntityInitialization() {
        // Test that entity initializes correctly
        Assertions.assertEquals(100, entityPlayer.getMaxHealth(), "Max health should be 100");
        Assertions.assertEquals(100, entityPlayer.getHealth(), "Initial health should be equal to max health");
        Assertions.assertEquals(10, entityPlayer.getAttack(), "Attack should be 10");
        Assertions.assertEquals(EntityType.PLAYER, entityPlayer.getType(), "Entity type should be PLAYER");
        Assertions.assertEquals(new Location(0, 0), entityPlayer.getLocation(), "Initial location should be (0, 0)");

        Assertions.assertEquals(50, entityNPC.getMaxHealth(), "Max health should be 100");
        Assertions.assertEquals(50, entityNPC.getHealth(), "Initial health should be equal to max health");
        Assertions.assertEquals(5, entityNPC.getAttack(), "Attack should be 10");
        Assertions.assertEquals(EntityType.ENEMY, entityNPC.getType(), "Entity type should be ENEMY");
        Assertions.assertEquals(new Location(0, 0), entityNPC.getLocation(), "Initial location should be (0, 0)");
    }

    /**
     * Test for damaging an entity.
     * Ensures that the health decreases when damage is applied.
     */
    @Test
    void testEntityDamage() {
        // Test damaging the entity
        entityPlayer.damage(20);
        Assertions.assertEquals(80, entityPlayer.getHealth(), "Health should decrease by 20 after damage");

        entityNPC.damage(10);
        Assertions.assertEquals(40, entityNPC.getHealth(), "Health should decrease by 10 after damage");
    }

    /**
     * Test for healing an entity.
     * Ensures that health is correctly restored after healing, but not beyond the
     * max health.
     */
    @Test
    void testEntityHeal() {
        // Test healing the entity
        entityPlayer.damage(50); // Damage first
        entityPlayer.heal(30);
        Assertions.assertEquals(80, entityPlayer.getHealth(), "Health should be 80 after healing 30");
    }

    /**
     * Test that healing cannot exceed the entity's maximum health.
     */
    @Test
    void testEntityOverHeal() {
        // Test healing above max health
        entityPlayer.damage(10); // Damage a bit
        entityPlayer.heal(20); // Heal more than required
        Assertions.assertEquals(100, entityPlayer.getHealth(), "Health should not exceed max health");
    }

    /**
     * Test for the death state of an entity.
     * Ensures that the entity's health does not go below zero and that it is marked
     * as dead when health reaches 0.
     */
    @Test
    void testEntityDeath() {
        // Test if the entity can die
        entityPlayer.damage(150); // More than max health
        Assertions.assertEquals(0, entityPlayer.getHealth(), "Health should not go below 0");
        Assertions.assertTrue(entityPlayer.isDied(), "Entity should be marked as dead");

        entityNPC.damage(150); // More than max health
        Assertions.assertEquals(0, entityNPC.getHealth(), "Health should not go below 0");
        Assertions.assertTrue(entityNPC.isDied(), "Entity should be marked as dead");

    }

    /**
     * Test for setting and recovering the pre-fight health.
     * Ensures that the health is restored to the pre-fight value after recovering.
     * for resurrect, enemy and player recover to pre Attack state
     */
    @Test
    void testSetPreFightHealthAndRecover() {
        // Test saving and recovering health (pre-fight health)
        entityPlayer.setPreFightHealth();
        entityNPC.setPreFightHealth();

        entityPlayer.damage(20); // Further damage after saving pre-fight health
        entityNPC.damage(10);
        entityPlayer.recover(); // Recover to pre-fight health
        entityNPC.recover();
        Assertions.assertEquals(100, entityPlayer.getHealth(), "Health should recover to pre-fight value");
        Assertions.assertEquals(100, entityPlayer.getHealth(), "Health should recover to pre-fight value");

    }

    /**
     * Test for the serialization of the entity.
     * Ensures that the entity is serialized into a valid JSON string with correct
     * attribute values.
     */
    @Test
    void testSerialization() {
        // Test if the entity is serialized correctly
        String serializedData = entityPlayer.serialize();
        System.out.println(serializedData);
        Assertions.assertTrue(serializedData.contains("\"type\": \"player\""),
                "Serialized data should contain entity type");
        Assertions.assertTrue(serializedData.contains("\"health\": 100"), "Serialized data should contain health");
        Assertions.assertTrue(serializedData.contains("\"attack\": 10"), "Serialized data should contain attack");

        // Test if the entity is serialized correctly
        serializedData = entityNPC.serialize();
        Assertions.assertTrue(serializedData.contains("\"type\": \"enemy\""),
                "Serialized data should contain entity type");
        Assertions.assertTrue(serializedData.contains("\"health\": 50"), "Serialized data should contain health");
        Assertions.assertTrue(serializedData.contains("\"attack\": 5"), "Serialized data should contain attack");
    }

    /**
     * Test for the deserialization of the entity.
     * Ensures that the entity is deserialized from a JSON string and that the
     * attributes match the deserialized values.
     */
    @Test
    void testDeserialization() {
        // Test if the entity is deserialized correctly
        String jsonData = "{\"type\": \"player\",\n " +
                "\"location\": \"{\\n  \\\"locationX\\\": 0,\\n  \\\"locationY\\\": 0\n}\",\n" +
                "\"health\": 90,\n" +
                "\"max_health\": 100,\n" +
                "\"attack\": 10\n" +
                "}";
        entityPlayer.deserialize(jsonData);
        Assertions.assertEquals(90, entityPlayer.getHealth(), "Deserialized health should be 90");
        Assertions.assertEquals(100, entityPlayer.getMaxHealth(), "Deserialized max health should be 100");
        Assertions.assertEquals(10, entityPlayer.getAttack(), "Deserialized attack should be 10");
        Assertions.assertEquals(EntityType.PLAYER, entityPlayer.getType(), "Deserialized entity type should be PLAYER");

        jsonData = "{\"type\": \"enemy\",\n " +
                "\"location\": \"{\\n  \\\"locationX\\\": 0,\\n  \\\"locationY\\\": 0\n}\",\n" +
                "\"health\": 45,\n" +
                "\"max_health\": 100,\n" +
                "\"attack\": 5\n" +
                "}";
        entityNPC.deserialize(jsonData);
        Assertions.assertEquals(45, entityNPC.getHealth(), "Deserialized health should be 90");
        Assertions.assertEquals(100, entityNPC.getMaxHealth(), "Deserialized max health should be 100");
        Assertions.assertEquals(5, entityNPC.getAttack(), "Deserialized attack should be 10");
        Assertions.assertEquals(EntityType.ENEMY, entityNPC.getType(), "Deserialized entity type should be ENEMY");
    }
}
