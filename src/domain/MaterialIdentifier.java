package domain;

import persistence.VisibilityConverter;

import javax.persistence.*;

@Entity
public class MaterialIdentifier {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Material info;
    private String place;

    @Convert(converter = VisibilityConverter.class)
    private Visibility visibility;
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace() {
        return this.place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Material getInfo() {
        return this.info;
    }

    public void setInfo(Material info) {
        this.info = info;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
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
    //</editor-fold>
}