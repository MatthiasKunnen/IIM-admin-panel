package domain;

import persistence.KeyConverter;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This class holds the settings of the application.
 *
 * @author Matthias Kunnen
 */
@Entity
public class Setting implements Serializable, IEntity {

    public enum Key {
        KEEP_HISTORY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, name = "name")
    @Convert(converter = KeyConverter.class)
    private Key key;

    private String data;

    /**
     * JPA constructor.
     */
    protected Setting() {
    }

    /**
     * Default constructor.
     * @param key the key used to refer to this setting.
     * @param data the data of this setting.
     */
    public Setting(Key key, String data) {
        this.key = key;
        this.data = data;
    }

    /**
     * Copy constructor.
     *
     * @param setting the setting to copy.
     */
    public Setting(Setting setting) {
        this.id = setting.id;
        this.key = setting.key;
        this.data = setting.data;
    }

    public Key getKey() {
        return key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
