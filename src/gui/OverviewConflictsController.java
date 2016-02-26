/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Reservation;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author matthiasseghers
 */
public class OverviewConflictsController extends AnchorPane {

    @FXML
    private TableColumn<Reservation, String> tcReservation;
    @FXML
    private TableColumn<Reservation, String> tcUser;
    @FXML
    private TableColumn<Reservation, String> tcConflictsWithUser;
    @FXML
    private TableView<Reservation> tvConflicts;
    
    private DomainController dc;
    
    
    public OverviewConflictsController(DomainController dc, Stage stage){
       FXMLLoader loader = new FXMLLoader(getClass().getResource("OverviewConflicts.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
            tcReservation.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
            tcUser.setCellValueFactory(new PropertyValueFactory<>("userId"));
            tcConflictsWithUser.setCellValueFactory((TableColumn.CellDataFeatures<Reservation, String> param) -> 
                 new SimpleStringProperty(param.getValue().getConflictWithUsers().toString()));
            tvConflicts.setItems(dc.getConflictReservations());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } 
    }
       
    
}
