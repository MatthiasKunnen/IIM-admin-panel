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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class TargetGroupTest {
    
    private TargetGroupWrapper volwassenen,  laatsteJaarsStudenten;
    
    @Before
    public void setUp() {
        volwassenen= new TargetGroupWrapper();
        laatsteJaarsStudenten= new TargetGroupWrapper();
        laatsteJaarsStudenten.setId(8);
    }    
    
    @Test
    public void testEqualSameId(){
        volwassenen.setId(laatsteJaarsStudenten.getId());
        assertEquals(laatsteJaarsStudenten, volwassenen);
        assertEquals(volwassenen, laatsteJaarsStudenten);
    }
    
     @Test
    public void testEqualTwoDifferentObjects() {
        assertFalse(new Curricular().equals(new Curricular()));
    }

    @Test
    public void testEqualNoId() {
        assertEquals(volwassenen, volwassenen);
    }

    @Test
    public void testEqualDifferentId() {
        volwassenen.setId(laatsteJaarsStudenten.getId() + 1);
        assertNotEquals(volwassenen, laatsteJaarsStudenten);
        assertNotEquals(laatsteJaarsStudenten, volwassenen);
    }
    
    class TargetGroupWrapper extends TargetGroup {
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
