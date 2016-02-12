/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class MaterialController extends AnchorPane {
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private ImageView ivPhoto;
    @FXML
    private TextField tfAmount;
    @FXML
    private TextArea tfDescription;
    @FXML
    private ComboBox<?> tfTargetAudience;
    @FXML
    private TextField tfArticleNumber;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfLocation;
    @FXML
    private ComboBox<?> tfCurricular;
    @FXML
    private CheckBox cbDocentAvailable;
    @FXML
    private CheckBox cbStudentAvailable;
    @FXML
    private ComboBox<?> tfFirm;
    @FXML
    private TextField tfName;
    @FXML
    private Label LbAvailable;
    @FXML
    private Button btnAdd;

    private Stage theStage;
    //private DomeinController dc;
    
//    public MaterialController(DomeinController dc,Stage stage){
//        this.theStage = stage;
//        this.dc = dc; 
//        //ivPhoto.setImage(new Image(getClass().getResource("/gui.images/picture-add.png").toExternalForm()));
//    }

    @FXML
    private void addMaterial(ActionEvent event) {
        
    }

    @FXML
    private void selectImage(MouseEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files",
                        "*.png"));
        fc.setTitle("Open file");
        File f = fc.showOpenDialog(theStage);
                
        try {
            ivPhoto.setImage(new Image(f.toURI().toURL().toString()));
        } catch (MalformedURLException ex) {
            
        }
    }
    
}
