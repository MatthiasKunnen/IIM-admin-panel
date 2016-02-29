/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;

public class ReservationRepository extends Repository<Reservation> {
    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public ReservationRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }
    //</editor-fold>
    
    /**
     * @return returns an ObservableList of no-reference {@link domain.Reservation}.
     */
    public ObservableList<Reservation> getReservations() {
        return FXCollections.unmodifiableObservableList(eObservableList);
    }
    
    public boolean doesReservationExist(Reservation reservation){
        return getItemById(reservation.getId()) != null;
    }
}
