/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Pieter
 */
public class MaterialIdentifierTest {
    
    public MaterialIdentifierTest() {
    }

    private MaterialIdentifier matID;
    private Material hamer;
    
    @Before
    public void initialize(){
        matID = new MaterialIdentifier();
        hamer = new Material("Hamer");
    }
    
    @Test
    public void testConstructor(){
        matID = new MaterialIdentifier(hamer, Visibility.Student);
        Assert.assertEquals(matID.getVisibility(), Visibility.Student);
        Assert.assertEquals(matID.getInfo(), hamer );
    }
       
    
    @Test
    public void testSetPlace(){
        matID.setPlace("keuken");
        Assert.assertEquals(matID.getPlace(), "keuken");
    }
    
       
    @Test
    public void testSetInfo(){
        matID.setInfo(hamer);
        Assert.assertEquals(matID.getInfo(), hamer);
        
    }
    
    @Test
    public void testSetVisibility(){
        matID.setVisibility(Visibility.Administrator);
        Assert.assertEquals(matID.getVisibility(), Visibility.Administrator);
    }
    
    @Test
    public void testSetId(){
        matID.setId(056);
        Assert.assertEquals(matID.getId(), 056);
    }
    
   
    
    
    
    
}
