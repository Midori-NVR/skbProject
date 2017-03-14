package be.kdg.sokoban.model;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.*;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

//TODO make getter for levelLoader.
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

    public FieldObject[][] startLevel(int levelNumber) {
        player = null;
        levelFinished = false;
        currentLevel = levelLoader.generateLvl(levelLoader.getLevel(levelNumber));
        return currentLevel;
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

    private boolean isValidStep(Player player, int direction) {
        FieldObject object = getNextObject(player, direction);

        return (!(object instanceof Wall) && !(object instanceof Crate));
    }

    private boolean isValidPush(Player player, int direction) {
        FieldObject object1 = getNextObject(player, direction);
        if (object1 != null && !(object1 instanceof Wall)) {
            FieldObject object2 = getNextObject(object1, direction);

            return (object1 instanceof Crate && (object2 instanceof Goal || object2 == null));
        }
        return false;
    }

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

    /**
     * Loads users.
     */
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

    /**
     * saves users to file
     * @throws IOException when saving of new users fails.
     */
    private void saveUsers() throws IOException {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(users);
        }

    }

    /**
     * saves given properties
     * @param properties to save
     * @throws IOException when saving of new config fails.
     */
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

    /**
     * Loads the configFile from the disk.
     * @return Properties of configFile.
     * @throws IOException when saving of new config fails.
     */
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

    /**
     * deletes user from given index and saves changes.
     * @param index index of user to delete.
     * @throws IOException when saving of new config fails.
     */
    public void deleteUser(int index) throws IOException {
        users[index] = null;
        saveUsers();
    }

    /**
     * Get if the level is finished.
     * @return true if the level is finished.
     */
    public boolean isLevelFinished() {
        return levelFinished;
    }

    /**
     * Get the users.
     * @return the array of users
     */
    public User[] getUsers() {
        return users;
    }

    /**
     * adds a user and save the changes
     * @param index of the new user
     * @param name of the new user
     * @throws IOException when saving of new config fails.
     */
    public void addUser(int index, String name) throws IOException {
        users[index] = new User(name);
        saveUsers();
    }

    /**
     * sets score for user of specific level
     * @param level to save the score too.
     * @param score to set of given level
     */
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
