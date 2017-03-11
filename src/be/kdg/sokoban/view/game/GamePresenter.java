package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.MoveAction;
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
        //FIXME resize later
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void resizeView() {
        view.getGameViewLevel().resizeLevel();
    }

    private void updateView(MoveAction moveAction) {
        //view.updateLevel(model.getCurrentLevel());
        view.updateLevel(moveAction);
        if (model.isLevelFinished()) {
            view.levelFinished();
        }
    }

    private void addEventHandlers() {

    }

    private void addStyleSheets() {
        view.getStylesheets().add("be/kdg/sokoban/view/game/css/game.css");
        view.getMainPane().getStyleClass().add("body");
        view.getGameEndView().getStyleClass().add("finishBody");
    }

    public void addWindowEventHandlers() {
        view.getScene().widthProperty().addListener(observable -> resizeView());
        view.getScene().heightProperty().addListener(observable -> resizeView());
        view.getScene().setOnKeyPressed(event -> {
            if (!view.getGameViewLevel().isAnimationRunning() && !model.isLevelFinished()) {
                if (event.getCode().equals(KeyCode.LEFT)) {

                    updateView(model.move(FieldObject.MOVE_LEFT));
                } else if (event.getCode().equals(KeyCode.RIGHT)) {

                    updateView(model.move(FieldObject.MOVE_RIGHT));
                } else if (event.getCode().equals(KeyCode.UP)) {

                    updateView(model.move(FieldObject.MOVE_UP));
                } else if (event.getCode().equals(KeyCode.DOWN)) {

                    updateView(model.move(FieldObject.MOVE_DOWN));
                }
            }
        });
    }
}
