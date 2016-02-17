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
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private TableColumn<?, ?> tcAmount;
    @FXML
    private TableColumn<?, ?> tcActions;
    @FXML
    private ImageView ivAddButton;
    private DomainController dc;
     private Stage theStage;
     private ObservableList<Material> materials;
    @FXML
    private TableView<Material> tvMaterials;
    
   

    public OverviewController(DomainController dc) {
        
        this.dc = dc;
         this.materials = FXCollections.observableArrayList();
       //this.theStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Overview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
           
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        //ivAddButton.setImage(new Image(getClass().getResource("/gui/images/material-add.png").toExternalForm()));

        materials.addAll(dc.getMaterials());
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcAmount.setCellValueFactory(new PropertyValueFactory<>("items"));
        //tcActions
        
        tvMaterials.setItems(this.materials);
    }
    @FXML
 private void addMaterial(MouseEvent event) {
        Stage huidigeStage = (Stage) this.getScene().getWindow();
        MaterialController mc=new MaterialController(dc,huidigeStage);
        
        huidigeStage.setTitle("materiaal toevoegen");
        this.getScene().setRoot(mc);
    }
       
    
}
