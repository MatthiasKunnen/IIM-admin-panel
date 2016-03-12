package domain;

import persistence.KeyConverter;

import java.io.Serializable;
import javax.persistence.*;

/**
 * This class holds the settings of the application.
 *
 * @param <E> the type of the setting.
 * @author Matthias Kunnen
 */
@Entity
public class Setting<E extends Serializable> implements Serializable, IEntity {

    public enum Key {
        KEEP_HISTORY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, name = "name")
    @Convert(converter = KeyConverter.class)
    private Key key;

    private E data;

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
    public Setting(Key key, E data) {
        this.key = key;
        this.data = data;
    }

    /**
     * Copy constructor.
     *
     * @param setting the setting to copy.
     */
    public Setting(Setting<E> setting) {
        this.id = setting.id;
        this.key = setting.key;
        this.data = setting.data;
    }

    public Key getKey() {
        return key;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
