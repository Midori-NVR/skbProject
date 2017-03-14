package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.model.User;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:52 AM
 */
class UserView extends GridPane {
    private Button[] btnUser;
    private Button[] btnDeleteUser;

    Button[] getBtnUser() {
        return btnUser;
    }

    Button[] getBtnDeleteUser() {
        return btnDeleteUser;
    }

    void setUsers(User[] users) {
        btnUser = new Button[users.length];
        for (int i = 0; i < users.length; i++) {
            btnUser[i] = new Button();
            btnUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            add(btnUser[i], 0, i);
        }

        btnDeleteUser = new Button[users.length];
        for (int i = 0; i < users.length; i++) {
            btnDeleteUser[i] = new Button("X");
            btnDeleteUser[i].setPadding(new Insets(1));
            btnDeleteUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            add(btnDeleteUser[i], 1, i);
        }
        updateUsers(users);
    }

    void updateUsers(User[] users) {
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                btnUser[i].setText(users[i].getName());
                btnDeleteUser[i].setText("X");
                btnDeleteUser[i].setVisible(true);
            } else {
                btnUser[i].setText("+ new user");
                btnDeleteUser[i].setText("");
                btnDeleteUser[i].setVisible(false);
            }
        }
    }
}