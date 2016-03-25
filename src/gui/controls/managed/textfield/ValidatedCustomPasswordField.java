package gui.controls.managed.textfield;

import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomPasswordField;

public class ValidatedCustomPasswordField<E> extends CustomPasswordField implements ValidatedFieldInterface<E> {
    private final ValidationManager<E> validationManager;

    public ValidatedCustomPasswordField() {
        this.validationManager = new ValidationManager<>(this);
    }

    @Override
    public ValidationManager<E> getValidationManager() {
        return this.validationManager;
    }

    @Override
    public TextField getNode() {
        return this;
    }

    @Override
    public void updateItem(E item) {
        this.setText(getValidationManager().convert(item));
        getValidationManager().validate();
    }
}
