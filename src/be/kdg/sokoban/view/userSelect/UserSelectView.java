package be.kdg.sokoban.view.userSelect;

import javafx.geometry.Pos;
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
        setAlignment(lblTitle, Pos.CENTER);
        this.setTop(lblTitle);
        this.setCenter(userView);
        //TODO set label css and scaling.
    }

    public UserView getUserView() {
        return userView;
    }

    private void initialise() {
        lblTitle = new Label("User Select");
        userView = new UserView();
    }

    public Label getLblTitle() {
        return lblTitle;
    }
}
