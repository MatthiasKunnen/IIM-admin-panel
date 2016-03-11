package gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Administrator;
import domain.DomainController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.util.Iterator;

public class AdministratorManagementScene extends HBox {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">

    @FXML
    private ListView<Administrator> lvAdministrators;

    @FXML
    private CustomTextField ctfUsername;

    @FXML
    private CustomPasswordField cpfPassword;

    @FXML
    private CustomPasswordField cpfPasswordRepeat;

    @FXML
    private HBox hbOptions;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnAddAdministrator;

    @FXML
    private VBox vbPermissions;

    @FXML
    private VBox vbPermissionsWrapper;

    @FXML
    private VBox vbAdministratorsWrapper;
    //</editor-fold>

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private final ObservableList<Administrator> administratorObservableList;
    private final DomainController dc;
    private final Stage stage;
    private final BiMap<Administrator.Permission, String> permissionStringBiMap = ImmutableBiMap.of(
            Administrator.Permission.MANAGE_MATERIALS, "Materialen beheren (toevoegen/wijzigen/verwijderen)",
            Administrator.Permission.MANAGE_USERS, "Administratoren beheren",
            Administrator.Permission.MANAGE_RESERVATIONS, "Reservaties beheren"
    );
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public AdministratorManagementScene(DomainController dc, Stage stage) {
        this.dc = dc;
        this.stage = stage;

        try {
            GuiHelper.loadFXML("AdministratorManagementScene.fxml", this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (this.dc.hasPermission(Administrator.Permission.MANAGE_USERS)) {
            this.administratorObservableList = this.dc.getAdministrators();
            this.lvAdministrators.setItems(administratorObservableList);
            this.lvAdministrators.setCellFactory(param -> new ListCell<Administrator>() {
                @Override
                protected void updateItem(Administrator item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.getName());
                    }
                }
            });
            this.lvAdministrators.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateSelectedAdministrator(newValue));
            SplitPane splitPane = new SplitPane();
            for (Iterator<Node> iterator = getChildren().iterator(); iterator.hasNext(); ) {
                Node node = iterator.next();
                splitPane.getItems().add(node);
                iterator.remove();
            }
            ObservableList<SplitPane.Divider> dividers = splitPane.getDividers();
            for (int i = 0; i < dividers.size(); i++) {
                dividers.get(i).setPosition((i + 1.0) / splitPane.getItems().size());
            }
            getChildren().add(splitPane);
            setHgrow(splitPane, Priority.ALWAYS);
        } else {
            this.administratorObservableList = null;
            getChildren().removeAll(this.vbAdministratorsWrapper, this.vbPermissionsWrapper);
            this.hbOptions.getChildren().remove(this.btnAddAdministrator);
            updateSelectedAdministrator(this.dc.getActiveAdministrator());
        }

        Platform.runLater(() -> {
            stage.setMinWidth(getWidth());
            stage.setMinHeight(getHeight());
        });
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">

    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void updateSelectedAdministrator(Administrator administrator) {
        if (administrator == null) {
            this.vbPermissions.getChildren().clear();
            this.ctfUsername.setText("");
        } else {
            this.ctfUsername.setText(administrator.getName());
            updatePermissions(administrator);
        }
        this.btnSave.setDisable(administrator == null);
    }

    private void updatePermissions(Administrator administrator) {
        this.vbPermissions.getChildren().clear();
        for (Administrator.Permission permission : permissionStringBiMap.keySet()) {
            CheckBox checkBox = new CheckBox(permissionStringBiMap.get(permission));
            checkBox.setSelected(administrator.hasPermission(permission));
            checkBox.setTooltip(new Tooltip(permissionStringBiMap.get(permission)));
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    administrator.removePermission(permission);
                } else {
                    administrator.addPermission(permission);
                }
            });
            this.vbPermissions.getChildren().add(checkBox);
        }
    }
    //</editor-fold>

    //<editor-fold desc="FXML Actions" defaultstate="collapsed">

    @FXML
    private void btnAddAdministrator_Clicked(ActionEvent event) {

    }

    @FXML
    private void btnSave_Clicked(ActionEvent event) {
        save();
    }

    @FXML
    private void propertyfields_onAction(ActionEvent event) {
        save();
    }
    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void save() {

    }

    //</editor-fold>
}

