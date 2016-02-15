package domain;

import exceptions.CouldNotUploadFileException;
import persistence.PersistenceController;

import java.util.Set;

public class DomainController {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private MaterialRepository materials;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public DomainController() {
        PersistenceController.start();
        this.materials = new MaterialRepository(PersistenceController.getEnforcer());
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

    public void updatePhoto(Material material, String imagePath) throws CouldNotUploadFileException {
        this.materials.updatePhoto(material, imagePath);
    }

    //</editor-fold>
}