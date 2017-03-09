package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.model.User;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:52 AM
 */
public class UserView extends VBox {
    private Button[] btnUser;

    public Button[] getBtnUser() {
        return btnUser;
    }

    void setUsers(User[] users) {
        btnUser = new Button[users.length];
        for (int i = 0; i < users.length; i++) {
            btnUser[i] = new Button();
            btnUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btnUser[i].setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE / users.length);
            setVgrow(btnUser[i], Priority.ALWAYS);
            this.getChildren().add(btnUser[i]);
        }

        updateUsers(users);
    }

    public void updateUsers(User[] users){
        for (int i = 0; i < users.length; i++) {
            if (users[i] != null) {
                btnUser[i].setText(users[i].getName());
            }
            else{
                btnUser[i].setText("+ new user");
            }
        }
    }
}