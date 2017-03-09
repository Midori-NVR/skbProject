package be.kdg.sokoban.view.userSelect;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/8/2017 12:23 PM
 */
public class UserSelectView extends BorderPane {
    private UserView userView;
    private Label lblTitle;

    public UserSelectView(){
        initialise();
        setup();
    }

    private void setup() {
        this.setCenter(userView);
        this.setTop(lblTitle);
        //TODO set label css and scaling.
    }

    public UserView getUserView() {
        return userView;
    }

    private void initialise() {
        userView = new UserView();
        lblTitle = new Label();

    }
}
