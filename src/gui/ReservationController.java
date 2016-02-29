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
import java.io.IOException;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ReservationController extends AnchorPane {

    @FXML
    private Button btnSave;
    @FXML
    private TableView<MaterialIdentifier> tvMaterials;
    @FXML
    private TableColumn<MaterialIdentifier, Integer> tcPlace;
    @FXML
    private TableColumn<MaterialIdentifier, String> tcMaterial;
    @FXML
    private DatePicker dpBringBackDate;
    @FXML
    private DatePicker dpPickUpDate;

    private DomainController dc;
    private ObservableList<MaterialIdentifier> identifiers;
    private Stage theStage;
    private Map<Material, Integer> materialAmountMap;
    private Reservation reservation;
    @FXML
    private TextField txfUserEmail;

    public ReservationController(DomainController dc, Stage stage) {
        //super(dc, stage, new Reservation());

    }

    public ReservationController(DomainController dc, Stage stage, Reservation reservation) {
        this.identifiers = FXCollections.observableArrayList();
        this.theStage = stage;
        this.dc = dc;
        this.materialAmountMap = new HashMap<>();
        this.reservation = reservation;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reservation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        txfUserEmail.setText(reservation.getUserEmail());
        dpBringBackDate.setValue(reservation.getBringBackDate());
        dpPickUpDate.setValue(reservation.getPickUpDate());
        tcMaterial.setCellValueFactory((TableColumn.CellDataFeatures<MaterialIdentifier, String> param) -> 
                 new SimpleStringProperty(param.getValue().getInfo().getName()));
        tcPlace.setCellValueFactory(new PropertyValueFactory<>("place"));
        
        identifiers.addAll(reservation.getMaterialIdentifiersList());
        tvMaterials.setItems(identifiers);
    }

    @FXML
    private void saveReservation(ActionEvent event) {
        
    }

}
