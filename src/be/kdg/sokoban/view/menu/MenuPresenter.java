package be.kdg.sokoban.view.menu;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.levelSelect.LevelSelectPresenter;
import be.kdg.sokoban.view.levelSelect.LevelSelectView;
import be.kdg.sokoban.view.userSelect.UserSelectPresenter;
import be.kdg.sokoban.view.userSelect.UserSelectView;

/**
 * @author Niels Van Reeth
 * @version 1.0 7-2-2017 10:23
 */
public class MenuPresenter {
    private SokobanModel model;
    private MenuView view;
    private LevelSelectView lsView;
    private LevelSelectPresenter lsPresenter;
    private UserSelectPresenter usPresenter;
    private UserSelectView usView;

    public MenuPresenter(SokobanModel model, MenuView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;
        this.addEventHandlers();
        this.addStyleSheets();
        model.loadSaveFile();
        this.update();
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime Menu: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/menu/css/menu.css");
        view.getBtnLvlSelect().getStyleClass().add("menuButton");
        view.getBtnOptions().getStyleClass().add("menuButton");
        view.getBtnChangeUser().getStyleClass().add("menuButton");
        view.getBtnExit().getStyleClass().add("menuButton");
        view.getButtonBox().getStyleClass().add("menuButtonBox");
        view.getLblFooter().getStyleClass().add("menuLabelFooter");
        view.getStyleClass().add("body");
        view.getLblTitle().getStyleClass().add("title");
        if (SokobanMain.DEBUG) System.out.println("StyleSheets loaded!");
    }

    private void update() {

    }

    //EVENTS
    private void addEventHandlers() {
        view.getBtnExit().setOnAction(event -> System.exit(0));
        view.getBtnLvlSelect().setOnAction(e -> {
            lsView = new LevelSelectView();
            lsPresenter = new LevelSelectPresenter(model, lsView);
            view.getScene().setRoot(lsView);
            //lsView.getScene().getWindow().sizeToScene();
            lsPresenter.addWindowEventHandlers();
        });

        view.getBtnChangeUser().setOnAction(event -> {
            usView = new UserSelectView();
            usPresenter = new UserSelectPresenter(model, usView);
            view.getScene().setRoot(usView);
        });
    }


    public void addWindowEventHandlers() {

    }
}
