package domain;

import static com.google.common.base.MoreObjects.toStringHelper;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.*;

import persistence.LocalDateConverter;
import util.ImmutabilityHelper;

@Entity
@Access(AccessType.PROPERTY)
public class Reservation implements Serializable, IEntity {

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.FIELD)
    private int id;
    private User user;
    private List<MaterialIdentifier> materialIdentifiersList;
    private ObjectProperty<LocalDateTime>
            creationDate = new SimpleObjectProperty<>(),
            startDate = new SimpleObjectProperty<>(),
            endDate = new SimpleObjectProperty<>();
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    /**
     * JPA-constructor
     */
    public Reservation() {
    }

    /**
     * Copy constructor
     *
     * @param reservation
     */
    public Reservation(Reservation reservation) {
        this.id = reservation.id;
        this.user = reservation.user;
        this.materialIdentifiersList = (List<MaterialIdentifier>) ImmutabilityHelper.copyCollectionDefensively(reservation.materialIdentifiersList, this);
        this.creationDate = reservation.creationDate;
        this.startDate = reservation.startDate;
        this.endDate = reservation.endDate;

    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    @Override
    public int getId() {
        return id;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToMany
    public List<MaterialIdentifier> getMaterialIdentifiersList() {
        return materialIdentifiersList;
    }

    public void setMaterialIdentifiersList(List<MaterialIdentifier> materialIdentifiersList) {
        this.materialIdentifiersList = materialIdentifiersList;
    }

    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    public LocalDateTime getCreationDate() {
        return creationDate.get();
    }

    @Transient
    public ObjectProperty<LocalDateTime> reservationDateProperty() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime reservationDate) {
        this.creationDate.set(reservationDate);
    }

    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    public LocalDateTime getStartDate() {
        return startDate.get();
    }

    @Transient
    public ObjectProperty<LocalDateTime> startDateProperty() {
        return startDate;
    }

    public void setStartDate(LocalDateTime pickUpDate) {
        this.startDate.set(pickUpDate);
    }

    @Column(nullable = false)
    @Convert(converter = LocalDateConverter.class)
    public LocalDateTime getEndDate() {
        return endDate.get();
    }

    @Transient
    public ObjectProperty<LocalDateTime> endDateProperty() {
        return endDate;
    }

    public void setEndDate(LocalDateTime bringBackDate) {
        this.endDate.set(bringBackDate);
    }

    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    @Override
    public String toString() {
        return toStringHelper(this)
                .omitNullValues()
                .add("ID", id)
                .add("User", user)
                .add("Identifiers", materialIdentifiersList)
                .add("Reservation date", creationDate)
                .add("Start date", startDate)
                .add("End date", endDate)
                .toString();
    }
    //</editor-fold>
}