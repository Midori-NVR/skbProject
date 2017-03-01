package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.FieldObject;
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
        model.startLevel(levelNumber);

        view.getGameViewLevel().setLevel(model.getCurrentLevel(), model.getMaxRows(), model.getMaxColumns());

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
        view.getStylesheets().add("be/kdg/sokoban/view/game/css/game.css");
        view.getStyleClass().add("body");
    }

    public void addWindowEventHandlers() {
        view.getScene().widthProperty().addListener(observable -> updateView());
        view.getScene().heightProperty().addListener(observable -> updateView());
        view.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                model.move(FieldObject.MOVE_LEFT);
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                model.move(FieldObject.MOVE_RIGHT);
            } else if (event.getCode().equals(KeyCode.UP)) {
                model.move(FieldObject.MOVE_UP);
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                model.move(FieldObject.MOVE_DOWN);
            }
        });
    }
}
