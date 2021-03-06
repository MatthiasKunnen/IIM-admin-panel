package gui;

import domain.DomainController;
import domain.Material;
import domain.Setting;
import domain.Visibility;
import gui.controls.GuiHelper;
import gui.controls.options.CustomOptionsController;
import gui.controls.searchfield.SearchField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Arrays;

/**
 * FXML Controller class
 */
public class OverviewController extends VBox {

    //<editor-fold desc="FXML Variables" defaultstate="collapsed">

    @FXML
    private TableColumn<?, ?> tcName;
    @FXML
    private TableColumn<?, ?> tcDescription;
    @FXML
    private TableColumn<Material, Integer> tcAmount;
    @FXML
    private TableColumn<Material, Boolean> tcActions;
    @FXML
    private ImageView ivAddButton;
    @FXML
    private TableView<Material> tvMaterials;
    @FXML
    private SearchField sfFilterMaterials;
    @FXML
    private HBox hbOptions;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private DomainController dc;
    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">

    public OverviewController(DomainController dc) {
        this.dc = dc;

        try {
            GuiHelper.loadFXML("Overview.fxml", this);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        FilteredList<Material> filteredList = new FilteredList<>(dc.getMaterials());
        SortedList<Material> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tvMaterials.comparatorProperty());


        this.sfFilterMaterials.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(m -> Arrays.asList(sfFilterMaterials.getText().toLowerCase().split(" ")).stream()
                    .allMatch(s->m.getName().toLowerCase().contains(s) || m.getDescription().toLowerCase().contains(s) || m.getArticleNr().toLowerCase().contains(s)));
        });
        /*
        CustomOptionsController coc = new CustomOptionsController();
        coc.setAlignment(Pos.CENTER_RIGHT);
        coc.addExistingSVG("firms", "briefcase");
        coc.addExistingSVG("users");
        coc.addExistingSVG("settings", "wrench");
        coc.bind("firms", MouseEvent.MOUSE_CLICKED, (event -> {
            Stage newStage = new Stage(StageStyle.DECORATED);
            openNewWindow(new FirmSceneController(dc, newStage), newStage);
        }));
        coc.prefWidthProperty().bind(tvMaterials.widthProperty());
        coc.setSpacing(20.0);
        hbOptions.getChildren().add(coc);*/
        ivAddButton.setImage(new Image(getClass().getResource("/gui/images/material-add.png").toExternalForm()));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcAmount.setCellValueFactory((TableColumn.CellDataFeatures<Material, Integer> param) ->
                new SimpleIntegerProperty((int) param.getValue().getIdentifiers()
                        .stream()
                        .filter(mi -> Boolean.valueOf(dc.getSettingData(Setting.Key.KEEP_HISTORY, "false")) || !mi.getVisibility().equals(Visibility.Administrator))
                        .count()).asObject());
        tcActions.setCellFactory(new Callback<TableColumn<Material, Boolean>, TableCell<Material, Boolean>>() {

            @Override
            public TableCell<Material, Boolean> call(TableColumn<Material, Boolean> param) {
                return new TableCell<Material, Boolean>() {
                    private final CustomOptionsController coc = new CustomOptionsController();

                    {
                        coc.addExistingSVG("delete");
                        coc.addExistingSVG("edit");
                        coc.bind("delete", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                //TODO Add warning?
                                Material m = (Material) getTableRow().getItem();
                                dc.removeMaterial(m);
                            }
                        });
                        coc.bind("edit", MouseEvent.MOUSE_CLICKED, event -> {
                            if (getTableRow().getItem() != null) {
                                editMaterial((Material) getTableRow().getItem());
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

        tvMaterials.setItems(sortedList);

        tvMaterials.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                editMaterial(tvMaterials.getSelectionModel().getSelectedItem());
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Private methods" defaultstate="collapsed">

    private void editMaterial(Material material) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        GuiHelper.setIcon(newStage);
        MaterialController mc = new MaterialController(dc, newStage, material);
        newStage.setTitle(material.getName() + " - IIM");
        openNewWindow(mc, newStage);
    }

    private void openNewWindow(Parent parent, Stage stage){
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }
    //</editor-fold>

    //<editor-fold desc="FXML methods" defaultstate="collapsed">

    @FXML
    private void addMaterial(MouseEvent event) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        GuiHelper.setIcon(newStage);
        newStage.setTitle("Nieuw Materiaal - IIM");
        MaterialController mc = new MaterialController(dc, newStage);
        openNewWindow(mc, newStage);
    }
    //</editor-fold>
}
