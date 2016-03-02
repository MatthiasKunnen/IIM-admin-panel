/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.TargetGroup;
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
public class TargetGroupRepository extends Repository<TargetGroup>{

    public TargetGroupRepository(PersistenceEnforcer persistence) {
        super(persistence);
        eList = persistence.retrieve(TargetGroup.class);
        eObservableList = FXCollections.observableList((List<TargetGroup>) ImmutabilityHelper.copyCollectionDefensively(eList));

    }

    public ObservableList<TargetGroup> getOptions(){
        return FXCollections.observableList(this.eList.stream().collect(Collectors.toList()));
    }
    
}
