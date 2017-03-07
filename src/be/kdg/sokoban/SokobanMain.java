package be.kdg.sokoban;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import be.kdg.sokoban.model.SokobanModel;
import be.kdg.sokoban.view.menu.MenuView;
import be.kdg.sokoban.view.menu.MenuPresenter;

public class SokobanMain extends Application {
    public static boolean DEBUG = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Sokoban" + (DEBUG ? " [debug mode enabled]" : ""));
        SokobanModel model =
                new SokobanModel();
        MenuView view =
                new MenuView();
        MenuPresenter presenter =
                new MenuPresenter(model, view);
        primaryStage.setScene(new Scene(view, 850, 500));
        presenter.addWindowEventHandlers();

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
