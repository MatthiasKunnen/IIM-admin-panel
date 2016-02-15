package gui;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import java.util.regex.Pattern;

public class GuiHelper {
    private static Pattern numberOnlyPattern = Pattern.compile("[0-9]");
    private static String DECIMAL_SEPARATOR = ",";
    public static EventHandler<KeyEvent> getKeyEventEventHandlerAssuringDecimalInput(){
        return event -> {
            if ((event.getCharacter().equals(DECIMAL_SEPARATOR) && event.getText().contains(DECIMAL_SEPARATOR)) || !numberOnlyPattern.matcher(event.getCharacter()).matches()){
                event.consume();
            }
        };
    }

    private GuiHelper(){

    }
}
