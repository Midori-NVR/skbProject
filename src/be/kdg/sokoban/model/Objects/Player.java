package be.kdg.sokoban.model.Objects;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Player extends FieldObject{
    private boolean onGoal;

    public Player(boolean onGoal, int x, int y) {
        super(x, y);
        this.onGoal = onGoal;
    }

    public void move(){
        //TODO
    }

    public boolean isOnGoal() {
        return onGoal;
    }

    public void setOnGoal(boolean onGoal) {
        this.onGoal = onGoal;
    }

    @Override
    public String toString() {
        return "@";
    }


}
