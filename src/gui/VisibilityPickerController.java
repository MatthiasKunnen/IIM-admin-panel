/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Visibility;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
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

    @FXML
    private SVGPath svgAdmin;
    @FXML
    private SVGPath svgDocent;
    @FXML
    private SVGPath svgStudent;

    public ObjectProperty<Visibility> visibility;
    private final Paint SELECTED = Paint.valueOf("#3cd728");

    public VisibilityPickerController() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VisibilityPicker.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        createRect(svgDocent, Visibility.Docent);
        createRect(svgStudent, Visibility.Student);
    }

    private void createRect(SVGPath svg, Visibility v) {
        Rectangle rect = new Rectangle(svg.getLayoutX(), svg.getLayoutY(), svg.getBoundsInParent().getWidth(), svg.getBoundsInParent().getHeight());
        rect.cursorProperty().set(Cursor.HAND);

        rect.setOnMouseEntered(mouseEvent -> {
            switch (v) {
                case Student:
                    svgStudent.setStroke(SELECTED);
                case Docent:
                    svgDocent.setStroke(SELECTED);
            }
        });

        rect.setOnMouseExited(event -> {
            svgStudent.setStroke(Color.BLACK);
            svgDocent.setStroke(Color.BLACK);
        });
    }

    @FXML
    private void adminClicked(MouseEvent event) {
        visibility.setValue(Visibility.Administrator);
        svgDocent.setStroke(Color.BLACK);
        svgStudent.setStroke(Color.BLACK);
    }

    @FXML
    private void docentClicked(MouseEvent event) {
        visibility.setValue(Visibility.Docent);
        svgDocent.setStroke(SELECTED);
        svgStudent.setStroke(Color.BLACK);
    }

    @FXML
    private void studentClicked(MouseEvent event) {
        visibility.setValue(Visibility.Student);
        svgDocent.setStroke(SELECTED);
        svgStudent.setStroke(SELECTED);
    }

    public Visibility getVisibility() {
        return visibility.get();
    }

    public void addChangeListener(ChangeListener<Visibility> cl) {
        this.visibility.addListener(cl);
    }

}
