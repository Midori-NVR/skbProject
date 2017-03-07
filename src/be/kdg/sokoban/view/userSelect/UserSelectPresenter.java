package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import javafx.scene.layout.RowConstraints;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:51 AM
 */
public class UserSelectPresenter {
    private SokobanModel model;
    private UserSelectView view;
    private RowConstraints[] rows = new RowConstraints[3];
    public static int max_users;

    public UserSelectPresenter(SokobanModel model, UserSelectView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        for (int i = 0; i < max_users; i++) {
            rows[i] = new RowConstraints(view.getHeight() / max_users);
        }
        addStyleSheets();
        addEventHandlers();
        view.setUsers(model.getUsers());
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addEventHandlers() {

    }

    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/menu/css/menu.css");
        for (int i = 0; i < max_users; i++) {
            view.getBtnUser()[i].getStyleClass().add("userButton");
        }
        if (SokobanMain.DEBUG) System.out.println("StyleSheets loaded!");
    }
}
