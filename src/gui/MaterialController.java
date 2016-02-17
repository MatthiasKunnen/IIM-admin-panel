/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Material;
import domain.MaterialIdentifier;
import domain.Visibility;
import exceptions.AzureException;
import exceptions.InvalidPriceException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import javafx.event.Event;
import javafx.event.EventHandler;

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
    private ComboBox cboFirm;
    @FXML
    private ComboBox cboCurricular;
    @FXML
    private ComboBox cboTargetAudience;
    @FXML
    private ImageView ivPhoto;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextArea tfDescription;
    @FXML
    private TextField tfArticleNumber;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfLocation;
    @FXML
    private TextField tfName;
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
    private TableView<MaterialIdentifier> tvIdentifiers;

    //</editor-fold>
    //<editor-fold desc="Variables" defaultstate="collapsed">
    private Stage theStage;
    private DomainController dc;
    private Material material;
    private SimpleObjectProperty<Visibility> defaultVisibility;
    private ObservableList<MaterialIdentifier> identifiers;
    private Path imagePath;
    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">
    public MaterialController(DomainController dc, Stage stage) {
        this(dc, stage, new Material(""));
    }

    public MaterialController(DomainController dc, Stage stage, Material material) {
        this.material = material;
        this.theStage = stage;
        this.dc = dc;
        this.defaultVisibility = new SimpleObjectProperty<>();
        this.identifiers = FXCollections.observableArrayList();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Material.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            GuiHelper.getKeyEventEventHandlerAssuringDecimalInput(this.tfPrice);
            GuiHelper.getKeyEventEventHandlerAssuringIntegerInput(this.tfAmount);
            VisibilityPickerController defaultVisibilityPicker = new VisibilityPickerController();
            defaultVisibilityPicker.bindVisibility(defaultVisibility);
            this.pnVisibilityPicker.getChildren().clear();
            this.pnVisibilityPicker.getChildren().add(defaultVisibilityPicker);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ivPhoto.setImage(new Image(getClass().getResource("/gui/images/picture-add.png").toExternalForm()));

        this.identifiers.addAll(material.getIdentifiers());
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("place"));
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
        tcActions.setCellValueFactory(new PropertyValueFactory<>("UNEXISTING"));
        tcActions.setCellFactory(new Callback<TableColumn<MaterialIdentifier, Boolean>, TableCell<MaterialIdentifier, Boolean>>() {

            @Override
            public TableCell<MaterialIdentifier, Boolean> call(TableColumn<MaterialIdentifier, Boolean> param) {
                return new TableCell<MaterialIdentifier, Boolean>() {
                    private final IdentifierOptionsController ioc = new IdentifierOptionsController();

                    @Override
                    protected void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setGraphic(ioc);
                            EventHandler filter = new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    MaterialIdentifier mi = (MaterialIdentifier) getTableRow().getItem();
                                    identifiers.remove(mi);
                                    material.removeIdentifier(mi);
                                }

                            };

                            ioc.getNodeByName("delete").addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
                        }

                    }

                };
            }
        });

        tvIdentifiers.setItems(this.identifiers);

    }
    //</editor-fold>

    //<editor-fold desc="FXML Actions" defaultstate="collapsed">
    @FXML
    private void saveMaterial(ActionEvent event) {
        if (!tfName.getText().trim().isEmpty()) {
            material.setName(tfName.getText().trim());
        }
        if (!tfArticleNumber.getText().trim().isEmpty()) {
            material.setArticleNr(tfArticleNumber.getText().trim());
        }

        if (!tfDescription.getText().trim().isEmpty()) {
            material.setDescription(tfDescription.getText().trim());
        }

        if (!tfPrice.getText().isEmpty()) {
            try {
                BigDecimal price = new BigDecimal(tfPrice.getText().replace(",", "."));
                material.setPrice(price);
            } catch (NumberFormatException | InvalidPriceException e) {
                return;
            }
        }
        //firma
        //doelgroep
        //leeftijdscathegorie
        if (dc.doesMaterialExist(material)) {
            dc.update(material);
        } else {
            material = dc.addMaterial(material);
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
            ivPhoto.setImage(new Image(f.toURI().toURL().toString()));
        } catch (InvalidPathException | MalformedURLException ex) {

        }
    }

    @FXML
    void addIdentifier(ActionEvent event) {
        for (int i = 0; i < Integer.parseInt(tfAmount.getText()); i++) {
            MaterialIdentifier id = new MaterialIdentifier(material, defaultVisibility.getValue());
            id.setPlace(tfLocation.getText());
            material.addIdentifier(id);
            identifiers.add(id);
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        theStage.close();
    }
    //</editor-fold>
}
