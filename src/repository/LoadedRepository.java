package repository;

import domain.IEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

import java.util.List;

import static util.ImmutabilityHelper.copyDefensively;

public abstract class LoadedRepository <E extends IEntity> extends Repository<E> {

    //<editor-fold desc="Declarations" defaultstate="collapsed">
    private boolean isLoaded = false;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public LoadedRepository(PersistenceEnforcer persistence, Class<E> eClass) {
        super(persistence, eClass);
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">

    public ObservableList<E> getObservableItems() {
        load();
        return FXCollections.unmodifiableObservableList(eObservableList);
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    @Override
    public E add(E add) {
        if (isLoaded){
            return super.add(add);
        }else{
            E toSave = copyDefensively(add);
            persistence.persist(toSave);
            return copyDefensively(toSave);
        }
    }

    @Override
    public void update(E update) {
        if (isLoaded){
            super.update(update);
        }else{
            persistence.merge(copyDefensively(update));
        }
    }

    @Override
    public void remove(E e) {
        if (isLoaded){
            super.remove(e);
        }else{
            persistence.remove(copyDefensively(e));
        }
    }

    //</editor-fold>

    //<editor-fold desc="Protected actions" defaultstate="collapsed">

    protected void load(){
        if (!isLoaded){
            eList = persistence.retrieve(getRepoItemClass());
            eObservableList = FXCollections.observableList((List<E>) ImmutabilityHelper.copyCollectionDefensively(eList));
            isLoaded = true;
        }
    }
    //</editor-fold>
}
