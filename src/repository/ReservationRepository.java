package repository;

import domain.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

import java.util.List;

public class ReservationRepository extends Repository<Reservation> {

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public ReservationRepository(PersistenceEnforcer persistence) {
        super(persistence);
        eList = persistence.retrieve(Reservation.class);
        eObservableList = FXCollections.observableList((List<Reservation>) ImmutabilityHelper.copyCollectionDefensively(eList));
    }
    //</editor-fold>
    
    /**
     * @return returns an ObservableList of no-reference {@link domain.Reservation}.
     */
    public ObservableList<Reservation> getReservations() {
        return FXCollections.unmodifiableObservableList(eObservableList);
    }

    /**
     * Checks if a {@link domain.Reservation} already exist.
     * @param reservation the {@link domain.Reservation} to search.
     * @return true if Reservation already exist if not it returns false.
     */
    public boolean doesReservationExist(Reservation reservation){
        return getItemById(reservation.getId()) != null;
    }
}
