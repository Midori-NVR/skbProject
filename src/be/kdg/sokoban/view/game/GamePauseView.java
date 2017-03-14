package be.kdg.sokoban.view.game;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/13/2017 10:35 AM
 */
public class GamePauseView extends VBox {
    private Label lblTitle;
    private Button btnResume;
    private Button btnRestart;
    private Button btnMenu;

    GamePauseView() {
        initialise();
        setup();
    }

    private void initialise() {
        lblTitle = new Label("Paused");
        btnResume = new Button("Resume");
        btnRestart = new Button("Restart");
        btnMenu = new Button("Menu");
    }


    private void setup() {
        getChildren().addAll(lblTitle, btnResume, btnRestart, btnMenu);
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    Button getBtnResume() {
        return btnResume;
    }

    Button getBtnRestart() {
        return btnRestart;
    }

    Button getBtnMenu() {
        return btnMenu;
    }
}
