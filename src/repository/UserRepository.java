package repository;

import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

import java.util.List;

public class UserRepository extends Repository<User>{
    
    public UserRepository(PersistenceEnforcer persistence) {
        super(persistence);
        eList = persistence.retrieve(User.class);
        eObservableList = FXCollections.observableList((List<User>) ImmutabilityHelper.copyCollectionDefensively(eList));
    }

    public ObservableList<User> getUsers() {
        return FXCollections.unmodifiableObservableList(eObservableList);
    }

    public User getUserByEmail(String email){
        return ImmutabilityHelper.copyDefensively(eList.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findAny()
                .orElse(null));
    }
}