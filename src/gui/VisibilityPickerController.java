/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Visibility;
import java.io.IOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class VisibilityPickerController extends HBox {

    @FXML
    private SVGPath svgpAdmin;
    @FXML
    private SVGPath svgpDocent;
    @FXML
    private SVGPath svgpStudent;

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

        createrect(svgpDocent, Visibility.Docent);
        createrect(svgpStudent, Visibility.Student);
    }

    private void createrect(SVGPath svgp, Visibility v) {
        Rectangle rect = new Rectangle(svgp.getLayoutX(), svgp.getLayoutY(), svgp.getBoundsInParent().getWidth(), svgp.getBoundsInParent().getHeight());
        rect.cursorProperty().set(Cursor.HAND);
        rect.opacityProperty().set(1);

        rect.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switch (v) {
                    case Student:
                        svgpStudent.setStroke(SELECTED);
                    case Docent:
                        svgpDocent.setStroke(SELECTED);
                }
            }
        });

        rect.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                svgpStudent.setStroke(Color.BLACK);
                svgpDocent.setStroke(Color.BLACK);
            }
        });
    }

    @FXML
    private void adminClicked(MouseEvent event) {
        visibility.setValue(Visibility.Administrator);
        svgpDocent.setStroke(Color.BLACK);
        svgpStudent.setStroke(Color.BLACK);
    }

    @FXML
    private void docentCliked(MouseEvent event) {
        visibility.setValue(Visibility.Docent);
        svgpDocent.setStroke(SELECTED);
        svgpStudent.setStroke(Color.BLACK);
    }

    @FXML
    private void studentClicked(MouseEvent event) {
        visibility.setValue(Visibility.Student);
        svgpDocent.setStroke(SELECTED);
        svgpStudent.setStroke(SELECTED);
    }

    public Visibility getVisibility() {
        return visibility.get();
    }

    public void addChangeListener(ChangeListener<Visibility> cl) {
        this.visibility.addListener(cl);
    }

}
