/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Reservation;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class ReservationController extends AnchorPane {

    @FXML
    private TextField tcUserEmail;
    @FXML
    private TextField tcOphaalDatum;
    @FXML
    private TextField tcTerugbrengDatum;
    private DomainController dc;
    @FXML
    private Button btnSave;
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");


    public ReservationController(DomainController dc) {
        this.dc = dc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        
    }

    @FXML
    private void addReservation(ActionEvent event) {
        Reservation r = new Reservation();
        LocalDate pickUpDate = LocalDate.parse(this.tcOphaalDatum.getText(), formatter);
        LocalDate bringBackDate = LocalDate.parse(this.tcTerugbrengDatum.getText(), formatter);
        r.setPickUpDate(pickUpDate);
        r.setBringBackDate(bringBackDate);
        r.setUserEmail(this.tcUserEmail.getText());
        
        dc.addReservation(r);
    }
    
}
