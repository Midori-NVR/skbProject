package be.kdg.sokoban.view.levelSelect;

import be.kdg.sokoban.SokobanMain;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.game.GamePresenter;
import be.kdg.sokoban.view.game.GameView;
import be.kdg.sokoban.view.menu.MenuPresenter;
import be.kdg.sokoban.view.menu.MenuView;
import javafx.scene.input.KeyCode;

/**
 * @author Niels Van Reeth
 * @version 1.0 2/9/2017 1:35 PM
 */
public class LevelSelectPresenter {
    private SokobanModel model;
    private LevelSelectView view;
    @SuppressWarnings("unused")
    private MenuPresenter mPresenter;
    private MenuView mView;
    private GamePresenter gamePresenter;
    private GameView gameView;


    public LevelSelectPresenter(SokobanModel model, LevelSelectView view) {
        long time = System.currentTimeMillis();
        this.model = model;
        this.view = view;

        view.initialiseLevels(model.getLevels(), model.getUsers()[model.getCurrentUserIndex()]);
        //view.setScore(model.getScore());
        addStyleSheets();
        addEventHandlers();
        updateView();
        if (SokobanMain.DEBUG)
            System.out.println("LoadTime LevelSelect: " + (System.currentTimeMillis() - time) + " milliseconds");
    }

    private void addStyleSheets() {
        view.getStylesheets().add("/be/kdg/sokoban/view/levelSelect/css/levelSelect.css");
        view.getBtnBack().getStyleClass().add("btnBack");
        view.getArrowLeft().getStyleClass().add("arrow");
        view.getArrowRight().getStyleClass().add("arrow");
        view.getLevelPane().getStyleClass().add("innerBody");
        view.getTitle().getStyleClass().add("title");
        view.getStyleClass().add("body");
        if (SokobanMain.DEBUG) System.out.println("StyleSheets loaded!");
    }

    private void updateView() {

    }

    public void addWindowEventHandlers() {
        view.getScene().setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                view.previous();
            } else if (event.getCode().equals(KeyCode.RIGHT)) {
                view.next();
            }
        });
    }

    private void addEventHandlers() {
        view.getBtnBack().setOnAction(event -> {
            mView = new MenuView();
            mPresenter = new MenuPresenter(model, mView);
            view.getScene().setRoot(mView);
            //mView.getScene().getWindow().sizeToScene();
        });
        view.getArrowLeft().setOnMouseClicked(event -> view.previous());
        view.getArrowRight().setOnMouseClicked(event -> view.next());
        for (int i = 0; i < view.getButtonLevels().size(); i++) {
            view.getButtonLevels().get(i).setOnAction(event -> {
                gameView = new GameView();
                view.getScene().setRoot(gameView);
                gamePresenter = new GamePresenter(model, gameView, ((LevelButton) event.getSource()).getNumber());
                //gameView.getScene().getWindow().sizeToScene();
                gamePresenter.addWindowEventHandlers();
            });
        }
    }
}
