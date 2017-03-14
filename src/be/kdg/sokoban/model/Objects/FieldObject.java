package be.kdg.sokoban.model.Objects;


/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:34 PM
 */
public abstract class FieldObject {
    private int posX, posY;
    public static final int MOVE_UP = 2, MOVE_DOWN = 0, MOVE_LEFT = 1, MOVE_RIGHT = 3;

    /**
     * Creates a FieldObject
     * @param x coordinate of this FieldObject
     * @param y coordinate of this FieldObject
     */
    FieldObject(int x, int y) {
        setPosX(x);
        setPosY(y);
    }

    /**
     * @param direction the direction to get the opposite of to move
     * @return static int of the opposite move of the given direction
     */
    public static int getOppositeMove(int direction) {
        switch (direction) {
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

    /**
     * @param direction to move to
     * @return the change to the current x coordinate needed to move into the given direction
     */
    public static int getXMove(int direction) {
        if (direction == FieldObject.MOVE_LEFT) {
            return -1;
        } else if (direction == FieldObject.MOVE_RIGHT) {
            return +1;
        } else {
            return 0;
        }
    }

    /**
     * @param direction to move to
     * @return the change to the current y coordinate needed to move into the given direction
     */
    public static int getYMove(int direction) {
        if (direction == FieldObject.MOVE_UP) {
            return -1;
        } else if (direction == FieldObject.MOVE_DOWN) {
            return +1;
        } else {
            return 0;
        }
    }

    /**
     * @return the current x position of this FieldObject
     */
    public int getPosX() {
        return posX;
    }

    /**
     * Sets the current x position of this FieldObject to the given x position
     * @param posX for this FieldObject
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @return the current y position of this FieldObject
     */
    public int getPosY() {
        return posY;
    }

    /**
     * Sets the current y position of this FieldObject
     * @param posY for this FieldObject
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * Sets the current position of this FieldObject
     * @param posX for this FieldObject
     * @param posY for this FieldObject
     */
    public void setPosition(int posX, int posY) {
        setPosX(posX);
        setPosY(posY);
    }

    @Override
    public String toString() {
        return " ";
    }
}
