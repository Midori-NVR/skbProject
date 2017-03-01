package be.kdg.sokoban.view.levelSelect;

import javafx.scene.control.Button;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 10:22 AM
 */
public class LevelButton extends Button {
    private int x;
    private int y;
    private int number;

    public LevelButton(int x, int y, int number) {
        this("",x,y,number);
    }

    public LevelButton(String text, int x, int y, int number) {
        super(text);
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
