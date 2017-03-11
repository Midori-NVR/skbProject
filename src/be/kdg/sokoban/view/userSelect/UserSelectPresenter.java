package be.kdg.sokoban.view.userSelect;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.model.User;
import be.kdg.sokoban.view.menu.MenuPresenter;
import be.kdg.sokoban.view.menu.MenuView;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;


/**
 * @author Lies Van der Haegen
 * @version 1.0 3/7/2017 9:51 AM
 */
public class UserSelectPresenter {
    private SokobanModel model;
    private UserSelectView view;
    @SuppressWarnings("unused")
    private MenuPresenter mPresenter;
    private MenuView mView;

    public UserSelectPresenter(SokobanModel model, UserSelectView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        model.loadSaveFile();
        view.getUserView().setUsers(model.getUsers());
        addStyleSheets();
        addEventHandlers();

        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addEventHandlers() {
        //TODO loop for all buttons at ones?
        //TODO add createNewUser();
        //TODO make custom button with position to remove final statement.
        for (int i = 0; i < view.getUserView().getBtnUser().length; i++) {
            final int j = i;
            view.getUserView().getBtnUser()[i].setOnAction(event -> {
                if (model.getUsers()[j] != null) {
                    model.setCurrentUserIndex(j);
                    mView = new MenuView();
                    mPresenter = new MenuPresenter(model, mView);
                    view.getScene().setRoot(mView);
                } else {
                    //TODO check if correct way and correct output
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Create User");
                    dialog.setContentText("Give your character a name (only letters):");
                    String name = null;
                    while (name == null){
                        name = dialog.showAndWait().get();
                        if (!name.trim().matches("^[A-Za-z' éèçáàêâëä]+$")){
                            name = null;
                            new Alert(Alert.AlertType.ERROR,"Only letters and spaces are allowed:\n(A-Za-z' éèçáàêâëä)").showAndWait();
                        }

                    }
                    model.addUser(j, new User(name.trim()));
                    updateView();
                }
            });
        }
    }

    private void updateView() {
        view.getUserView().updateUsers(model.getUsers());
    }

    //TODO bind selection with keys
    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/userSelect/css/userSelect.css");
        for (int i = 0; i < view.getUserView().getBtnUser().length; i++) {
            view.getUserView().getBtnUser()[i].getStyleClass().add("userButton");
        }
        view.getLblTitle().getStyleClass().add("title");
        view.getStyleClass().add("borderPane");
        if (SokobanMain.DEBUG) System.out.println("StyleSheets loaded!");
    }
}
