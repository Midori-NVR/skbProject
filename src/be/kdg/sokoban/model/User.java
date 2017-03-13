package be.kdg.sokoban.model;

import java.io.Serializable;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:03 PM
 */
public class User implements Serializable {
    private String name;
    private int highscores[][];

    public User(String name){
        this.highscores = new int[50][3];
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHighscores(int levelNumber, int[] score){
        this.highscores[levelNumber-1] = score;
    }

    public void setHighscoreMoves(int levelNumber, int moves){
        this.highscores[levelNumber-1][0] = moves;
    }

    public void setHighscorePushes(int levelNumber, int pushes){
        this.highscores[levelNumber-1][1] = pushes;
    }

    public void setHighscoreTime(int levelNumber, int time){
        this.highscores[levelNumber-1][2] = time;
    }

    public int getHighscoreMoves(int levelNumber){
        return this.highscores[levelNumber-1][0];
    }

    public int getHighscorePushes(int levelNumber){
        return this.highscores[levelNumber-1][1];
    }

    public int getHighscoreTime(int levelNumber){
        return this.highscores[levelNumber-1][2];
    }

    public int[] getHighscores(int levelNumber){
        return highscores[levelNumber-1];
    }

    @Override
    public String toString() {
        return name;
    }
}
