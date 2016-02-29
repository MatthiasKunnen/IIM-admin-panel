
package repository;

import domain.Firm;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;


public class FirmRepository extends Repository<Firm>{

    public FirmRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }

    public ObservableList<Firm> getExistingFirms(){
        return FXCollections.observableArrayList(this.eList.stream().distinct().collect(Collectors.toList()));
    }
}
