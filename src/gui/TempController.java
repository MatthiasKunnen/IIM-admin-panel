/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;


import java.beans.EventHandler;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class TempController<E> extends AnchorPane{
    @FXML
    private Label lblTitel;
    @FXML
    private VBox vbNodes;
    @FXML
    private ListView<E> lvItems;
    @FXML
    private HBox hbButtons;

    public TempController(String titel,ObservableList<E> items){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Temp.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
        this.lvItems.setItems(items);
        this.lblTitel.setText(titel);
        
    }
    
    public void addVerticalNode(Node theNode){
        this.vbNodes.getChildren().add(theNode);
        VBox.setMargin(theNode, new Insets(5, 0, 5, 0));
    }
    
    public void addHorizontalButton(Node button){
        this.hbButtons.getChildren().add(button);
        HBox.setMargin(button, new Insets(0, 5, 0, 5));
    }
    
    public ListView<E> getListView(){
        return this.lvItems;
    }
    
}
