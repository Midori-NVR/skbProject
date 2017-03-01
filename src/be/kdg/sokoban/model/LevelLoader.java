package be.kdg.sokoban.model;


import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.Objects.*;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 2:56 PM
 */
public class LevelLoader {
    private List<String> levels;
    private int maxRows;

    public int getMaxRows() {
        return maxRows;
    }

    public int getMaxColumns() {
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
            levelChars[i] = rows[i].toCharArray();
        }

        FieldObject[][] levelObjects = new FieldObject[levelChars.length][];
        // TODO on goal objects, -> list?
        maxRows = levelChars.length;
        for (int row = 0; row < levelChars.length; row++) {
            //TODO - 1 ???
            if (maxColumns < levelChars[row].length - 1) {
                maxColumns = levelChars[row].length - 1;
            }
            levelObjects[row] = new FieldObject[levelChars[row].length];
            for (int column = 0; column < levelChars[row].length; column++) {
                if (levelChars[row][column] == '#') {
                    levelObjects[row][column] = new Wall();
                } else if (levelChars[row][column] == '$') {
                    levelObjects[row][column] = new Crate();
                } else if (levelChars[row][column] == '.') {
                    levelObjects[row][column] = new Goal();
                } else if (levelChars[row][column] == '@') {
                    levelObjects[row][column] = new Player();
                }

            }
        }
        if (SokobanMain.DEBUG) System.out.println(levelObjects);
        return levelObjects;
    }

    public void reloadLevels() throws IOException {
        levels = loadLevels();
    }

    List<String> getLevels() {
        return levels;
    }

    private List<String> loadLevels() throws IOException {
        Path levelsFile = Paths.get("src/be/kdg/sokoban/model/files/levels.txt");
        List<String> levelList;
        try(Scanner sc = new Scanner(levelsFile)) {
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
        } catch (IOException e){
            //To make sure the scanner is closed.
            throw e;
        }
        return levelList;
    }

    private static boolean isValidLevel(String line) {

        if (line.trim().matches("^\\d[\\s\\S]*#$")) {
            if (line.length() - line.replace("@", "").length() == 1 && line.length() - line.replace("$", "").length() == line.length() - line.replace(".", "").length()) {
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
        try {
            return getLevels().get(levelnumber);
        } catch (IndexOutOfBoundsException e) {
            if (SokobanMain.DEBUG) e.printStackTrace();
            //TODO make exception
            return null;
        }
    }
}