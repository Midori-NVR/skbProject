package be.kdg.sokoban.view.levelSelect;

import javafx.scene.control.Button;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 10:22 AM
 */
class LevelButton extends Button {
    private int number;

    LevelButton(String text, int number) {
        super(text);
        this.number = number;
    }

    int getNumber() {
        return number;
    }
}
