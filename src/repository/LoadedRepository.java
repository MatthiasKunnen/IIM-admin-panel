package repository;

import domain.IEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

import java.util.List;

public class LoadedRepository <E extends IEntity> extends Repository<E> {

    //<editor-fold desc="Constructors" defaultstate="collapsed">
    public LoadedRepository(PersistenceEnforcer persistence, Class<E> eClass) {
        super(persistence);
        eList = persistence.retrieve(eClass);
        eObservableList = FXCollections.observableList((List<E>) ImmutabilityHelper.copyCollectionDefensively(eList));
    }
    //</editor-fold>

    //<editor-fold desc="Properties" defaultstate="collapsed">

    public ObservableList<E> getObservableItems() {
        return FXCollections.unmodifiableObservableList(eObservableList);
    }
    //</editor-fold>
}
