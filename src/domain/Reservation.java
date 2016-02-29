/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import static com.google.common.base.MoreObjects.toStringHelper;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import util.ImmutabilityHelper;

/**
 *
 * @author matthiasseghers
 */
@Entity
public class Reservation implements Serializable, IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private User user;
    private List<MaterialIdentifier> materialIdentifiersList;
    private ObjectProperty<LocalDate>  reservationDate = new SimpleObjectProperty<>() ;
    private ObjectProperty<LocalDate>  pickUpDate= new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate>  bringBackDate= new SimpleObjectProperty<>();
    
   
    /**
    JPA-constructor
    */
    public Reservation() { 
    }
    
    /**
     * Copy constructor
     * @param reservation 
     */
    public Reservation(Reservation reservation){
        this.id=reservation.id;
        this.user= reservation.user;
        this.materialIdentifiersList= (List<MaterialIdentifier>) ImmutabilityHelper.copyCollectionDefensively(reservation.materialIdentifiersList, this);
        this.reservationDate=reservation.reservationDate;
        this.pickUpDate=reservation.pickUpDate;
        this.bringBackDate=reservation.bringBackDate;
        
    }

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">
    @Override
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MaterialIdentifier> getMaterialIdentifiersList() {
        return materialIdentifiersList;
    }

    public void setMaterialIdentifiersList(List<MaterialIdentifier> materialIdentifiersList) {
        this.materialIdentifiersList = materialIdentifiersList;
    }

    public LocalDate getReservationDate() {
        return reservationDate.get();
    }

    public void setReservatieDate(LocalDate reservationDate) {
        this.reservationDate.set(reservationDate);
    }

    public LocalDate getPickUpDate() {
        return pickUpDate.get();
    }

    public void setPickUpDate(LocalDate pickUpDate) {
        this.pickUpDate.set(pickUpDate);
    }

    public LocalDate getBringBackDate() {
        return bringBackDate.get();
    }

    public void setBringBackDate(LocalDate bringBackDate) {
        this.bringBackDate.set(bringBackDate);
    }

    public ObjectProperty<LocalDate> getBringBackDateProperty() {
        return bringBackDate;
    }

//    public void setBringBackDateProperty(ObjectProperty<LocalDate> bringBackDateProperty) {
//        this.bringBackDateProperty = bringBackDateProperty;
//    }

    public ObjectProperty<LocalDate> getPickUpDateProperty() {
        return pickUpDate;
    }

//    public void setPickUpDateProperty(ObjectProperty<LocalDate> pickUpDateProperty) {
//        this.pickUpDateProperty = pickUpDateProperty;
//    }

    public ObjectProperty<LocalDate> getReservationDateProperty() {
        return reservationDate;
    }

//    public void setReservationDateProperty(ObjectProperty<LocalDate> reservationDateProperty) {
//        this.reservationDateProperty = reservationDateProperty;
//    }

    //</editor-fold>
    /**
     * De reservatie wordt vroeg tijdig stopgezet
     */
//    public void reservationSuspend(LocalDate earlyBringBack) {
//        this.bringBackDate = earlyBringBack;
//    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("user", user)
                .add("material identifiers list", materialIdentifiersList)
                .add("Date of reservation", reservationDate)
                .add("Date of pickup", pickUpDate)
                .add("Date of bringback", bringBackDate)
                .toString();
    }

}
