package be.kdg.sokoban.view.option;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.util.Properties;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/10/2017 6:04 PM
 */
public class OptionView extends BorderPane {
    private GridPane gridPane;
    private Label lblTitle;
    private Label lblAnimation;
    private SwitchButton btnEnable;
    private Button btnBack;

    public OptionView() {
        initialise();
        setup();
    }

    private void initialise() {
        gridPane = new GridPane();
        lblTitle = new Label("Options");
        lblAnimation = new Label("Animation");
        btnEnable = new SwitchButton();
        btnBack = new Button("Back");
    }

    private void setup() {
        this.setTop(lblTitle);
        this.setCenter(gridPane);
        this.setBottom(btnBack);
        this.setPadding(new Insets(30, 20, 20, 20));
        gridPane.setPadding(new Insets(50, 0, 0, 0));
        gridPane.add(lblAnimation, 0, 0);
        gridPane.add(btnEnable, 1, 0);
        gridPane.setAlignment(Pos.TOP_CENTER);
        for (int i = 0; i < 2; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(40);
            gridPane.getColumnConstraints().add(cc);
        }
        setAlignment(lblTitle, Pos.CENTER);
        setAlignment(btnBack, Pos.CENTER);
        setAlignment(gridPane, Pos.CENTER);
    }

    void setConfig(Properties properties) {
        btnEnable.setEnabled(Boolean.valueOf(properties.getProperty("animation", "true")));
    }

    Properties getConfig() {
        Properties properties = new Properties();
        properties.setProperty("animation", String.valueOf(btnEnable.isEnabled()));
        return properties;
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    Label getLblAnimation() {
        return lblAnimation;
    }

    Button getBtnEnable() {
        return btnEnable;
    }

    GridPane getGridPane() {
        return gridPane;
    }

    Button getBtnBack() {
        return btnBack;
    }
}
