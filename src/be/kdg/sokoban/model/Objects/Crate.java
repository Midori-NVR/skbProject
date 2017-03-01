package be.kdg.sokoban.model.Objects;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Crate extends FieldObject {
    private boolean onGoal;

    public Crate(boolean onGoal, int x, int y){
        super(x, y);
        this.onGoal = onGoal;
    }

    public boolean isOnGoal() {
        return onGoal;
    }

    public void setOnGoal(boolean onGoal) {
        this.onGoal = onGoal;
    }

    @Override
    public boolean isMovable() {
        return true;
    }

    @Override
    public String toString() {
        return "$";
    }
}
