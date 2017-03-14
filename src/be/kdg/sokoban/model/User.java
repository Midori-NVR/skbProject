package be.kdg.sokoban.model;

import java.io.Serializable;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:03 PM
 */
public class User implements Serializable {
    static final long serialVersionUID = -6200919286368045528L;
    public static final int MOVES = 0, PUSHES = 1, TIME = 2;
    private String name;
    private int highScores[][];

    public User(String name) {
        this.highScores = new int[51][3];
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    void setHighScores(int levelNumber, int[] score) {
        this.highScores[levelNumber - 1] = score;
    }

    void setHighScoreMoves(int levelNumber, int moves) {
        this.highScores[levelNumber - 1][MOVES] = moves;
    }

    void setHighScorePushes(int levelNumber, int pushes) {
        this.highScores[levelNumber - 1][PUSHES] = pushes;
    }

    void setHighScoreTime(int levelNumber, int time) {
        this.highScores[levelNumber - 1][TIME] = time;
    }

    int getHighScoreMoves(int levelNumber) {
        return this.highScores[levelNumber - 1][MOVES];
    }

    int getHighScorePushes(int levelNumber) {
        return this.highScores[levelNumber - 1][PUSHES];
    }

    int getHighScoreTime(int levelNumber) {
        return this.highScores[levelNumber - 1][TIME];
    }

    public int[] getHighScores(int levelNumber) {
        return highScores[levelNumber - 1];
    }

    @Override
    public String toString() {
        return name;
    }
}
