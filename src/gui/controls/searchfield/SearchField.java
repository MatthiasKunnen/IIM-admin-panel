package gui.controls.searchfield;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.textfield.CustomTextField;

public class SearchField extends CustomTextField {
    public SearchField() {
        ImageView ivClear = new ImageView(new Image(getClass().getResource("/gui/images/x.png").toExternalForm()));
        ivClear.setPreserveRatio(true);
        ivClear.setCursor(Cursor.HAND);
        ivClear.fitHeightProperty().bind(heightProperty().divide(2));
        ivClear.setOnMouseClicked((MouseEvent) -> clear());
        StackPane sp = new StackPane(ivClear);
        sp.setPadding(new Insets(0, 4, 0, 0));
        setRight(sp);
    }
}
