package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.model.User;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:52 AM
 */
public class UserView extends GridPane {
    private Button[] btnUser;
    private Button[] btnDeleteUser;

    public Button[] getBtnUser() {
        return btnUser;
    }

    public Button[] getBtnDeleteUser(){
        return btnDeleteUser;
    }

    void setUsers(User[] users) {
        btnUser = new Button[users.length];
        for (int i = 0; i < users.length; i++) {
            btnUser[i] = new Button();
            btnUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            //btnUser[i].setPrefSize(getMaxWidth()/0.8, Double.MAX_VALUE / users.length);
            //setVgrow(btnUser[i], Priority.ALWAYS);
            add(btnUser[i], 0, i);
        }

        btnDeleteUser = new Button[users.length];
        for (int i = 0; i < users.length; i++) {
            btnDeleteUser[i] = new Button("X");
            btnDeleteUser[i].setPadding(new Insets(1));
            btnDeleteUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            //btnDeleteUser[i].setPrefSize(this.getWidth()*0.80, Double.MAX_VALUE / users.length);
            //setVgrow(btnDeleteUser[i], Priority.ALWAYS);
            add(btnDeleteUser[i], 1, i);
        }
        updateUsers(users);
    }

    public void updateUsers(User[] users){
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                btnUser[i].setText(users[i].getName());
                btnDeleteUser[i].setText("X");
            }
            else{
                btnUser[i].setText("+ new user");
                btnDeleteUser[i].setText("");
            }

            if (btnUser[i].getText() != "+ new user"){
                btnDeleteUser[i].setVisible(true);
            } else {
                btnDeleteUser[i].setVisible(false);
            }
        }
    }
}