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
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GuiHelper {
    //<editor-fold desc="Variables" defaultstate="collapsed">
    private static Map<String, String> svgContent = new HashMap<>();
    private final static Pattern NUMBER_ONLY_PATTERN = Pattern.compile("[0-9]");
    private final static String DECIMAL_SEPARATOR = ",";
    //</editor-fold>

    //<editor-fold desc="Static initializer" defaultstate="collapsed">
    static {
        svgContent.put("edit", "M3 17.25V21H6.75L17.81 9.94 14.06 6.19 3 17.25ZM20.71 7.04A1 1 0 0 0 20.71 5.63L18.37 3.29A1 1 0 0 0 16.96 3.29L15.13 5.12 18.88 8.87 20.71 7.04Z");
        svgContent.put("plus", "M24.000000000000263 13.714285714285992H13.714285714285992V24.000000000000263H10.285714285714555V13.714285714285992H2.6290081223123707e-13V10.285714285714555H10.285714285714555V2.6290081223123707e-13H13.714285714285992V10.285714285714555H24.000000000000263V13.714285714285992Z");
        svgContent.put("calendar", "M1.71 22.29H5.57V18.43H1.71V22.29ZM6.43 22.29H10.71V18.43H6.43V22.29ZM1.71 17.57H5.57V13.29H1.71V17.57ZM6.43 17.57H10.71V13.29H6.43V17.57ZM1.71 12.43H5.57V8.57H1.71V12.43ZM11.57 22.29H15.86V18.43H11.57V22.29ZM6.43 12.43H10.71V8.57H6.43V12.43ZM16.71 22.29H20.57V18.43H16.71V22.29ZM11.57 17.57H15.86V13.29H11.57V17.57ZM6.86 6V2.14Q6.86 1.97 6.73 1.84T6.43 1.71H5.57Q5.4 1.71 5.27 1.84T5.14 2.14V6Q5.14 6.17 5.27 6.3T5.57 6.43H6.43Q6.6 6.43 6.73 6.3T6.86 6ZM16.71 17.57H20.57V13.29H16.71V17.57ZM11.57 12.43H15.86V8.57H11.57V12.43ZM16.71 12.43H20.57V8.57H16.71V12.43ZM17.14 6V2.14Q17.14 1.97 17.02 1.84T16.71 1.71H15.86Q15.68 1.71 15.56 1.84T15.43 2.14V6Q15.43 6.17 15.56 6.3T15.86 6.43H16.71Q16.89 6.43 17.02 6.3T17.14 6ZM22.29 5.14V22.29Q22.29 22.98 21.78 23.49T20.57 24H1.71Q1.02 24 0.51 23.49T0 22.29V5.14Q0 4.45 0.51 3.94T1.71 3.43H3.43V2.14Q3.43 1.26 4.06 0.63T5.57 0H6.43Q7.31 0 7.94 0.63T8.57 2.14V3.43H13.71V2.14Q13.71 1.26 14.34 0.63T15.86 0H16.71Q17.6 0 18.23 0.63T18.86 2.14V3.43H20.57Q21.27 3.43 21.78 3.94T22.29 5.14Z");
        svgContent.put("delete", "M6 19C6 20.1 6.9 21 8 21H16C17.1 21 18 20.1 18 19V7H6V19ZM19 4H15.5L14.5 3H9.5L8.5 4H5V6H19V4Z");
        svgContent.put("user", "M7.72 2.15C6.71 2.9 6.44 4.1 6.49 4.74 6.55 5.51 6.71 6.53 6.71 6.53S6.4 6.7 6.4 7.38C6.5 9.1 7.08 8.36 7.2 9.11 7.48 10.93 8.13 10.6 8.13 11.59 8.13 13.24 7.45 14.01 5.33 14.93 3.2 15.85 1 17 1 19V20H19V19C19 17 16.8 15.85 14.67 14.93 12.55 14.01 11.87 13.24 11.87 11.59 11.87 10.6 12.52 10.93 12.8 9.11 12.92 8.36 13.5 9.1 13.61 7.38 13.61 6.7 13.29 6.53 13.29 6.53S13.45 5.52 13.51 4.74C13.58 3.92 13.12 2.18 11.21 1.64 10.88 1.3 10.66 0.76 11.68 0.22 9.44 0.11 8.92 1.28 7.72 2.15Z");
        svgContent.put("add-user", "M15.99 19.13C16 17 13.8 15.74 11.67 14.82 9.55 13.91 8.87 13.14 8.87 11.49 8.87 10.5 9.52 10.82 9.8 9.01 9.92 8.25 10.5 8.99 10.61 7.28 10.61 6.59 10.29 6.42 10.29 6.42S10.45 5.41 10.51 4.63C10.58 3.81 10.12 2.07 8.21 1.54 7.88 1.2 7.66 0.65 8.68 0.11 6.44 0.01 5.92 1.18 4.72 2.04 3.71 2.8 3.44 4 3.48 4.63 3.55 5.41 3.71 6.42 3.71 6.42S3.39 6.59 3.39 7.28C3.5 9 4.08 8.26 4.2 9.01 4.48 10.82 5.13 10.5 5.13 11.49 5.13 13.14 4.92 13.7 2.79 14.61 0.66 15.53 0 17 0.01 19.13 0.01 19.77 0 20 0 20H16C16 20 15.99 19.77 15.99 19.13ZM17 10V7H15V10H12V12H15V15H17V12H20V10H17Z");
        svgContent.put("remove-user", "M15.99 19.13C15.99 16.88 13.8 15.74 11.67 14.82 9.55 13.91 8.87 13.14 8.87 11.49 8.87 10.5 9.52 10.82 9.8 9.01 9.92 8.26 10.5 9 10.61 7.28 10.61 6.59 10.29 6.42 10.29 6.42S10.45 5.41 10.51 4.63C10.58 3.81 10.12 2.07 8.21 1.54 7.88 1.2 7.66 0.65 8.68 0.11 6.44 0.01 5.92 1.18 4.72 2.04 3.71 2.8 3.44 4 3.48 4.63 3.55 5.41 3.71 6.42 3.71 6.42S3.39 6.59 3.39 7.28C3.5 9 4.08 8.26 4.2 9.01 4.48 10.82 5.13 10.5 5.13 11.49 5.13 13.14 4.92 13.7 2.79 14.61 0.66 15.53 0 17 0.01 19.13 0.01 19.77 0 20 0 20H16C16 20 15.99 19.77 15.99 19.13ZM16 10.04L13.7 7.64 12.64 8.7 15.04 11 12.64 13.3 13.7 14.36 16 11.96 18.3 14.36 19.36 13.3 16.96 11 19.36 8.7 18.3 7.64 16 10.04Z");
        svgContent.put("users", "M15.99 19.13C15.99 16.88 13.8 15.74 11.67 14.82 9.55 13.91 8.87 13.14 8.87 11.49 8.87 10.5 9.52 10.82 9.8 9.01 9.92 8.26 10.5 9 10.61 7.28 10.61 6.59 10.29 6.42 10.29 6.42S10.45 5.41 10.51 4.63C10.58 3.81 10.12 2.07 8.21 1.54 7.88 1.2 7.66 0.65 8.68 0.11 6.44 0.01 5.92 1.18 4.72 2.04 3.71 2.8 3.44 4 3.48 4.63 3.55 5.41 3.71 6.42 3.71 6.42S3.39 6.59 3.39 7.28C3.5 9 4.08 8.26 4.2 9.01 4.48 10.82 5.13 10.5 5.13 11.49 5.13 13.14 4.92 13.7 2.79 14.61 0.66 15.53 0 17 0.01 19.13 0.01 19.77 0 20 0 20H16C16 20 15.99 19.77 15.99 19.13ZM18.53 13.37C17.39 12.91 16.92 12.36 16.92 11.3 16.92 10.66 17.34 10.87 17.52 9.7 17.6 9.21 17.97 9.69 18.04 8.58 18.04 8.14 17.84 8.03 17.84 8.03S17.94 7.37 17.98 6.87C18.03 6.24 17.62 4.62 15.72 4.62 13.81 4.62 13.4 6.24 13.45 6.87 13.49 7.37 13.59 8.03 13.59 8.03S13.39 8.14 13.39 8.58C13.46 9.69 13.83 9.21 13.91 9.7 14.09 10.87 14.51 10.66 14.51 11.3 14.51 12.36 14.07 12.86 12.7 13.45 12.63 13.48 12.58 13.52 12.52 13.55 14.16 14.27 16.74 15.49 17.36 18H20C20 18 20 16.09 20 15.68 20 14.68 19.73 13.85 18.53 13.37Z");
        svgContent.put("account", "M12 2C6.48 2 2 6.48 2 12S6.48 22 12 22 22 17.52 22 12 17.52 2 12 2ZM12 5C13.66 5 15 6.34 15 8S13.66 11 12 11 9 9.66 9 8 10.34 5 12 5ZM12 19.2A7.2 7.2 0 0 1 6 15.98C6.03 13.99 10 12.9 12 12.9 13.99 12.9 17.97 13.99 18 15.98A7.2 7.2 0 0 1 12 19.2Z");
        svgContent.put("gavel", "M1 21H13V23H1ZM5.25 8.07L8.07 5.24 22.21 19.38 19.39 22.21ZM12.32 1L17.97 6.66 15.14 9.49 9.49 3.83ZM3.83 9.48L9.48 15.14 6.65 17.97 1 12.31Z");
        svgContent.put("home", "M10 20V14H14V20H19V12H22L12 3 2 12H5V20Z");
        svgContent.put("institution", "M12.86 0L25.71 5.14V6.86H24Q24 7.21 23.73 7.46T23.08 7.71H2.64Q2.26 7.71 1.99 7.46T1.71 6.86H0V5.14ZM3.43 8.57H6.86V18.86H8.57V8.57H12V18.86H13.71V8.57H17.14V18.86H18.86V8.57H22.29V18.86H23.08Q23.45 18.86 23.73 19.11T24 19.71V20.57H1.71V19.71Q1.71 19.37 1.99 19.11T2.64 18.86H3.43V8.57ZM24.79 21.43Q25.17 21.43 25.44 21.68T25.71 22.29V24H0V22.29Q0 21.94 0.27 21.68T0.92 21.43H24.79Z");
        svgContent.put("graduation-cap", "M23.76 11.2L24 15.43Q24.05 16.35 22.9 17.14T19.75 18.4 15.43 18.86 11.1 18.4 7.96 17.14 6.86 15.43L7.1 11.2 14.79 13.62Q15.08 13.71 15.43 13.71T16.07 13.62ZM30.86 6.86Q30.86 7.17 30.56 7.27L15.56 11.99Q15.51 12 15.43 12T15.29 11.99L6.56 9.23Q5.99 9.68 5.61 10.72T5.16 13.11Q6 13.59 6 14.57 6 15.5 5.22 16L6 21.8Q6.03 21.99 5.89 22.14 5.77 22.29 5.57 22.29H3Q2.8 22.29 2.68 22.14 2.54 21.99 2.57 21.8L3.35 16Q2.57 15.5 2.57 14.57 2.57 13.59 3.44 13.08 3.59 10.31 4.75 8.67L0.29 7.27Q0 7.17 0 6.86T0.29 6.44L15.29 1.73Q15.35 1.71 15.43 1.71T15.56 1.73L30.56 6.44Q30.86 6.55 30.86 6.86Z");
        svgContent.put("gear", "M13.71 12Q13.71 10.58 12.71 9.58T10.29 8.57 7.86 9.58 6.86 12 7.86 14.42 10.29 15.43 12.71 14.42 13.71 12ZM20.57 10.54V13.51Q20.57 13.67 20.46 13.82T20.2 14L17.72 14.37Q17.46 15.09 17.2 15.59 17.67 16.26 18.63 17.44 18.76 17.6 18.76 17.77T18.64 18.08Q18.28 18.58 17.32 19.53T16.06 20.48Q15.9 20.48 15.71 20.36L13.86 18.91Q13.27 19.22 12.64 19.42 12.43 21.24 12.25 21.91 12.16 22.29 11.77 22.29H8.8Q8.61 22.29 8.47 22.17T8.32 21.88L7.94 19.42Q7.29 19.21 6.74 18.92L4.85 20.36Q4.71 20.48 4.51 20.48 4.33 20.48 4.18 20.33 2.49 18.8 1.97 18.08 1.88 17.95 1.88 17.77 1.88 17.61 1.98 17.46 2.18 17.18 2.67 16.57T3.39 15.63Q3.03 14.96 2.84 14.3L0.39 13.94Q0.21 13.92 0.11 13.77T0 13.46V10.49Q0 10.33 0.11 10.18T0.36 10L2.85 9.63Q3.04 9.01 3.38 8.4 2.84 7.63 1.94 6.55 1.81 6.39 1.81 6.23 1.81 6.09 1.93 5.92 2.28 5.44 3.25 4.48T4.51 3.52Q4.69 3.52 4.86 3.66L6.71 5.09Q7.3 4.78 7.93 4.58 8.14 2.76 8.32 2.09 8.41 1.71 8.8 1.71H11.77Q11.96 1.71 12.1 1.83T12.25 2.12L12.63 4.58Q13.29 4.79 13.83 5.08L15.74 3.64Q15.86 3.52 16.06 3.52 16.23 3.52 16.39 3.66 18.12 5.25 18.6 5.93 18.7 6.04 18.7 6.23 18.7 6.39 18.59 6.54 18.39 6.82 17.91 7.43T17.18 8.37Q17.53 9.04 17.73 9.68L20.18 10.06Q20.36 10.08 20.46 10.23T20.57 10.54Z");
    }
    //</editor-fold>

    //<editor-fold desc="Private constructor" defaultstate="collapsed">

    private GuiHelper() {

    }
    //</editor-fold>

    //<editor-fold desc="EventHandlers" defaultstate="collapsed">
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
    //</editor-fold>

    //<editor-fold desc="TextFields" defaultstate="collapsed">

    public static void showWarning(TextField ctf, String message){
        resetTextfieldColorCss(ctf);
        ImageView iv = new ImageView(new Image(GuiHelper.class.getResource("/gui/images/shield-warning-icon.png").toExternalForm()));
        iv.setPreserveRatio(true);
        iv.setFitHeight(20);
        ctf.getStyleClass().add("warning");
        if (ctf instanceof CustomTextField) {
            ((CustomTextField) ctf).setRight(iv);
        }else if (ctf instanceof CustomPasswordField){
            ((CustomPasswordField) ctf).setRight(iv);
        }
        ctf.setTooltip(new Tooltip(message));
    }

    public static void showError(TextField ctf, String message) {
        resetTextfieldColorCss(ctf);
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
        resetTextfieldColorCss(ctf);
        ctf.setTooltip(null);
    }

    private static void resetTextfieldColorCss(TextField tf){
        tf.getStyleClass().removeAll("error", "warning");
    }
    //</editor-fold>

    //<editor-fold desc="TextFields" defaultstate="collapsed">
    public static String getSVGContent(String key){
        return svgContent.get(key);
    }
    //</editor-fold>

    //<editor-fold desc="MethodBuilder methods" defaultstate="collapsed">

    public static MethodBuilder createMethodBuilder(Object source) {
        return new MethodBuilder(source);
    }
    //</editor-fold>
}
//<editor-fold desc="MethodBuilder class" defaultstate="collapsed">

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
//</editor-fold>
