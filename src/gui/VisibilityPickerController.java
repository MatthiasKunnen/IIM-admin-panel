/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.Visibility;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    
    
    
    public VisibilityPickerController(){
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VisibilityPicker.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        for(Node c : this.getChildren() ){
            Rectangle rect = new Rectangle(c.getLayoutX(), c.getLayoutY(), c.getBoundsInParent().getWidth(), c.getBoundsInParent().getHeight());
            rect.cursorProperty().set(Cursor.HAND);
            rect.opacityProperty().set(1);
            
        }
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
    
    public void addChangeListener(ChangeListener<Visibility> cl){
        this.visibility.addListener(cl);
    }

    
    
    

       
}
