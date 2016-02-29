package domain;

import exceptions.InvalidEmailException;
import exceptions.InvalidPhoneNumberException;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.apache.commons.validator.EmailValidator;

@Entity
public class Firm implements IEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;
    private String email;
    private String phoneNumber;
    private String website;

    public Firm() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidEmailException {
        if (EmailValidator.getInstance().isValid(email)) {
            this.email = email;
        }

        throw new InvalidEmailException("Het emailadres is niet correct!");

    }

    public String getPhoneNumber() {

        return phoneNumber;

    }

    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if (phoneNumber.matches("^[0-9]{10}$")) {
            this.phoneNumber = phoneNumber;
        }

        throw new InvalidPhoneNumberException("Het telefoonnummer is niet correct");
    }

    public String getWebsite() {

        return website;
    }

    public void setWebsite(String website) {
        this.website = website;

    }

    @Override
    public int getId() {
        return this.id;
    }
}
