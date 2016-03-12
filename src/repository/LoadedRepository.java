package repository;

import domain.IEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

import java.util.List;

public class LoadedRepository <E extends IEntity> extends Repository<E> {

    private boolean isLoaded = false;
    private Class<E> eClass;

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public LoadedRepository(PersistenceEnforcer persistence, Class<E> eClass) {
        super(persistence);
        this.eClass = eClass;
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">

    public ObservableList<E> getObservableItems() {
        load();
        return FXCollections.unmodifiableObservableList(eObservableList);
    }
    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">

    private void load(){
        if (!isLoaded){
            eList = persistence.retrieve(eClass);
            eObservableList = FXCollections.observableList((List<E>) ImmutabilityHelper.copyCollectionDefensively(eList));
            isLoaded = true;
        }
    }
    //</editor-fold>
}
