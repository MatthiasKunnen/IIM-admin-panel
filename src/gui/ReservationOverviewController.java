package gui;

import domain.DomainController;
import domain.Reservation;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


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

        try {
            GuiHelper.loadFXML("ReservationOverview.fxml", this);
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
        lvReservaties.setCellFactory(p-> new ListCell<Reservation>(){
            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(String.format("%s (%s - %s)", item.getUser().getEmail(), item.getStartDate().format(GuiHelper.getDateTimeFormatter()), item.getEndDate().format(GuiHelper.getDateTimeFormatter())));
                }
            }
        });
        lvReservaties.setOnMouseClicked(event -> {
            if (event.getClickCount() >= 2) {
                openReservation(lvReservaties.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void filterDate() {
        if (dpEndDate.getValue() == null) {
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
        newStage.setTitle(String.format("%s %s - IIM", selectedItem.getUser().getEmail(), selectedItem.getStartDate().format(GuiHelper.getDateTimeFormatter())));
        Scene scene = new Scene(new ReservationController(dc, newStage, selectedItem));
        newStage.setScene(scene);
        newStage.show();
    }
}
