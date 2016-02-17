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
import javafx.event.EventHandler;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class OverviewController extends AnchorPane {

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

    private DomainController dc;    

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
                                    Material m = (Material) getTableRow().getItem();
                                    dc.removeMaterial(m);
                                    
                                }

                            };

                            ioc.getNodeByName("delete").addEventFilter(MouseEvent.MOUSE_CLICKED, filter);
                        }

                    }

                };
            }
        });

        tvMaterials.setItems(dc.getMaterials());
        
        tvMaterials.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() >= 2){
                    Stage newStage = new Stage(StageStyle.DECORATED);
                    Material theMaterial = tvMaterials.getSelectionModel().getSelectedItem();
                    MaterialController mc = new MaterialController(dc,newStage,theMaterial);
                    newStage.setTitle(theMaterial.getName() + " - IMM");
                    openMaterialScreen(mc, newStage);
                }
            }
        
        });
    }

    @FXML
    private void addMaterial(MouseEvent event) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle("Niew Materiaal - IIM");
        MaterialController mc = new MaterialController(dc, newStage);

        openMaterialScreen(mc, newStage);
    }
    
    private void openMaterialScreen(MaterialController mc, Stage newStage){
        
        Scene scene = new Scene(mc);

        
        newStage.setScene(scene);
        newStage.show();
    }

}
