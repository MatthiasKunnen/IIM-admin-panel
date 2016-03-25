package gui.controls.managed.listview;

import gui.controls.GuiHelper;
import gui.controls.managed.textfield.ValidatedFieldInterface;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;

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
    private Map<String, ValidatedFieldInterface<E>> managedCustomTextFields = new HashMap<>();
    private Predicate<E> saveEvent;
    private BooleanSupplier addEvent;

    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    /**
     * Creates a new TempController.
     *
     * @param title              the text to display on top of the control.
     * @param stringPresentation a {@link Function} defining how {@link E} is displayed in the {@link ListView}.
     * @param items              the items to add to the {@link ListView}.
     */
    public TempController(String title, Function<E, String> stringPresentation, ObservableList<E> items) {
        try {
            GuiHelper.loadFXML("Temp.fxml", this);
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
                if (empty) {
                    setText(null);
                    setGraphic(null);
                }else{
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
                this.managedCustomTextFields.values().forEach(tf -> tf.updateItem(newValue));
            }
        });
    }

    //</editor-fold>

    //<editor-fold desc="Public actions" defaultstate="collapsed">

    public void addManagedCustomTextField(String key, ValidatedFieldInterface<E> managedCustomTextField) {
        managedCustomTextField.getNode().setOnAction(this::save);
        this.managedCustomTextFields.put(key, managedCustomTextField);
        this.vbNodes.getChildren().add(managedCustomTextField.getNode());
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
        return managedCustomTextFields.get(key).getNode().getText();
    }

    public DoubleProperty getListViewMaxHeightProperty() {
        return this.lvItems.maxHeightProperty();
    }

    public ReadOnlyDoubleProperty getListViewHeightProperty() {
        return this.lvItems.heightProperty();
    }

    public boolean statusIsSaving() {
        return !this.lvItems.getSelectionModel().isEmpty();
    }

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void clear() {
        managedCustomTextFields.values().forEach(mtf -> mtf.getNode().clear());
        this.lvItems.getSelectionModel().clearSelection();
        this.managedCustomTextFields.values().stream().forEach(tf -> GuiHelper.hideError(tf.getNode()));
    }

    //</editor-fold>

    //<editor-fold desc="FXML actions" defaultstate="collapsed">
    @FXML
    private void save(ActionEvent event) {
        boolean exit = false;
        for (ValidatedFieldInterface<E> tf : this.managedCustomTextFields.values()) {
            if (!tf.getValidationManager().validate())
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


