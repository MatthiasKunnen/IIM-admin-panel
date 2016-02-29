/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.Reservation;
import persistence.PersistenceEnforcer;

public class ReservationRepository extends Repository<Reservation> {
    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public ReservationRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }
    //</editor-fold>
}
