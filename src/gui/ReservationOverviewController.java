package gui;

import domain.DomainController;
import domain.Reservation;

import java.io.IOException;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class ReservationOverviewController extends AnchorPane {

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private ListView<Reservation> lvReservaties;

    @FXML
    private TextField tfFilter;

    @FXML
    private DatePicker dpEndDate;

    private DomainController dc;

    private FilteredList<Reservation> reservationsList;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public ReservationOverviewController(DomainController dc) {
        this.dc = dc;
        reservationsList = new FilteredList<>(dc.getReservations());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReservationOverview.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        dpStartDate.setOnAction(event -> {
            filterDate();
        });

        dpEndDate.setOnAction(event -> {
            filterDate();
        });

        tfFilter.setOnKeyReleased(event -> {
            filterName();
        });

        lvReservaties.setItems(reservationsList);
        lvReservaties.setOnMouseClicked(event -> {
            if (event.getClickCount() > 2) {
                openReservation(lvReservaties.getSelectionModel().getSelectedItem());
            }
        });

    }

    private void filterDate() {

        if (dpEndDate.getValue() != null) {
            reservationsList = new FilteredList<>(dc.getReservations(), r -> r.getStartDate().minusDays(1).isBefore(dpStartDate.getValue().atStartOfDay()));
        } else {
            reservationsList = new FilteredList<>(dc.getReservations(), r -> r.getStartDate().minusDays(1).isAfter(dpStartDate.getValue().atStartOfDay()) && r.getEndDate().plusDays(1).isAfter(dpEndDate.getValue().atStartOfDay()));
        }

        filterName();
    }

    private void filterName() {
        if (!tfFilter.getText().trim().isEmpty()) {
            reservationsList = reservationsList.filtered(r -> r.getUser().getEmail().contains(tfFilter.getText()));
        }
    }

    private void openReservation(Reservation selectedItem) {
        Stage newStage = new Stage(StageStyle.DECORATED);
        newStage.setTitle(selectedItem.getUser().getEmail() + " " + selectedItem.getStartDate() + " - IIM");
        //create reservationController

    }
}
