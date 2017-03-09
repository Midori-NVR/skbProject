package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.model.User;
import javafx.scene.control.TextInputDialog;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:51 AM
 */
public class UserSelectPresenter {
    private SokobanModel model;
    private UserSelectView view;

    public UserSelectPresenter(SokobanModel model, UserSelectView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        view.getUserView().setUsers(model.getUsers());
        addStyleSheets();
        addEventHandlers();
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addEventHandlers() {
        /*for (int i = 0; i < model.getMax_users(); i++) {
            view.getBtnUser(i).setOnAction(event -> {
                for (int j = 0; j < model.getMax_users(); j++) {
                    if (model.getUser(j).getName() == view.getBtnUser(i).getText()){
                        model.setCurrentUser(model.getUser(j));
                    }
                }
            });
        }*/
        //TODO loop for all buttons at ones?
        //TODO add createNewUser();
        //TODO make custom button with position to remove final statement.
        for (int i = 0; i < view.getUserView().getBtnUser().length; i++) {
            final int j = i;
            view.getUserView().getBtnUser()[i].setOnAction(event -> {
                if (model.getUsers()[j] != null) {
                    model.setCurrentUserIndex(j);
                } else {
                    //TODO check if correct way and correct output
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Create User");
                    dialog.setContentText("Give your character a name:");
                    String name = dialog.showAndWait().toString();
                    model.addUser(j, new User(name));
                    updateView();
                }
            });
        }

    }

    private void updateView() {
        view.getUserView().updateUsers(model.getUsers());
    }

    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/userSelect/css/userSelect.css");
        for (int i = 0; i < view.getUserView().getBtnUser().length; i++) {
            view.getUserView().getBtnUser()[i].getStyleClass().add("userButton");
        }
        if (SokobanMain.DEBUG) System.out.println("StyleSheets loaded!");
    }
}
