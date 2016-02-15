package domain;

import com.google.common.io.Files;
import exceptions.CouldNotUploadFileException;
import exceptions.MaterialAlreadyExistsException;
import exceptions.MaterialNotFoundException;
import persistence.AzureBlobStorage;
import persistence.PersistenceEnforcer;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static util.ImmutabilityHelper.copyCollectionDefensively;
import static util.ImmutabilityHelper.copyDefensively;

public class MaterialRepository {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private Set<Material> materials;
    private Set<MaterialIdentifier> materialIdentifiers;
    private PersistenceEnforcer persistence;
    private AzureBlobStorage azureBlobStorage;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public MaterialRepository(PersistenceEnforcer persistence) {
        this.persistence = persistence;

        List<Material> initialize = persistence.retrieve(Material.class);
        this.materials = new HashSet<>(initialize);
        this.materialIdentifiers = new HashSet<>(initialize
                .stream()
                .flatMap(m -> m.getIdentifiers().stream())
                .collect(Collectors.toSet()));
        this.azureBlobStorage = new AzureBlobStorage();
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
        Material original = getMaterialByIdForced(material, "Cannot update a record that does not appear in the database.");
        Material toSave = copyDefensively(material);

        persistence.merge(toSave);
        removeMaterialFromCollections(original);
        addMaterialToCollections(toSave);
    }

    public void updatePhoto(Material material, String imagePath) throws CouldNotUploadFileException {
        Material original = getMaterialByIdForced(material, "Cannot add photo of a nonexistent material.");
        String extension = Files.getFileExtension(imagePath);
        File upload = new File(imagePath);

        this.azureBlobStorage.upload(upload, "images", String.format("%d.%s", original.getId(), extension));
        material.setEncoding(extension);
        persistence.startTransaction();
        original.setEncoding(extension);
        persistence.commitTransaction();
    }

    private Material getMaterialByIdForced(Material material, String exceptionMessage) {
        Material found = getMaterialById(material.getId());
        if (found == null)
            throw new MaterialNotFoundException(exceptionMessage, material);
        return found;
    }

    public Material getMaterialById(int id) {
        return id == 0 ? null : this.materials
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