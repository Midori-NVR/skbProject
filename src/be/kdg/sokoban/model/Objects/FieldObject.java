package be.kdg.sokoban.model.Objects;


/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:34 PM
 */
public abstract class FieldObject {
    private int posX, posY;
    public static final int MOVE_UP = 2, MOVE_DOWN = 0, MOVE_LEFT = 1, MOVE_RIGHT = 3;
    public static int getOppositeMove(int direction){
        switch (direction){
            case FieldObject.MOVE_UP:
                return FieldObject.MOVE_DOWN;
            case FieldObject.MOVE_DOWN:
                return FieldObject.MOVE_UP;
            case FieldObject.MOVE_LEFT:
                return FieldObject.MOVE_RIGHT;
            case FieldObject.MOVE_RIGHT:
                return FieldObject.MOVE_LEFT;
        }
        return direction;
    }
    public static int getXMove(int direction){
        if (direction == FieldObject.MOVE_LEFT){
            return -1;
        }else if(direction == FieldObject.MOVE_RIGHT){
            return +1;
        }else{
            return 0;
        }
    }
    public static int getYMove(int direction){
        if (direction == FieldObject.MOVE_UP){
            return -1;
        }else if(direction == FieldObject.MOVE_DOWN){
            return +1;
        }else{
            return 0;
        }
    }

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
