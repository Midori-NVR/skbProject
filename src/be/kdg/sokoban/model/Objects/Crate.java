package be.kdg.sokoban.model.Objects;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Crate extends FieldObject {

    public Crate(){
        super();
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
