package be.kdg.sokoban;

import be.kdg.sokoban.model.SokobanModel;

import java.io.IOException;


/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 19/02/2017.
 */
public class SokobanTest {
    public static void main(String[] args) throws IOException {
        SokobanMain.DEBUG = true;
        SokobanModel model = new SokobanModel();
        model.loadSaveFile();
        model.addUser(1,"Niels");
        model.addUser(2,"Empty");
        model.addUser(3,"Empty");

        model.save();

    }
}
