/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

public class CurricularTest {
    
    private CurricularWrapper eersteGraad,  tweedeGraad;
    
    @Before
    public void setUp() {
        eersteGraad= new CurricularWrapper();
        tweedeGraad= new CurricularWrapper();
        tweedeGraad.setId(8);
    }    
    
    @Test
    public void testEqualSameId(){
        eersteGraad.setId(tweedeGraad.getId());
        assertEquals(tweedeGraad, eersteGraad);
        assertEquals(eersteGraad, tweedeGraad);
    }
    
     @Test
    public void testEqualTwoDifferentObjects() {
        assertFalse(new Curricular().equals(new Curricular()));
    }

    @Test
    public void testEqualNoId() {
        assertEquals(eersteGraad, eersteGraad);
    }

    @Test
    public void testEqualDifferentId() {
        eersteGraad.setId(tweedeGraad.getId() + 1);
        assertNotEquals(eersteGraad, tweedeGraad);
        assertNotEquals(tweedeGraad, eersteGraad);
    }
    
    class CurricularWrapper extends Curricular {
        private int id;

        @Override
        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
