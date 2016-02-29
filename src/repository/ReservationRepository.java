/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import com.google.common.io.Files;
import domain.IEntity;
import domain.Material;
import domain.MaterialIdentifier;
import domain.Reservation;
import domain.Visibility;
import exceptions.AzureException;
import exceptions.MaterialAlreadyExistsException;
import exceptions.MaterialNotFoundException;
import exceptions.ReservationAlreadyExistsException;
import exceptions.ReservationNotFoundException;
import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.AzureBlobStorage;
import persistence.PersistenceEnforcer;
import static util.ImmutabilityHelper.copyCollectionDefensively;
import static util.ImmutabilityHelper.copyDefensively;


public class ReservationRepository extends Repository<Reservation> {
//
      //<editor-fold desc="Variables" defaultstate="collapsed">

    private List<MaterialIdentifier> materialIdentifiers;
    private AzureBlobStorage azureBlobStorage;
    private ObservableList<MaterialIdentifier> materialIdentifiersObservableList;
        //</editor-fold>

      //<editor-fold desc="Constructors" defaultstate="collapsed">

    public ReservationRepository(PersistenceEnforcer persistence) {
        super(persistence);
        List<Reservation> initialize = persistence.retrieve(Reservation.class);
        this.eList = new ArrayList<>(initialize);
        this.materialIdentifiers = new ArrayList<>(initialize
                .stream()
                .flatMap(r -> r.getMaterialIdentifiersList().stream())
                .collect(Collectors.toList()));
        this.eObservableList = FXCollections.observableList((List<Reservation>) copyCollectionDefensively(this.eList));
        this.materialIdentifiersObservableList = FXCollections.observableList((List<MaterialIdentifier>) copyCollectionDefensively(this.materialIdentifiers));
        this.azureBlobStorage = new AzureBlobStorage();
    }
    //</editor-fold>

      //<editor-fold desc="Getters and setters" defaultstate="collapsed">

    /**
     * @return returns an ObservableList of no-reference {@link domain.Reservation}.
     */
    public ObservableList<Reservation> getMaterials() {
        return FXCollections.unmodifiableObservableList(eObservableList);
    }

    /**
     * @return returns an ObservableList of no-reference {@link domain.MaterialIdentifier}.
     */
    public ObservableList<MaterialIdentifier> getMaterialIdentifiers() {
        return FXCollections.unmodifiableObservableList(materialIdentifiersObservableList);
    }
    //</editor-fold>

      //<editor-fold desc="Actions" defaultstate="collapsed">

    /**
     * Saves a new {@link domain.Reservation} in the database.
     *
     * @param reservation the Reservation to save.
     * @return the reservation with updates database fields.
     */
    @Override
    public Reservation add(Reservation reservation) {
        if (getItemById(reservation.getId()) != null)
            throw new ReservationAlreadyExistsException(reservation);
        Reservation toPersist = copyDefensively(reservation);
        persistReservation(toPersist);
        addReservationSynced(toPersist);
        return copyDefensively(toPersist);
    }

    /**
     * Removes a {@link domain.Reservation} from the database.
     *
     * @param reservation the Reservation to remove.
     */
    @Override
    public void remove(Reservation reservation) {
        Reservation remove = getItemByIdForced(reservation.getId(), "Cannot remove a nonexistent Reservation");
        persistence.remove(remove);
        persistence.remove(remove.getMaterialIdentifiersList());
        removeReservationSynced(remove);
    }

    /**
     * Updates a {@link domain.Reservation} in the database.
     *
     * @param reservation the Reservation to update.
     */
    @Override
    public void update(Reservation reservation) {
        Reservation original = getItemByIdForced(reservation.getId(), "Cannot update a record that does not appear in the database.");
        for (MaterialIdentifier mi : reservation.getMaterialIdentifiersList()) {
            if (mi.getId() == 0) {
                persistence.persist(mi);
            } else {
                persistence.merge(mi);
            }
        }

        List<MaterialIdentifier> remove = new ArrayList<>(original.getMaterialIdentifiersList());
        remove.removeAll(reservation.getMaterialIdentifiersList());
        remove.forEach(materialIdentifier -> persistence.remove(materialIdentifier));

        Reservation toSave = copyDefensively(reservation);

        persistence.merge(toSave);
        removeReservationSynced(original);
        addReservationSynced(toSave);
    }

    //</editor-fold>
    
        //<editor-fold desc="Private actions" defaultstate="collapsed">

     private void addReservationSynced(Reservation reservation) {
        addReservationLocally(reservation);
        addReservationToObservers(copyDefensively(reservation));
    }
     private void addReservationLocally(Reservation reservation) {
        this.eObservableList.add(reservation);
        this.materialIdentifiers.addAll(reservation.getMaterialIdentifiersList());
    }
     private void addReservationToObservers(Reservation reservation) {
        this.eObservableList.add(reservation);
        this.materialIdentifiers.addAll(reservation.getMaterialIdentifiersList());
    }
     private void removeReservationSynced(Reservation reservation) {
        removeReservationLocally(reservation);
        removeReservationFromObservers(reservation);
    }
     private void removeReservationLocally(Reservation reservation) {
        removeById(this.eObservableList, reservation.getId());
        removeById(this.materialIdentifiers, reservation.getMaterialIdentifiersList().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
    }

    private void removeReservationFromObservers(Reservation reservation) {
        removeById(this.eObservableList, reservation.getId());
        removeById(this.materialIdentifiers, reservation.getMaterialIdentifiersList().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
    }
    private void removeById(Collection<? extends IEntity> collection, int id) {
        collection.removeIf(e -> e.getId() == id);
    }
     private void removeById(Collection<? extends IEntity> collection, Collection<Integer> ids) {
        collection.removeIf(e -> ids.contains(e.getId()));
    }
    private void persistReservation(Reservation reservation) {
        persistence.persist(reservation);
        persistence.persist(reservation.getMaterialIdentifiersList());
    }
    //</editor-fold>

    
    
    
    
    
    
    
    
    
    
//    private ObservableList<MaterialIdentifier> materialIdentifiers;
//    private PersistenceEnforcer persistence;
//    private List<Reservation> reservations = new ArrayList<>();
//
//    public ReservationRepository(PersistenceEnforcer persistence) {
//        this.persistence = persistence;
//        //List<Reservation> initialize = persistence.retrieve(Reservation.class);
//        reservations = new ArrayList<>(/*initialize*/);
//    }
//    
//    public ObservableList<Reservation> getReservations(){
//        Reservation r = new Reservation();
//        LocalDate pickUpDate = LocalDate.of(2016,Month.JANUARY,24);
//        r.setBringBackDate(pickUpDate.plusDays(7));
//        r.setPickUpDate(pickUpDate);
//        r.setReservatieDate(LocalDate.of(2016, Month.JANUARY, 20));
//        r.setUserEmail("ik@hotmail.com");
//        MaterialIdentifier mi = new MaterialIdentifier(new Material("Wereldbol"),Visibility.Docent);
//        List<MaterialIdentifier> l = new ArrayList<>();
//        l.add(mi);
//        r.setMaterialIdentifiersList(l);
//        reservations.add(r);
//        return FXCollections.observableList(this.reservations);
//    }
//     /**
//     * Saves a new {@link domain.Reservation} in the database.
//     *
//     * @param reservation the Reservation to save.
//     * @return the reservation with updates database fields.
//     */
//    public Reservation addReservation(Reservation reservation) {
//        if (getReservationById(reservation.getId()) != null)
//            throw new ReservationAlreadyExistsException(reservation);
//        Reservation toPersist = copyDefensively(reservation);
//        persistReservation(toPersist);
//        addReservationSynced(toPersist);
//        return copyDefensively(toPersist);
//    }
//    /**
//     * Removes a {@link domain.Reservation} from the database.
//     *
//     * @param reservation the Reservation to remove.
//     */
//    public void removeReservation(Reservation reservation) {
//        Reservation remove = getReservationById(reservation.getId());
//        if (remove == null)
//            throw new ReservationNotFoundException(reservation);
//        persistence.remove(remove);
//        persistence.remove(remove.getMaterialIdentifiersList());
//        removeReservationSynced(remove);
//    }
//    /**
//     * Updates a {@link domain.Reservation} in the database.
//     *
//     * @param reservation the Reservation to update.
//     */
//    public void update(Reservation reservation) {
//        Reservation original = getReservationByIdForced(reservation.getId(), "Cannot update a record that does not appear in the database.");
//        for (MaterialIdentifier mi : reservation.getMaterialIdentifiersList()) {
//            if (mi.getId() == 0) {
//                persistence.persist(mi);
//            } else {
//                persistence.merge(mi);
//            }
//        }
//
//        List<MaterialIdentifier> remove = new ArrayList<>(original.getMaterialIdentifiersList());
//        remove.removeAll(reservation.getMaterialIdentifiersList());
//        remove.forEach(materialIdentifier -> persistence.remove(materialIdentifier));
//
//        Reservation toSave = copyDefensively(reservation);
//
//        persistence.merge(toSave);
//        removeReservationSynced(original);
//        addReservationSynced(toSave);
//    }
//    
//    private Reservation getReservationByIdForced(int id, String exceptionMessage) {
//        Reservation found = getReservationById(id);
//        if (found == null)
//            throw new MaterialNotFoundException(exceptionMessage);
//        return found;
//    }
//    public Reservation getReservationById(int reservationId) {
//         Reservation reservation = reservationId == 0 ? null : this.reservations
//                .stream()
//                .filter(r -> r.getId()== reservationId)
//                .findAny()
//                .orElse(null);
//         
//         return reservation;
//    }
//    //public Reservation getReservationByUser(int userId) {
////         Reservation reservation = userId == 0 ? null : this.reservations
////                .stream()
////                .filter(r -> r.getId()== userId)
////                .findAny()
////                .orElse(null);
////         if (reservation == null)
////            throw new ReservationNotFoundException("This user hasn't any reservations");
////         return reservation;
////    }
//    
//   
//    
////    public void conflictTracing() throws ParseException {
////        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
////
////        for (Reservation r1 : reservations) {
////
////            for (Reservation r2 : reservations) {
////                if (!r1.equals(r2)) {
////                    if (r1.getPickUpDate().before(r2.getBringBackDate()) && (r2.getPickUpDate().before(r1.getBringBackDate()))) {
////                        r1.setConflictFlag(true);
////                        //r1.getConflictWithUsers().put(materialIdentifier, Integer.MIN_VALUE));//de gebruiker die conflicteert toevoevoegen aan de reservatie
////                        r2.setConflictFlag(true);
////                        //r2.getConflictWithUsers().add(r1.getUserId());
////                        
////                    }
////                }
////
////            }
////
////        }
////
////    }
//    
    
//
//    private ObservableList<MaterialIdentifier> materialIdentifiers;
//    private PersistenceEnforcer persistence;
//    private List<Reservation> reservations = new ArrayList<>();
//
//    public ReservationRepository(PersistenceEnforcer persistence) {
//        this.persistence = persistence;
//        //List<Reservation> initialize = persistence.retrieve(Reservation.class);
//        reservations = new ArrayList<>(/*initialize*/);
//    }
//    
//    public ObservableList<Reservation> getReservations(){
//        Reservation r = new Reservation();
//        LocalDate pickUpDate = LocalDate.of(2016,Month.JANUARY,24);
//        r.setBringBackDate(pickUpDate.plusDays(7));
//        r.setPickUpDate(pickUpDate);
//        r.setReservatieDate(LocalDate.of(2016, Month.JANUARY, 20));
//        r.setUserEmail("ik@hotmail.com");
//        MaterialIdentifier mi = new MaterialIdentifier(new Material("Wereldbol"),Visibility.Docent);
//        List<MaterialIdentifier> l = new ArrayList<>();
//        l.add(mi);
//        r.setMaterialIdentifiersList(l);
//        reservations.add(r);
//        return FXCollections.observableList(this.reservations);
//    }
//     /**
//     * Saves a new {@link domain.Reservation} in the database.
//     *
//     * @param reservation the Reservation to save.
//     * @return the reservation with updates database fields.
//     */
//    public Reservation addReservation(Reservation reservation) {
//        if (getReservationById(reservation.getId()) != null)
//            throw new ReservationAlreadyExistsException(reservation);
//        Reservation toPersist = copyDefensively(reservation);
//        persistReservation(toPersist);
//        addReservationSynced(toPersist);
//        return copyDefensively(toPersist);
//    }
//    /**
//     * Removes a {@link domain.Reservation} from the database.
//     *
//     * @param reservation the Reservation to remove.
//     */
//    public void removeReservation(Reservation reservation) {
//        Reservation remove = getReservationById(reservation.getId());
//        if (remove == null)
//            throw new ReservationNotFoundException(reservation);
//        persistence.remove(remove);
//        persistence.remove(remove.getMaterialIdentifiersList());
//        removeReservationSynced(remove);
//    }
//    /**
//     * Updates a {@link domain.Reservation} in the database.
//     *
//     * @param reservation the Reservation to update.
//     */
//    public void update(Reservation reservation) {
//        Reservation original = getReservationByIdForced(reservation.getId(), "Cannot update a record that does not appear in the database.");
//        for (MaterialIdentifier mi : reservation.getMaterialIdentifiersList()) {
//            if (mi.getId() == 0) {
//                persistence.persist(mi);
//            } else {
//                persistence.merge(mi);
//            }
//        }
//
//        List<MaterialIdentifier> remove = new ArrayList<>(original.getMaterialIdentifiersList());
//        remove.removeAll(reservation.getMaterialIdentifiersList());
//        remove.forEach(materialIdentifier -> persistence.remove(materialIdentifier));
//
//        Reservation toSave = copyDefensively(reservation);
//
//        persistence.merge(toSave);
//        removeReservationSynced(original);
//        addReservationSynced(toSave);
//    }
//    
//    private Reservation getReservationByIdForced(int id, String exceptionMessage) {
//        Reservation found = getReservationById(id);
//        if (found == null)
//            throw new MaterialNotFoundException(exceptionMessage);
//        return found;
//    }
//    public Reservation getReservationById(int reservationId) {
//         Reservation reservation = reservationId == 0 ? null : this.reservations
//                .stream()
//                .filter(r -> r.getId()== reservationId)
//                .findAny()
//                .orElse(null);
//         
//         return reservation;
//    }
//    //public Reservation getReservationByUser(int userId) {
////         Reservation reservation = userId == 0 ? null : this.reservations
////                .stream()
////                .filter(r -> r.getId()== userId)
////                .findAny()
////                .orElse(null);
////         if (reservation == null)
////            throw new ReservationNotFoundException("This user hasn't any reservations");
////         return reservation;
////    }
//    
//   
//    
////    public void conflictTracing() throws ParseException {
////        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
////
////        for (Reservation r1 : reservations) {
////
////            for (Reservation r2 : reservations) {
////                if (!r1.equals(r2)) {
////                    if (r1.getPickUpDate().before(r2.getBringBackDate()) && (r2.getPickUpDate().before(r1.getBringBackDate()))) {
////                        r1.setConflictFlag(true);
////                        //r1.getConflictWithUsers().put(materialIdentifier, Integer.MIN_VALUE));//de gebruiker die conflicteert toevoevoegen aan de reservatie
////                        r2.setConflictFlag(true);
////                        //r2.getConflictWithUsers().add(r1.getUserId());
////                        
////                    }
////                }
////
////            }
////
////        }
////
////    }
//    
//     private void addReservationSynced(Reservation reservation) {
//        addReservationLocally(reservation);
//        addReservationToObservers(copyDefensively(reservation));
//    }
//     private void addReservationLocally(Reservation reservation) {
//        this.reservations.add(reservation);
//        this.materialIdentifiers.addAll(reservation.getMaterialIdentifiersList());
//    }
//     private void addReservationToObservers(Reservation reservation) {
//        this.reservations.add(reservation);
//        this.materialIdentifiers.addAll(reservation.getMaterialIdentifiersList());
//    }
//     private void removeReservationSynced(Reservation reservation) {
//        removeReservationLocally(reservation);
//        removeReservationFromObservers(reservation);
//    }
//     private void removeReservationLocally(Reservation reservation) {
//        removeById(this.reservations, reservation.getId());
//        removeById(this.materialIdentifiers, reservation.getMaterialIdentifiersList().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
//    }
//
//    private void removeReservationFromObservers(Reservation reservation) {
//        removeById(this.reservations, reservation.getId());
//        removeById(this.materialIdentifiers, reservation.getMaterialIdentifiersList().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
//    }
//    private void removeById(Collection<? extends IEntity> collection, int id) {
//        collection.removeIf(e -> e.getId() == id);
//    }
//     private void removeById(Collection<? extends IEntity> collection, Collection<Integer> ids) {
//        collection.removeIf(e -> ids.contains(e.getId()));
//    }
//    private void persistReservation(Reservation reservation) {
//        persistence.persist(reservation);
//        persistence.persist(reservation.getMaterialIdentifiersList());
//    }
}
