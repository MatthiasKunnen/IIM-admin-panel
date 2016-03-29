package gui.controls.calendar;

import domain.Reservation;
import domain.ReservationDetail;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import gui.controls.progressbar.ProgressBar;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * @author Evert
 */
public class ReservationAddOn implements CalendarAddOn {

    private Map<LocalDate, Node> theNodes;
    private ObservableList<Reservation> observableReservations;

    public ReservationAddOn(ObservableList<Reservation> observableReservations) {
        this.observableReservations = observableReservations;
        createProgressBars();
        observableReservations.addListener((ListChangeListener<Reservation>) c -> {
            createProgressBars();
            theNodes.entrySet()
                    .stream()
                    .filter(n -> n instanceof ReservationProgressBar)
                    .forEach(n -> ((ReservationProgressBar) n).updateLabels());
        });
    }

    private void createProgressBars() {
        Map<LocalDate, List<Reservation>> reservationDateMap = observableReservations.stream()
                .collect(Collectors.groupingBy(r -> r.getStartDate().toLocalDate()));
        theNodes = reservationDateMap.keySet().stream()
                .collect(Collectors.toMap(localDate -> localDate, reservationList -> new ReservationProgressBar(reservationDateMap.get(reservationList))));
    }

    @Override
    public Map<LocalDate, Node> getNodes() {
        return theNodes;
    }

    static class ReservationProgressBar extends ProgressBar {
        private List<Reservation> theList;
        private final static Color
                GREEN = Color.rgb(0, 172, 0),
                RED = Color.rgb(220, 0, 4);

        public ReservationProgressBar(List<Reservation> res) {
            theList = res;
            this.createBar(GREEN);
            this.createBar(RED);
            //this.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
            updateLabels();
        }

        private void updateLabels() {
            double totalReservations = theList.size(),
                    finished = Math.toIntExact(theList.stream().filter(r -> r.getReservationDetails().stream().allMatch(ReservationDetail::isBroughtBack)).count()),
                    notFinished = totalReservations - finished;
            setBar(0, (finished / totalReservations), String.format("%.0f", finished));
            setBar(1, (notFinished / totalReservations), String.format("%.0f", notFinished));
        }
    }

}
