package engine.level;

import data.ISerializable;
import engine.IDisplayable;

/**
 * Represents a grid cell in the game.
 * All grid cells must implement serialization and display functionality.
 */
public interface Grid extends ISerializable, IDisplayable {

    /**
     * Check if the given position is solid
     *
     * @return true if the position is solid
     */
    boolean isSolid();

}
