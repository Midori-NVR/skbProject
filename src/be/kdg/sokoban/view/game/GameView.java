package be.kdg.sokoban.view.game;

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
    private int moves = 0;
    private Timeline timer;
    private int time = 0;
    private Label lblTime;
    //private int highScore = 0;

    public GameView() {
        initialise();
        setup();
    }

    private void setup() {
        this.setCenter(gameViewLevel);
        this.setBottom(statsBar);

        updateStats();
        statsBar.getChildren().addAll(lblMoves, lblTime);
        statsBar.setStyle("-fx-background-color: white");
        lblMoves.setStyle("-fx-text-fill: black; -fx-font-weight: bold");
        lblTime.setStyle("-fx-text-fill: black; -fx-font-weight: bold");

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
        lblTime = new Label();
        //lblHighscore = new Label("Highscore: ");
    }

    GameViewLevel getGameViewLevel() {
        return gameViewLevel;
    }

    void updateLevel(FieldObject[][] level) {
        moves++;
        updateStats();
        gameViewLevel.updateLevel(level);
    }

    void startLevel(FieldObject[][] currentLevel, int maxRows, int maxColumns) {
        gameViewLevel.setLevel(currentLevel, maxRows, maxColumns);
        timer.play();
    }

    //TODO max amount
    private void updateStats() {
        lblTime.setText("Time:" + time);
        lblMoves.setText("Moves:" + moves);
    }

    void levelFinished() {

    }
}
