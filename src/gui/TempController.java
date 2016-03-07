package gui;

import java.io.IOException;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.CustomTextField;

public class TempController<E> extends AnchorPane {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">

    @FXML
    private Label lblTitel;
    @FXML
    private VBox vbNodes;
    @FXML
    private ListView<E> lvItems;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private Map<String, ManagedCustomTextField<E>> managedCustomTextFields = new HashMap<>();
    private Predicate<E> saveEvent;
    private BooleanSupplier addEvent;

    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public TempController(String title, Function<E, String> stringPresentation, ObservableList<E> items) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Temp.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            this.getStylesheets().add("/gui/style/form.css");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.lvItems.setItems(items);
        this.lblTitel.setText(title);
        this.lvItems.setCellFactory(param -> new ListCell<E>() {
            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(stringPresentation.apply(item));
                }
            }
        });
        this.lvItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean b = newValue == null;
            btnCancel.setDisable(b);
            btnCancel.setVisible(!b);
            btnDelete.setDisable(b);
            btnDelete.setVisible(!b);
            if (!b) { //Something is selected
                for (ManagedCustomTextField<E> tf : this.managedCustomTextFields.values()) {
                    tf.setText(tf.convert(newValue));
                }
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="Public actions" defaultstate="collapsed">
    public void addManagedCustomTextField(String key, ManagedCustomTextField<E> managedCustomTextField) {
        this.managedCustomTextFields.put(key, managedCustomTextField);
        this.vbNodes.getChildren().add(managedCustomTextField);
    }

    public void setOnDelete(Predicate<E> deleteEvent) {
        btnDelete.setOnAction(event -> {
            if (deleteEvent.test(lvItems.getSelectionModel().getSelectedItem()))
                clear();
        });
    }

    public void setOnSave(Predicate<E> saveEvent) {
        this.saveEvent = saveEvent;
    }

    public void setOnAdd(BooleanSupplier addEvent) {
        this.addEvent = addEvent;
    }

    public String getValue(String key) {
        return managedCustomTextFields.get(key).getText();
    }

    public DoubleProperty getListViewMaxHeightProperty() {
        return this.lvItems.maxHeightProperty();
    }

    public ReadOnlyDoubleProperty getListViewHeightProperty() {
        return this.lvItems.heightProperty();
    }

    public boolean statusIsSaving(){
        return !this.lvItems.getSelectionModel().isEmpty();
    }

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void clear() {
        managedCustomTextFields.values().forEach(TextInputControl::clear);
        this.lvItems.getSelectionModel().clearSelection();
    }

    //</editor-fold>

    //<editor-fold desc="FXML actions" defaultstate="collapsed">
    @FXML
    private void save(ActionEvent event) {
        boolean exit = false;
        for (ManagedCustomTextField tf : this.managedCustomTextFields.values()) {
            if (!tf.validate())
                exit = true;
        }
        if (exit) return;
        if (this.lvItems.getSelectionModel().isEmpty() ?
                addEvent == null || addEvent.getAsBoolean() :
                saveEvent == null || saveEvent.test(this.lvItems.getSelectionModel().getSelectedItem()))
            clear();
    }

    @FXML
    private void cancel(ActionEvent event) {
        clear();
    }
    //</editor-fold>
}

//<editor-fold desc="Classes" defaultstate="collapsed">
class ManagedCustomTextFieldBuilder<E> {
    private ManagedCustomTextField<E> ctf = new ManagedCustomTextField<>();

    public ManagedCustomTextFieldBuilder<E> addWarningPredicate(Predicate<String> p, String warning) {
        ctf.addWarningPredicate(p, warning);
        return this;
    }

    public ManagedCustomTextFieldBuilder<E> addErrorPredicate(Predicate<String> p, String error) {
        ctf.addErrorPredicate(p, error);
        return this;
    }

    public ManagedCustomTextFieldBuilder<E> setPromptText(String text) {
        ctf.setPromptText(text);
        return this;
    }

    public ManagedCustomTextFieldBuilder<E> setConverter(Function<E, String> converter) {
        ctf.setConverter(converter);
        return this;
    }

    public ManagedCustomTextField<E> get() {
        return ctf;
    }
}

class ManagedCustomTextField<E> extends CustomTextField {
    private List<Validation> warnings, errors;
    private Function<E, String> converter;

    public ManagedCustomTextField() {
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
        GuiHelper.hideError(this);
        for (Validation v : errors) {
            if (!v.getPredicate().test(getText())) {
                GuiHelper.showError(this, v.getMessage());
                return false;
            }
        }
        Optional<Validation> validation = warnings.stream()
                .filter(v -> !v.getPredicate().test(getText()))
                .findAny();
        if (validation.isPresent())
            GuiHelper.showWarning(this, validation.get().getMessage());
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

//</editor-fold>
