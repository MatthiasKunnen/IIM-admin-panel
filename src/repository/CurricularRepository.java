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

/**
 *
 * @author Evert
 */
public class CurricularRepository extends Repository<Curricular>{

    public CurricularRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }
    
    public CurricularRepository(PersistenceEnforcer persistence, List<Curricular> theList) {
        super(persistence, theList);
    }
    
    public ObservableList<Curricular> getOptions(){
        return FXCollections.observableList(this.eList.stream().distinct().collect(Collectors.toList()));
    }
}
