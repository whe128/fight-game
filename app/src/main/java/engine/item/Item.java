package engine.item;

import data.ISerializable;
import engine.IDisplayable;
import utils.Location;

/**
 * Represents an abstract item that can be either a weapon or a recovery item.
 * This class implements serialization and display functionalities.
 */
public abstract class Item implements ISerializable, IDisplayable {
    ItemType type;
    Location location;

    /**
     * Constructs an Item with a specified location and type.
     *
     * @param location the location of the item
     * @param type     the type of the item
     */
    public Item(Location location, ItemType type) {
        this.location = location;
        this.type = type;
    }

    /**
     * Gets the type of the item.
     *
     * @return the type of the item
     */
    public ItemType getType() {
        return type;
    }

    /**
     * Gets the location of the item.
     *
     * @return the location of the item
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Gets the attributes of the item. This method must be implemented
     * by subclasses to return specific attributes for each item type.
     *
     * @return the attributes of the item
     */
    public abstract int getAttributes();

    /**
     * Returns a string representation of the item, including its type
     * and attributes.
     *
     * @return a string representation of the item
     */
    @Override
    public String toString() {
        return "type:" + type + ((type == ItemType.WEAPON) ? ("  attack:") : ("  recover:")) + getAttributes();
    }
}
