/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package repository;

import domain.Reservation;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import static util.ImmutabilityHelper.copyDefensively;

/*
UserRepository gemaakt omdat de admin het emailadres ging laten invullen 
en zo de user opzoeken en in de reservatie zetten. wanneer het emailadres nie bestaat 
wordt dit getoont in de gui.
*/
public class UserRepository extends Repository{
    
    public UserRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }
    public ObservableList<User> getUsers() {
        return FXCollections.unmodifiableObservableList(eObservableList);
    }
    public User doesUserExist(String email){
        // de getters kan ik hier niet gebruiken?
        //eList.stream().find(User::getEmail == email);
        return null;
    }
}
