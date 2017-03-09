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
    private int max_users;
    private Button[] btnUser;
    private User[] users;

    UserView() {
        initialise();
        setup();
    }

    private void initialise() {

    }

    //TODO autoscale
    private void setup() {

    }

    public void setUsers(User[] users, int max_users) {
        this.max_users = max_users;
        btnUser = new Button[max_users];
        for (int i = 0; i < users.length; i++) {
            btnUser[i] = new Button();
            btnUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btnUser[i].setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE / max_users);
            setVgrow(btnUser[i], Priority.ALWAYS);
            btnUser[i].setText(users[i].getName());
            this.getChildren().add(btnUser[i]);
        }
    }

    public Button getBtnUser(int index) {
        if (index < btnUser.length) {
            return btnUser[index];
        }
        return null;
    }

    public void updateUsers(User[] users){
        this.users = users;
        for (int i = 0; i < max_users; i++) {
            btnUser[i].setText(users[i].getName());
        }
    }
}