package domain;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FirmTest {

    private FirmWrapper toysInc, lego;

    @Before
    public void setUp() {
        toysInc = new FirmWrapper();
        lego = new FirmWrapper();
        toysInc.setId(10);
    }

    @Test
    public void testEqualSameId() {
        lego.setId(toysInc.getId());
        assertEquals(lego, toysInc);
        assertEquals(toysInc, lego);
    }

    @Test
    public void testEqualTwoDifferentObjects() {
        assertFalse(new Firm().equals(new Firm()));
    }

    @Test
    public void testEqualNoId() {
        assertEquals(lego, lego);
    }

    @Test
    public void testEqualDifferentId() {
        lego.setId(toysInc.getId() + 1);
        assertNotEquals(lego, toysInc);
        assertNotEquals(toysInc, lego);
    }

    @Test
    public void testEqualNull() {
        assertNotEquals(null, toysInc);
    }

    class FirmWrapper extends Firm {
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