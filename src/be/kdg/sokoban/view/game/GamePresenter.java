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

        view.startLevel(model.getCurrentLevel(), model.getMaxRows(), model.getMaxColumns());
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void resizeView() {
        view.getGameViewLevel().resizeLevel();
    }

    private void updateView() {
        view.updateLevel(model.getCurrentLevel());
        if (model.isLevelFinished()) {
            view.levelFinished();
        }
    }

    private void addEventHandlers() {

    }

    private void addStyleSheets() {
        view.getStylesheets().add("be/kdg/sokoban/view/game/css/game.css");
        view.getStyleClass().add("body");
    }

    public void addWindowEventHandlers() {
        view.getScene().widthProperty().addListener(observable -> resizeView());
        view.getScene().heightProperty().addListener(observable -> resizeView());
        view.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                model.move(FieldObject.MOVE_LEFT);
                updateView();
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                model.move(FieldObject.MOVE_RIGHT);
                updateView();
            } else if (event.getCode().equals(KeyCode.UP)) {
                model.move(FieldObject.MOVE_UP);
                updateView();
            } else if (event.getCode().equals(KeyCode.DOWN)) {
                model.move(FieldObject.MOVE_DOWN);
                updateView();
            }
        });
    }
}
