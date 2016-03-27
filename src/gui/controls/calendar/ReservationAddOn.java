package gui.controls.calendar;

import domain.DomainController;
import domain.Reservation;
import domain.ReservationDetail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * @author Evert
 */
public class ReservationAddOn implements CalendarAddOn {

    private Map<LocalDateTime, Node> theNodes;

    public ReservationAddOn(ObservableList<Reservation> reservations) {
        Map<LocalDateTime, List<Reservation>> temp = reservations.stream().collect(Collectors.groupingBy(Reservation::getStartDate));
        this.theNodes = temp.keySet().stream().collect(Collectors.toMap(k -> k, ke -> new ProgressBar(temp.get(ke))));
        reservations.addListener((ListChangeListener<Reservation>) c -> theNodes.entrySet().forEach(n -> ((ProgressBar) n).updateLabels()));
    }

    @Override
    public Map<LocalDateTime, Node> getNodes() {
        return theNodes;
    }

    class ProgressBar extends HBox {

        private HBox green, red;
        private Label lblGreen, lblRed;

        List<Reservation> theList;

        public ProgressBar(List<Reservation> res) {
            theList = res;

            lblGreen = new Label();
            lblRed = new Label();

            green = new HBox(lblGreen);
            red = new HBox(lblRed);

            green.setAlignment(Pos.CENTER);
            green.setStyle("-fx-background-color: #00AC00");
            red.setAlignment(Pos.CENTER);
            red.setStyle("-fx-background-color: #DC0004");

            this.getChildren().addAll(green, red);
            this.lookupAll(".split-pane-divider").stream().forEach(div -> div.setMouseTransparent(true));
            updateLabels();

            this.widthProperty().addListener(event -> updateLabels());
        }

        private void updateLabels() {
            double totalReservations = theList.size(),
                    finished = Math.toIntExact(theList.stream().filter(r -> r.getReservationDetails().stream().allMatch(ReservationDetail::isBroughtBack)).count()),
                    notFinished = totalReservations - finished;

            lblGreen.setText(String.format("%.0f", finished));
            lblRed.setText(String.format("%.0f", notFinished));
            green.maxWidthProperty().set(this.getWidth() * (finished / totalReservations));
        }
    }

}
