package be.kdg.sokoban.model;

import java.io.Serializable;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 3:03 PM
 */
public class User implements Serializable {
    private String name;
    private int highscores[];

    public User(String name){
        this.highscores = new int[50];
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //TODO remove comments
    public void getHighscores() {
        for (int i = 0; i < highscores.length; i++) {
            System.out.println("Lvl " + (i+1) + ": " + this.getHighscore(i+1));
        }
    }

    public void setHighscore(int levelNumber, int score){
        this.highscores[levelNumber-1] = score;
    }

    public int getHighscore(int levelNumber){
        return highscores[levelNumber-1];
    }

    public void resetHighscores(){
        for (int i = 0; i < 50; i++) {
            this.setHighscore(i+1, 0);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
