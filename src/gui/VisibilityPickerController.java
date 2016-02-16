/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Visibility;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.io.IOException;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class VisibilityPickerController extends HBox {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    @FXML
    private Rectangle rectAdmin;

    @FXML
    private SVGPath svgAdmin;

    @FXML
    private Rectangle rectDocent;

    @FXML
    private SVGPath svgDocent;

    @FXML
    private Rectangle rectStudent;

    @FXML
    private SVGPath svgStudent;
    //</editor-fold>

    public ObjectProperty<Visibility> visibility;
    private final Paint SELECTED = Paint.valueOf("#3cd728");
    private final Visibility DEFAULT = Visibility.Administrator;
    public VisibilityPickerController() {
        this.visibility = new SimpleObjectProperty<>();
        this.visibility.setValue(DEFAULT);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VisibilityPicker.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.rectStudent.setOnMouseClicked((event -> setVisibility(Visibility.Student)));
        this.rectDocent.setOnMouseClicked((event -> setVisibility(Visibility.Docent)));
        this.rectAdmin.setOnMouseClicked((event -> setVisibility(Visibility.Administrator)));
        this.svgAdmin.setFill(SELECTED);
    }

    public Visibility getVisibility() {
        return visibility.getValue();
    }

    public void setVisibility(Visibility visibility) {
        switch (visibility){
            case Student:
                svgStudent.setFill(SELECTED);
                svgDocent.setFill(SELECTED);
                break;
            case Docent:
                svgStudent.setFill(Color.BLACK);
                svgDocent.setFill(SELECTED);
                break;
            case Administrator:
                svgStudent.setFill(Color.BLACK);
                svgDocent.setFill(Color.BLACK);
        }
        this.visibility.set(visibility);
    }

    /**
     * Binds a variable to this control's Visibility property.
     * @param property the property to be bound.
     */
    public void bindVisibility(SimpleObjectProperty<Visibility> property){
        property.bind(this.visibility);
    }
}
