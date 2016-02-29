/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.Curricular;
import domain.TargetGroup;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;

/**
 *
 * @author Evert
 */
public class TargetGroupRepository extends Repository<TargetGroup>{

    public TargetGroupRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }
    
    public TargetGroupRepository(PersistenceEnforcer persistence, List<TargetGroup> theItems) {
        super(persistence,theItems);
    }
    
    public ObservableList<TargetGroup> getOptions(){
        return FXCollections.observableList(this.eList.stream().distinct().collect(Collectors.toList()));
    }
    
}
