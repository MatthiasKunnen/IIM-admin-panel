package repository;

import domain.Reservation;
import persistence.PersistenceEnforcer;

public class ReservationRepository extends LoadedRepository<Reservation> {

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public ReservationRepository(PersistenceEnforcer persistence) {
        super(persistence, Reservation.class);
    }
    //</editor-fold>
    
    /**
     * Checks if a {@link domain.Reservation} already exist.
     * @param reservation the {@link domain.Reservation} to search.
     * @return true if Reservation already exist if not it returns false.
     */
    public boolean doesReservationExist(Reservation reservation){
        return getItemById(reservation.getId()) != null;
    }
}
