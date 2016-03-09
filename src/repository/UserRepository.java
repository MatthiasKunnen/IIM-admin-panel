package repository;

import domain.User;
import persistence.PersistenceEnforcer;
import util.ImmutabilityHelper;

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