package domain;

import com.google.common.io.Files;
import exceptions.AzureException;
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

    /**
     * @return returns a Set of no-reference {@link domain.Material}.
     */
    public Set<Material> getMaterials() {
        return (Set<Material>) copyCollectionDefensively(this.materials);
    }

    /**
     * @return returns a Set of no-reference {@link domain.MaterialIdentifier}.
     */
    public Set<MaterialIdentifier> getMaterialIdentifiers() {
        return (Set<MaterialIdentifier>) copyCollectionDefensively(this.materialIdentifiers);
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    /**
     * Saves a new {@link domain.Material} in the database.
     * @param material the Material to save.
     * @return the material with updates database fields.
     */
    public Material addMaterial(Material material) {
        if (getMaterialById(material.getId()) != null)
            throw new MaterialAlreadyExistsException(material);
        Material toPersist = copyDefensively(material);
        persistMaterial(toPersist);
        addMaterialToCollections(toPersist);
        return copyDefensively(toPersist);
    }

    /**
     * Removes a {@link domain.Material} from the database.
     * @param material the Material to remove.
     */
    public void removeMaterial(Material material) {
        Material remove = getMaterialById(material.getId());
        if (remove == null)
            throw new MaterialNotFoundException(material);
        persistence.remove(remove);
        persistence.remove(remove.getIdentifiers());
        removeMaterialFromCollections(remove);
    }

    /**
     * Updates a {@link domain.Material} in the database.
     * @param material the Material to update.
     */
    public void update(Material material) {
        Material original = getMaterialByIdForced(material.getId(), "Cannot update a record that does not appear in the database.");
        Material toSave = copyDefensively(material);

        persistence.merge(toSave);
        removeMaterialFromCollections(original);
        addMaterialToCollections(toSave);
    }

    /**
     * Set new picture for a {@link domain.Material} And save it in the database.
     * @param material the material where a new picture will be set.
     * @param imagePath the path of the image to add.
     * @throws AzureException
     */
    public void updatePhoto(Material material, String imagePath) throws AzureException {
        Material original = getMaterialByIdForced(material.getId(), "Cannot add photo of a nonexistent material.");
        material.setEncoding(Files.getFileExtension(imagePath));
        File upload = new File(imagePath);

        if (original.getEncoding() != null)
            this.azureBlobStorage.remove("images", original.getFileName());
        this.azureBlobStorage.upload(upload, "images", material.getFileName());
        original.setEncoding(material.getEncoding());
        update(original);
    }

    /**
     * Finds a persisted material by id.
     * @param id the id of the material to search.
     * @param exceptionMessage thrown when the material is not found.
     * @return the Material that has been found.
     */
    private Material getMaterialByIdForced(int id, String exceptionMessage) {
        Material found = getMaterialById(id);
        if (found == null)
            throw new MaterialNotFoundException(exceptionMessage);
        return found;
    }

    /**
     * Finds a persisted material by id.
     * @param id the id to search.
     * @return the Material if one has been found or Null.
     */
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