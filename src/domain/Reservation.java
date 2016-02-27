/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author matthiasseghers
 */
@Entity
public class Reservation implements Serializable,IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userEmail;
    private List<MaterialIdentifier> materialIdentifiersList;
    private LocalDate reservationDate;
    private LocalDate pickUpDate;
    private LocalDate bringBackDate;
    private boolean conflictFlag;
    private Map<MaterialIdentifier, Integer> conflictWithUsers=new HashMap<>();;
    
    
    public Reservation(){}
   
    
    //<editor-fold desc="Getters and setters" defaultstate="collapsed">

    @Override
    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public List<MaterialIdentifier> getMaterialIdentifierList() {
        return materialIdentifiersList;
    }

    public void setMaterialIdentifierList(List<MaterialIdentifier> materialIdentifierList) {
        this.materialIdentifiersList = materialIdentifierList;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservatieDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(LocalDate pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public LocalDate getBringBackDate() {
        return bringBackDate;
    }

    public void setBringBackDate(LocalDate bringBackDate) {
        this.bringBackDate = bringBackDate;
    }

    public boolean isConflictFlag() {
        return conflictFlag;
    }

    public void setConflictFlag(boolean conflictFlag) {
        this.conflictFlag = conflictFlag;
    }

    public Map<MaterialIdentifier, Integer> getConflictWithUsers() {
        return conflictWithUsers;
    }

    public void setConflictWithUsers(Map<MaterialIdentifier, Integer> conflictWithUsers) {
        this.conflictWithUsers = conflictWithUsers;
    }
    
 //</editor-fold>
    
     /**
     * De reservatie wordt vroeg tijdig stopgezet
     */
    public void reservationSuspend(LocalDate earlyBringBack){
        this.bringBackDate= earlyBringBack;
    }
    

    @Override
    public String toString() {
        return "domain.Reservatie[ id=" + id + " ]";
    }


    
}
