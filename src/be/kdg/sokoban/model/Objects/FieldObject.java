package be.kdg.sokoban.model.Objects;



/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:34 PM
 */
public abstract class FieldObject {
    public static final int MOVE_UP=0, MOVE_DOWN=1, MOVE_LEFT=2, MOVE_RIGHT=3;
    FieldObject(){
    }

    public boolean isMovable(){return false;}

    @Override
    public String toString() {
        return " ";
    }
}
