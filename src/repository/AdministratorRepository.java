package repository;

import domain.Administrator;
import exceptions.LoginException;
import persistence.PersistenceEnforcer;

import static util.ImmutabilityHelper.copyDefensively;

public class AdministratorRepository extends Repository<Administrator>{

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public AdministratorRepository(PersistenceEnforcer persistence) {
        super(persistence);
    }
    //</editor-fold>

    //<editor-fold desc="Public actions" defaultstate="collapsed">

    /**
     * Returns an {@link Administrator} if the provided credentials were correct.
     *
     * @param username the username.
     * @param password the password.
     * @return an {@link Administrator}.
     * @throws LoginException thrown if login failed.
     */
    public Administrator login(String username, String password) throws LoginException {
        Administrator administrator;
        try {
            administrator = (Administrator) persistence.getNamedQuery(Administrator.class.getSimpleName() + ".findByName").setParameter("name", username).getSingleResult();
        } catch (Exception e) {
            throw new LoginException();
        }
        if (administrator == null || !administrator.checkPassword(password)) {
            throw new LoginException();
        } else if (administrator.isSuspended()) {
            throw new LoginException(LoginException.Cause.ACCOUNT_SUSPENDED);
        } else {
            return copyDefensively(administrator);
        }
    }
    //</editor-fold>
}