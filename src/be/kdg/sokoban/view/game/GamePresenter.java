package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import javafx.scene.input.KeyCode;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 11:43 AM
 */
public class GamePresenter {
    private SokobanModel model;
    private GameView view;
    private int levelNumber;

    public GamePresenter(SokobanModel model, GameView view, int levelNumber) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        this.levelNumber = levelNumber;
        addStyleSheets();
        addEventHandlers();
        view.getGameViewLevel().setLevel(model.generateLevel(levelNumber), model.getMaxRows(), model.getMaxColumns());

        updateView();
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void updateView() {
        view.getGameViewLevel().resizeLevel();
    }

    private void addEventHandlers() {

    }

    private void addStyleSheets() {
        view.setStyle("-fx-background-image: url(\"be/kdg/sokoban/view/game/images/floor.png\")");
    }

    public void addWindowEventHandlers() {
        view.getScene().widthProperty().addListener(observable -> {
            updateView();
        });
        view.getScene().heightProperty().addListener(observable -> {
            updateView();
        });
        view.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
//model.move(FieldObject.MOVE_LEFT)
            } else if (event.getCode().equals(KeyCode.RIGHT)) {

            } else if (event.getCode().equals(KeyCode.UP)) {

            } else if (event.getCode().equals(KeyCode.DOWN)) {

            }
        });
    }
}
