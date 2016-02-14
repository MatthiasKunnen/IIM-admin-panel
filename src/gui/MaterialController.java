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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private DomainController dc;
    private String imageEncoding;

    public MaterialController(DomainController dc, Stage stage) {
        this.theStage = stage;
        this.dc = dc;
        imageEncoding = null;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Material.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        ivPhoto.setImage(new Image(getClass().getResource("/gui/images/picture-add.png").toExternalForm()));
    }

    @FXML
    private void addMaterial(ActionEvent event) {
        Material m = new Material(tfName.getText());

        if (!tfArticleNumber.getText().trim().isEmpty()) {
            m.setArticleNr(tfArticleNumber.getText());
        }

        if (!tfDescription.getText().trim().isEmpty()) {
            m.setDescription(tfDescription.getText());
        }

        if (imageEncoding != null) {
            m.setEncoding(imageEncoding);
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
            MaterialIdentifier mi = new MaterialIdentifier(m, determinVisibility());
            mi.setId(i);
            mi.setPlace(tfLocation.getText());
            mi.setInfo(m);

            m.addIdentifier(mi);
        }

        dc.addMaterial(m);

    }

    private Visibility determinVisibility() {
        Visibility v = Visibility.Administrator;
        if (cbDocentAvailable.isSelected()) {
            v = Visibility.Docent;
        }
        if (cbStudentAvailable.isSelected()) {
            v = Visibility.Student;
        }

        return v;
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
        
        imageEncoding = f.getAbsolutePath().substring(f.getAbsolutePath().lastIndexOf('.'), f.getAbsolutePath().length());

        try {
            ivPhoto.setImage(new Image(f.toURI().toURL().toString()));
        } catch (MalformedURLException ex) {

        }
    }

}
