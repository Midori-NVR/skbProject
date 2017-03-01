package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.util.Arrays;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 11:43 AM
 */
public class GameViewLevel extends GridPane {
    private Image crateImage, goalImage, wallImage, floorImage, playerImage;
    private FieldObject[][] level;
    private ImageView[][] levelLayout;
    private int maxRows, maxColumns;

    GameViewLevel() {
        initialise();
        setup();
    }

    private void initialise() {
        crateImage = new Image("be/kdg/sokoban/view/game/images/crate.png");
        goalImage = new Image("be/kdg/sokoban/view/game/images/goal.png");
        floorImage = new Image("be/kdg/sokoban/view/game/images/floor.png");
        playerImage = new Image("be/kdg/sokoban/view/game/images/player.png");
        wallImage = new Image("be/kdg/sokoban/view/game/images/wall.png");
    }

    private void setup() {
        this.setManaged(false);
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(Double.MAX_VALUE);
        this.setPrefHeight(Double.MAX_VALUE);
    }

    void setLevel(FieldObject[][] level, int maxRows, int maxColumns) {
        this.maxColumns = maxColumns;
        this.maxRows = maxRows;
        if (SokobanMain.DEBUG) {
            System.out.println("-----START-----");
            System.out.println();
            for (FieldObject[] fieldObjects : level) {

                for (FieldObject fieldObject : fieldObjects) {
                    if (fieldObject == null)
                        System.out.print(" ");
                    else
                        System.out.print(fieldObject);

                }
                System.out.println();
            }
            System.out.println(Arrays.deepToString(level));
            System.out.println("-----STATS-----");
            System.out.printf("Rows: %d%n Columns: %d%n", maxRows, maxColumns);
        }

        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.ALWAYS);
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS);

        if (maxColumns <= maxRows) {
            rc.setPercentHeight(100.0 / maxRows);
            cc.setPercentWidth(100.0 / maxRows);
            for (int i = 0; i < maxRows; i++) {
                this.getRowConstraints().add(rc);
                this.getColumnConstraints().add(cc);
            }
        } else {
            rc.setPercentHeight(100.0 / maxColumns);
            cc.setPercentWidth(100.0 / maxColumns);
            for (int i = 0; i < maxColumns; i++) {
                this.getColumnConstraints().add(cc);
                this.getRowConstraints().add(rc);
            }
        }

        this.level = level;
        levelLayout = new ImageView[level.length][];
        if (SokobanMain.DEBUG) this.setGridLinesVisible(true);
        for (int row = 0; row < level.length; row++) {
            levelLayout[row] = new ImageView[level[row].length];
            for (int column = 0; column < level[row].length; column++) {
                if (level[row][column] != null) {
                    if (level[row][column] instanceof Crate) {
                        levelLayout[row][column] = new ImageView(crateImage);
                    } else if (level[row][column] instanceof Player) {
                        levelLayout[row][column] = new ImageView(playerImage);
                    } else if (level[row][column] instanceof Wall) {
                        levelLayout[row][column] = new ImageView(wallImage);
                    } else if (level[row][column] instanceof Goal) {
                        levelLayout[row][column] = new ImageView(goalImage);
                    }

                    //TODO scale images
                    levelLayout[row][column].setPreserveRatio(true);
                    this.add(levelLayout[row][column], maxRows>maxColumns ? column + (int) Math.floor(((double)maxRows-maxColumns)/2): column, maxRows<maxColumns ? row + (int) Math.floor(((double)maxColumns-maxRows)/2): row);

                }
            }
        }


    }

    public void update() {

    }

    void resizeLevel() {
        if (getScene().getWidth() <= getScene().getHeight()) {
            //Set to width
            resize(getScene().getWidth(), getScene().getWidth());

        } else {
            //Set to height
            resize(getScene().getHeight(), getScene().getHeight());

        }
        if (maxRows > maxColumns){
            for (int row = 0; row < levelLayout.length; row++) {
                for (int column = 0; column < levelLayout[row].length; column++) {
                    if (levelLayout[row][column] != null) {
                        double size = this.getHeight() / maxRows;
                        levelLayout[row][column].setFitWidth(size);
                        levelLayout[row][column].setFitHeight(size);
                    }
                }
            }
        }
        else{
            for (int row = 0; row < levelLayout.length; row++) {
                for (int column = 0; column < levelLayout[row].length; column++) {
                    if (levelLayout[row][column] != null) {
                        double size = this.getWidth() / maxColumns;
                        levelLayout[row][column].setFitWidth(size);
                        levelLayout[row][column].setFitHeight(size);
                    }
                }
            }
        }

    }


}