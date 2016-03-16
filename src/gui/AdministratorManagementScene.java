package gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Administrator;
import domain.DomainController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.IOException;
import java.util.Iterator;

public class AdministratorManagementScene extends HBox {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">

    @FXML
    private ListView<Administrator> lvAdministrators;

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

    @FXML
    private VBox vbCurrentAdministratorProperties;

    @FXML
    private CheckBox cbIsActive;
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
    private ValidatedFieldInterface<Administrator> tfUsername, tfPassword, tfPasswordRepeat;
    private Administrator selectedAdministrator;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public AdministratorManagementScene(DomainController dc, Stage stage) {
        this.dc = dc;
        this.stage = stage;

        try {
            GuiHelper.loadFXML("AdministratorManagementScene.fxml", this);
            this.getStylesheets().add("/gui/style/form.css");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        EventHandler<ActionEvent> saveEvent = event -> save();

        this.tfUsername = new ValidatedFieldBuilder<Administrator>(CustomTextField.class)
                .setPromptText("Gebruikersnaam")
                .setConverter(Administrator::getName)
                .setDisabled(true)
                .setOnAction(saveEvent)
                .addErrorPredicate(String::isEmpty, "De naam moet ingevuld worden.")
                .addErrorPredicate(a -> (selectedAdministrator.getName() == null || !selectedAdministrator.getName().equalsIgnoreCase(a)) && this.dc.isUsernameInUse(a), "Deze gebruikersnaam is al in gebruik!")
                .build();
        this.tfPassword = new ValidatedFieldBuilder<Administrator>(CustomPasswordField.class)
                .setPromptText("Wachtwoord")
                .setDisabled(true)
                .setOnAction(saveEvent)
                .addErrorPredicate(String::isEmpty, "Het wachtwoord moet ingevuld worden.")
                .addErrorPredicate(s -> s.length() < 6, "Het wachtwoord moet minimum 6 karakters bevatten.")
                .addWarningPredicate(s -> !s.matches(".*\\d+.*"), "Een wachtwoord zonder nummers is in het algemeen niet veilig.")
                .addWarningPredicate(s -> !s.matches(".*[^a-zA-Z0-9]+.*"), "Een wachtwoord zonder speciale karakters is in het algemeen niet veilig.")
                .build();
        this.tfPasswordRepeat = new ValidatedFieldBuilder<Administrator>(CustomPasswordField.class)
                .setPromptText("Wachtwoord herhalen")
                .setDisabled(true)
                .setOnAction(saveEvent)
                .addErrorPredicate(String::isEmpty, "Het wachtwoord moet ingevuld worden.")
                .addErrorPredicate(s -> !s.equals(this.tfPassword.getNode().getText()), "De ingegeven wachtwoorden komen niet overeen.")
                .build();

        this.vbCurrentAdministratorProperties.getChildren().addAll(this.tfUsername.getNode(), this.tfPassword.getNode(), this.tfPasswordRepeat.getNode());

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
            //Now move all children into a SplitPane
            SplitPane splitPane = new SplitPane();
            splitPane.setStyle("-fx-box-border: transparent;");
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

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void updateSelectedAdministrator(Administrator administrator) {
        this.selectedAdministrator = administrator;
        boolean isNull = administrator == null;
        clearTextFields(tfUsername.getNode(), tfPassword.getNode(), tfPasswordRepeat.getNode());
        if (isNull) {
            this.vbPermissions.getChildren().clear();
            this.tfUsername.getNode().setText("");
        } else {
            this.tfUsername.getNode().setText(administrator.getName());
            updatePermissions(administrator);
        }
        this.btnSave.setDisable(isNull);
        this.tfUsername.getNode().setDisable(isNull);
        this.tfPassword.getNode().setDisable(isNull);
        this.tfPasswordRepeat.getNode().setDisable(isNull);
        this.cbIsActive.setVisible(!isNull);
    }

    private void updatePermissions(Administrator administrator) {
        this.cbIsActive.setSelected(!administrator.isSuspended());
        this.vbPermissions.getChildren().clear();
        for (Administrator.Permission permission : permissionStringBiMap.keySet()) {
            CheckBox checkBox = new SettingsBox(permissionStringBiMap.get(permission), permission);
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

    private void save() {
        /**
         This if will evaluate to true IF:
         The username is valid
         <ul>
         <li>
         The administrator is a new one AND the passwords are correct.
         </li>
         <li>
         The administrator is being edited AND the password fields are empty OR the password fields are non empty AND valid
         </li>
         </ul>
         */
        if (this.tfUsername.getValidationManager().validate() & //If username is valid AND
                ((this.selectedAdministrator.getId() != 0 && //if administrator exists AND
                        (this.tfPassword.getNode().getText() == null || this.tfPassword.getNode().getText().isEmpty()) && //password is empty AND
                        (this.tfPasswordRepeat.getNode().getText() == null || this.tfPasswordRepeat.getNode().getText().isEmpty())) || //password repetition is empty OR
                        (this.tfPassword.getValidationManager().validate() &
                                this.tfPasswordRepeat.getValidationManager().validate()))) {
            selectedAdministrator.getPermissions().clear();
            this.vbPermissions.getChildren().stream()
                    .filter(node -> node instanceof SettingsBox)
                    .map(node -> (SettingsBox) node)
                    .filter(CheckBox::isSelected)
                    .forEach(node -> selectedAdministrator.addPermission(node.getPermission()));
            this.selectedAdministrator.setName(this.tfUsername.getNode().getText());
            if (this.tfPassword.getNode().getText() != null && !this.tfPassword.getNode().getText().isEmpty() &&
                    this.tfPasswordRepeat.getNode().getText() != null && !this.tfPasswordRepeat.getNode().getText().isEmpty()) {
                this.selectedAdministrator.setPassword(this.tfPassword.getNode().getText());
            }
            this.selectedAdministrator.setSuspended(!cbIsActive.isSelected());
            if (selectedAdministrator.getId() == 0) {
                this.dc.addAdministrator(selectedAdministrator);
            } else {
                this.dc.updateAdministrator(selectedAdministrator);
            }
            updateSelectedAdministrator(null);
        }
    }

    private void clearTextFields(TextField... textFields) {
        GuiHelper.hideError(textFields);
        for (TextField tf : textFields) {
            tf.clear();
        }
    }
    //</editor-fold>

    //<editor-fold desc="FXML Actions" defaultstate="collapsed">

    @FXML
    private void btnAddAdministrator_Clicked(ActionEvent event) {
        this.lvAdministrators.getSelectionModel().clearSelection();
        this.selectedAdministrator = new Administrator();
        this.updateSelectedAdministrator(this.selectedAdministrator);
        this.tfUsername.getNode().requestFocus();
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

    class SettingsBox extends CheckBox {
        private final Administrator.Permission permission;

        public SettingsBox(String text, Administrator.Permission permission) {
            super(text);
            this.permission = permission;
        }

        public Administrator.Permission getPermission() {
            return permission;
        }
    }
}

