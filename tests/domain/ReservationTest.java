/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Pieter
 */
public class ReservationTest {
    
    private List<MaterialIdentifier> reservationList = new ArrayList<>();
    private int userId = 007;
    private Long reservationId= new Long(007);
    private GregorianCalendar reservationDate= new GregorianCalendar(2016,01,01);
    private GregorianCalendar pickUpDate= new GregorianCalendar(2016,02,07);
    private GregorianCalendar bringBackDate= new GregorianCalendar(2016,02,14);
    private boolean conflictFlag= false;
    private Reservation res;
    
    @Before
    public void init(){
        Material hamer = new Material("Hamer");
        MaterialIdentifier redHamer = new MaterialIdentifier(hamer,Visibility.Student);
        reservationList.add(redHamer);
        res = new Reservation(reservationId,userId,reservationList,reservationDate,pickUpDate,bringBackDate,conflictFlag);

    }
    
    @Test
    public void testConstructor(){
                
        Assert.assertEquals(userId,res.getUserId());
        Assert.assertEquals(reservationId, res.getReservationId());
        Assert.assertEquals(reservationDate, res.getReservationDate());
        Assert.assertEquals(pickUpDate, res.getPickUpDate());
        Assert.assertEquals(bringBackDate,res.getBringBackDate());
        Assert.assertEquals(conflictFlag, res.isConflictFlag());
    
    }
    
    @Test
    public void testReservationSuspend(){
        GregorianCalendar early =  new GregorianCalendar(2016, 2, 8);
        res.reservationSuspend(early);
        Assert.assertEquals(early, res.getBringBackDate());
    }
    
    
    
    
    
    
}
