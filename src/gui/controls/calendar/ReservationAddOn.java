package gui.controls.calendar;

import domain.Reservation;
import domain.ReservationDetail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gui.controls.progressbar.ProgressBar;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * @author Evert
 */
@Deprecated
public class ReservationAddOn implements CalendarAddOn {

    private ObservableMap<LocalDate, Node> theNodes;
    private ObservableList<Reservation> observableReservations;

    public ReservationAddOn(ObservableList<Reservation> observableReservations) {
        throw new UnsupportedOperationException("The class ReservationAddOn is not finished.");
        /*
        this.observableReservations = observableReservations;

        Map<LocalDate, List<Reservation>> reservationDateMap = observableReservations.stream()
                .collect(Collectors.groupingBy(r -> r.getStartDate().toLocalDate()));
        theNodes = FXCollections.observableMap(reservationDateMap.keySet().stream()
                .collect(Collectors.toMap(localDate -> localDate, reservationList -> new ReservationProgressBar(reservationDateMap.get(reservationList)))));

        observableReservations.addListener((ListChangeListener<Reservation>) c -> {
            /*
            Map<LocalDate, List<Reservation>> newReservations = c.getAddedSubList().stream()
                    .filter(r -> !theNodes.containsKey(r.getStartDate().toLocalDate()))
                    .map(r -> (Reservation) r)
                    .collect(Collectors.groupingBy(Reservation::getStartDate));

            theNodes.entrySet()
                    .stream()
                    .filter(n -> n instanceof ReservationProgressBar)
                    .forEach(n -> ((ReservationProgressBar) n).updateLabels());
        });
                  */
    }

    @Override
    public ObservableMap<LocalDate, Node> getNodes() {
        return theNodes;
    }

    static class ReservationNode{
        private ReservationProgressBar start, end;
        private ObservableList<Reservation> reservations;

        public ReservationNode(ObservableList<Reservation> reservations) {
            start = new ReservationProgressBar();
            end = new ReservationProgressBar();
        }
    }

    static class ReservationProgressBar extends ProgressBar {
        private final static Color
                GREEN = Color.rgb(0, 172, 0),
                RED = Color.rgb(220, 0, 4);

        public ReservationProgressBar() {
            this.createBar(GREEN);
            this.createBar(RED);
        }

        private void updateLabels(double totalReservations, double finished, double notFinished) {
            /*
            double totalReservations = theList.size(),
                    finished = Math.toIntExact(theList.stream().filter(r -> r.getReservationDetails().stream().allMatch(ReservationDetail::isBroughtBack)).count()),
                    notFinished = totalReservations - finished;
                    */
            setBar(0, (finished / totalReservations), String.format("%.0f", finished));
            setBar(1, (notFinished / totalReservations), String.format("%.0f", notFinished));
        }
    }
}
