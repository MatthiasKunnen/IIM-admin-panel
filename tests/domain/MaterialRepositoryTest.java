/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import persistence.PersistenceEnforcer;
import repository.MaterialRepository;

/**
 *
 * @author matthiasseghers
 */
public class MaterialRepositoryTest {
    
    private static final String name="Wereldbol";
    private Material material;
    
    //private DomainController dc;
    @Mock
    private PersistenceEnforcer persistenceEnforcer;
//    private MaterialRepository materialRepository;

     public MaterialRepositoryTest()
    {}
     @Before
    public void before() {
        MockitoAnnotations.initMocks(this); 
//        materialRepository= new MaterialRepository(PersistenceEnforcer);
//        dc= new DomainController();
        material=new Material(name);
    }
    @Test
    public void testAddMaterial(){
        Mockito.when(persistenceEnforcer.persist(material));
    }
//    @Test
//    public void testAddMaterial(){
//        
//        Mockito.when(dc.addMaterial(material)).thenReturn(material);
//        dc.addMaterial(material);
//        Material m = materialRepository.getMaterialByName(name);
//        System.out.println(m.getName());
//        Assert.assertEquals(material.getName(),m.getName());
//        Mockito.verify(materialRepository).add(material);
//        Mockito.verify(PersistenceEnforcer).persist(material);
//        
//    }
}
