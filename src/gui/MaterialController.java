package gui;

import domain.*;
import exceptions.AzureException;
import exceptions.InvalidPriceException;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.textfield.CustomTextField;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static gui.GuiHelper.*;
import static java.util.stream.Collectors.groupingBy;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.layout.StackPane;

public class MaterialController extends VBox {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">
    @FXML
    private Pane pnVisibilityPicker;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnAddIdentifier;
    @FXML
    private ComboBox<Firm> cboFirm;
    @FXML
    private CheckComboBox<Curricular> cboCurricular;
    @FXML
    private CheckComboBox<TargetGroup> cboTargetAudience;
    @FXML
    private ImageView ivPhoto;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextArea tfDescription;
    @FXML
    private TextField tfArticleNumber;
    @FXML
    private CustomTextField tfPrice;
    @FXML
    private TextField tfLocation;
    @FXML
    private CustomTextField tfName;
    @FXML
    private Label LbAvailable;
    @FXML
    private Label lblCurricularBox;
    @FXML
    private Label lblTargetAudienceBox;
    @FXML
    private StackPane spTargetAudience;
    @FXML
    private StackPane spCurricular;
    @FXML
    private TableColumn<MaterialIdentifier, Visibility> tcAvailable;
    @FXML
    private TableColumn<MaterialIdentifier, Integer> tcAmount;
    @FXML
    private TableColumn<MaterialIdentifier, Boolean> tcActions;
    @FXML
    private TableColumn<MaterialIdentifier, String> tcLocation;
    @FXML
    private TableColumn<MaterialIdentifier, Integer> tcId;
    @FXML
    private TableView<MaterialIdentifier> tvIdentifiers;
    @FXML
    private TableView<SimplifiedMaterialIdentifier> tvSimplifiedIdentifiers;
    @FXML
    private TableColumn<SimplifiedMaterialIdentifier, String> tcSimplifiedLocation;
    @FXML
    private TableColumn<SimplifiedMaterialIdentifier, Integer> tcForLoan;
    @FXML
    private TableColumn<SimplifiedMaterialIdentifier, Integer> tcNotForLoan;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private Stage theStage;
    private DomainController dc;
    private Material material;
    private SimpleObjectProperty<Visibility> defaultVisibility;
    private ObservableList<MaterialIdentifier> identifiers;
    private ObservableList<SimplifiedMaterialIdentifier> simplifiedIdentifiers;
    private Path imagePath;
    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">
    public MaterialController(DomainController dc, Stage stage) {
        this(dc, stage, new Material(""));
    }

    public MaterialController(DomainController dc, Stage stage, Material material) {
        this.identifiers = FXCollections.observableArrayList(material.getIdentifiers());
        this.simplifiedIdentifiers = FXCollections.observableArrayList();
        this.theStage = stage;
        this.dc = dc;
        this.defaultVisibility = new SimpleObjectProperty<>();

        try {
            GuiHelper.loadFXML("Material.fxml", this);
            this.getStylesheets().add("/gui/style/form.css");
            GuiHelper.getKeyEventEventHandlerAssuringDecimalInput(this.tfPrice);
            GuiHelper.getKeyEventEventHandlerAssuringIntegerInput(this.tfAmount);
            VisibilityPickerController defaultVisibilityPicker = new VisibilityPickerController();
            defaultVisibilityPicker.bindVisibility(defaultVisibility);
            this.pnVisibilityPicker.getChildren().clear();
            this.pnVisibilityPicker.getChildren().add(defaultVisibilityPicker);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if ((boolean) dc.getSettingData(Setting.Key.KEEP_HISTORY, false)) {
            this.tvIdentifiers.setItems(this.identifiers);

            tcId.setCellValueFactory(new PropertyValueFactory<>("id"));

            tcAvailable.setCellValueFactory(new PropertyValueFactory<>("visibility"));
            tcAvailable.setCellFactory(new Callback<TableColumn<MaterialIdentifier, Visibility>, TableCell<MaterialIdentifier, Visibility>>() {
                @Override
                public TableCell<MaterialIdentifier, Visibility> call(TableColumn<MaterialIdentifier, Visibility> param) {
                    return new TableCell<MaterialIdentifier, Visibility>() {
                        private final VisibilityPickerController vpc = new VisibilityPickerController();
                        private SimpleObjectProperty<Visibility> visibilityProperty;

                        @Override
                        protected void updateItem(Visibility item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                setGraphic(vpc);
                                if (getTableRow().getItem() != null) {
                                    SimpleObjectProperty<Visibility> sop = ((MaterialIdentifier) getTableRow().getItem()).getVisibilityProperty();
                                    if (this.visibilityProperty == null) {
                                        this.visibilityProperty = sop;
                                        this.vpc.bindBiDirectional(sop);
                                    }
                                    if (this.visibilityProperty != sop) {
                                        this.vpc.unBindVisibility(visibilityProperty);
                                        this.visibilityProperty = sop;
                                        this.vpc.bindBiDirectional(sop);
                                    }
                                }
                            }
                        }
                    };
                }
            });

            tcActions.setCellFactory(new Callback<TableColumn<MaterialIdentifier, Boolean>, TableCell<MaterialIdentifier, Boolean>>() {

                @Override
                public TableCell<MaterialIdentifier, Boolean> call(TableColumn<MaterialIdentifier, Boolean> param) {
                    return new TableCell<MaterialIdentifier, Boolean>() {
                        private final CustomOptionsController coc = new CustomOptionsController();

                        {
                            coc.addExistingSVG("delete");
                            coc.addExistingSVG("calendar");
                            coc.bind("delete", MouseEvent.MOUSE_CLICKED, event -> {
                                if (getTableRow().getItem() != null) {
                                    getTableView().getItems().remove((MaterialIdentifier) getTableRow().getItem());
                                }
                            });
                            coc.bind("calendar", MouseEvent.MOUSE_CLICKED, event -> {
                                if (getTableRow().getItem() != null) {
                                    //TODO implement history
                                }
                            });
                        }

                        @Override
                        protected void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                setGraphic(coc);
                            }
                        }
                    };
                }
            });

            tcLocation.setCellValueFactory(new PropertyValueFactory<>("place"));
            tcLocation.setCellFactory(TextFieldTableCell.<MaterialIdentifier>forTableColumn());
            tcLocation.setOnEditCommit(event -> event.getRowValue().setPlace(event.getNewValue()));
        } else {
            ((HBox) this.pnVisibilityPicker.getParent()).getChildren().removeAll(pnVisibilityPicker, tfAmount);
            ((AnchorPane) this.tvIdentifiers.getParent()).getChildren().removeAll(this.tvIdentifiers);
            this.tvSimplifiedIdentifiers.setVisible(true);
            this.updateSimplifiedIdentifiersFromIdentifiers();
            this.tvSimplifiedIdentifiers.setItems(this.simplifiedIdentifiers);

            tcSimplifiedLocation.setCellFactory(TextFieldTableCell.<SimplifiedMaterialIdentifier>forTableColumn());
            tcSimplifiedLocation.setCellValueFactory(new PropertyValueFactory<>("place"));
            tcSimplifiedLocation.setOnEditCommit(event -> event.getRowValue().setPlace(event.getNewValue()));

            tcForLoan.setCellFactory(TextFieldTableCell.<SimplifiedMaterialIdentifier, Integer>forTableColumn(new IntegerStringConverter()));
            tcForLoan.setCellValueFactory(new PropertyValueFactory<>("studentAmount"));
            tcForLoan.setOnEditCommit(event -> event.getRowValue().setStudentAmount(event.getNewValue()));

            tcNotForLoan.setCellFactory(TextFieldTableCell.<SimplifiedMaterialIdentifier, Integer>forTableColumn(new IntegerStringConverter()));
            tcNotForLoan.setCellValueFactory(new PropertyValueFactory<>("docentAmount"));
            tcNotForLoan.setOnEditCommit(event -> event.getRowValue().setDocentAmount(event.getNewValue()));
        }

        this.cboCurricular.getItems().addAll(dc.getCurricular());
        this.cboCurricular.setConverter(new StringConverter<Curricular>() {
            @Override
            public String toString(Curricular c) {
                return c.getName();
            }

            @Override
            public Curricular fromString(String string) {
                return dc.getCurricular().stream()
                        .filter(c -> c.getName().equals(string))
                        .findAny()
                        .orElse(null);
            }
        });
        this.spCurricular.prefWidthProperty().bind(this.spTargetAudience.widthProperty());
        this.cboCurricular.getCheckModel().getCheckedItems().addListener((Observable event) ->
                lblCurricularBox.setVisible(cboCurricular.getCheckModel().getCheckedItems().isEmpty()));
        
        this.cboTargetAudience.getItems().addAll(dc.getTargetGroups());
        this.cboTargetAudience.setConverter(new StringConverter<TargetGroup>() {
            @Override
            public String toString(TargetGroup t) {
                return t.getName();
            }

            @Override
            public TargetGroup fromString(String string) {
                return dc.getTargetGroups().stream()
                        .filter(c -> c.getName().equals(string))
                        .findAny()
                        .orElse(null);
            }
        });
        this.spTargetAudience.prefWidthProperty().bind(this.cboFirm.widthProperty());
        this.cboTargetAudience.getCheckModel().getCheckedItems().addListener((Observable event) ->
                lblTargetAudienceBox.setVisible(cboTargetAudience.getCheckModel().getCheckedItems().isEmpty()));
        
        this.cboFirm.setItems(dc.getFirms());
        this.cboFirm.setConverter(new StringConverter<Firm>() {
            @Override
            public String toString(Firm f) {
                return f.getName();
            }

            @Override
            public Firm fromString(String string) {
                return dc.getFirms().stream()
                        .filter(f -> f.getName().equals(string))
                        .findAny()
                        .orElse(null);
            }
        });
        this.cboFirm.prefWidthProperty().bind(this.spCurricular.widthProperty());

        setMaterial(material);

        Platform.runLater(() -> theStage.setMinWidth(theStage.getWidth()));
    }
    //</editor-fold>

    //<editor-fold desc="FXML Actions" defaultstate="collapsed">
    @FXML
    private void saveMaterial(ActionEvent event) {
        boolean abort = false;
        String name = (String) createMethodBuilder(tfName).addMethods("getText", "trim").run();
        if (name != null && !name.isEmpty()) {
            material.setName(name);
            hideError(tfName);
            Material matchingMaterial = dc.getMaterialByName(name);
            if (matchingMaterial != null && matchingMaterial.getId() != material.getId()) {
                showError(tfName, "Materiaal is al in gebruik!");
                abort = true;
            }
        } else {
            showError(tfName, "Naam moet ingevuld zijn!");
            abort = true;
        }

        setMaterialProperty(tfArticleNumber, "setArticleNr");
        setMaterialProperty(tfDescription, "setDescription");

        try {
            String price = ((String) createMethodBuilder(this.tfPrice)
                    .addMethods("getText")
                    .setDefaultValue("")
                    .run()).replace(',', '.');
            material.setPrice(price.equals("") ? null : new BigDecimal(price));
            hideError(this.tfPrice);
        } catch (InvalidPriceException e) {
            switch (e.getPriceInvalidityCause()) {
                case EXCEEDED_PRECISION:
                    showError(this.tfPrice, "De ingevoerde prijs heeft een te grote precisie!");
                    abort = true;
                    break;
                case LOWER_THAN_ZERO:
                    showError(this.tfPrice, "De ingevoerde prijs mag niet kleiner zijn dan 0!");
                    abort = true;
                    break;
                case EXCEEDED_SCALE:
                    showWarning(this.tfPrice, "De ingevoerde prijs heeft meer getallen na de komma dan er opgeslagen worden.");
                    break;
                default:
                    showError(this.tfPrice, "De ingevoerde prijs is niet geldig.");
                    abort = true;
                    break;
            }
        }

        material.setTargetGroups(cboTargetAudience.getCheckModel().getCheckedItems());
        material.setFirm(cboFirm.getValue());
        material.setCurricular(cboCurricular.getCheckModel().getCheckedItems());
        if (abort) {
            return;
        }

        if ((boolean) dc.getSettingData(Setting.Key.KEEP_HISTORY, false)) {
            material.setIdentifiers(this.identifiers);
        } else {
            material.setIdentifiers(this.simplifiedIdentifiers.stream().flatMap(smi -> smi.getIdentifiers().stream()).collect(Collectors.toList()));
        }

        if (dc.doesMaterialExist(material)) {
            dc.update(material);
        } else {
            material = dc.addMaterial(material);
        }
        theStage.setTitle(String.format("%s - IIM", material.getName()));
        this.identifiers.clear();
        this.identifiers.addAll(material.getIdentifiers());
        updateSimplifiedIdentifiersFromIdentifiers();
        try {
            if (imagePath != null) {
                dc.updatePhoto(material, imagePath.toString());
            }
        } catch (AzureException ex) {
            //warning
        }
    }

    @FXML
    private void selectImage(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif", "*.webp", "*.tiff", "*.tif"));
        fc.setTitle("Open file");
        File f = fc.showOpenDialog(theStage);

        if (f == null) {
            return;
        }
        try {
            imagePath = f.toPath();
            double widthBefore = ivPhoto.getBoundsInParent().getWidth();
            ivPhoto.setImage(new Image(f.toURI().toURL().toString()));
            double widthDiff = ivPhoto.getBoundsInParent().getWidth() - widthBefore;
            theStage.setWidth(theStage.getWidth() + widthDiff);
            Platform.runLater(() -> theStage.setMinWidth(theStage.getMinWidth() + widthDiff));
        } catch (InvalidPathException | MalformedURLException ignored) {
        }
    }

    @FXML
    void addIdentifier(MouseEvent event) {
        addIdentifier();
    }

    @FXML
    void tfLocationAction(ActionEvent event) {
        addIdentifier();
    }

    @FXML
    void cancel(ActionEvent event) {
        theStage.close();
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    private void updateSimplifiedIdentifiersFromIdentifiers() {
        simplifiedIdentifiers.clear();
        identifiers.stream()
                .collect(groupingBy(MaterialIdentifier::getPlace))
                .forEach((place, materialIdentifiers) -> simplifiedIdentifiers.add(new SimplifiedMaterialIdentifier(place, materialIdentifiers)));
    }

    private void setMaterialProperty(TextInputControl input, String setter) {
        createMethodBuilder(input)
                .addMethods("getText", "trim")
                .setTarget(material, setter)
                .run();
    }

    private void setMaterial(Material material) {
        if (material == null || !dc.doesMaterialExist(material)) {
            this.material = new Material("");
        } else {
            this.material = material;
            this.tfName.setText(material.getName());
            createMethodBuilder(this.material)
                    .addMethods("getPrice", "toString")
                    .addMethod("replaceAll", "\\.", ",")
                    .setTarget(this.tfPrice, "setText")
                    .run();
            tfDescription.setText(this.material.getDescription());
            tfArticleNumber.setText(this.material.getArticleNr());
            String photoUrl = this.material.getPhotoUrl();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                ivPhoto.setImage(new Image(photoUrl));
            }
            cboCurricular.getItems().stream().filter(c -> material.getCurricular().contains(c)).forEach(c -> cboCurricular.getCheckModel().check(c));
            cboTargetAudience.getItems().stream().filter(t -> material.getTargetGroups().contains(t)).forEach(t -> cboTargetAudience.getCheckModel().check(t));
            this.cboFirm.getSelectionModel().select(material.getFirm());
            this.identifiers.addAll(this.material.getIdentifiers());
        }
    }

    private void addIdentifier() {
        if ((boolean) dc.getSettingData(Setting.Key.KEEP_HISTORY, false)) {
            for (int i = 0; i < Integer.parseInt(tfAmount.getText()); i++) {
                MaterialIdentifier id = new MaterialIdentifier(material, defaultVisibility.getValue());
                id.setPlace(tfLocation.getText());
                material.addIdentifier(id);
                identifiers.add(id);
            }
        } else {
            simplifiedIdentifiers.add(new SimplifiedMaterialIdentifier(this.tfLocation.getText()));
        }
        this.tfAmount.setText("");
        this.tfLocation.setText("");
        this.tfAmount.requestFocus();
    }
    //</editor-fold>

    public class SimplifiedMaterialIdentifier {

        private ObservableList<MaterialIdentifier> identifiers;
        private SimpleStringProperty place;
        private SimpleIntegerProperty studentAmount, docentAmount;
        private final Comparator<MaterialIdentifier> miComparator = (o1, o2) -> Integer.compare(o1.getId(), o2.getId());
        private boolean isUpdating = false;

        public SimplifiedMaterialIdentifier(String place) {
            this(place, new ArrayList<>());
        }

        public SimplifiedMaterialIdentifier(String place, List<MaterialIdentifier> identifiers) {
            this.identifiers = FXCollections.observableArrayList(identifiers);
            this.studentAmount = new SimpleIntegerProperty(getStudentCount());
            this.docentAmount = new SimpleIntegerProperty(getDocentCount());
            this.place = new SimpleStringProperty(place);

            this.identifiers.addListener((ListChangeListener<MaterialIdentifier>) c -> {
                if (!isUpdating) {
                    studentAmount.setValue(getStudentCount());
                    docentAmount.setValue(getDocentAmount());
                }
            });

            this.studentAmount.addListener((observable, oldValue, newValue) -> change(oldValue.intValue(), newValue.intValue(), Visibility.Student));
            this.docentAmount.addListener((observable, oldValue, newValue) -> change(oldValue.intValue(), newValue.intValue(), Visibility.Docent));
        }

        private void change(int oldValue, int newValue, Visibility visibility) {
            isUpdating = true;
            if (oldValue > newValue) {
                for (int i = oldValue - newValue; i > 0; i--) {
                    identifiers.remove(identifiers.stream()
                            .filter(mi -> mi.getVisibility().equals(visibility))
                            .max(miComparator)
                            .get());
                }
            } else {
                for (int i = newValue - oldValue; i > 0; i--) {
                    addIdentifier(new MaterialIdentifier(material, visibility));
                }
            }
            isUpdating = false;
        }

        private int getStudentCount() {
            return (int) identifiers.stream().filter(mi -> mi.getVisibility().equals(Visibility.Student)).count();
        }

        private int getDocentCount() {
            return (int) identifiers.stream().filter(mi -> mi.getVisibility().equals(Visibility.Docent)).count();
        }

        public ObservableList<MaterialIdentifier> getIdentifiers() {
            return identifiers;
        }

        public void addIdentifier(MaterialIdentifier identifier) {
            this.identifiers.add(identifier);
            identifier.getPlaceProperty().bind(place);
        }

        public void removeIdentifier(MaterialIdentifier identifier) {
            this.identifiers.remove(identifier);
        }

        public String getPlace() {
            return place.get();
        }

        public SimpleStringProperty placeProperty() {
            return place;
        }

        public void setPlace(String place) {
            this.place.set(place);
        }

        public int getStudentAmount() {
            return studentAmount.get();
        }

        public SimpleIntegerProperty studentAmountProperty() {
            return studentAmount;
        }

        public void setStudentAmount(int studentAmount) {
            this.studentAmount.set(studentAmount);
        }

        public int getDocentAmount() {
            return docentAmount.get();
        }

        public SimpleIntegerProperty docentAmountProperty() {
            return docentAmount;
        }

        public void setDocentAmount(int docentAmount) {
            this.docentAmount.set(docentAmount);
        }
    }
}
