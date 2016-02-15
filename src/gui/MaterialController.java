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
import exceptions.InvalidPriceException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private SVGPath svgAdmin;
    @FXML
    private SVGPath svgDocent;
    @FXML
    private SVGPath svgStudent;
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

    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private Stage theStage;
    private DomainController dc;
    private Material material;
    private Visibility defaultVisibility;
    private VisibilityPickerController visibilityPickerController;
    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">

    public MaterialController(DomainController dc, Stage stage) {
        this.theStage = stage;
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Material.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            GuiHelper.getKeyEventEventHandlerAssuringDecimalInput(this.tfPrice);
            GuiHelper.getKeyEventEventHandlerAssuringIntegerInput(this.tfAmount);
            this.pnVisibilityPicker.getChildren().clear();
            //this.pnVisibilityPicker.getChildren().add(FXMLLoader.load(getClass().getResource("VisibilityPicker.fxml"))); //TODO implement partial view
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        ivPhoto.setImage(new Image(getClass().getResource("/gui/images/picture-add.png").toExternalForm()));
    }
    //</editor-fold>

    //<editor-fold desc="FXML Actions" defaultstate="collapsed">

    @FXML
    private void saveMaterial(ActionEvent event) {
        Material m = new Material(tfName.getText());

        if (!tfArticleNumber.getText().trim().isEmpty()) {
            m.setArticleNr(tfArticleNumber.getText());
        }

        if (!tfDescription.getText().trim().isEmpty()) {
            m.setDescription(tfDescription.getText());
        }

        if (!tfPrice.getText().trim().isEmpty()) {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRENCH);
            String pattern = "#,##0.0#";
            DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
            decimalFormat.setParseBigDecimal(true);

            // parse the string
            BigDecimal bigDecimal = null;
            
            try {
                bigDecimal = (BigDecimal) decimalFormat.parse(tfPrice.getText());
            } catch (ParseException ex) {
            }
            
            try {
                m.setPrice(bigDecimal);
            } catch (InvalidPriceException ex) {
                
            }
        }

        //m.setFirm();
        //m.setfirmEmail();
        for (int i = 0; Integer.parseInt(tfAmount.getText()) >= i; i++) {
            MaterialIdentifier mi = new MaterialIdentifier(m, defaultVisibility);
            mi.setId(i);
            mi.setPlace(tfLocation.getText());
            mi.setInfo(m);

            m.addIdentifier(mi);
        }

        dc.addMaterial(m);

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

        try {
            ivPhoto.setImage(new Image(f.toURI().toURL().toString()));
        } catch (MalformedURLException ex) {

        }
    }
    //</editor-fold>
}
