package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.MoveAction;
import be.kdg.sokoban.model.Objects.FieldObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 28/02/2017.
 */
public class GameView extends StackPane {
    private GameViewLevel gameViewLevel;
    private HBox statsBar;
    private Label lblMoves;
    private Pane resizePane;

    private Label lblPushes;
    private int moves = 0, pushes = 0;
    private Timeline timer;
    private int time = 0;
    private Label lblTime;
    private Label lblPlayerCoords;
    private int playerX = 0, playerY = 0;
    private BorderPane mainPane;
    private GameEndView gameEndView;
    private boolean paused;
    private GamePauseView gamePauseView;

    //TODO restart level and quit level

    public GameView() {
        initialise();
        setup();
    }

    private void initialise() {
        mainPane = new BorderPane();
        resizePane = new Pane();
        gameEndView = new GameEndView();
        gamePauseView = new GamePauseView();
        gameViewLevel = new GameViewLevel();
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            time++;
            updateStats();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        statsBar = new HBox();
        lblMoves = new Label();
        lblPushes = new Label();
        lblTime = new Label();
        lblPlayerCoords = new Label();
    }


    private void setup() {
        this.getChildren().add(0, mainPane);
        resizePane.getChildren().add(gameViewLevel);
        mainPane.setCenter(resizePane);
        mainPane.setBottom(statsBar);
        updateStats();
        statsBar.getChildren().addAll(lblMoves, lblPushes, lblTime, lblPlayerCoords);
        //getGameViewLevel().maxWidthProperty().bind(this.widthProperty().subtract(widthProperty().multiply(0.2)));
        //FIXME center imageViewLevel
        /*setAlignment(gameViewLevel, Pos.CENTER);
        gameViewLevel.setAlignment(Pos.CENTER);*/
    }

    void updateLevel(MoveAction moveAction) {
        if (moveAction.getActionType() == MoveAction.ACTION_MOVE) {
            moves++;
            updateStats();
        } else if (moveAction.getActionType() == MoveAction.ACTION_PUSH) {
            moves++;
            pushes++;
            updateStats();
        } else if (moveAction.getActionType() == MoveAction.ACTION_NULL) {
            //nothing atm
        }
        if (SokobanMain.DEBUG) {
            this.playerX = moveAction.getPlayer().getPosX();
            this.playerY = moveAction.getPlayer().getPosY();
        }
        //gameViewLevel.updateLevel(moveAction);
        gameViewLevel.updateLevel(moveAction);
    }

    void startLevel(FieldObject[][] currentLevel, int maxRows, int maxColumns) {
        gameViewLevel.setLevel(currentLevel, maxRows, maxColumns);
        timer.play();
    }

    //TODO max amount
    private void updateStats() {
        lblTime.setText("Time:" + time/60 + ":" + (time%60 < 10 ? "0"+time%60 : time%60));
        lblMoves.setText("Moves:" + moves);
        lblPushes.setText("Pushes:" + pushes);
        if (SokobanMain.DEBUG) lblPlayerCoords.setText("(" + playerX + "," + playerY + ")");
    }

    void levelFinished(boolean lastLevel) {
        gameEndView.setScore("This level took you " + moves + " moves, " + pushes + " pushes and " + time/60 + " minutes " + (time%60 < 10 ? "0"+time%60 : time%60));
        if (lastLevel){
            gameEndView.lastLevel();
        }
        this.getChildren().add(1, gameEndView);
        gameEndView.maxHeightProperty().bind(this.heightProperty().divide(1.8));
        gameEndView.maxWidthProperty().bind(this.widthProperty().divide(1.5));
        gameEndView.getLblTitle().maxWidthProperty().bind(gameEndView.widthProperty());
        gameEndView.getBtnMenu().maxWidthProperty().bind(getGameEndView().widthProperty().divide(3));
        gameEndView.getBtnSelect().maxWidthProperty().bind(getGameEndView().widthProperty().divide(3));
        gameEndView.getBtnNext().maxWidthProperty().bind(getGameEndView().widthProperty().divide(3));
//TODO finish level
    }

    GameViewLevel getGameViewLevel() {
        return gameViewLevel;
    }

    BorderPane getMainPane() {
        return mainPane;
    }

    GameEndView getGameEndView() {
        return gameEndView;
    }

    public HBox getStatsBar() {
        return statsBar;
    }

    public Label getLblMoves() {
        return lblMoves;
    }

    public Label getLblPushes() {
        return lblPushes;
    }

    public Label getLblTime() {
        return lblTime;
    }

    public Label getLblPlayerCoords() {
        return lblPlayerCoords;
    }

    public void showPauseMenu() {
        paused = true;
        getChildren().add(1, gamePauseView);
        timer.pause();
        gamePauseView.maxHeightProperty().bind(this.heightProperty().divide(1.8));
        gamePauseView.maxWidthProperty().bind(this.widthProperty().divide(1.5));
        gamePauseView.getBtnResume().maxWidthProperty().bind(getGamePauseView().widthProperty().divide(2));
        gamePauseView.getBtnRestart().maxWidthProperty().bind(getGamePauseView().widthProperty().divide(2));
        gamePauseView.getBtnMenu().maxWidthProperty().bind(getGamePauseView().widthProperty().divide(2));
    }

    public void closePauseMenu() {
        paused = false;
        getChildren().remove(gamePauseView);
        timer.play();
    }

    public boolean isPaused() {
        return paused;
    }

    public GamePauseView getGamePauseView() {
        return gamePauseView;
    }
}
