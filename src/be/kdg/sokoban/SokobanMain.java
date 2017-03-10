package be.kdg.sokoban;

import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.userSelect.UserSelectPresenter;
import be.kdg.sokoban.view.userSelect.UserSelectView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SokobanMain extends Application {
    public static boolean DEBUG = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sokoban" + (DEBUG ? " [debug mode enabled]" : ""));
        SokobanModel model =
                new SokobanModel();
        UserSelectView view =
                new UserSelectView();
        UserSelectPresenter presenter =
                new UserSelectPresenter(model, view);
        primaryStage.setScene(new Scene(view, 600, 700));

        primaryStage.show();
    }

    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--debug")) {
                DEBUG = true;
            }
        }
        launch(args);
    }
}
