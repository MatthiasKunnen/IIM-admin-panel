/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import exceptions.ReservationNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;

/**
 *
 * @author matthiasseghers
 */
public class ReservatieRepository {

    private MaterialIdentifier materialIdentifier;
    private PersistenceEnforcer persistence;
    private List<Reservation> reservations = new ArrayList<>();
    private ObservableList<Reservation> reservationList;

    public ReservatieRepository(PersistenceEnforcer persistence) {
        this.persistence = persistence;
        List<Reservation> initialize = persistence.retrieve(Reservation.class);
        reservations = new ArrayList<>(initialize);
        reservationList = FXCollections.observableList(this.reservations);
    }

    public Reservation getReservationById(int reservationId) {
         Reservation reservation = reservationId == 0 ? null : this.reservations
                .stream()
                .filter(r -> r.getReservationId()== reservationId)
                .findAny()
                .orElse(null);
         if (reservation == null)
            throw new ReservationNotFoundException("There's no reservation founded.");
         return reservation;
    }
    public Reservation getReservationByUser(int userId) {
         Reservation reservation = userId == 0 ? null : this.reservations
                .stream()
                .filter(r -> r.getReservationId()== userId)
                .findAny()
                .orElse(null);
         if (reservation == null)
            throw new ReservationNotFoundException("This user hasn't any reservations");
         return reservation;
    }
    public List<Reservation> getConflictedReservations(){
        return reservationList.stream().filter(r-> r.isConflictFlag()).collect(Collectors.toList());
    }

    public void conflictTracing() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

        for (Reservation r1 : reservations) {

            for (Reservation r2 : reservations) {
                if (!r1.equals(r2)) {
                    if (r1.getPickUpDate().before(r2.getBringBackDate()) && (r2.getPickUpDate().before(r1.getBringBackDate()))) {
                        r1.setConflictFlag(true);
                        r2.setConflictFlag(true);
                    }
                }

            }

        }

    }
}
