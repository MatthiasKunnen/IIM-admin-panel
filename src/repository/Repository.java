package repository;

import domain.IEntity;
import exceptions.RepositoryItemAlreadyExistsException;
import exceptions.RepositoryItemNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;

import java.util.ArrayList;
import java.util.List;

import static util.ImmutabilityHelper.copyCollectionDefensively;
import static util.ImmutabilityHelper.copyDefensively;

public abstract class Repository<E extends IEntity> {

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    protected PersistenceEnforcer persistence;
    protected List<E> eList;
    protected ObservableList<E> eObservableList;
    private Class<E> eClass;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public Repository(PersistenceEnforcer persistence, Class<E> eClass) {
        this.eClass = eClass;
        this.persistence = persistence;
        this.eList = new ArrayList<>();
        this.eObservableList = FXCollections.observableList(eList);
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    /**
     * Adds a new {@link E repository item}.
     *
     * @param add the {@link E repository item} to add.
     * @return the {@link E repository item} with updated database variables.
     */
    public E add(E add) {
        if (getItemById(add.getId()) != null)
            throw new RepositoryItemAlreadyExistsException(add);
        E toSave = copyDefensively(add);
        persistence.persist(toSave);
        addItem(toSave);
        return copyDefensively(toSave);
    }

    /**
     * Updates a {@link E repository item}.
     *
     * @param update {@link E repository item} to update.
     */
    public void update(E update) {
        E original = getItemByIdForced(update.getId(), String.format("Cannot update an object of type %s that does not exist.", getClass().getSimpleName()));
        E toSave = copyDefensively(update);
        persistence.merge(toSave);
        removeItem(original);
        addItem(toSave);
    }

    /**
     * Remove a {@link E repository item}.
     *
     * @param e {@link E repository item} to remove.
     */
    public void remove(E e) {
        E remove = getItemByIdForced(e.getId(), String.format("Cannot remove a nonexistent %s.", getClass().getSimpleName()));
        persistence.remove(persistence.getReference(eClass, e.getId()));
        removeItem(remove);
    }
    //</editor-fold>

    //<editor-fold desc="Protected actions" defaultstate="collapsed">

    /**
     * Finds a persisted {@link E repository item} by id.
     *
     * @param id               the id of the {@link E repository item} to search.
     * @param exceptionMessage thrown when the {@link E repository item} is not found.
     * @return the {@link E repository item} that has been found.
     */
    protected E getItemByIdForced(int id, String exceptionMessage) {
        E found = getItemById(id);
        if (found == null)
            throw new RepositoryItemNotFoundException(exceptionMessage);
        return found;
    }

    /**
     * Finds a persisted {@link E repository item} by id.
     *
     * @param id the id to search.
     * @return the {@link E repository item} if one has been found or Null.
     */
    protected E getItemById(int id) {
        return id == 0 ? null : this.eList
                .stream()
                .filter(m -> m.getId() == id)
                .findAny()
                .orElse(null);
    }

    protected void addItem(E add) {
        this.eList.add(add);
        this.eObservableList.add(copyDefensively(add));
    }

    protected void removeItem(E remove) {
        this.eList.removeIf(e-> e.getId() == remove.getId());
        this.eObservableList.removeIf(e-> e.getId() == remove.getId());
    }

    protected Class<E> getRepoItemClass(){
        return this.eClass;
    }
    //</editor-fold>
}
