package gui.controls.searchfield;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.textfield.CustomTextField;

public class SearchField extends CustomTextField {
    public SearchField() {
        ImageView ivClear = new ImageView(new Image(getClass().getResource("/gui/images/x.png").toExternalForm()));
        ivClear.setPreserveRatio(true);
        ivClear.setCursor(Cursor.HAND);
        ivClear.fitHeightProperty().bind(heightProperty().divide(2));
        ivClear.setOnMouseClicked((MouseEvent) -> clear());
        setRight(ivClear);
    }
}
