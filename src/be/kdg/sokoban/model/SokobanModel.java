package be.kdg.sokoban.model;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.*;
import javafx.scene.control.Alert;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private User currentUser;
    private int max_users = 3;
    private File file;

    public SokobanModel() {
        try {
            levelLoader = new LevelLoader();
            file = new File("src/be/kdg/sokoban/save.txt");
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
            System.out.println("Player(" + player.getPosY() + ", " + player.getPosX() + ")");
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
            return new MoveAction(direction, player, MoveAction.ACTION_PUSH, movePlayer(player, direction),getNextObject(player, direction));


        }

        if (isValidStep(player, direction)) {
            return new MoveAction(direction, player, MoveAction.ACTION_MOVE, movePlayer(player, direction),getNextObject(player, direction));
        }
        return new MoveAction(direction, player, MoveAction.ACTION_NULL,false,getNextObject(player,direction));
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

    public void loadSaveFile() {
        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                users = (User[]) input.readObject();
            } catch (IOException | ClassNotFoundException e) {                
                e.printStackTrace();
                //TODO exception
            }
            System.out.println(users.get(0).getName());
            users.get(0).getHighscores();
        } else {
            try {
                if(!file.createNewFile()){
                    //TODO exception
                }
            } catch (IOException e) {
                e.printStackTrace();
                //TODO exception
            }

            users = new ArrayList<>();
            users.add(new User("Empty"));
            users.add(new User("Empty"));
            users.add(new User("Empty"));
            for (int i = 0; i < 3; i++) {
                users.get(i).resetHighscores();
            }
            users.get(0).getHighscores();

            try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
                output.writeObject(users.toArray(new User[users.size()]));
            } catch (IOException e) {
                e.printStackTrace();
                //TODO exception
            }
        }
    }

    public void save() {
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
            output.writeObject(users.toArray(new User[users.size()]));
        } catch (IOException e) {
            e.printStackTrace();
            //TODO exception
        }

    }

    public void addUser(int userNr, User user){
        users.set(userNr-1, user);
    }

    public boolean deleteUser(User user){
        if (users.contains(user)) {
            int index = users.indexOf(user);
            users.get(index).setName("Empty");
            users.get(index).resetHighscores();
            users.get(index).getHighscores();
            return true;
        }
        return false;
    }

    public User getUser(int userNr){
        return users.get(userNr-1);
    }

    public boolean isLevelFinished() {
        return levelFinished;
    }

    public List<User> getUsers() {
        return users;
    }
}
