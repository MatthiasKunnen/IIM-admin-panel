package gui;

import domain.User;
import gui.controls.GuiHelper;
import gui.controls.searchfield.SearchField;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static gui.controls.GuiHelper.openUserDetails;

public class UserPickerController extends VBox {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">
    @FXML
    private SearchField tfEmail, tfName, tfTelNumber;

    @FXML
    private TableView<User> tvUsers;

    @FXML
    private TableColumn<User, Hyperlink> tcFirstName;

    @FXML
    private TableColumn<User, String> tcLastName, tcEamil, tcTelNumber;

    @FXML
    private Button btnSelectAndExit, btnCancel;
    //</editor-fold>

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private final Stage stage;
    private final ReadOnlyObjectWrapper<User> selectedUser;
    private final ReadOnlyObjectWrapper<Status> status;

    public enum Status {
        OPEN, CANCELLED, CLOSED
    }
    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">

    public UserPickerController(Stage theStage, ObservableList<User> users) {
        this.stage = theStage;
        this.selectedUser = new ReadOnlyObjectWrapper<>();
        this.status = new ReadOnlyObjectWrapper<>(Status.OPEN);

        theStage.setOnCloseRequest(event -> {
            if (status.get().equals(Status.OPEN)) {
                status.set(Status.CANCELLED);
            }
        });

        theStage.setTitle("Gebruikers - IIM");

        try {
            GuiHelper.loadFXML("UserPickerScene.fxml", this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        tcFirstName.setCellValueFactory(param -> {
            Hyperlink h = new Hyperlink(param.getValue().getFirstName());
            h.setOnAction(event -> openUserDetails(param.getValue()));
            return new SimpleObjectProperty<>(h);
        });
        tcFirstName.setComparator((o1, o2) -> o1.getText().compareTo(o2.getText()));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcEamil.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcTelNumber.setCellValueFactory(new PropertyValueFactory<>("telNumber"));

        FilteredList<User> filteredList = new FilteredList<>(users);
        SortedList<User> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tvUsers.comparatorProperty());

        this.tvUsers.setItems(sortedList);

        ChangeListener<String> searchEvent = (observable, oldValue, newValue) -> filteredList.setPredicate(user ->
                (tfEmail.getText().isEmpty() || user.getEmail().toLowerCase().contains(tfEmail.getText().toLowerCase())) &&
                        (tfName.getText().isEmpty() || user.getFullName().toLowerCase().contains(tfName.getText().toLowerCase())) &&
                        (tfTelNumber.getText().isEmpty() ||
                                (tfTelNumber.getText().startsWith("+") && user.getTelNumber().startsWith(tfTelNumber.getText())) ||
                                (!tfTelNumber.getText().startsWith("+") && user.getTelNumber().contains(tfTelNumber.getText()))));

        this.tfEmail.textProperty().addListener(searchEvent);
        this.tfName.textProperty().addListener(searchEvent);
        this.tfTelNumber.textProperty().addListener(searchEvent);

        this.tvUsers.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                selectAndExit();
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    public ReadOnlyObjectProperty<User> selectedUserProperty() {
        return this.selectedUser.getReadOnlyProperty();
    }

    public ReadOnlyObjectProperty<Status> statusProperty() {
        return this.status.getReadOnlyProperty();
    }
    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">

    private void selectAndExit() {
        selectedUser.set(tvUsers.getSelectionModel().getSelectedItem());
        status.set(Status.CLOSED);
        stage.close();
    }
    //</editor-fold>

    //<editor-fold desc="FXML actions" defaultstate="collapsed">

    @FXML
    private void btnSelectAndExitClicked(ActionEvent event) {
        selectAndExit();
    }

    @FXML
    private void btnCancelClicked(ActionEvent event) {
        stage.close();
    }
    //</editor-fold>
}
