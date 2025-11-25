package utils;

import data.DataManager;
import data.ISerializable;

/**
 * Represents a location with X and Y coordinates.
 * This class implements the ISerializable interface to provide serialization
 * and deserialization functionality.
 */
public class Location implements ISerializable {

    private int locationX;
    private int locationY;

    /**
     * Default constructor initializes the location to invalid coordinates (-1, -1).
     */
    public Location() {
        this.locationX = -1;
        this.locationY = -1;
    }

    /**
     * Constructs a Location with specified X and Y coordinates.
     *
     * @param locationX the X coordinate of the location
     * @param locationY the Y coordinate of the location
     */
    public Location(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
    }

    /**
     * Gets the X coordinate of the location.
     *
     * @return the X coordinate
     */
    public int getLocationX() {
        return locationX;
    }

    /**
     * Gets the Y coordinate of the location.
     *
     * @return the Y coordinate
     */
    public int getLocationY() {
        return locationY;
    }

    /**
     * Sets the X coordinate of the location.
     *
     * @param locationX the new X coordinate
     */
    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    /**
     * Sets the Y coordinate of the location.
     *
     * @param locationY the new Y coordinate
     */
    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    /**
     * Checks if the location is valid (i.e., not equal to the default invalid
     * coordinates).
     *
     * @return true if the location is valid, false otherwise
     */
    public boolean validLocation() {
        return locationX != -1 && locationY != -1;
    }

    /**
     * Serializes the current Location object to a JSON string using GSON.
     *
     * @return a JSON representation of the Location object
     */
    @Override
    public String serialize() {
        return DataManager.GSON.toJson(this);
    }

    /**
     * Deserializes the provided JSON string to populate the Location object's
     * coordinates.
     *
     * @param data a JSON string representing a Location object
     */
    @Override
    public void deserialize(String data) {
        Location location = DataManager.GSON.fromJson(data, Location.class);
        this.locationX = location.locationX;
        this.locationY = location.locationY;
    }

    /**
     * Compares this Location object to another object for equality.
     * Two Location objects are considered equal if their X and Y coordinates are
     * the same.
     *
     * @param obj the object to compare this Location against
     * @return true if the given object is a Location and has the same coordinates,
     *         false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location otherLocation) {
            return locationX == otherLocation.locationX && locationY == otherLocation.locationY;
        }
        return false;
    }

    /**
     * Returns a string representation of the Location object in the format
     * "[locationX, locationY]".
     *
     * @return a string representation of the Location object
     */
    @Override
    public String toString() {
        return "[" + locationX + "," + locationY + ']';
    }
}
