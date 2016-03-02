/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.Curricular;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

/**
 *
 * @author Evert
 */
public class CurricularRepository extends Repository<Curricular> {

    public CurricularRepository(PersistenceEnforcer persistence) {
        super(persistence);
        eList = persistence.retrieve(Curricular.class);
        eObservableList = FXCollections.observableList((List<Curricular>) ImmutabilityHelper.copyCollectionDefensively(eList));
    }

    public ObservableList<Curricular> getOptions() {
        return FXCollections.observableList(this.eList.stream().collect(Collectors.toList()));
    }
}
