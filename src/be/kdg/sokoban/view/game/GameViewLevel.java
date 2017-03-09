package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.MoveAction;
import be.kdg.sokoban.model.Objects.*;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.util.Arrays;
//TODO Clean-up code: remove goal image checks and removes only keep for array; remove extra checks and rotations.

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
    private boolean animationRunning;
    private boolean resizeLvl;

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

    private void setAnimationByDirection(TranslateTransition transition, int direction, int distance) {
        switch (direction) {
            case FieldObject.MOVE_DOWN:
                transition.setByY(distance);
                break;
            case FieldObject.MOVE_RIGHT:
                transition.setByX(distance);
                break;
            case FieldObject.MOVE_UP:
                transition.setByY(-distance);
                break;
            case FieldObject.MOVE_LEFT:
                transition.setByX(-distance);
                break;
        }
    }


    boolean isAnimationRunning() {
        return animationRunning;
    }


    //TODO fix one millisecond disappearing glitch after movement
    //TODO make sound
    void updateLevel(MoveAction moveAction) {
        int squareSize = (int) (this.getHeight() * this.getRowConstraints().get(0).getPercentHeight() / 100);
        if (moveAction.getActionType() == MoveAction.ACTION_NULL) {
            animationRunning = true;
            ImageView wall = levelLayout[moveAction.getNextObject().getPosY()][moveAction.getNextObject().getPosX()];
            TranslateTransition moveBack = new TranslateTransition(Duration.millis(50), wall);
            TranslateTransition moveForward = new TranslateTransition(Duration.millis(150), wall);
            setAnimationByDirection(moveBack, FieldObject.getOppositeMove(moveAction.getDirection()), squareSize / 10);
            setAnimationByDirection(moveForward, moveAction.getDirection(), squareSize / 10);
            SequentialTransition wallMove = new SequentialTransition(moveForward, moveBack);
            wallMove.setOnFinished(event -> animationRunning = false);
            wallMove.play();
        } else {
            animationRunning = true;
            ImageView player = levelLayout[moveAction.getPlayer().getPosY() - FieldObject.getYMove(moveAction.getDirection())][moveAction.getPlayer().getPosX() - FieldObject.getXMove(moveAction.getDirection())];
            final ImageView crate;
            if (moveAction.getActionType() == MoveAction.ACTION_PUSH) {
                crate = levelLayout[moveAction.getPlayer().getPosY()][moveAction.getPlayer().getPosX()];
            } else {
                crate = null;
            }
            boolean rotationRequired = 90 * moveAction.getPlayer().getWatchingDirection() != player.getRotate();
            TranslateTransition playerMove = new TranslateTransition(Duration.millis(200), player);


            SequentialTransition playerSequence;
            if (rotationRequired) {
                RotateTransition playerRotate = new RotateTransition(Duration.millis(150), player);
                playerRotate.setToAngle(90 * moveAction.getPlayer().getWatchingDirection());
                playerSequence = new SequentialTransition(player, playerRotate, playerMove);
            } else {
                playerMove.setDuration(Duration.millis(350));
                playerSequence = new SequentialTransition(player, playerMove);
            }
            player.setImage(playerImage);


            setAnimationByDirection(playerMove, moveAction.getDirection(), squareSize);


            playerSequence.setOnFinished(event -> {


                TranslateTransition playerMoveBack = new TranslateTransition(Duration.ONE, player);
                setAnimationByDirection(playerMoveBack, FieldObject.getOppositeMove(moveAction.getDirection()), squareSize);


                //UPDATE images

                if (moveAction.getPlayer().isOnGoal()) {
                    player.setImage(playerOnGoalImage);
                }
                player.setRotate(90 * moveAction.getPlayer().getWatchingDirection());
                if (crate != null) {
                    if (((Crate) moveAction.getNextObject()).isOnGoal()) {
                        crate.setImage(crateOnGoalImage);
                    } else {
                        crate.setImage(crateImage);
                    }
                }


                playerMoveBack.setOnFinished(event1 -> {
                    //ADD TO field
                    if (crate != null) {
                        this.add(crate, moveAction.getNextObject().getPosX() + getColumnTopSpacing(), moveAction.getNextObject().getPosY() + getRowLeftSpacing());
                    }
                    this.add(player, moveAction.getPlayer().getPosX() + getColumnTopSpacing(), moveAction.getPlayer().getPosY() + getRowLeftSpacing());

                    animationRunning = false;
                    if (resizeLvl) resizeLevel();
                });
                if (crate != null) {
                    levelLayout[moveAction.getNextObject().getPosY()][moveAction.getNextObject().getPosX()] = crate;
                }
                levelLayout[moveAction.getPlayer().getPosY()][moveAction.getPlayer().getPosX()] = player;
                levelLayout[moveAction.getPlayer().getPosY() - FieldObject.getYMove(moveAction.getDirection())][moveAction.getPlayer().getPosX() - FieldObject.getXMove(moveAction.getDirection())] = null;

                //DELETE FROM field
                this.getChildren().remove(player);
                this.getChildren().remove(crate);
                playerMoveBack.play();

            });
            playerSequence.play();
            if (crate != null) {

                TranslateTransition crateMove = new TranslateTransition(Duration.millis(100), crate);
                if (rotationRequired) {
                    crateMove.setDelay(Duration.millis(250));
                } else {
                    crateMove.setDuration(Duration.millis(200));
                    crateMove.setDelay(Duration.millis(150));
                }
                TranslateTransition crateMoveBack = new TranslateTransition(Duration.ONE, crate);
                setAnimationByDirection(crateMove, moveAction.getDirection(), squareSize);

                setAnimationByDirection(crateMoveBack, FieldObject.getOppositeMove(moveAction.getDirection()), squareSize);
                SequentialTransition crateSequence = new SequentialTransition(crateMove, crateMoveBack);

                crateSequence.play();
            }


        }
    }

    private void setImages(FieldObject[][] level) {
        for (int row = 0; row < level.length; row++) {
            levelLayout[row] = new ImageView[level[row].length];
            for (int column = 0; column < level[row].length; column++) {
                if (level[row][column] != null) {
                    if (level[row][column] instanceof Crate) {
                        if (((Crate) level[row][column]).isOnGoal()) {
                            levelLayout[row][column] = new ImageView(crateOnGoalImage);
                            this.add(new ImageView(goalImage), column + getColumnTopSpacing(), row + getRowLeftSpacing());
                        } else
                            levelLayout[row][column] = new ImageView(crateImage);
                    } else if (level[row][column] instanceof Player) {
                        if (((Player) level[row][column]).isOnGoal()) {
                            levelLayout[row][column] = new ImageView(playerOnGoalImage);
                            this.add(new ImageView(goalImage), column + getColumnTopSpacing(), row + getRowLeftSpacing());
                        } else {
                            levelLayout[row][column] = new ImageView(playerImage);
                        }
                        levelLayout[row][column].setRotate(90 * ((Player) level[row][column]).getWatchingDirection());
                    } else if (level[row][column] instanceof Wall) {
                        levelLayout[row][column] = new ImageView(wallImage);
                    } else if (level[row][column] instanceof Goal) {
                        levelLayout[row][column] = new ImageView(goalImage);
                    }
                    levelLayout[row][column].setPreserveRatio(true);
                    this.add(levelLayout[row][column], column + getColumnTopSpacing(), row + getRowLeftSpacing());
                }
            }
        }
        resizeLevel();
    }

    private int getColumnTopSpacing() {
        return maxRows > maxColumns ? (int) Math.floor(((double) maxRows - maxColumns) / 2) : 0;
    }

    private int getRowLeftSpacing() {
        return maxRows < maxColumns ? (int) Math.floor(((double) maxColumns - maxRows) / 2) : 0;
    }

    void resizeLevel() {
        if (!animationRunning) {
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
                for (Node node : this.getChildren()) {
                    if (node != null && node instanceof ImageView) {
                        double size = this.getWidth() / maxColumns;
                        ((ImageView) node).setFitWidth(size);
                        ((ImageView) node).setFitHeight(size);
                    }
                }
            }
            resizeLvl = false;
        } else {
            resizeLvl = true;
        }
    }


}
