/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
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
    private LocalDateTime pickUp = LocalDateTime.of(2016, Month.FEBRUARY, 1, 16, 0);
    private LocalDateTime bringBack = LocalDateTime.of(2016, Month.FEBRUARY, 14, 7, 0);
    private LocalDateTime resDate = LocalDateTime.of(2016, Month.JANUARY, 14, 2, 15);
    
    
    
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
        res.setCreationDate(resDate);
        Assert.assertEquals(resDate, res.getCreationDate());
    }
    
    @Test
    public void testSetPickUpDate(){
        res.setStartDate(pickUp);
        Assert.assertEquals(pickUp, res.getStartDate());
    }
    
    @Test
    public void testSetBringBackDate(){
        res.setStartDate(pickUp);
        res.setEndDate(bringBack);
        Assert.assertEquals(bringBack, res.getEndDate());
    }
    
    
    //When bringBack is befor pickUp, bringBack should be standard value (4 days after pickUp)
    @Test
    public void testSetBringBackDateBeforePickup(){
        res.setStartDate(pickUp);
        res.setEndDate(resDate);
        
        Assert.assertEquals(pickUp.plusDays(4), res.getEndDate());
    }
    
    
    
    
    
    
}
