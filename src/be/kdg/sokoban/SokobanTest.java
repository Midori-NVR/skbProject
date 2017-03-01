package be.kdg.sokoban;

import be.kdg.sokoban.model.SokobanModel;


/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 19/02/2017.
 */
public class SokobanTest {
    public static void main(String[] args) {
        SokobanMain.DEBUG = true;
        SokobanModel model = new SokobanModel();
        model.startLevel(model.getLevel(50));
    }
}
