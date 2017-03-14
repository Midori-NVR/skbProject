package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.MoveAction;
import be.kdg.sokoban.model.Objects.FieldObject;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.levelSelect.LevelSelectPresenter;
import be.kdg.sokoban.view.levelSelect.LevelSelectView;
import be.kdg.sokoban.view.menu.MenuPresenter;
import be.kdg.sokoban.view.menu.MenuView;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import java.io.IOException;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 11:43 AM
 */
public class GamePresenter {
    private SokobanModel model;
    private GameView view;
    private int levelNumber;
    private MenuView mView;
    @SuppressWarnings("unused")
    private MenuPresenter mPresenter;
    private LevelSelectView lsView;
    private LevelSelectPresenter lsPresenter;
    private GameView gView;
    private GamePresenter gPresenter;


    public GamePresenter(SokobanModel model, GameView view, int levelNumber) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        this.levelNumber = levelNumber;
        addStyleSheets();
        addEventHandlers();
        model.startLevel(levelNumber);
        try {
            view.getGameViewLevel().setConfig(model.loadConfig());
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "can't save configFile\n" + e.getMessage());
            alert.showAndWait();
        }

        view.startLevel(model.getCurrentLevel(), model.getMaxRows(), model.getMaxColumns());
        //FIXME resize on fullscreen
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void resizeView() {
        view.resizeView();
    }

    private void updateView(MoveAction moveAction) {
        //view.updateLevel(model.getCurrentLevel());
        view.updateLevel(moveAction);
        if (model.isLevelFinished()) {
            model.setScore(levelNumber + 1, view.getScores());
            view.levelFinished(levelNumber + 1 >= model.getLevels().size());
        }
    }

    private void addEventHandlers() {
        view.getGameEndView().getBtnMenu().setOnAction(event -> {
            mView = new MenuView();
            mPresenter = new MenuPresenter(model, mView);
            view.getScene().setRoot(mView);
        });

        view.getGameEndView().getBtnSelect().setOnAction(event -> {
            lsView = new LevelSelectView();
            lsPresenter = new LevelSelectPresenter(model, lsView);
            view.getScene().setRoot(lsView);
            lsPresenter.addWindowEventHandlers();
        });

        view.getGameEndView().getBtnNext().setOnAction(event -> {
            if (levelNumber + 1 <= model.getLevels().size()) {
                gView = new GameView();
                view.getGameViewLevel().getScene().setRoot(gView);
                gPresenter = new GamePresenter(model, gView, levelNumber + 1);
                gPresenter.addWindowEventHandlers();
            }
        });

        view.getGamePauseView().getBtnResume().setOnAction(event -> view.closePauseMenu());

        view.getGamePauseView().getBtnRestart().setOnAction(event -> {
            gView = new GameView();
            view.getGameViewLevel().getScene().setRoot(gView);
            gPresenter = new GamePresenter(model, gView, levelNumber);
            gPresenter.addWindowEventHandlers();
        });

        view.getGamePauseView().getBtnMenu().setOnAction(event -> {
            mView = new MenuView();
            mPresenter = new MenuPresenter(model, mView);
            view.getScene().setRoot(mView);
        });
    }

    private void addStyleSheets() {
        view.getStylesheets().add("be/kdg/sokoban/view/game/css/game.css");
        view.getMainPane().getStyleClass().add("body");
        view.getStatsBar().getStyleClass().add("statusBar");
        view.getGameEndView().getStyleClass().add("finishBody");
        view.getGameEndView().getLblTitle().getStyleClass().add("title");
        view.getLblMoves().getStyleClass().add("lblStatusBar");
        view.getLblPushes().getStyleClass().add("lblStatusBar");
        view.getLblTime().getStyleClass().add("lblStatusBar");
        view.getLblPlayerCoords().getStyleClass().add("lblStatusBar");
        view.getGamePauseView().getStyleClass().add("pauseBody");
        view.getGamePauseView().getLblTitle().getStyleClass().add("title");
    }

    public void addWindowEventHandlers() {
        view.getScene().widthProperty().addListener(observable -> resizeView());
        view.getScene().heightProperty().addListener(observable -> resizeView());
        view.getScene().setOnKeyPressed(event -> {
            if (!model.isLevelFinished()) {
                if (!view.getGameViewLevel().isAnimationRunning() && !view.isPaused()) {
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

                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    if (view.isPaused()) {
                        view.closePauseMenu();
                    } else {
                        view.showPauseMenu();
                    }
                }
            }
        });
    }
}
