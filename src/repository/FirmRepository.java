
package repository;

import domain.Firm;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;


public class FirmRepository extends Repository<Firm>{

    public FirmRepository(PersistenceEnforcer persistence) {
        super(persistence);
        eList = persistence.retrieve(Firm.class);
        eObservableList = FXCollections.observableList((List<Firm>) ImmutabilityHelper.copyCollectionDefensively(eList));
    }

    public ObservableList<Firm> getExistingFirms(){
        return FXCollections.unmodifiableObservableList(eObservableList);
    }
}
