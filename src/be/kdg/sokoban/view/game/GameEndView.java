package be.kdg.sokoban.view.game;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @author Niels Van Reeth & Lies Van der Haegen
 * @version 1.0 10/03/2017.
 */
public class GameEndView extends BorderPane {
    private Label lblText;
    private Label lblTitle;
    private Button btnMenu;
    private Button btnNext;
    private Button btnSelect;
    private HBox btnBox;

    public GameEndView() {
        initialise();
        setup();
    }

    //FIXME fix css
    //FIXME scaling
    //FIXME saveScore

    private void initialise() {
        lblText = new Label();
        lblTitle = new Label("Finished!");
        btnBox = new HBox();
        btnMenu = new Button("Menu Screen");
        btnNext = new Button("Next level");
        btnSelect = new Button("Level select");
    }


    private void setup() {
        this.setTop(lblTitle);
        this.setCenter(lblText);
        btnBox.getChildren().addAll(btnMenu, btnSelect, btnNext);
        this.setBottom(btnBox);
        lblTitle.setAlignment(Pos.CENTER);
        btnMenu.setMinWidth(getWidth()/2);
    }

    void setScore(String text) {
        lblText.setText(text);
    }

    public HBox getBtnBox() {
        return btnBox;
    }

    public Button getBtnMenu() {
        return btnMenu;
    }

    public Button getBtnNext() {
        return btnNext;
    }

    public Button getBtnSelect() {
        return btnSelect;
    }

    public void lastLevel() {
        btnBox.getChildren().remove(btnNext);
    }

    public Label getLblTitle() {
        return lblTitle;
    }
}
