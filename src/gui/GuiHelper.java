package gui;

import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class GuiHelper {
    private static Pattern numberOnlyPattern = Pattern.compile("[0-9]");
    private static String DECIMAL_SEPARATOR = ",";

    public static void getKeyEventEventHandlerAssuringDecimalInput(TextField input) {
        input.setOnKeyTyped(event -> {
            if (event.getCharacter().equals(DECIMAL_SEPARATOR)) {
                if (input.getText().contains(DECIMAL_SEPARATOR)) {
                    event.consume();
                }
            }else if(!input.getText().contains(DECIMAL_SEPARATOR) && event.getCharacter().equals(".")){
                input.insertText(input.getCaretPosition(), DECIMAL_SEPARATOR);
                event.consume();
            } else if (!numberOnlyPattern.matcher(event.getCharacter()).matches()) {
                event.consume();
            }
        });
    }

    public static void getKeyEventEventHandlerAssuringIntegerInput(TextField input) {
        input.setOnKeyTyped(event -> {
           if (!numberOnlyPattern.matcher(event.getCharacter()).matches()) {
                event.consume();
            }
        });
    }

    private GuiHelper() {

    }
}
