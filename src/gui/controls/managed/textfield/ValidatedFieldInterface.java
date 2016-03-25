package gui.controls.managed.textfield;

import javafx.scene.control.TextField;

public interface ValidatedFieldInterface<E> {
    ValidationManager<E> getValidationManager();

    TextField getNode();

    void updateItem(E item);
}
