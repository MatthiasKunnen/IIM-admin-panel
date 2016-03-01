/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.time.LocalDate;
import java.time.Month;
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

    private Reservation res;
    private User user = new User();
    private List<MaterialIdentifier> resList;
    private MaterialIdentifier matId;
    private Material mat;
    private LocalDate pickUp = LocalDate.of(2016, Month.FEBRUARY, 1);
    private LocalDate bringBack = LocalDate.of(2016, Month.FEBRUARY, 14);
    private LocalDate resDate = LocalDate.of(2016, Month.JANUARY, 14);
    
    
    
    @Before
    public void init(){
        res = new Reservation();
        mat = new Material("Tool");
        matId = new MaterialIdentifier(mat, Visibility.Student);
        resList = new ArrayList<>();
        resList.add(matId);
        
     }
    
    
    @Test
    public void testSetMaterialIdentifierList(){
        res.setMaterialIdentifiersList(resList);
        Assert.assertEquals(resList, res.getMaterialIdentifiersList());
    }
    
    @Test
    public void testSetReservationDate(){
        res.setReservatieDate(resDate);
        Assert.assertEquals(resDate, res.getReservationDate());
    }
    
    @Test
    public void testSetPickUpDate(){
        res.setPickUpDate(pickUp);        
        Assert.assertEquals(pickUp, res.getPickUpDate());
    }
    
    @Test
    public void testSetBringBackDate(){
        res.setPickUpDate(pickUp);
        res.setBringBackDate(bringBack);
        Assert.assertEquals(bringBack, res.getBringBackDate());
    }
    
    
    //When bringBack is befor pickUp, bringBack should be standard value (4 days after pickUp)
    @Test
    public void testSetBringBackDateBeforePickup(){
        res.setPickUpDate(pickUp);
        res.setBringBackDate(resDate);
        
        Assert.assertEquals(pickUp.plusDays(4), res.getBringBackDate());
    }
    
    
    
    
    
    
}
