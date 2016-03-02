/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domain.DomainController;
import domain.Material;
import domain.MaterialIdentifier;
import domain.Reservation;
import domain.User;

import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static gui.GuiHelper.*;


public class ReservationController extends AnchorPane {
    //<editor-fold desc="FXML variables" defaultstate="collapsed">
    @FXML
    private Button btnSave;
    @FXML
    private TableView<Material> tvMaterials;
    @FXML
    private TableColumn<Material, Integer> tcPlace;
    @FXML
    private TableColumn<Material, String> tcMaterialName;
    @FXML
    private DatePicker dpBringBackDate;
    @FXML
    private DatePicker dpPickUpDate;
    @FXML
    private TextField tfUserEmail;
    @FXML
    private AnchorPane AnchorPane;
    //</editor-fold>

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private DomainController dc;
    private ObservableList<MaterialIdentifier> identifiers;
    private Stage theStage;
    private Reservation reservation;

    //</editor-fold>

    //<editor-fold desc="Constructor" defaultstate="collapsed">
    public ReservationController(DomainController dc, Stage stage) {
        this(dc, stage, new Reservation());

    }

    public ReservationController(DomainController dc, Stage stage, Reservation reservation) {
        this.identifiers = FXCollections.observableArrayList();
        this.theStage = stage;
        this.dc = dc;
        this.reservation = reservation;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        setReservation(this.reservation);
//        txfUserEmail.setText(reservation.getUserEmail());
//        dpBringBackDate.setValue(reservation.getBringBackDate());
//        dpPickUpDate.setValue(reservation.getPickUpDate());
//        tcMaterial.setCellValueFactory((TableColumn.CellDataFeatures<MaterialIdentifier, String> param) -> 
//                 new SimpleStringProperty(param.getValue().getInfo().getName()));
//        tcPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
//        
//        tvMaterials.setItems(identifiers);
    }
    //</editor-fold>

    //<editor-fold desc="Action" defaultstate="collapsed">

    /**
     * deze methode wordt gebruikt om te kijken of er een nieuwe reservatie moet
     * laden worden in het scherm of een reservatie die al bestaat
     *
     * @param reservation
     */
    private void setReservation(Reservation reservation) {
        if (reservation == null || !dc.doesReservationExist(reservation)) {
            this.reservation = new Reservation();
        } else {
            this.reservation = reservation;
            //this.txfUserEmail.setText(reservation.getUserEmail());
            this.dpBringBackDate.setValue(reservation.getEndDate());
            this.dpPickUpDate.setValue(reservation.getStartDate());

            this.identifiers.addAll(this.reservation.getMaterialIdentifiersList());
        }
    }
    //</editor-fold>

    //<editor-fold desc="FXML actions" defaultstate="collapsed">

    /**
     * om de reservatie op te slaan ik wist niet hoe ik de materialen moest
     * toevoegen aan reservatie (via nieuw scherm, via combobox, via
     * zoekfunctie)
     *
     * @param event
     */
    @FXML
    private void saveReservation(ActionEvent event) {
        reservation.setEndDate(this.dpBringBackDate.getValue());
        reservation.setStartDate(this.dpPickUpDate.getValue());
        reservation.setCreationDate(LocalDate.now());
        User user = dc.getUserByEmail(tfUserEmail.getText());
        if (user == null) {
            showError(tfUserEmail, "Deze gebruiker bestaat niet, controleer het emailadres.");
        } else {

            reservation.setUser(user);
            //reservatie mag maar toegevoegd worden wanneer user bekent is 
            dc.addReservation(reservation);
        }

    }

    /**
     * Check if user exist when leaving userEmail textfield
     *
     * @param event
     */
    @FXML
    private void checkUserEmail(MouseEvent event) {
        if (dc.getUserByEmail(tfUserEmail.getText()) == null) {
            showError(tfUserEmail, "Deze gebruiker bestaat niet, controleer het emailadres.");
        } else {
            hideError(tfUserEmail);
        }
    }

    //</editor-fold>
}
