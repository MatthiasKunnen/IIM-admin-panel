package domain;

import java.util.*;

import persistence.*;

public class MaterialRepository {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private Set<Material> materials;
    private Set<MaterialIdentifier> materialIdentifiers;
    private PersistenceEnforcer persistence;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public MaterialRepository(PersistenceEnforcer persistence) {
        this.materials = new HashSet<>();
        this.materialIdentifiers = new HashSet<>();
        this.persistence = persistence;
    }
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">

    public Set<Material> getMaterials() {
        return Collections.unmodifiableSet(materials);
    }

    public Set<MaterialIdentifier> getMaterialIdentifiers() {
        return Collections.unmodifiableSet(materialIdentifiers);
    }
    //</editor-fold>
}