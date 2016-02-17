/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Evert
 */
public class IdentifierOptionsController extends HBox {

    @FXML
    private Rectangle rectDelete;

    public IdentifierOptionsController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IdentifierOptions.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void delete(MouseEvent event) {

    }

    public Node getNodeByName(String name) {
        switch (name.toLowerCase()) {
            case "delete" : 
                return rectDelete;
        }
        
        return null;
    }

}
