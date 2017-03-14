package be.kdg.sokoban.model.Objects;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Crate extends FieldObject {
    private boolean onGoal;

    /**
     * Creates a crate
     * @param onGoal true if the crate is on a goal at this moment
     * @param x coordinate of this crate
     * @param y coordinate of this crate
     */
    public Crate(boolean onGoal, int x, int y) {
        super(x, y);
        this.onGoal = onGoal;
    }

    /**
     * Checks if this crate is crate is on a goal
     * @return true if this crate is on a goal
     */
    public boolean isOnGoal() {
        return onGoal;
    }

    /**
     * Sets this crate on a goal
     * @param onGoal true if this crate is on a goal
     */
    public void setOnGoal(boolean onGoal) {
        this.onGoal = onGoal;
    }

    @Override
    public String toString() {
        return "$";
    }
}
