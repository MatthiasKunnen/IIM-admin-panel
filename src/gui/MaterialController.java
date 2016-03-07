package gui;

import domain.Curricular;
import domain.DomainController;
import domain.Firm;
import domain.Material;
import domain.MaterialIdentifier;
import domain.TargetGroup;
import domain.Visibility;
import exceptions.AzureException;
import exceptions.InvalidPriceException;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import static gui.GuiHelper.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * FXML Controller class
 *
 * @author Evert
 */
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
    private TableColumn<MaterialIdentifier, Visibility> tcAvailable;
    @FXML
    private TableColumn<MaterialIdentifier, Boolean> tcActions;
    @FXML
    private TableColumn<MaterialIdentifier, String> tcLocation;
    @FXML
    private TableColumn<MaterialIdentifier, Integer> tcId;
    @FXML
    private TableColumn<MaterialIdentifier, Integer> tcAmount;
    @FXML
    private TableView<MaterialIdentifier> tvIdentifiers;

    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private Stage theStage;
    private DomainController dc;
    private Material material;
    private SimpleObjectProperty<Visibility> defaultVisibility;
    private ObservableList<MaterialIdentifier> identifiers;
    private List<MaterialIdentifier> identifiersMaterial;

    private Path imagePath;
    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">
    public MaterialController(DomainController dc, Stage stage) {
        this(dc, stage, new Material(""));
    }

    public MaterialController(DomainController dc, Stage stage, Material material) {
        this.identifiers = FXCollections.observableArrayList();
        this.identifiersMaterial = material.getIdentifiers();
        this.theStage = stage;
        this.dc = dc;
        this.defaultVisibility = new SimpleObjectProperty<>();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Material.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
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
//        System.out.println(this.identifiersMaterial);
//        System.out.println("zonder duplicates: ");
//        System.out.println(removeDuplicates(this.identifiersMaterial));
      this.tvIdentifiers.setItems(removeDuplicates(this.identifiersMaterial));
        
        
        tcId.setVisible(false);
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("place"));
        tcLocation.setCellFactory(TextFieldTableCell.<MaterialIdentifier>forTableColumn());
        tcLocation.setOnEditCommit(event -> event.getRowValue().setPlace(event.getNewValue()));
        
        tcAvailable.setVisible(false);
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
        tcActions.setCellValueFactory(new PropertyValueFactory<>("NONEXISTENT"));
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

        setMaterial(material);

        Platform.runLater(() -> theStage.setMinWidth(theStage.getWidth()));
    }
    //</editor-fold>

    //<editor-fold desc="FXML Actions" defaultstate="collapsed">
    @FXML
    private void saveMaterial(ActionEvent event) {
        boolean abort = false;
        String name = (String) createMethodBuilder(tfName).addMethods("getText", "trim").run();
        if (name != null) {
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
                    .run()
            ).replace(',', '.');
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
        if (abort) return;

        material.setIdentifiers(this.identifiers);
        if (dc.doesMaterialExist(material)) {
            dc.update(material);
        } else {
            material = dc.addMaterial(material);
            theStage.setTitle(String.format("%s - IIM", material.getName()));
        }
        this.identifiers.clear();
        this.identifiers.addAll(material.getIdentifiers());
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
        for (int i = 0; i < Integer.parseInt(tfAmount.getText()); i++) {
            MaterialIdentifier id = new MaterialIdentifier(material, defaultVisibility.getValue());
            id.setPlace(tfLocation.getText());
            material.addIdentifier(id);
            identifiers.add(id);
        }
        this.tfAmount.setText("");
        this.tfLocation.setText("");
        this.tfAmount.requestFocus();
    }
    //</editor-fold>
    
    private ObservableList<MaterialIdentifier> removeDuplicates(List<MaterialIdentifier> listWithDuplicates) {
    /* Set of all attributes seen so far */
    Set<String> attributes = new HashSet<String>();
    /* All confirmed duplicates go in here */
    //List duplicates = new ArrayList<MaterialIdentifier>(listWithDuplicates);
    ObservableList<MaterialIdentifier> noDuplicates  = FXCollections.observableArrayList();
    for(MaterialIdentifier x : listWithDuplicates) {
        if(!attributes.contains(x.getPlace())) {
            noDuplicates.add(x);
        }
        attributes.add(x.getPlace());
    }
    /* Clean list without any dups */
    //listWithDuplicates.removeAll(duplicates);
    return noDuplicates;
//    HashSet<MaterialIdentifier> set = new HashSet<MaterialIdentifier>();
//    Map<String, Integer> uniqueIdent = new HashMap<>();
//    int value;
//        for (MaterialIdentifier arrayElement : listWithDuplicates)
//        {
//            if(!uniqueIdent.containsKey(arrayElement.getPlace()))
//            {
//                value= uniqueIdent.get(arrayElement.getPlace())==null?0: uniqueIdent.get(arrayElement.getPlace());
//                uniqueIdent.put(arrayElement.getPlace(), value+1);
//            }
//        }
    }
}
