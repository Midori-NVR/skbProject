package be.kdg.sokoban.model.Objects;



/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:34 PM
 */
public abstract class FieldObject {

    FieldObject(){
    }

    public boolean isMovable(){return false;}

    @Override
    public String toString() {
        return " ";
    }
}
