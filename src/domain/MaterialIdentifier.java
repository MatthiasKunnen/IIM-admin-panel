package domain;

import com.google.common.base.MoreObjects;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import persistence.VisibilityConverter;
import util.ImmutabilityHelper;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(value = AccessType.PROPERTY)
public class MaterialIdentifier implements Serializable, IEntity {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(value = AccessType.FIELD)
    private int id;
    private Material info;
    private SimpleStringProperty place = new SimpleStringProperty();
    private SimpleObjectProperty<Visibility> visibility = new SimpleObjectProperty<>();
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">
    public int getId() {
        return id;
    }

    public String getPlace() {
        return this.place.getValue();
    }

    public void setPlace(String place) {
        this.place.setValue(place);
    }

    @Transient
    public SimpleStringProperty getPlaceProperty() {
        return this.place;
    }

    @ManyToOne
    public Material getInfo() {
        return this.info;
    }

    public void setInfo(Material info) {
        this.info = info;
    }

    @Convert(converter = VisibilityConverter.class)
    public Visibility getVisibility() {
        return visibility.getValue();
    }

    public void setVisibility(Visibility visibility) {
        this.visibility.setValue(visibility);
    }

    @Transient
    public SimpleObjectProperty<Visibility> getVisibilityProperty() {
        return this.visibility;
    }
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    /**
     * JPA constructor
     */
    protected MaterialIdentifier() {

    }

    /**
     * @param info The material
     */
    public MaterialIdentifier(Material info, Visibility visibility) {
        this.setInfo(info);
        this.setVisibility(visibility);
    }

    public MaterialIdentifier(MaterialIdentifier identifier) {
        this(identifier, ImmutabilityHelper.copyDefensively(identifier.info));
    }

    public MaterialIdentifier(MaterialIdentifier identifier, Material info) {
        this.id = identifier.id;
        this.info = info;
        this.place.setValue(identifier.place.getValue());
        this.visibility.setValue(identifier.visibility.getValue());
    }
    //</editor-fold>

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("info", info.getName())
                .add("place", place.getValue())
                .add("visibility", visibility.getValue())
                .toString();

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof MaterialIdentifier)) return false;
        MaterialIdentifier o2 = (MaterialIdentifier) obj;
        return o2.getId() != 0 && getId() == o2.getId() || super.equals(obj);
    }
}