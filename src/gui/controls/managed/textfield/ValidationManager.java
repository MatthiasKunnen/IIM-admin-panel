package gui.controls.managed.textfield;

import gui.controls.GuiHelper;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValidationManager<E> {
    private final TextField textField;
    private final List<Validation> warnings, errors;
    private Function<E, String> converter;

    public ValidationManager(TextField textField) {
        this.textField = textField;
        this.warnings = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    public void addWarningPredicate(Predicate<String> p, String message) {
        warnings.add(new Validation(p, message));
    }

    public void addErrorPredicate(Predicate<String> p, String message) {
        errors.add(new Validation(p, message));
    }

    public void setConverter(Function<E, String> converter) {
        this.converter = converter;
    }

    public String convert(E e) {
        return hasConverter() ? null : this.converter.apply(e);
    }

    public boolean hasConverter() {
        return this.converter == null;
    }

    public boolean validate() {
        GuiHelper.hideError(textField);
        for (Validation v : errors) {
            if (v.getPredicate().test(textField.getText())) {
                GuiHelper.showError(textField, v.getMessage());
                return false;
            }
        }
        Optional<Validation> validation = warnings.stream()
                .filter(v -> v.getPredicate().test(textField.getText()))
                .findFirst();
        if (validation.isPresent())
            GuiHelper.showWarning(textField, validation.get().getMessage());
        return true;
    }
}

class Validation {
    private Predicate<String> p;
    private String message;

    public Validation(Predicate<String> p, String message) {
        this.p = p;
        this.message = message;
    }

    public Predicate<String> getPredicate() {
        return p;
    }

    public String getMessage() {
        return message;
    }
}
