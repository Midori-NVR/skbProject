package be.kdg.sokoban.view.levelSelect;

import be.kdg.sokoban.SokobanMain;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

//FIXME add score to screen / change to custom button
//TODO change to image of level
/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 1:47 PM
 */
public class LevelSelectView extends BorderPane {
    private GridPane levelPane;
    private Label title;
    //private LevelButton[][][] buttonLevels;
    private List<LevelButton> buttonLevels;
    private Button btnBack;
    private Polygon arrowLeft;
    private Polygon arrowRight;
    private int page = 1;

    public LevelSelectView() {
        initialise();
        setup();
    }

    private void initialise() {
        levelPane = new GridPane();
        title = new Label("Level Select");
        arrowLeft = new Polygon(0.0, 15.0, 20.0, 0.0, 20.0, 30.0);
        arrowRight = new Polygon(20.0, 15.0, 0.0, 0.0, 0.0, 30.0);
        btnBack = new Button("Back");

    }


    private void setup() {
        this.setCenter(levelPane);
        levelPane.setAlignment(Pos.CENTER);
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);
        for (int i = 0; i < 3; i++) {
            levelPane.getColumnConstraints().add(cc);
            levelPane.getRowConstraints().add(rc);
        }

        this.setTop(title);
        setAlignment(title, Pos.TOP_CENTER);
        this.setLeft(arrowLeft);
        setAlignment(arrowLeft, Pos.CENTER_LEFT);
        setMargin(arrowLeft, new Insets(0.0, 0.0, 0.0, 3.0));
        this.setRight(arrowRight);
        setAlignment(arrowRight, Pos.CENTER_RIGHT);
        setMargin(arrowRight, new Insets(0.0, 3.0, 0.0, 0.0));
        this.setBottom(btnBack);
        setAlignment(btnBack, Pos.BOTTOM_CENTER);
    }

    List<LevelButton> getButtonLevels() {
        return buttonLevels;
    }

    void initialiseLevels(List<String> stringLevels) {
        //buttonLevels = new LevelButton[(int)Math.ceil((double)stringLevels.size()/9)][3][3];
        buttonLevels = new ArrayList<>();
        for (int i = 0; i < (stringLevels != null ? stringLevels.size() : 9); i++) {
            /*
              1.1 2.1 3.1
              1.2 2.2 3.2
              1.3 2.3 3.3
             */
            LevelButton button = new LevelButton(stringLevels == null ? "ERROR" : "Level " + (i + 1), i);
            buttonLevels.add(button);
        }
        setupLevels();
    }

    private void setupLevels() {
        levelPane.getChildren().removeAll(buttonLevels);
        for (int i = page * 9 - 9, x = 0, y = 0; i < page * 9; i++, x++) {
            if (x == 3) {
                x = 0;
                y++;
            }
            if (i < buttonLevels.size()) {
                levelPane.add(buttonLevels.get(i), x, y);
                buttonLevels.get(i).setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setMargin(buttonLevels.get(i), new Insets(10));
            }
        }
        if (SokobanMain.DEBUG) {
            System.out.println(levelPane.getChildren());
        }
    }

    /**
     * @return true if successful, false if failed
     */
    boolean next() {
        if (page * 9 < buttonLevels.size()) {
            page++;
            setupLevels();
            return true;
        }
        return false;
    }

    /**
     * @return true if successful, false if failed
     */
    boolean previous() {
        if (page > 1) {
            page--;
            setupLevels();
            return true;
        }
        return false;
    }

    Button getBtnBack() {
        return btnBack;
    }

    GridPane getLevelPane() {
        return levelPane;
    }

    Label getTitle() {
        return title;
    }

    Polygon getArrowLeft() {
        return arrowLeft;
    }

    Polygon getArrowRight() {
        return arrowRight;
    }
}
