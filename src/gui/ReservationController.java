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
import static gui.GuiHelper.createMethodBuilder;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ReservationController extends AnchorPane {

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

    private DomainController dc;
    private ObservableList<MaterialIdentifier> identifiers;
    private Stage theStage;
    private Reservation reservation;
    @FXML
    private TextField txfUserEmail;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private TextField txfSearch;

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
    /**
     * deze methode wordt gebruikt om te kijken of er een nieuwe reservatie moet laden worden 
     * in het scherm of een reservatie die al bestaat
     * @param reservation 
     */
    private void setReservation(Reservation reservation) {
        if (reservation == null || !dc.doesReservationExist(reservation)) {
            this.reservation = new Reservation();
        } else {
            this.reservation = reservation;
            //this.txfUserEmail.setText(reservation.getUserEmail());
            this.dpBringBackDate.setValue(reservation.getBringBackDate());
            this.dpPickUpDate.setValue(reservation.getPickUpDate());
                  
            this.identifiers.addAll(this.reservation.getMaterialIdentifiersList());
        }
    }
    /**
     * om de reservatie op te slaan
     * ik wist niet hoe ik de materialen moest toevoegen aan reservatie (via nieuw scherm, via combobox, via zoekfunctie)
     * @param event 
     */
    @FXML
    private void saveReservation(ActionEvent event) {
        reservation.setBringBackDate(this.dpBringBackDate.getValue());
        reservation.setPickUpDate(this.dpPickUpDate.getValue());
        reservation.setReservatieDate(LocalDate.now());
        //reservation.setUserEmail(txfUserEmail.getText());
        dc.addReservation(reservation);
    }
/**
 * zoekfunctie voor het materiaal te zoeken.
 * @param event 
 */
    @FXML
    private void searchMaterial(KeyEvent event) {
        this.tvMaterials.setItems(dc.searchMaterialByName(txfSearch.getText()));
    }

}
