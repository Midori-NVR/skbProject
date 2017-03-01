package be.kdg.sokoban.view.game;

import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;

/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 28/02/2017.
 */
public class GameView extends BorderPane {
    private GameViewLevel gameViewLevel;

    public GameView() {
        initialise();
        setup();
    }

    private void setup() {
        this.setCenter(gameViewLevel);

        //TODO center imageViewLevel
        /*setAlignment(gameViewLevel, Pos.CENTER);
        gameViewLevel.setAlignment(Pos.CENTER);*/
    }

    private void initialise() {
        gameViewLevel = new GameViewLevel();
    }

    GameViewLevel getGameViewLevel() {
        return gameViewLevel;
    }
}
