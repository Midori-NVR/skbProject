package be.kdg.sokoban.model;


import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.*;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 2:56 PM
 */
public class LevelLoader {
    private List<String> levels;
    private int maxRows;

    int getMaxRows() {
        return maxRows;
    }

    int getMaxColumns() {
        return maxColumns;
    }

    private int maxColumns = 0;

    LevelLoader() throws IOException {
        levels = loadLevels();
    }

    FieldObject[][] generateLvl(String level) {
        //Filter 2 first lines
        if (SokobanMain.DEBUG) System.out.println(level + "\n --------------");
        level = level.replaceFirst(level.substring(0, level.indexOf("\n") + 3), "");
        if (SokobanMain.DEBUG) System.out.println(level);

        //put into array
        char[][] levelChars;
        String[] rows = level.split("\r\n");
        levelChars = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            levelChars[i] = rows[i].replaceFirst("\\s+$", "").toCharArray();
        }

        FieldObject[][] levelObjects = new FieldObject[levelChars.length][];
        maxRows = levelChars.length;
        for (int row = 0; row < levelChars.length; row++) {
            if (maxColumns < levelChars[row].length) {
                maxColumns = levelChars[row].length;
            }
            levelObjects[row] = new FieldObject[levelChars[row].length];
            for (int column = 0; column < levelChars[row].length; column++) {
                if (levelChars[row][column] == '#') {
                    levelObjects[row][column] = new Wall(column, row);
                } else if (levelChars[row][column] == '$') {
                    levelObjects[row][column] = new Crate(false, column, row);
                } else if (levelChars[row][column] == '*') {
                    levelObjects[row][column] = new Crate(true, column, row);
                } else if (levelChars[row][column] == '.') {
                    levelObjects[row][column] = new Goal(column, row);
                } else if (levelChars[row][column] == '@') {
                    levelObjects[row][column] = new Player(false, column, row);
                    System.out.println("Player: (" + row + ", " + column + ")");
                } else if (levelChars[row][column] == '+') {
                    levelObjects[row][column] = new Player(true, column, row);
                    System.out.println("Player: (" + row + ", " + column + ")");
                }
            }
        }
        if (SokobanMain.DEBUG) System.out.println(Arrays.deepToString(levelObjects));
        return levelObjects;
    }

    List<String> getLevels() {
        return levels;
    }

    private List<String> loadLevels() throws IOException {
        Path levelsFile = Paths.get("src/be/kdg/sokoban/model/files/levels.txt");
        List<String> levelList;
        try (Scanner sc = new Scanner(levelsFile)) {
            sc.useDelimiter(";");
            String line;
            levelList = new ArrayList<>();
            while (sc.hasNext()) {
                line = sc.next();
                if (isValidLevel(line)) {
                    levelList.add(line);
                }
            }
            if (SokobanMain.DEBUG) {
                for (String levelString : levelList) {
                    System.out.println(levelString);
                    System.out.println("---------------------");
                }
            }
        }
        return levelList;
    }

    private static boolean isValidLevel(String line) {

        if (line.trim().matches("^\\d[\\s\\S]*#$")) {
            if ((line.length() - line.replace("@", "").length() == 1 || line.length() - line.replace("+", "").length() == 1) && (line.length() - line.replace("$", "").length()) == line.length() - line.replace(".", "").length()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param levelnumber The number of the level
     * @return null if the level doesn't exist.
     */
    String getLevel(int levelnumber) {
        return getLevels().get(levelnumber);
    }

    Player getPlayer(FieldObject[][] level) {
        for (int row = 0; row < level.length; row++) {
            for (int column = 0; column < level[row].length; column++) {
                if (level[row][column] != null && level[row][column] instanceof Player) {
                    Player player = (Player) level[row][column];
                    player.setPosition(column, row);
                    return player;
                }
            }
        }
        return null;
    }
}
