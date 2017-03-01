package be.kdg.sokoban;

import be.kdg.sokoban.model.Objects.FieldObject;
import be.kdg.sokoban.model.SokobanModel;


/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 19/02/2017.
 */
public class SokobanTest {
    public static void main(String[] args) {
        SokobanMain.DEBUG = true;
        SokobanModel model = new SokobanModel();
        model.startLevel(50);
        System.out.println(model.move(FieldObject.MOVE_RIGHT));
    }
}
