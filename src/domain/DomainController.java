package domain;

import java.util.Set;

import persistence.*;

public class DomainController {

     //<editor-fold desc="Variables" defaultstate="collapsed">
    private MaterialRepository materials;
    private PersistenceController persistence;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DomainController() {
        this.persistence = new PersistenceController();
        this.persistence.start();
        this.materials = new MaterialRepository(this.persistence.getEnforcer());
    }
    //</editor-fold>

    //<editor-fold desc="Getters" defaultstate="collapsed">
    public Set<Material> getMaterials() {
        return materials.getMaterials();
    }

    public Set<MaterialIdentifier> getMaterialIdentifiers() {
        return materials.getMaterialIdentifiers();
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    public Material addMaterial(Material material) {
        return this.materials.addMaterial(material);
    }

    public void removeMaterial(Material material) {
        this.materials.removeMaterial(material);
    }

    public void update(Material material) {
        this.materials.update(material);
    }
    //</editor-fold>

}