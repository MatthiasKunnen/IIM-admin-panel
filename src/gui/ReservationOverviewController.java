/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Reservation;
import java.io.IOException;
import java.time.LocalDate;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ReservationOverviewController extends AnchorPane {
    //<editor-fold desc="FXML variables" defaultstate="collapsed">

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private ImageView ivAddButton;
    @FXML
    private TableColumn<Reservation, String> tcReservedFor;
    @FXML
    private TableColumn<Reservation, LocalDate> tcPickUpDate;
    @FXML
    private TableColumn<Reservation, LocalDate> tcBringBackDate;
    @FXML
    private TableView<Reservation> tvReservations;
    @FXML
    private TableColumn<?, ?> tcOptions;
    @FXML
    private Button btnRemove;
        //</editor-fold>

    //<editor-fold desc="variables" defaultstate="collapsed">
    private DomainController dc;
        //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    

    public ReservationOverviewController(DomainController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservationOverview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        ivAddButton.setImage(new Image(getClass().getResource("/gui/images/material-add.png").toExternalForm()));
        //tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcReservedFor.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        tcPickUpDate.setCellValueFactory(col->col.getValue().getPickUpDateProperty());
        tcBringBackDate.setCellValueFactory(col->col.getValue().getBringBackDateProperty());
        
        this.tvReservations.setItems(dc.getReservations());
               

        tvReservations.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    Stage newStage = new Stage(StageStyle.DECORATED);
                    Reservation theReservation = tvReservations.getSelectionModel().getSelectedItem();
                    ReservationController rc = new ReservationController(dc, newStage, theReservation);
                    newStage.setTitle("Reservatie van "+theReservation.getUser().getFirstName()+" "+theReservation.getUser().getLastName()+ " - IIM");
                    openReservationScreen(rc, newStage);
                }
            }

        });
    }
        //</editor-fold>

    //<editor-fold desc="FXML actions" defaultstate="collapsed">

    @FXML
    private void addReservation(MouseEvent event) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle("Nieuwe reservatie - IIM");
        ReservationController rc = new ReservationController(dc, newStage);
        openReservationScreen(rc, newStage);
    }
    @FXML
    private void removeReservation(MouseEvent event) {
        dc.removeReservation(this.tvReservations.getSelectionModel().getSelectedItem());
        //het scherm zou moeten refreshen 
    }
    //</editor-fold>

    //<editor-fold desc="actions" defaultstate="collapsed">

    private void openReservationScreen(ReservationController rc, Stage newStage){
        
        Scene scene = new Scene(rc);

        newStage.setMinWidth(620);
        newStage.setMinHeight(463);
        newStage.setScene(scene);
        newStage.show();
    }
    //</editor-fold>

    
    
    
}

