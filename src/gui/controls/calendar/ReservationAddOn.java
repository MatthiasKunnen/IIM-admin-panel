/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controls.calendar;

import domain.DomainController;
import domain.Reservation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Evert
 */
public class ReservationAddOn implements CalendarAddOn {

    private HashMap<LocalDate, Node> theNodes;

    public ReservationAddOn(DomainController dc, List<Reservation> reservations) {
        HashMap<LocalDate,List<Reservation>> temp;
        //temp = reservations.stream().collect(Collectors.groupingBy()));
    }

    @Override
    public HashMap<LocalDate, Node> getNodes() {
        return theNodes;
    }

    class ReservationList extends VBox {

        private DomainController dc;

        public ReservationList(DomainController dc, List<Reservation> reservation) {
            this.dc = dc;

            int counter = 0;
            HBox hb = new HBox();
            Iterator<Reservation> it = reservation.iterator();

            while (it.hasNext()) {
                if (counter == 5) {
                    this.getChildren().add(hb);
                    hb = new HBox();
                }
                
            }

            Node square = createNewsquare(it.next());
            HBox.setMargin(square, new Insets(2, 2, 2, 2));

            hb.getChildren().add(square);
        }
    }

    private Node createNewsquare(Reservation r) {
        AnchorPane pane = new AnchorPane();
        pane.setStyle("-fx-background-color : #3DFF3A");

        pane.setOnMouseClicked(event -> {
//            Stage newStage = new Stage(StageStyle.DECORATED);
//            newStage.setTitle(r.getUser().getEmail() + " " + r.getStartDate().format(GuiHelper.getDateTimeFormatter()) + " - IIM");
//            Scene scene = new Scene(new ReservationController(dc, newStage, r));
//            newStage.setScene(scene);
//            newStage.show();
        });

        return pane;
    }
}


