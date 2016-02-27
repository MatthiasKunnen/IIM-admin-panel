package domain;

import com.google.common.io.Files;
import exceptions.AzureException;
import exceptions.MaterialAlreadyExistsException;
import exceptions.MaterialNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.AzureBlobStorage;
import persistence.PersistenceEnforcer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static util.ImmutabilityHelper.copyCollectionDefensively;
import static util.ImmutabilityHelper.copyDefensively;

public class MaterialRepository {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private List<Material> materials;
    private List<MaterialIdentifier> materialIdentifiers;
    private PersistenceEnforcer persistence;
    private AzureBlobStorage azureBlobStorage;
    private ObservableList<Material> materialObservableList;
    private ObservableList<MaterialIdentifier> materialIdentifierObservableList;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public MaterialRepository(PersistenceEnforcer persistence) {
        this.persistence = persistence;
        List<Material> initialize = persistence.retrieve(Material.class);
        this.materials = new ArrayList<>(initialize);
        this.materialIdentifiers = new ArrayList<>(initialize
                .stream()
                .flatMap(m -> m.getIdentifiers().stream())
                .collect(Collectors.toList()));
        this.materialObservableList = FXCollections.observableList((List<Material>) copyCollectionDefensively(this.materials));
        this.materialIdentifierObservableList = FXCollections.observableList((List<MaterialIdentifier>) copyCollectionDefensively(this.materialIdentifiers));
        this.azureBlobStorage = new AzureBlobStorage();
    }
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">

    /**
     * @return returns an ObservableList of no-reference {@link domain.Material}.
     */
    public ObservableList<Material> getMaterials() {
        return FXCollections.unmodifiableObservableList(materialObservableList);
    }

    /**
     * @return returns an ObservableList of no-reference {@link domain.MaterialIdentifier}.
     */
    public ObservableList<MaterialIdentifier> getMaterialIdentifiers() {
        return FXCollections.unmodifiableObservableList(materialIdentifierObservableList);
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    /**
     * Saves a new {@link domain.Material} in the database.
     *
     * @param material the Material to save.
     * @return the material with updates database fields.
     */
    public Material addMaterial(Material material) {
        if (getMaterialById(material.getId()) != null)
            throw new MaterialAlreadyExistsException(material);
        Material toPersist = copyDefensively(material);
        persistMaterial(toPersist);
        addMaterialSynced(toPersist);
        return copyDefensively(toPersist);
    }

    /**
     * Removes a {@link domain.Material} from the database.
     *
     * @param material the Material to remove.
     */
    public void removeMaterial(Material material) {
        Material remove = getMaterialByIdForced(material.getId(), "Cannot remove a nonexistent Material");
        persistence.remove(remove);
        persistence.remove(remove.getIdentifiers());
        removeMaterialSynced(remove);
    }

    /**
     * Updates a {@link domain.Material} in the database.
     *
     * @param material the Material to update.
     */
    public void update(Material material) {
        Material original = getMaterialByIdForced(material.getId(), "Cannot update a record that does not appear in the database.");
        for (MaterialIdentifier mi : material.getIdentifiers()) {
            if (mi.getId() == 0) {
                persistence.persist(mi);
            } else {
                persistence.merge(mi);
            }
        }

        List<MaterialIdentifier> remove = new ArrayList<>(original.getIdentifiers());
        remove.removeAll(material.getIdentifiers());
        remove.forEach(materialIdentifier -> persistence.remove(materialIdentifier));

        Material toSave = copyDefensively(material);

        persistence.merge(toSave);
        removeMaterialSynced(original);
        addMaterialSynced(toSave);
    }

    /**
     * Set new picture for a {@link domain.Material} And save it in the database.
     *
     * @param material  the material where a new picture will be set.
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
     *
     * @param id               the id of the material to search.
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
     *
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

    /**
     * Check if a name of a material already exists.
     *
     * @param name the name to check.
     * @return true if the name is already in use. False otherwise.
     */
    public boolean doesMaterialNameAlreadyExist(String name) {
        return this.materials.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    /**
     * Returns a material with a matching name.
     *
     * @param name the material name to search for.
     * @return a material with a matching name or null if no material has been found.
     */
    public Material getMaterialByName(String name) {
        return copyDefensively(this.materials.stream().filter(m -> m.getName().equals(name)).findAny().orElse(null));
    }

    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void persistMaterial(Material material) {
        persistence.persist(material);
        persistence.persist(material.getIdentifiers());
    }

    private void addMaterialSynced(Material material) {
        addMaterialLocally(material);
        addMaterialToObservers(copyDefensively(material));
    }

    private void removeMaterialSynced(Material material) {
        removeMaterialLocally(material);
        removeMaterialFromObservers(material);
    }

    private void addMaterialLocally(Material material) {
        this.materials.add(material);
        this.materialIdentifiers.addAll(material.getIdentifiers());
    }

    private void addMaterialToObservers(Material material) {
        this.materialObservableList.add(material);
        this.materialIdentifierObservableList.addAll(material.getIdentifiers());
    }

    private void removeMaterialLocally(Material material) {
        removeById(this.materials, material.getId());
        removeById(this.materialIdentifiers, material.getIdentifiers().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
    }

    private void removeMaterialFromObservers(Material material) {
        removeById(this.materialObservableList, material.getId());
        removeById(this.materialIdentifierObservableList, material.getIdentifiers().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
    }

    private void removeById(Collection<? extends IEntity> collection, int id) {
        collection.removeIf(e -> e.getId() == id);
    }

    private void removeById(Collection<? extends IEntity> collection, Collection<Integer> ids) {
        collection.removeIf(e -> ids.contains(e.getId()));
    }
    //</editor-fold>
}