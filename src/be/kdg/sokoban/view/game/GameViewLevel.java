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
class GameViewLevel extends GridPane {
    private Image crateImage;
    private Image goalImage;
    private Image wallImage;
    private Image playerImage;
    private Image playerOnGoalImage;
    private Image crateOnGoalImage;
    private ImageView[][] levelLayout;
    private int maxRows, maxColumns;

    GameViewLevel() {
        initialise();
        setup();
    }

    private void initialise() {
        crateImage = new Image("be/kdg/sokoban/view/game/images/crate.png");
        goalImage = new Image("be/kdg/sokoban/view/game/images/goal.png");
        playerImage = new Image("be/kdg/sokoban/view/game/images/player.png");
        wallImage = new Image("be/kdg/sokoban/view/game/images/wall.png");
        playerOnGoalImage = new Image("be/kdg/sokoban/view/game/images/playerOnGoal.png");
        crateOnGoalImage = new Image("be/kdg/sokoban/view/game/images/crateOnGoal.png");
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
        levelLayout = new ImageView[level.length][];
        if (SokobanMain.DEBUG) this.setGridLinesVisible(true);
        setImages(level);

    }

    //TODO change to update info -> with events what needs to happen.
    void updateLevel(FieldObject[][] level) {
        this.getChildren().removeAll(this.getChildren());
        setImages(level);
    }

    private void setImages(FieldObject[][] level) {
        for (int row = 0; row < level.length; row++) {
            levelLayout[row] = new ImageView[level[row].length];
            for (int column = 0; column < level[row].length; column++) {
                if (level[row][column] != null) {
                    if (level[row][column] instanceof Crate) {
                        if (((Crate) level[row][column]).isOnGoal()) {
                            levelLayout[row][column] = new ImageView(crateOnGoalImage);
                        } else
                            levelLayout[row][column] = new ImageView(crateImage);
                    } else if (level[row][column] instanceof Player) {
                        if (((Player) level[row][column]).isOnGoal()) {
                            levelLayout[row][column] = new ImageView(playerOnGoalImage);
                        }
                        levelLayout[row][column] = new ImageView(playerImage);
                    } else if (level[row][column] instanceof Wall) {
                        levelLayout[row][column] = new ImageView(wallImage);
                    } else if (level[row][column] instanceof Goal) {
                        levelLayout[row][column] = new ImageView(goalImage);
                    }
                    levelLayout[row][column].setPreserveRatio(true);
                    this.add(levelLayout[row][column], maxRows > maxColumns ? column + (int) Math.floor(((double) maxRows - maxColumns) / 2) : column, maxRows < maxColumns ? row + (int) Math.floor(((double) maxColumns - maxRows) / 2) : row);
                }
            }
        }
        resizeLevel();
    }

    void resizeLevel() {
        if (getScene().getWidth() <= getScene().getHeight()) {
            //Set to width
            resize(getScene().getWidth(), getScene().getWidth());

        } else {
            //Set to height
            resize(getScene().getHeight(), getScene().getHeight());

        }
        if (maxRows > maxColumns) {
            for (ImageView[] levelLayoutRow : levelLayout) {
                for (ImageView levelLayoutImage : levelLayoutRow) {
                    if (levelLayoutImage != null) {
                        double size = this.getHeight() / maxRows;
                        levelLayoutImage.setFitWidth(size);
                        levelLayoutImage.setFitHeight(size);
                    }
                }
            }
        } else {
            for (ImageView[] levelLayoutRow : levelLayout) {
                for (ImageView levelLayoutImage : levelLayoutRow) {
                    if (levelLayoutImage != null) {
                        double size = this.getWidth() / maxColumns;
                        levelLayoutImage.setFitWidth(size);
                        levelLayoutImage.setFitHeight(size);
                    }
                }
            }
        }

    }


}
