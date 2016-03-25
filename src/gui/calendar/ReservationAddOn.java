package gui.calendar;

import domain.DomainController;
import domain.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import org.eclipse.persistence.jpa.jpql.tools.model.ListChangeEvent;

public class ReservationAddOn implements CalendarAddOn {

    private DomainController dc;
    private Map<LocalDateTime, Node> theNodes;

    public ReservationAddOn(DomainController dc, ObservableList<Reservation> reservations) {
        this.dc = dc;

        Map<LocalDateTime, List<Reservation>> temp = reservations.stream().collect(Collectors.groupingBy(r -> r.getStartDate()));
        this.theNodes = temp.keySet().stream().collect(Collectors.toMap(k -> k, ke -> new ProgressBar(temp.get(ke))));
        reservations.addListener(new ListChangeListener(){

            @Override
            public void onChanged(ListChangeListener.Change c) {
                theNodes.entrySet().forEach(n-> ((ProgressBar)n).updateLabels());
            }
            
        });
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

            this.widthProperty().addListener(event -> {
                updateLabels();
            });
            
        }

        private void updateLabels() {
            double totalReservations = theList.size(),
                    finished = Math.toIntExact(theList.stream().filter(r -> r.getReservationDetails().stream().allMatch(i -> i.isBroughtBack())).count()),
                    notFinished = totalReservations - finished;

            lblGreen.setText(String.format("%.0f", finished));
            lblRed.setText(String.format("%.0f", notFinished));
            green.maxWidthProperty().set(this.getWidth() * (finished / totalReservations));

        }
    }

}
