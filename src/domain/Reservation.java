/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author matthiasseghers
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long reservationId;

    private int userId;
    private List<MaterialIdentifier> materialIdentifiersList;
    private Date reservationDate;
    private Date ophaalmoment;
    private Date indienmoment;
    private boolean conflictFlag;
    
    public Reservation(Long reservationId, int userId, List<MaterialIdentifier> materialIdentifiersList, Date reservationDate, Date ophaalmoment, Date indienmoment, boolean conflictflag){
        this.userId=userId;
        this.reservationId=reservationId;
        this.materialIdentifiersList=materialIdentifiersList;
        this.reservationDate=reservationDate;
        this.ophaalmoment=ophaalmoment;
        this.indienmoment=indienmoment;
        this.conflictFlag=conflictflag;
    }
   
    
    //<editor-fold desc="Getters and setters" defaultstate="collapsed">

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<MaterialIdentifier> getMaterialIdentifierList() {
        return materialIdentifiersList;
    }

    public void setMaterialIdentifierList(List<MaterialIdentifier> materialIdentifierList) {
        this.materialIdentifiersList = materialIdentifierList;
    }

    public Date getReservationDatum() {
        return reservationDate;
    }

    public void setReservatieDatum(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public Date getOphaalmoment() {
        return ophaalmoment;
    }

    public void setOphaalmoment(Date ophaalmoment) {
        this.ophaalmoment = ophaalmoment;
    }

    public Date getIndienmoment() {
        return indienmoment;
    }

    public void setIndienmoment(Date indienmoment) {
        this.indienmoment = indienmoment;
    }

    public boolean isConflictFlag() {
        return conflictFlag;
    }

    public void setConflictFlag(boolean conflictFlag) {
        this.conflictFlag = conflictFlag;
    }
    
 //</editor-fold>
    
     /**
     * De reservatie wordt vroeg tijdig stopgezet
     */
    public void reservationSuspend(Date vervroegdeEinddatum){
        this.indienmoment= vervroegdeEinddatum;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Reservatie[ id=" + reservationId + " ]";
    }
    
}
