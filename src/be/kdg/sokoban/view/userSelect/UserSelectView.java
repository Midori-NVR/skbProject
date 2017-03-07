package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.model.User;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:52 AM
 */
public class UserSelectView extends VBox {
    private List<User> users = new ArrayList();
    private Button[] btnUser = new Button[3];


    public UserSelectView() {
        initialise();
        setup();
    }

    private void initialise() {
        for (int i = 0; i < 3; i++) {
            btnUser[i] = new Button();
        }
    }
    private void setup() {
        for (int i = 0; i < 3; i++) {
            btnUser[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btnUser[i].setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE/3);
            setVgrow(btnUser[i], Priority.ALWAYS);
            this.getChildren().add(btnUser[i]);
        }

    }

    public void setUsers(List<User> users){
       this.users = users;
        for (int i = 0; i < 3; i++) {
            btnUser[i].setText(users.get(i).getName());
        }
    }

    public Button[] getBtnUser() {
        return btnUser;
    }
}
