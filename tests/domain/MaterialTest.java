package domain;

import exceptions.InvalidPriceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Suite.class)
@Suite.SuiteClasses({InvalidMaterialFirmEmailTest.class, ValidMaterialFirmEmailTest.class})
public class MaterialTest {

    private Material material;
    @Before
    public void setUp() throws Exception {
        material = new Material();
    }

    @Test(expected = InvalidPriceException.class)
    public void testNegativePrice() throws Exception {
        material.setPrice(BigDecimal.ONE.negate());
    }

    @Test
    public void testAddIdentifier() throws Exception {
        MaterialIdentifier mi = new MaterialIdentifier();
        material.addIdentifier(mi);
        Assert.assertEquals(1, material.getIdentifiers().size());
    }

    @Test
    public void testRemoveIdentifier() throws Exception {
        MaterialIdentifier mi = new MaterialIdentifier();
        material.addIdentifier(mi);
        Assert.assertEquals(1, material.getIdentifiers().size());
        material.removeIdentifier(mi);
        Assert.assertEquals(0, material.getIdentifiers().size());
    }
    
    @Test
    public void testSetFirm(){
        Firm firma = new Firm();
        material.setFirm(firma);
        Assert.assertEquals(firma, material.getFirm());
    }
    
    @Test
    public void testCurriculars(){
        List<Curricular> curriculars = new ArrayList<>(Arrays.asList(new Curricular("Aardrijkskunde"), new Curricular("Fysica")));
        material.setCurricular(curriculars);
        Assert.assertTrue(curriculars.containsAll(material.getCurricular()) && material.getCurricular().containsAll(curriculars));
    }
}