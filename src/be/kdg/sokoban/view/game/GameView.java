package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.MoveAction;
import be.kdg.sokoban.model.Objects.FieldObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 28/02/2017.
 */
public class GameView extends BorderPane {
    private GameViewLevel gameViewLevel;
    private HBox statsBar;
    private Label lblMoves;
    private Label lblPushes;
    private int moves = 0, pushes = 0;
    private Timeline timer;
    private int time = 0;
    private Label lblTime;
    private Label lblPlayerCoords;
    private int playerX = 0, playerY = 0;

    public GameView() {
        initialise();
        setup();
    }

    private void setup() {
        this.setCenter(gameViewLevel);
        this.setBottom(statsBar);

        updateStats();
        statsBar.getChildren().addAll(lblMoves, lblPushes, lblTime, lblPlayerCoords);
        statsBar.setStyle("-fx-background-color: white");
        lblMoves.setStyle("-fx-text-fill: black; -fx-font-weight: bold");
        lblPushes.setStyle("-fx-text-fill: black; -fx-font-weight: bold");
        lblTime.setStyle("-fx-text-fill: black; -fx-font-weight: bold");
        lblPlayerCoords.setStyle("-fx-text-fill: black; -fx-font-weight: bold");

        //TODO center imageViewLevel
        /*setAlignment(gameViewLevel, Pos.CENTER);
        gameViewLevel.setAlignment(Pos.CENTER);*/
    }

    private void initialise() {
        gameViewLevel = new GameViewLevel();

        timer = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            time++;
            updateStats();
        }));
        timer.setCycleCount(Animation.INDEFINITE);
        statsBar = new HBox();
        lblMoves = new Label();
        lblPushes = new Label();
        lblTime = new Label();
        lblPlayerCoords = new Label();
    }

    GameViewLevel getGameViewLevel() {
        return gameViewLevel;
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
        lblTime.setText("Time:" + time);
        lblMoves.setText("Moves:" + moves);
        lblPushes.setText("Pushes:" + pushes);
        if (SokobanMain.DEBUG) lblPlayerCoords.setText("(" + playerX + "," + playerY + ")");
    }

    void levelFinished() {
//TODO finish level
    }

    //TODO restart level and quit level
}
