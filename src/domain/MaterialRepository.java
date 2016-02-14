package domain;

import java.util.*;

import exceptions.MaterialAlreadyExistsException;
import exceptions.MaterialNotFoundException;
import persistence.*;

import static util.ImmutabilityHelper.copyCollectionDefensively;
import static util.ImmutabilityHelper.copyDefensively;

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
        return (Set<Material>) copyCollectionDefensively(this.materials);
    }

    public Set<MaterialIdentifier> getMaterialIdentifiers() {
        return (Set<MaterialIdentifier>) copyCollectionDefensively(this.materialIdentifiers);
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    public Material addMaterial(Material material) {
        if (getMaterialById(material.getId()) != null)
            throw new MaterialAlreadyExistsException(material);
        Material toPersist = copyDefensively(material);
        persistMaterial(toPersist);
        addMaterialToCollections(toPersist);
        return copyDefensively(toPersist);
    }

    public void removeMaterial(Material material) {
        Material remove = getMaterialById(material.getId());
        if (remove == null)
            throw new MaterialNotFoundException(material);
        persistence.remove(remove);
        persistence.remove(remove.getIdentifiers());
        removeMaterialFromCollections(remove);
    }

    public void update(Material material) {
        if (material.getId() == 0)
            throw new MaterialNotFoundException("Cannot update a record that does not appear in the database.", material);

        Material toSave = copyDefensively(material);
        Material original = getMaterialById(material.getId());

        persistence.merge(toSave);

        removeMaterialFromCollections(original);
        addMaterialToCollections(toSave);
    }

    public Material getMaterialById(int id) {
        return this.materials
                .stream()
                .filter(m -> m.getId() == id)
                .findAny()
                .orElse(null);
    }

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void persistMaterial(Material material) {
        persistence.persist(material);
        persistence.persist(material.getIdentifiers());
    }

    private void addMaterialToCollections(Material material) {
        this.materials.add(material);
        this.materialIdentifiers.addAll(material.getIdentifiers());
    }

    private void removeMaterialFromCollections(Material material) {
        this.materials.remove(material);
        this.materialIdentifiers.removeAll(material.getIdentifiers());
    }
    //</editor-fold>
}