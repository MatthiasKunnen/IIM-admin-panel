package repository;

import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

import java.util.List;

public class UserRepository extends LoadedRepository<User>{
    
    public UserRepository(PersistenceEnforcer persistence) {
        super(persistence, User.class);
    }

    public User getUserByEmail(String email){
        return ImmutabilityHelper.copyDefensively(eList.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findAny()
                .orElse(null));
    }
}