package be.kdg.sokoban.view.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

//TODO add option screen: option to disable animation
/**
 * @author Niels Van Reeth
 * @version 1.0 7-2-2017 09:08
 */
public class MenuView extends BorderPane {
    //PRIVATE VARIABLES
    private Label lblTitle;
    private Button btnLvlSelect;
    private Button btnOptions;
    private Button btnChangeUser;
    private Button btnExit;
    private Label lblFooter;
    private VBox buttonBox;

    public MenuView() {
        initialise();
        setup();
    }

    private void initialise() {
        lblTitle = new Label("Sokoban");
        buttonBox = new VBox();
        btnLvlSelect = new Button("Level Select");
        btnOptions = new Button("Options");
        btnChangeUser = new Button("Change User");
        btnExit = new Button("Exit");
        lblFooter = new Label("© 2017 Niels Van Reeth & Lies Van der Haegen");
    }

    private void setup() {
        setAlignment(lblTitle, Pos.TOP_CENTER);
        this.setTop(lblTitle);
        buttonBox.getChildren().addAll(btnLvlSelect, btnOptions, btnChangeUser, btnExit);
        buttonBox.setAlignment(Pos.CENTER);
        for (Node node : buttonBox.getChildren()) {
            setMargin(node, new Insets(5));
        }

        this.setCenter(buttonBox);
        lblFooter.setMaxWidth(Double.MAX_VALUE);
        setAlignment(lblFooter, Pos.BOTTOM_CENTER);
        lblFooter.setTextAlignment(TextAlignment.CENTER);
        lblFooter.setAlignment(Pos.CENTER);
        this.setBottom(lblFooter);
    }

    //GETTERS
    Button getBtnLvlSelect() {
        return btnLvlSelect;
    }

    Button getBtnOptions() {
        return btnOptions;
    }

    Button getBtnChangeUser() {
        return btnChangeUser;
    }

    Button getBtnExit() {
        return btnExit;
    }

    VBox getButtonBox() {
        return buttonBox;
    }

    Label getLblFooter() {
        return lblFooter;
    }

    Label getLblTitle() {
        return lblTitle;
    }
}
