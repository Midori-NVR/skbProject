package be.kdg.sokoban.model.Objects;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:02 PM
 */
public class Wall extends FieldObject{
    public Wall(int x, int y){
        super(x, y);
    }

    @Override
    public String toString() {
        return "#";
    }

    public Wall(){
        super();
    }
}
