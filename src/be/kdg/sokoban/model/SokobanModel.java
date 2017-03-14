package be.kdg.sokoban.model;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.*;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * @author Niels Van Reeth
 * @version 1.0 7-2-2017 10:23
 */
public class SokobanModel {
    private LevelLoader levelLoader;
    private FieldObject[][] currentLevel;
    private Player player = null;
    private boolean levelFinished;
    private User[] users;
    private File file;
    private int currentUserIndex;

    public int getCurrentUserIndex() {
        return currentUserIndex;
    }

    public void setCurrentUserIndex(int currentUserIndex) {
        this.currentUserIndex = currentUserIndex;
    }

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

    /**
     * Starts the given level
     * @param levelNumber number of the level
     * @return array of FieldObjects of the given level
     */
    public FieldObject[][] startLevel(int levelNumber) {
        player = null;
        levelFinished = false;
        currentLevel = levelLoader.generateLvl(levelLoader.getLevel(levelNumber));
        return currentLevel;
    }

    /**
     * @return 2d array of FieldObjects of the current level
     */
    public FieldObject[][] getCurrentLevel() {
        return currentLevel;
    }

    /**
     * @return int maximum number of rows of the level
     */
    public int getMaxRows() {
        return levelLoader.getMaxRows();
    }

    /**
     * @return maximum number of columns of the level
     */
    public int getMaxColumns() {
        return levelLoader.getMaxColumns();
    }

    /**
     * Moves the player in the given direction
     * @param direction to move to
     * @return MoveAction in given direction
     */
    public MoveAction move(int direction) {
        if (player != null) {
            if (SokobanMain.DEBUG) System.out.println("Player(" + player.getPosY() + ", " + player.getPosX() + ")");
        } else {
            this.player = levelLoader.getPlayer(currentLevel);
        }
        if (SokobanMain.DEBUG) {
            FieldObject tmpNextObject = getNextObject(player, direction);
            System.out.println("CHECK" +
                    "\n -----" +
                    "\nplayer«" + player.getPosX() + "," + player.getPosY() + "»(x,y)");
            if (tmpNextObject != null)
                System.out.println("\n" + tmpNextObject.getClass() + "«" + tmpNextObject.getPosX() + "," + tmpNextObject.getPosY() + "»(x,y)");
            else
                System.out.println("floor");
        }

        player.setWatchingDirection(direction);

        if (isValidPush(player, direction)) {
            moveCrate(player, direction);
            return new MoveAction(direction, player, MoveAction.ACTION_PUSH, movePlayer(player, direction), getNextObject(player, direction));
        }

        if (isValidStep(player, direction)) {
            return new MoveAction(direction, player, MoveAction.ACTION_MOVE, movePlayer(player, direction), getNextObject(player, direction));
        }
        return new MoveAction(direction, player, MoveAction.ACTION_NULL, false, getNextObject(player, direction));
    }

    /**
     * Moves the crate in the given direction in front of the given player
     * @param player to move crate of
     * @param direction to move to
     */
    private void moveCrate(Player player, int direction) {
        int posX = getNextObject(player, direction).getPosX();
        int posY = getNextObject(player, direction).getPosY();
        Crate crate = (Crate) getNextObject(player, direction);

        boolean wasGoal = false;

        if (getNextObject(crate, direction) instanceof Goal) {
            wasGoal = true;
        }

        if (crate.isOnGoal()) {
            currentLevel[crate.getPosY()][crate.getPosX()] = new Goal(crate.getPosX(), crate.getPosY());
            crate.setOnGoal(false);
        } else {
            currentLevel[crate.getPosY()][crate.getPosX()] = null;
        }

        if (wasGoal) {
            crate.setOnGoal(true);
            //Every goal filled?
            levelFinished = filledAllGoals();
        }

        setPos(crate, posX, posY, direction);

        currentLevel[crate.getPosY()][crate.getPosX()] = crate;
    }

    /**
     * Moves the given player into the given direction, sets the position of the player
     * @param player to move
     * @param direction to move to
     * @return true when the player was on a goal.
     */
    private boolean movePlayer(Player player, int direction) {
        int posX = player.getPosX();
        int posY = player.getPosY();
        boolean wasGoal = false;

        if (getNextObject(player, direction) instanceof Goal) {
            wasGoal = true;
        }

        if (player.isOnGoal()) {
            currentLevel[posY][posX] = new Goal(posX, posY);
            player.setOnGoal(false);
        } else {
            currentLevel[posY][posX] = null;
        }

        setPos(player, posX, posY, direction);

        if (wasGoal) {
            player.setOnGoal(true);
        }
        currentLevel[player.getPosY()][player.getPosX()] = player;
        return wasGoal;
    }

    /**
     * Sets the position of the given FieldObject to the given x and y coordinates into the given direction
     * @param object to move
     * @param posX x coordinate of the given object
     * @param posY y coordinate of the given object
     * @param direction to move into
     */
    private void setPos(FieldObject object, int posX, int posY, int direction) {
        switch (direction) {
            case FieldObject.MOVE_UP:
                object.setPosY(posY - 1);
                break;
            case FieldObject.MOVE_RIGHT:
                object.setPosX(posX + 1);
                break;
            case FieldObject.MOVE_DOWN:
                object.setPosY(posY + 1);
                break;
            case FieldObject.MOVE_LEFT:
                object.setPosX(posX - 1);
                break;
        }
    }

    /**
     * Checks if the step of the given player into the given direction is valid
     * @param player that steps
     * @param direction to move into
     * @return true if the next FieldObject in the given direction of the given player is not a Wall or Crate
     */
    private boolean isValidStep(Player player, int direction) {
        FieldObject object = getNextObject(player, direction);

        return (!(object instanceof Wall) && !(object instanceof Crate));
    }

    /**
     * Checks if the push of the given player against the object into the given direction is valid
     * @param player that pushes
     * @param direction to push into
     * @return true if the FieldObject in the direction of the player is a crate
     * AND the FieldObject behind that crate is a Goal or null
     */
    private boolean isValidPush(Player player, int direction) {
        FieldObject object1 = getNextObject(player, direction);
        if (object1 != null && !(object1 instanceof Wall)) {
            FieldObject object2 = getNextObject(object1, direction);

            return (object1 instanceof Crate && (object2 instanceof Goal || object2 == null));
        }
        return false;
    }

    /**
     * @param fieldObject to get the next object of
     * @param direction to check
     * @return the FieldObject in the given direction of the given FieldObject
     */
    private FieldObject getNextObject(FieldObject fieldObject, int direction) {
        FieldObject nextObject = null;
        int row = fieldObject.getPosY();
        int column = fieldObject.getPosX();


        if (direction == FieldObject.MOVE_UP) {
            nextObject = currentLevel[row - 1][column];
            if (nextObject != null)
                nextObject.setPosition(column, row - 1);

        } else if (direction == FieldObject.MOVE_RIGHT) {
            nextObject = currentLevel[row][column + 1];
            if (nextObject != null)
                nextObject.setPosition(column + 1, row);

        } else if (direction == FieldObject.MOVE_DOWN) {
            nextObject = currentLevel[row + 1][column];
            if (nextObject != null)
                nextObject.setPosition(column, row + 1);

        } else if (direction == FieldObject.MOVE_LEFT) {
            nextObject = currentLevel[row][column - 1];
            if (nextObject != null)
                nextObject.setPosition(column - 1, row);

        }
        return nextObject;
    }

    /**
     * Checks if all the goals off the current level are filled
     * @return true if all the crates stand on a goal
     */
    private boolean filledAllGoals() {
        for (FieldObject[] currentLevelRow : currentLevel) {
            for (FieldObject currentLevelField : currentLevelRow) {
                if (currentLevelField instanceof Crate) {
                    if (!((Crate) currentLevelField).isOnGoal()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void loadUsers() {
        file = new File("src/be/kdg/sokoban/model/files/users.txt");

        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                users = (User[]) input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            users = new User[3];
        }
    }

    private void saveUsers() throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(users);
        }

    }

    public void saveConfig(Properties properties) throws IOException {
        File configFile = new File("src/be/kdg/sokoban/model/files/config.properties");
        if (!configFile.exists()) {
            if (!configFile.createNewFile()) {
                throw new IOException("ConfigFile not created.");
            }
        }
        try (FileOutputStream output = new FileOutputStream(configFile)) {
            properties.store(output, "---Config Sokoban---");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Can't write to configFile.");
        }

    }

    public Properties loadConfig() throws IOException {
        File configFile = new File("src/be/kdg/sokoban/model/files/config.properties");
        Properties config = new Properties();
        if (configFile.exists()) {
            try (FileInputStream input = new FileInputStream(configFile)) {
                config.load(input);
                return config;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            config = new Properties();
            config.setProperty("animation", "true");
            saveConfig(config);
            return config;
        }
    }

    public void deleteUser(int index) throws IOException {
        users[index] = null;
        saveUsers();
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }

    public User[] getUsers() {
        return users;
    }

    public void addUser(int index, String name) throws IOException {
        users[index] = new User(name);
        saveUsers();
    }

    public void setScore(int level, int[] score) {
        if (users[getCurrentUserIndex()].getHighScoreMoves(level) < score[User.MOVES]) {
            users[getCurrentUserIndex()].setHighScoreMoves(level, score[User.MOVES]);
        }
        if (users[getCurrentUserIndex()].getHighScorePushes(level) < score[User.PUSHES]) {
            users[getCurrentUserIndex()].setHighScorePushes(level, score[User.PUSHES]);
        }
        if (users[getCurrentUserIndex()].getHighScoreTime(level) < score[User.TIME]) {
            users[getCurrentUserIndex()].setHighScoreTime(level, score[User.TIME]);
        }
    }
}
