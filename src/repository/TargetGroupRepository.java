/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.TargetGroup;
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

    public ObservableList<TargetGroup> getOptions(){
        return FXCollections.observableList(this.eList.stream().collect(Collectors.toList()));
    }
    
}
