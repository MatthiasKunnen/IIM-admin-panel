package domain;

import exceptions.InvalidEmailException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class InvalidMaterialFirmEmailTest {
    private String email;
    private Material material;

    @Before
    public void before() {
        material = new Material();
    }

    public InvalidMaterialFirmEmailTest(String email) {
        this.email = email;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getTestParameters() {
        return Arrays.asList(
                new Object[][]{
                        {"plainaddress"},
                        {"#@%^%#$@#$@#.com"},
                        {"@example.com"},
                        {"Joe Smith <email@example.com>"},
                        {"email.example.com"},
                        {"email@example@example.com"},
                        {".email@example.com"},
                        {"email.@example.com"},
                        {"email..email@example.com"},
                        {"email@example.com (Joe Smith)"},
                        {"email@example"},
                        {"email@-example.com"},
                        {"email@example.web"},
                        {"email@111.222.333.44444"},
                        {"email@example..com"},
                        {"Abc..123@example.com"}
                });
    }

    @Test(expected = InvalidEmailException.class)
    public void testEmail() throws InvalidEmailException {
        material.setFirmEmail(email);
        System.out.println(email);
    }
}
