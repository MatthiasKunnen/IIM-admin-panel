package domain;

import com.google.common.base.MoreObjects;
import exceptions.InvalidEmailException;
import exceptions.InvalidPhoneNumberException;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.validator.routines.EmailValidator;

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

        } else {

            throw new InvalidEmailException("Het emailadres is niet correct!");
        }
    }

    public String getPhoneNumber() {

        return phoneNumber;

    }

    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        this.phoneNumber = phoneNumber;

    }

    public String getWebsite() {

        return website;
    }

    public void setWebsite(String website) {
        this.website = website;

    }

    public Firm(Firm firm) {
        this.id = firm.id;
        this.name = firm.name;
        this.email = firm.email;
        this.phoneNumber = firm.phoneNumber;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Firm)) {
            return false;
        }
        Firm firm = (Firm) obj;
        return firm.getId() != 0 && firm.getId() == this.id || super.equals(obj);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("ID", id)
                .add("Name", name)
                .add("E-mail", email)
                .add("Phone number", phoneNumber)
                .toString();
    }
}
