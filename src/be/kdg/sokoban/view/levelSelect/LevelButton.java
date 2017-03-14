package be.kdg.sokoban.view.levelSelect;

import be.kdg.sokoban.model.User;
import javafx.scene.control.Button;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 10:22 AM
 */
class LevelButton extends Button {
    private int number;

    LevelButton(String text, int[] score, int number) {
        super(text + "\n" + score[User.MOVES] + " moves\n" + score[User.PUSHES] + " pushes\n" + score[User.TIME] + " time");
        this.number = number;
    }

    int getNumber() {
        return number;
    }
}
