package engine.entity;

import engine.IDisplayable;
import utils.Location;

import static utils.ANSIColors.ANSI_BLUE;
import static utils.ANSIColors.ANSI_RESET;

/**
 * EntityPlayer class represents the player entity in the game.
 * It extends the Entity class and implements the IDisplayable interface.
 */
public class EntityPlayer extends Entity implements IDisplayable {

    // Maximum health for the player
    private static final int MAX_HEALTH = 100;

    /**
     * Constructs a new EntityPlayer at the specified location with the maximum
     * health.
     *
     * @param location the initial location of the player
     */
    public EntityPlayer(Location location) {
        super(MAX_HEALTH, 0, location, EntityType.PLAYER);
    }

    /**
     * Displays the player's current health and maximum health in a formatted way.
     */
    @Override
    public void display() {
        int health = getHealth();
        String healthString;
        if (health < 10) {
            healthString = "  " + health;
        } else if (health < 100) {
            healthString = " " + health;
        } else {
            healthString = String.valueOf(health);
        }

        int maxHealth = getMaxHealth();
        String maxHealthString = String.valueOf(health);
        if (health < 10) {
            maxHealthString = "  " + maxHealth;
        } else if (health < 100) {
            maxHealthString = " " + maxHealth;
        } else {
            maxHealthString = String.valueOf(maxHealth);
        }

        System.out.print(ANSI_BLUE + "Player--HP:" + healthString + "(" + getMaxHealth() + ")" + ANSI_RESET);
    }

    /**
     * Returns a string representation of the player, including its health and other
     * details.
     *
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return super.toString();
    }
}
