package be.kdg.sokoban.model.Objects;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Goal extends FieldObject {

    /**
     * Creates a goal
     * @param x coordinate for this goal
     * @param y coordinate for this goal
     */
    public Goal(int x, int y) {
        super(x, y);
    }

    @Override
    public String toString() {
        return ".";
    }
}
