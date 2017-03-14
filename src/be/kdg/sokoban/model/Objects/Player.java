package be.kdg.sokoban.model.Objects;


/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Player extends FieldObject {
    private boolean onGoal;
    private int watchingDirection;

    /**
     * Creates a player
     * @param onGoal true if this player is on a goal
     * @param x coordinate for this player
     * @param y coordinate for this player
     */
    public Player(boolean onGoal, int x, int y) {
        super(x, y);
        this.onGoal = onGoal;
        watchingDirection = FieldObject.MOVE_UP;
    }

    /**
     * @return true if this player is on a goal
     */
    public boolean isOnGoal() {
        return onGoal;
    }

    /**
     * Sets if the player is on a goal or not
     * @param onGoal true if this player is on a goal
     */
    public void setOnGoal(boolean onGoal) {
        this.onGoal = onGoal;
    }

    @Override
    public String toString() {
        return "@";
    }

    /**
     * @return the direction this player is watching
     */
    public int getWatchingDirection() {
        return watchingDirection;
    }

    /**
     * Sets the direction this player is watching
     * @param direction to look at
     */
    public void setWatchingDirection(int direction) {
        this.watchingDirection = direction;
    }
}
