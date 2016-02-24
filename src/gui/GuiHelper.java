package gui;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuiHelper {
    private final static Pattern NUMBER_ONLY_PATTERN = Pattern.compile("[0-9]");
    private final static String DECIMAL_SEPARATOR = ",";

    public static void getKeyEventEventHandlerAssuringDecimalInput(TextField input) {
        input.setOnKeyTyped(event -> {
            if (event.getCharacter().equals(DECIMAL_SEPARATOR)) {
                if (input.getText().contains(DECIMAL_SEPARATOR)) {
                    event.consume();
                }
            }else if(!input.getText().contains(DECIMAL_SEPARATOR) && event.getCharacter().equals(".")){
                input.insertText(input.getCaretPosition(), DECIMAL_SEPARATOR);
                event.consume();
            } else if (!NUMBER_ONLY_PATTERN.matcher(event.getCharacter()).matches()) {
                event.consume();
            }
        });
    }

    public static void getKeyEventEventHandlerAssuringIntegerInput(TextField input) {
        input.setOnKeyTyped(event -> {
            if (!NUMBER_ONLY_PATTERN.matcher(event.getCharacter()).matches()) {
                event.consume();
            }
        });
    }

    private GuiHelper() {

    }

    public static void showError(TextField ctf, String message) {
        ImageView iv = new ImageView(new Image(GuiHelper.class.getResource("/gui/images/shield-error-icon.png").toExternalForm()));
        iv.setPreserveRatio(true);
        iv.setFitHeight(20);
        ctf.getStyleClass().add("error");
        if (ctf instanceof CustomTextField) {
            ((CustomTextField) ctf).setRight(iv);
        }else if (ctf instanceof CustomPasswordField){
            ((CustomPasswordField) ctf).setRight(iv);
        }

        ctf.setTooltip(new Tooltip(message));
    }

    public static void hideError(TextField ctf) {
        Node set = new Pane();
        if (ctf instanceof CustomTextField) {
            ((CustomTextField) ctf).setRight(set);
        }else if (ctf instanceof CustomPasswordField){
            ((CustomPasswordField) ctf).setRight(set);
        }
        ctf.setTooltip(null);
        ctf.getStyleClass().remove("error");
    }

    public static MethodBuilder createMethodBuilder(Object source) {
        return new MethodBuilder(source);
    }
}

class MethodBuilder {
    private Object source;
    private Object defaultValue = null;
    private List<MethodCall> callList = new ArrayList<>();
    private MethodCall targetCall;
    private Object targetObject;

    public MethodBuilder(Object source) {
        this.source = source;
    }

    public MethodBuilder setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public MethodBuilder addMethods(String... methods) {
        for (String method : methods) {
            callList.add(new MethodCall(method));
        }
        return this;
    }

    public MethodBuilder addMethod(String method, Object... parameters) {
        callList.add(new MethodCall(method, parameters));
        return this;
    }

    public MethodBuilder setTarget(Object targetObject, String method, Object... parameters) {
        this.targetCall = new MethodCall(method, parameters);
        this.targetObject = targetObject;
        return this;
    }

    public Object run() {
        Object result = this.source;
        for (MethodCall m : this.callList) {
            result = invoke(result, m.getName(), m.getParameters());
            if (result == null) {
                return defaultValue;
            }
        }
        if (targetCall != null && targetObject != null) {
            List<Object> parameters = new ArrayList<>(Arrays.asList(targetCall.getParameters()));
            parameters.add(0, result);
            invoke(targetObject, targetCall.getName(), parameters.toArray(new Object[parameters.size()]));
        }
        return result;
    }

    private Object invoke(Object obj, String method, Object... parameters) {
        try {
            if (parameters.length == 0) {
                return obj.getClass().getMethod(method).invoke(obj);
            } else {
                List<Class<?>> typeList = Arrays.stream(parameters).map(Object::getClass).collect(Collectors.toList());
                Class[] types = typeList.toArray(new Class[typeList.size()]);
                return obj.getClass().getMethod(method, types).invoke(obj, parameters);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return this.defaultValue;
        }
    }


   private class MethodCall {
        private String name;
        private Object[] parameters;

        public MethodCall(String name, Object... parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public Object[] getParameters() {
            return parameters;
        }
    }
}
