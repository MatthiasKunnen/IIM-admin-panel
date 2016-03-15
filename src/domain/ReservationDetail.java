package domain;

import persistence.LocalDateTimeConverter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class ReservationDetail implements Serializable, IEntity {
    //<editor-fold desc="Declarations" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.FIELD)
    private int id;

    @ManyToOne
    private MaterialIdentifier materialIdentifier;

    @ManyToOne
    private Reservation reservation;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime broughtBackDate;

    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public ReservationDetail() {
    }

    public ReservationDetail(MaterialIdentifier materialIdentifier, Reservation reservation) {
        this.materialIdentifier = materialIdentifier;
        this.reservation = reservation;
    }

    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    @Override
    public int getId() {
        return this.id;
    }

    public MaterialIdentifier getMaterialIdentifier() {
        return materialIdentifier;
    }

    public void setMaterialIdentifier(MaterialIdentifier materialIdentifier) {
        this.materialIdentifier = materialIdentifier;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public LocalDateTime getBroughtBackDate() {
        return broughtBackDate;
    }

    public void setBroughtBackDate(LocalDateTime broughtBackDate) {
        this.broughtBackDate = broughtBackDate;
    }

    public boolean isBroughtBack(){
        return this.broughtBackDate != null;
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">

    //</editor-fold>
}