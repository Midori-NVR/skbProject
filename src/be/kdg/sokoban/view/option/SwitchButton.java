package be.kdg.sokoban.view.option;

import javafx.scene.control.Button;

/**
 * @author Lies Van der Haegen
 * @version 1.0 3/11/2017 4:58 PM
 */
public class SwitchButton extends Button {
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }
    public SwitchButton() {
        this(true);
    }
    public SwitchButton(boolean enabled) {
        setEnabled(enabled);
        setOnAction(event -> {
            this.enabled = !this.enabled;
            updateText();
            updateColor();
        });
    }

     void setEnabled(boolean enabled){
        this.enabled = enabled;
        updateText();
        updateColor();
    }

    private void updateText(){
        setText(enabled ? "On" : "Off");
    }

    private void updateColor(){
        setStyle("-fx-background-color: " + (enabled ? "green" : "red"));
    }
}
