package gui.controls.managed.textfield;

import gui.controls.GuiHelper;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;

public class ValidatedCustomTextField<E> extends CustomTextField implements ValidatedFieldInterface<E> {
    private final ValidationManager<E> validationManager;

    public ValidatedCustomTextField() {
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
        GuiHelper.hideError(this);
    }
}
