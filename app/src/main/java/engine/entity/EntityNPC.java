package engine.entity;

import engine.IDisplayable;
import utils.Location;

import static utils.ANSIColors.ANSI_BLUE;
import static utils.ANSIColors.ANSI_RESET;

/**
 * EntityNPC class represents a non-playable character (NPC) in the game.
 * It extends the Entity class and implements the IDisplayable interface.
 */
public class EntityNPC extends Entity implements IDisplayable {

    /**
     * Constructs a new EntityNPC with the specified maximum health, attack, and
     * location.
     *
     * @param maxHealth the maximum health of the NPC
     * @param attack    the attack power of the NPC
     * @param location  the initial location of the NPC
     */
    public EntityNPC(int maxHealth, int attack, Location location) {
        super(maxHealth, attack, location, EntityType.ENEMY);
    }

    /**
     * Returns a string representation of the NPC, including its health and attack
     * details.
     *
     * @return a string representation of the NPC
     */
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Displays the NPC's details, including its health and attack power, in a
     * formatted way.
     */
    @Override
    public void display() {
        int health = getHealth();
        String healthString = String.valueOf(health);
        if (health < 10) {
            healthString = "  " + health;
        } else if (health < 100) {
            healthString = " " + health;
        } else {
            healthString = String.valueOf(health);
        }

        System.out.println(ANSI_BLUE + "NPC-----HP:" + healthString + "  Attack:" + getAttack() + ANSI_RESET);

    }
}
