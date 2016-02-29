/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Material;
import domain.MaterialIdentifier;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 */
public class OverviewController extends AnchorPane {
    //<editor-fold desc="FXML variables" defaultstate="collapsed">

    @FXML
    private AnchorPane AnchorPane;
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
        openMaterialScreen(mc, newStage);
    }

    private void openMaterialScreen(MaterialController mc, Stage newStage) {
        Scene scene = new Scene(mc);
        newStage.setMinWidth(620);
        newStage.setMinHeight(463);
        newStage.setScene(scene);
        newStage.show();
    }
    //</editor-fold>

    //<editor-fold desc="FXML methods" defaultstate="collapsed">

    @FXML
    private void addMaterial(MouseEvent event) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle("Nieuw Materiaal - IIM");
        MaterialController mc = new MaterialController(dc, newStage);
        openMaterialScreen(mc, newStage);
    }
    //</editor-fold>
}
