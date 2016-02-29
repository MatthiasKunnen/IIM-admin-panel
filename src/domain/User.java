package domain;

import com.google.common.base.MoreObjects;
import persistence.UserTypeConverter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Holds a user of the application.
 */
@Entity
@Table(name = "\"user\"")
public class User implements Serializable {
    //<editor-fold desc="Declarations" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String email;
    private String faculty, firstName, lastName, telNumber;

    @Convert(converter = UserTypeConverter.class)
    private Type type;

    public enum Type {
        STUDENT, STAFF
    }
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    protected User() {
    }

    public User(User user) {
        this.id = user.id;
        this.faculty = user.faculty;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.telNumber = user.telNumber;
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">
    public int getId() {
        return this.id;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public Type getType() {
        return type;
    }

    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("ID", id)
                .add("First name", firstName)
                .add("Last name", lastName)
                .add("Type", type)
                .add("Faculty", faculty)
                .add("Email", email)
                .add("Telephone number", telNumber)
                .toString();
    }
    //</editor-fold>
}
