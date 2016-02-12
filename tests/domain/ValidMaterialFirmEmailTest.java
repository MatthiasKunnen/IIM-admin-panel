package domain;

import exceptions.InvalidEmailException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class ValidMaterialFirmEmailTest {
    private String email;
    private Material material;

    @Before
    public void before() {
        material = new Material();
    }

    public ValidMaterialFirmEmailTest(String email) {
        this.email = email;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(
                new Object[][]{
                        {"Fred+Bloggs”@example.com"},
                        {"“Joe\\Blow”@example.com"},
                        {"customer/department=shipping@example.com"},
                        {"$A12345@example.com"},
                        {"!def!xyz%abc@example.com"},
                        {"_somename@example.com"},
                        {"much.“more\\ unusual”@example.com"},
                });
    }

    @Test()
    public void testEmail() throws InvalidEmailException {
        material.setFirmEmail(email);
    }
}
