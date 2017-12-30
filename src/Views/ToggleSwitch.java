package Views;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * A helper class implemented in HomeWindowController
 *
 * Source: https://gist.github.com/TheItachiUchiha/12e40a6f3af6e1eb6f75
 *
 */

public class ToggleSwitch extends HBox {

    /**
     * FXML label.
     */
    private final Label label = new Label();

    /**
     * FXML button Button.
     */
    private final Button button = new Button();

    /**
     * SimpleBooleanProperty used in implementing the switch.
     */
    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(false);
    SimpleBooleanProperty switchOnProperty() { return switchedOn; }

    /**
     * Initializes the toggle and its properties.
     */
    private void init() {

        label.setText("TAGS");

        getChildren().addAll(label, button);
        button.setOnAction((e) -> {
            switchedOn.set(!switchedOn.get());
        });
        label.setOnMouseClicked((e) -> {
            switchedOn.set(!switchedOn.get());
        });
        setStyle();
        bindProperties();
    }

    /**
     * Initializes the style and type of the toggle switch.
     */
    private void setStyle() {
        setWidth(125);
        label.setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: grey; -fx-text-fill:black; -fx-background-radius: 4;");
        setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * Binds the properties of the toggle switch
     */
    private void bindProperties() {
        label.prefWidthProperty().bind(widthProperty().divide(2));
        label.prefHeightProperty().bind(heightProperty());
        button.prefWidthProperty().bind(widthProperty().divide(2));
        button.prefHeightProperty().bind(heightProperty());
    }

    /**
     * Constructs the ToggleSwitch
     */
    ToggleSwitch() {
        init();
        switchedOn.addListener((a,b,c) -> {
            if (c) {
                label.setText("IMAGES");
                setStyle("-fx-background-color: green;");
                label.toFront();
            }
            else {
                label.setText("TAGS");
                setStyle("-fx-background-color: grey;");
                button.toFront();
            }
        });
    }
}

