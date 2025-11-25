package engine;

/**
 * Enum representing the various statuses of the game.
 * Each status reflects a different state or phase in the game.
 */
public enum GameStatus {
    /**
     * The game is in a state where the player can move.
     */
    READY_MOVE,
    /**
     * The player is in a state where they can find a bonus.
     */
    FIND_BONUS,
    /**
     * The player has encountered a NPC.
     */
    MEET_NPC,
    /**
     * The player is currently engaged in a fight with an NPC.
     */
    FIGHTING,
    /**
     * The player has won the game.
     */
    WIN,
    /**
     * The player has lost the game.
     */
    LOSS;

    /**
     * Provides a string representation of the current game status.
     * This method returns a specific message based on the game's status.
     *
     * @return A string message corresponding to the current game status.
     */
    @Override
    public String toString() {
        switch (this) {
            case READY_MOVE -> {
                return "Ready Move press W / A / S / D to move";
            }
            case FIND_BONUS -> {
                return "Find Bonus press Y or N to take or drop";
            }
            case MEET_NPC -> {
                return "Find NPC press Y or N to fight or run away";
            }
            case FIGHTING -> {
                return "In Fighting with NPC, choose the weapon (press the index) every time to fight";
            }
            default -> {
                return this.toString();
            }
        }

    }
}
