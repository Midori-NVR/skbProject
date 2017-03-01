package be.kdg.sokoban.model;

import be.kdg.sokoban.model.Objects.FieldObject;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Niels Van Reeth
 * @version 1.0 7-2-2017 10:23
 */
public class SokobanModel {
    private LevelLoader levelLoader;

    public SokobanModel() {
        try {
            levelLoader = new LevelLoader();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Levels not found, " + Paths.get("src/be/kdg/sokoban/model/files/levels.txt").toAbsolutePath() + "\n" + e.getMessage());
        }
    }

    public List<String> getLevels() {
        return levelLoader.getLevels();
    }
    public void startLevel(String level){
        levelLoader.generateLvl(level);
    }
    public String getLevel(int level){
        return levelLoader.getLevel(level);

    }
    public FieldObject[][] generateLevel(int level){
        return levelLoader.generateLvl(getLevel(level));
    }
    public int getMaxRows(){
        return levelLoader.getMaxRows();
    }
    public int getMaxColumns(){
        return levelLoader.getMaxColumns();
    }

    public void move(int move) {
        //TODO ...
    }
}
