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

    public FieldObject[][] startLevel(int levelNumber) {
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

    //TODO split in 2 functions
    public void loadSaveFile() {
        file = new File("src/be/kdg/sokoban/model/files/save.txt");

        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                users = (User[]) input.readObject();
            } catch (IOException | ClassNotFoundException e) {                
                e.printStackTrace();
                //TODO exception
            }
        } else {
            users = new User[3];
        }
    }

    public void save() {
        //FIXME overwrite
        if (!file.delete()){

            //TODO exception
        }
        try {
            if (!file.createNewFile()){
                //TODO exception
            }
        } catch (IOException e) {
            e.printStackTrace();
            //TODO exception
        }

        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
            //TODO exception
        }

    }

    public void saveConfig(Properties properties){
        File configFile = new File("src/be/kdg/sokoban/model/files/config.properties");
        //todo check if works
        if (!configFile.exists()){
            try {
                if (!configFile.createNewFile()){
                    //TODO exception
                }
            } catch (IOException e) {
                e.printStackTrace();
                //TODO exception
            }
        }
        try (FileOutputStream output = new FileOutputStream(configFile)) {
            properties.store(output, "---Config Sokoban---");
        } catch (IOException e) {
            e.printStackTrace();
            //TODO exception
        }

    }

    public Properties loadConfig(){
        //todo check if works
        File configFile = new File("src/be/kdg/sokoban/model/files/config.properties");
        Properties config = new Properties();
        if (configFile.exists()) {
            try (FileInputStream input = new FileInputStream(configFile)) {

                config.load(input);
                return config;
            } catch (IOException e) {
                e.printStackTrace();
                //TODO exception
                return null;
            }
        } else {
            config = new Properties();
            config.setProperty("animation", "true");
            saveConfig(config);
            return config;
            //TODO defaultProperties
        }
    }

    //FIXME add user

    public void deleteUser(int index) {
        users[index] = null;
        save();
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }

    public User[] getUsers() {
        return users;
    }

    public void addUser(int index, User user) {
        users[index] = user;
        save();
    }

    public void setScore(int level, int score){
        if (users[getCurrentUserIndex()].getHighscore(level) < score){
            users[getCurrentUserIndex()].setHighscore(level, score);
        }
    }
}
