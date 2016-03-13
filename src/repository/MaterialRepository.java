package repository;

import com.google.common.io.Files;
import domain.IEntity;
import domain.Material;
import domain.MaterialIdentifier;
import exceptions.AzureException;
import exceptions.MaterialAlreadyExistsException;
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

public class MaterialRepository extends Repository<Material> {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private List<MaterialIdentifier> materialIdentifiers;
    private AzureBlobStorage azureBlobStorage;
    private ObservableList<MaterialIdentifier> materialIdentifierObservableList;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public MaterialRepository(PersistenceEnforcer persistence) {
        super(persistence);
        List<Material> initialize = persistence.retrieve(Material.class);
        this.eList = new ArrayList<>(initialize);
        this.materialIdentifiers = new ArrayList<>(initialize
                .stream()
                .flatMap(m -> m.getIdentifiers().stream())
                .collect(Collectors.toList()));
        this.eObservableList = FXCollections.observableList((List<Material>) copyCollectionDefensively(this.eList));
        this.materialIdentifierObservableList = FXCollections.observableList((List<MaterialIdentifier>) copyCollectionDefensively(this.materialIdentifiers));
        this.azureBlobStorage = new AzureBlobStorage();
    }
    //</editor-fold>

    //<editor-fold desc="Getters and setters" defaultstate="collapsed">

    /**
     * @return returns an ObservableList of no-reference {@link domain.Material}.
     */
    public ObservableList<Material> getMaterials() {
        return FXCollections.unmodifiableObservableList(eObservableList);
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
    @Override
    public Material add(Material material) {
        if (getItemById(material.getId()) != null)
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
    @Override
    public void remove(Material material) {
        Material remove = getItemByIdForced(material.getId(), "Cannot remove a nonexistent Material");
        persistence.remove(remove);
        persistence.remove(remove.getIdentifiers());
        removeMaterialSynced(remove);
    }

    /**
     * Updates a {@link domain.Material} in the database.
     *
     * @param material the Material to update.
     */
    @Override
    public void update(Material material) {
        Material original = getItemByIdForced(material.getId(), "Cannot update a record that does not appear in the database.");
        for (MaterialIdentifier mi : material.getIdentifiers()) {
            if (mi.getId() == 0) {
                persistence.persist(mi);
            } else {
                persistence.merge(mi);
            }
        }

        getAllItemsInAThatAreNotInB(original.getIdentifiers(), material.getIdentifiers())
                .forEach(materialIdentifier ->  {
                    persistence.merge(materialIdentifier);
                    persistence.remove(materialIdentifier);
                });

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
        Material original = getItemByIdForced(material.getId(), "Cannot add photo of a nonexistent material.");
        material.setEncoding(Files.getFileExtension(imagePath));
        File upload = new File(imagePath);

        if (original.getEncoding() != null)
            this.azureBlobStorage.remove("images", original.getFileName());
        this.azureBlobStorage.upload(upload, "images", material.getFileName());
        original.setEncoding(material.getEncoding());
        update(original);
    }

    /**
     * Check if a name of a material already exists.
     *
     * @param name the name to check.
     * @return true if the name is already in use. False otherwise.
     */
    public boolean doesMaterialNameAlreadyExist(String name) {
        return this.eList.stream().anyMatch(m -> m.getName().equalsIgnoreCase(name));
    }

    public boolean doesMaterialExist(Material material) {
        return getItemById(material.getId()) != null;
    }

    /**
     * Returns a material with a matching name.
     *
     * @param name the material name to search for.
     * @return a material with a matching name or null if no material has been found.
     */
    public Material getMaterialByName(String name) {
        return copyDefensively(this.eList.stream().filter(m -> m.getName().equals(name)).findAny().orElse(null));
    }
    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">

    private <E extends IEntity> List<E> getAllItemsInAThatAreNotInB(List<E> a, List<E> b) {
        List<E> result = new ArrayList<>(a);
        result.removeAll(b);
        return result;
    }

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
        this.eList.add(material);
        this.materialIdentifiers.addAll(material.getIdentifiers());
    }

    private void addMaterialToObservers(Material material) {
        this.eObservableList.add(material);
        this.materialIdentifierObservableList.addAll(material.getIdentifiers());
    }

    private void removeMaterialLocally(Material material) {
        removeById(this.eList, material.getId());
        removeById(this.materialIdentifiers, material.getIdentifiers().stream().map(MaterialIdentifier::getId).collect(Collectors.toList()));
    }

    private void removeMaterialFromObservers(Material material) {
        removeById(this.eObservableList, material.getId());
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