package be.kdg.sokoban.model.Objects;


/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:34 PM
 */
public abstract class FieldObject {
    private int posX, posY;
    public static final int MOVE_UP = 2, MOVE_DOWN = 0, MOVE_LEFT = 1, MOVE_RIGHT = 3;

    FieldObject() {

    }

    FieldObject(int x, int y) {
        setPosX(x);
        setPosY(y);
    }


    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosition(int posX, int posY) {
        setPosX(posX);
        setPosY(posY);
    }

    @Override
    public String toString() {
        return " ";
    }
}
