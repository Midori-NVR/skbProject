package be.kdg.sokoban.model;

import be.kdg.sokoban.model.Objects.*;
import javafx.scene.control.Alert;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Niels Van Reeth
 * @version 1.0 7-2-2017 10:23
 */
public class SokobanModel {
    private LevelLoader levelLoader;
    private FieldObject[][] currentLevel;
    private Player player = null;

    public SokobanModel() {

        try {
            levelLoader = new LevelLoader();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Levels not found, " + Paths.get("src/be/kdg/sokoban/model/files/levels.txt").toAbsolutePath() + "\n" + e.getMessage());
        }
    }

    public List<String> getLevels() {
        return levelLoader.getLevels();
    }

    public FieldObject[][] startLevel(int levelnumber) {
        currentLevel = levelLoader.generateLvl(levelLoader.getLevel(levelnumber));
        return currentLevel;
    }

    public String getLevel(int level) {
        return levelLoader.getLevel(level);

    }

    public FieldObject[][] getCurrentLevel() {
        return currentLevel;
    }

    public int getMaxRows() {
        return levelLoader.getMaxRows();
    }

    public int getMaxColumns() {
        return levelLoader.getMaxColumns();
    }

    public boolean move(int direction) {
        if (player == null) {
            this.player = levelLoader.getPlayer(currentLevel);
        }

        if (isValidPush(player, direction)) {
            moveCrate(player, direction);
            return true;

        }

        if (isValidStep(player, direction)) {
            movePlayer(player, direction);
            return true;
        } else {
            return false;
        }
    }

    public void moveCrate(Player player, int direction) {
        int posX = getNextX(player, direction);
        int posY = getNextY(player, direction);
        Crate crate = (Crate) getNextObject(player, direction);

        boolean wasGoal = false;

        if (getNextObject(crate, direction) instanceof Goal) {
            wasGoal = true;
        }

        if (crate.isOnGoal()) {
            currentLevel[crate.getPosY()][crate.getPosX()] = new Goal(crate.getPosX(), crate.getPosY());
        } else {
            currentLevel[crate.getPosY()][crate.getPosX()] = null;
        }

        if (wasGoal) {
            crate.setOnGoal(true);
            //Every goal filled?
            if (filledAllGoals()){

            };
        }

        setPos(crate, posX, posY, direction);

        currentLevel[crate.getPosY()][crate.getPosX()] = crate;
    }

    public void movePlayer(Player player, int direction) {
        int posX = player.getPosX();
        int posY = player.getPosY();
        boolean wasGoal = false;

        if (getNextObject(player, direction) instanceof Goal) {
            wasGoal = true;
        }

        setPos(player, posX, posY, direction);

        if (player.isOnGoal()) {
            currentLevel[posY][posX] = new Goal(posX, posY);
        } else {
            currentLevel[posY][posX] = null;
        }

        if (wasGoal) {
            player.setOnGoal(true);
        }
        currentLevel[player.getPosY()][player.getPosX()] = player;
    }

    public void setPos(FieldObject object, int posX, int posY, int direction) {
        switch (direction) {
            case FieldObject.MOVE_UP:
                if (isValidRow(posY - 1)) {
                    object.setPosY(posY - 1);
                }
                break;
            case FieldObject.MOVE_RIGHT:
                if (isValidColumn(posY, posX + 1)) {
                    object.setPosX(posX + 1);
                }
                break;
            case FieldObject.MOVE_DOWN:
                if (isValidRow(posY + 1)) {
                    object.setPosY(posY + 1);
                }
                break;
            case FieldObject.MOVE_LEFT:
                if (isValidColumn(posY, posX - 1)) {
                    object.setPosX(posX - 1);
                }
                break;
        }
    }
    //TODO ^^

    /*public boolean isMovableObject(FieldObject object, int direction) {
        FieldObject object2 = getNextObject(direction, object);

        return (object.isMovable() && object2 == null);
    }*/

    /*public boolean isValidMove(Player player, int direction){
        if (getNextObject(player, direction) instanceof Crate){
            return isValidPush(player, direction);
        } else {
            return isValidStep(player, direction);
        }
    }*/

    public boolean isValidStep(Player player, int direction) {
        FieldObject object = getNextObject(player, direction);

        return (!(object instanceof Wall) && !(object instanceof Crate));
    }

    public boolean isValidPush(Player player, int direction) {
        FieldObject object1 = getNextObject(player, direction);
        FieldObject object2 = getNextObject(object1, direction);

        return (object1 instanceof Crate && (object2 instanceof Goal || object2 == null));
    }

    public FieldObject getNextObject(FieldObject fieldObject, int direction) {
        if (fieldObject != null) {
            int row = fieldObject.getPosY();
            int column = fieldObject.getPosX();

            if (direction == FieldObject.MOVE_UP) {
                if (isValidRow(row - 1)) {
                    return currentLevel[row - 1][column];
                }
            } else if (direction == FieldObject.MOVE_RIGHT) {
                if (isValidColumn(row, column + 1)) {
                    return currentLevel[row][column + 1];
                }
            } else if (direction == FieldObject.MOVE_DOWN) {
                if (isValidRow(row + 1)) {
                    return currentLevel[row + 1][column];
                }
            } else if (direction == FieldObject.MOVE_LEFT) {
                if (isValidColumn(row, column - 1)) {
                    return currentLevel[row][column - 1];
                }
            }
        }
        return null;
    }

    public int getNextX(FieldObject fieldObject, int direction) {
        int row = fieldObject.getPosY();
        int column = fieldObject.getPosX();

        if (direction == FieldObject.MOVE_UP || direction == FieldObject.MOVE_DOWN) {
            return column;
        } else if (direction == FieldObject.MOVE_RIGHT) {
            if (isValidColumn(row, column + 1)) {
                return (column + 1);
            } else {
                return -1;
            }
        } else if (direction == FieldObject.MOVE_LEFT) {
            if (isValidColumn(row, column - 1)) {
                return (column - 1);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public int getNextY(FieldObject fieldObject, int direction) {
        int row = fieldObject.getPosY();
        int column = fieldObject.getPosX();

        if (direction == FieldObject.MOVE_RIGHT || direction == FieldObject.MOVE_LEFT) {
            return row;

        } else if (direction == FieldObject.MOVE_UP) {
            if (isValidRow(row - 1)) {
                return (row - 1);
            } else {
                return -1;
            }
        } else if (direction == FieldObject.MOVE_DOWN) {
            if (isValidRow(row + 1)) {
                return (row + 1);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public boolean isValidRow(int row) {
        return (row < currentLevel.length && row >= 0);
    }

    public boolean isValidColumn(int row, int column) {
        return (column < currentLevel[row].length && column >= 0);
    }

    public boolean filledAllGoals(){
        for (int row = 0; row < currentLevel.length; row++) {
            for (int column = 0; column < currentLevel[row].length; column++) {
                if (currentLevel[row][column] instanceof  Goal){

                }
            }
        }
        return true;
    }
}
