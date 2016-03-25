package gui.controls.managed.textfield;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import java.util.function.Function;
import java.util.function.Predicate;

public class ValidatedFieldBuilder<E> {
    private final ValidatedFieldInterface<E> ctf;

    public ValidatedFieldBuilder(Class tClass) {
        this(tClass, null);
    }

    public ValidatedFieldBuilder(Class tClass, String promptText) {
        if (tClass.equals(CustomPasswordField.class)) {
            ctf = new ValidatedCustomPasswordField<>();
        } else if (tClass.equals(CustomTextField.class)) {
            ctf = new ValidatedCustomTextField<>();
        } else {
            throw new AssertionError("Type of ValidatedField does not have an associated class.");
        }
        ctf.getNode().setPromptText(promptText);
    }

    /**
     * Adds a validation to the control that will warn the user upon submission if the predicate evaluates to true.
     *
     * @param p       predicate to test.
     * @param warning the warning message to display when the predicate evaluates to true.
     * @return this.
     */
    public ValidatedFieldBuilder<E> addWarningPredicate(Predicate<String> p, String warning) {
        ctf.getValidationManager().addWarningPredicate(p, warning);
        return this;
    }

    /**
     * Adds a validation to the control that will stop submission if the predicate evaluates to true.
     *
     * @param p     predicate to test.
     * @param error the error message to display when the predicate evaluates to true.
     * @return this.
     */
    public ValidatedFieldBuilder<E> addErrorPredicate(Predicate<String> p, String error) {
        ctf.getValidationManager().addErrorPredicate(p, error);
        return this;
    }

    public ValidatedFieldBuilder<E> setConverter(Function<E, String> converter) {
        ctf.getValidationManager().setConverter(converter);
        return this;
    }

    public ValidatedFieldBuilder<E> setPromptText(String promptText) {
        ctf.getNode().setPromptText(promptText);
        return this;
    }

    public ValidatedFieldBuilder<E> setDisabled(boolean disabled) {
        ctf.getNode().setDisable(disabled);
        return this;
    }

    public ValidatedFieldBuilder<E> setOnAction(EventHandler<ActionEvent> actionEvent) {
        ctf.getNode().setOnAction(actionEvent);
        return this;
    }

    public ValidatedFieldInterface<E> build() {
        return ctf;
    }
}
