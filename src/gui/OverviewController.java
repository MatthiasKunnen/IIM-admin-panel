package gui;

import domain.DomainController;
import domain.Material;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 */
public class OverviewController extends VBox {
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
    private TextField txfFilterMaterials;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private DomainController dc;
    //</editor-fold>
    

    //<editor-fold desc="Constructor" defaultstate="collapsed">

    public OverviewController(DomainController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Overview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
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
        getChildren().add(0, coc);
        ivAddButton.setImage(new Image(getClass().getResource("/gui/images/material-add.png").toExternalForm()));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcAmount.setCellValueFactory((TableColumn.CellDataFeatures<Material, Integer> param) ->
                new SimpleIntegerProperty(param.getValue().getIdentifiers().size()).asObject());
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

        tvMaterials.setItems(dc.getMaterials());

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
        newStage.setTitle("Nieuw Materiaal - IIM");
        MaterialController mc = new MaterialController(dc, newStage);
        openNewWindow(mc, newStage);
    }
    //</editor-fold>

    @FXML
    private void filterMaterials(KeyEvent event) {
        this.tvMaterials.setItems(dc.filterMaterialByName(txfFilterMaterials.getText()));
                Platform.runLater(() -> this.setMinWidth(this.getWidth()));

    }
}
