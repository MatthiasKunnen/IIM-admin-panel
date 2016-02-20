package util;

import org.junit.Assert;
import org.junit.Test;


public class BCryptTest {
    private final String PASSWORD = "!HQ@s!X8l5aDjYU!xc7amQG!S2NXDIkqzJ24G!t";
    private final String SALT = BCrypt.generateSalt();
    private final String HASH = BCrypt.hashPassword(PASSWORD, SALT);

    @Test
    public void testCreateHashIsConsistent() {
        Assert.assertEquals(HASH, BCrypt.hashPassword(PASSWORD, SALT));
    }

    @Test
    public void testCheckValidPassword() {
        Assert.assertTrue(BCrypt.checkPassword(PASSWORD, HASH));
    }

    @Test
    public void testWrongPasswordIsNotValid() {
        Assert.assertFalse(BCrypt.checkPassword(PASSWORD + "T", HASH));
    }

    @Test
    public void testWrongHashIsNotValid() {
        Assert.assertFalse(BCrypt.checkPassword(PASSWORD, HASH + "T"));
    }
}