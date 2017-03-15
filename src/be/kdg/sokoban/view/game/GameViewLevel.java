package be.kdg.sokoban.view.game;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.MoveAction;
import be.kdg.sokoban.model.Objects.*;
import javafx.animation.*;
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
import java.util.Properties;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/21/2017 11:43 AM
 */
class GameViewLevel extends GridPane {
    private Image crateImage;
    private Image goalImage;
    private Image wallImage;
    private Image[] playerImage;
    private Image playerOnGoalImage;
    private Image crateOnGoalImage;
    private ImageView[][] levelLayout;
    private int maxRows, maxColumns;
    private boolean animationRunning;
    private int playerAnimationCount = 0;
    private boolean playerAnimationReverse = false;
    private Properties config;

    GameViewLevel() {
        initialise();
        setup();
    }

    private void initialise() {
        crateImage = new Image("be/kdg/sokoban/view/game/images/crate.png");
        goalImage = new Image("be/kdg/sokoban/view/game/images/goal.png");
        playerImage = new Image[4];
        playerImage[0] = new Image("be/kdg/sokoban/view/game/images/player/sokoban_player.png");
        playerImage[1] = new Image("be/kdg/sokoban/view/game/images/player/sokoban_player1.png");
        playerImage[2] = new Image("be/kdg/sokoban/view/game/images/player/sokoban_player2.png");
        playerImage[3] = new Image("be/kdg/sokoban/view/game/images/player/sokoban_player3.png");
        wallImage = new Image("be/kdg/sokoban/view/game/images/wall.png");
        playerOnGoalImage = new Image("be/kdg/sokoban/view/game/images/player/playerOnGoal.png");
        crateOnGoalImage = new Image("be/kdg/sokoban/view/game/images/crateOnGoal.png");
    }

    private void setup() {
        this.setManaged(true);
        this.setAlignment(Pos.CENTER);
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

    //TODO make sound
    void updateLevel(MoveAction moveAction) {
        int squareSize = (int) (this.getHeight() * this.getRowConstraints().get(0).getPercentHeight() / 100);

        if (moveAction.getActionType() == MoveAction.ACTION_NULL) {
            if (Boolean.valueOf(config.get("animation").toString())) {
                animationRunning = true;
                ImageView player = levelLayout[moveAction.getPlayer().getPosY()][moveAction.getPlayer().getPosX()];
                ImageView wall = levelLayout[moveAction.getNextObject().getPosY()][moveAction.getNextObject().getPosX()];
                TranslateTransition moveBack = new TranslateTransition(Duration.millis(50), wall);
                TranslateTransition moveForward = new TranslateTransition(Duration.millis(150), wall);
                setAnimationByDirection(moveBack, FieldObject.getOppositeMove(moveAction.getDirection()), squareSize / 10);
                setAnimationByDirection(moveForward, moveAction.getDirection(), squareSize / 10);
                SequentialTransition wallMove = new SequentialTransition(moveForward, moveBack);

                boolean rotationRequired = 90 * moveAction.getPlayer().getWatchingDirection() != player.getRotate();

                TranslateTransition playerMoveBack = new TranslateTransition(Duration.millis(50), player);
                TranslateTransition playerMoveForward = new TranslateTransition(Duration.millis(150), player);
                setAnimationByDirection(playerMoveBack, FieldObject.getOppositeMove(moveAction.getDirection()), squareSize / 10 + squareSize / 4);
                setAnimationByDirection(playerMoveForward, moveAction.getDirection(), squareSize / 10 + squareSize / 4);
                SequentialTransition playerMove;
                if (rotationRequired) {
                    RotateTransition playerRotate = new RotateTransition(Duration.millis(50), player);
                    double rotation = 90 * moveAction.getPlayer().getWatchingDirection() - player.getRotate();
                    if (rotation > 180) {
                        rotation -= 360;
                    } else if (rotation < -180) {
                        rotation += 360;
                    }
                    playerRotate.setByAngle(rotation);
                    playerMoveBack.setDuration(Duration.millis(100));
                    playerMove = new SequentialTransition(playerRotate, playerMoveForward, playerMoveBack);
                } else {
                    playerMove = new SequentialTransition(playerMoveForward, playerMoveBack);
                }

                wallMove.setOnFinished(event -> animationRunning = false);
                playerMove.play();
                wallMove.play();
            }
        } else {
            ImageView player = levelLayout[moveAction.getPlayer().getPosY() - FieldObject.getYMove(moveAction.getDirection())][moveAction.getPlayer().getPosX() - FieldObject.getXMove(moveAction.getDirection())];
            final ImageView crate;
            if (moveAction.getActionType() == MoveAction.ACTION_PUSH) {
                crate = levelLayout[moveAction.getPlayer().getPosY()][moveAction.getPlayer().getPosX()];
            } else {
                crate = null;
            }

            if (Boolean.valueOf(config.get("animation").toString())) {
                //ANIMATION
                animationRunning = true;
                boolean rotationRequired = 90 * moveAction.getPlayer().getWatchingDirection() != player.getRotate();
                TranslateTransition playerMove = new TranslateTransition(Duration.millis(200), player);


                SequentialTransition playerSequence;
                if (rotationRequired) {
                    RotateTransition playerRotate = new RotateTransition(Duration.millis(100), player);
                    double rotation = 90 * moveAction.getPlayer().getWatchingDirection() - player.getRotate();
                    if (rotation > 180) {
                        rotation -= 360;
                    } else if (rotation < -180) {
                        rotation += 360;
                    }
                    System.out.println(rotation);
                    playerRotate.setByAngle(rotation);
                    playerSequence = new SequentialTransition(player, playerRotate, playerMove);
                } else {
                    playerMove.setDuration(Duration.millis(300));
                    playerSequence = new SequentialTransition(player, playerMove);
                }

                setAnimationByDirection(playerMove, moveAction.getDirection(), squareSize);
                Timeline playerAnimation = new Timeline(new KeyFrame(Duration.millis(100), event -> {

                    if (!playerAnimationReverse) {
                        if (++playerAnimationCount >= playerImage.length) {
                            playerAnimationCount -= 2;
                            playerAnimationReverse = true;
                        }
                    } else {
                        if (--playerAnimationCount < 0) {
                            playerAnimationCount += 2;
                            playerAnimationReverse = false;
                        }
                    }
                    player.setImage(playerImage[playerAnimationCount]);
                }));
                playerAnimation.setCycleCount(Timeline.INDEFINITE);

                playerSequence.setOnFinished(event -> {
                    playerAnimation.stop();

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
                    System.out.println(this.getChildren());
                    GridPane newGrid = new GridPane();
                    newGrid.getChildren().addAll(this.getChildren());
                    newGrid.getChildren().remove(crate);
                    newGrid.getChildren().remove(player);
                    ImageView newPlayer = new ImageView(player.getImage());
                    newPlayer.setRotate(player.getRotate());

                    newGrid.add(newPlayer, moveAction.getPlayer().getPosX() + getColumnTopSpacing(), moveAction.getPlayer().getPosY() + getRowLeftSpacing());
                    if (crate != null) {
                        ImageView newCrate = new ImageView(crate.getImage());
                        newGrid.add(newCrate, moveAction.getNextObject().getPosX() + getColumnTopSpacing(), moveAction.getNextObject().getPosY() + getRowLeftSpacing());
                        levelLayout[moveAction.getNextObject().getPosY()][moveAction.getNextObject().getPosX()] = newCrate;
                    }
                    System.out.println(newGrid.getChildren());
                    levelLayout[moveAction.getPlayer().getPosY()][moveAction.getPlayer().getPosX()] = newPlayer;
                    levelLayout[moveAction.getPlayer().getPosY() - FieldObject.getYMove(moveAction.getDirection())][moveAction.getPlayer().getPosX() - FieldObject.getXMove(moveAction.getDirection())] = null;
                    this.getChildren().setAll(newGrid.getChildren());
                    System.out.println(this.getChildren());
                    System.out.println(newGrid.getChildren());
                    //25 is base height of bottom borderPane
                    resizeLevel(getScene().getWidth(),getScene().getHeight()-25);
                    animationRunning = false;
                    //DELETE FROM field

                });

                if (crate != null) {
                    TranslateTransition crateMove = new TranslateTransition(Duration.millis(150), crate);
                    if (rotationRequired) {
                        crateMove.setDelay(Duration.millis(150));
                    } else {
                        crateMove.setDuration(Duration.millis(200));
                        crateMove.setDelay(Duration.millis(100));
                    }
                    TranslateTransition crateMoveBack = new TranslateTransition(Duration.ONE, crate);
                    setAnimationByDirection(crateMove, moveAction.getDirection(), squareSize);

                    setAnimationByDirection(crateMoveBack, FieldObject.getOppositeMove(moveAction.getDirection()), squareSize);
                    SequentialTransition crateSequence = new SequentialTransition(crateMove, crateMoveBack);

                    crateSequence.play();
                }

                playerAnimation.play();
                playerSequence.play();
                //END ANIMATION

            } else {
                //NO ANIMATION
                if (moveAction.getPlayer().isOnGoal()) {
                    player.setImage(playerOnGoalImage);
                } else {
                    player.setImage(playerImage[0]);
                }
                player.setRotate(90 * moveAction.getPlayer().getWatchingDirection());
                if (crate != null) {
                    if (((Crate) moveAction.getNextObject()).isOnGoal()) {
                        crate.setImage(crateOnGoalImage);
                    } else {
                        crate.setImage(crateImage);
                    }
                }

                if (crate != null) {
                    levelLayout[moveAction.getNextObject().getPosY()][moveAction.getNextObject().getPosX()] = crate;
                }
                levelLayout[moveAction.getPlayer().getPosY()][moveAction.getPlayer().getPosX()] = player;
                levelLayout[moveAction.getPlayer().getPosY() - FieldObject.getYMove(moveAction.getDirection())][moveAction.getPlayer().getPosX() - FieldObject.getXMove(moveAction.getDirection())] = null;

                //DELETE FROM field
                this.getChildren().remove(player);
                this.getChildren().remove(crate);
                this.add(player, moveAction.getPlayer().getPosX() + getColumnTopSpacing(), moveAction.getPlayer().getPosY() + getRowLeftSpacing());
                if (crate != null) {
                    this.add(crate, moveAction.getNextObject().getPosX() + getColumnTopSpacing(), moveAction.getNextObject().getPosY() + getRowLeftSpacing());
                }
                //END NO ANIMATION
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
                            levelLayout[row][column] = new ImageView(playerImage[0]);
                        }
                        levelLayout[row][column].setRotate(90 * ((Player) level[row][column]).getWatchingDirection());
                    } else if (level[row][column] instanceof Wall) {
                        levelLayout[row][column] = new ImageView(wallImage);
                    } else if (level[row][column] instanceof Goal) {
                        levelLayout[row][column] = new ImageView(goalImage);
                    }
                    levelLayout[row][column].setPreserveRatio(false);
                    this.add(levelLayout[row][column], column + getColumnTopSpacing(), row + getRowLeftSpacing());
                }
            }
        }
    }

    private int getColumnTopSpacing() {
        return maxRows > maxColumns ? (int) Math.floor(((double) maxRows - maxColumns) / 2) : 0;
    }

    private int getRowLeftSpacing() {
        return maxRows < maxColumns ? (int) Math.floor(((double) maxColumns - maxRows) / 2) : 0;
    }

    //TODO fix random spacing
    void resizeLevel(double width, double height) {
        //double width = ((Pane) getParent()).getWidth();
        //double height = ((Pane) getParent()).getHeight();
        if (width <= height) {
            //Set to width

            setMaxWidth(width);
            setMaxHeight(width);
            //noinspection SuspiciousNameCombination
            resize(width, width);

        } else {
            //Set to height

            setMaxWidth(height);
            setMaxHeight(height);
            //noinspection SuspiciousNameCombination
            resize(height, height);

        }
        resizeImages();
    }

    private void resizeImages() {
        if (maxRows > maxColumns) {
            for (Node node : this.getChildren()) {
                if (node != null && node instanceof ImageView) {
                    double size = this.getHeight() / maxRows;
                    ((ImageView) node).setFitWidth(size);
                    ((ImageView) node).setFitHeight(size);
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
    }

    void setConfig(Properties config) {
        this.config = config;
    }
}
