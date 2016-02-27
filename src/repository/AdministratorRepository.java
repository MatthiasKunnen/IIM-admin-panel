package domain;

import exceptions.LoginException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceEnforcer;

import java.util.ArrayList;
import java.util.List;

import static util.ImmutabilityHelper.copyCollectionDefensively;
import static util.ImmutabilityHelper.copyDefensively;

public class AdministratorRepository {

    //<editor-fold desc="Variables" defaultstate="collapsed">

    private List<Administrator> administrators;
    private PersistenceEnforcer persistence;
    private ObservableList<Administrator> administratorObservableList;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public AdministratorRepository(PersistenceEnforcer persistence) {
        this.persistence = persistence;
        this.administrators = new ArrayList<>();
        this.administratorObservableList = FXCollections.observableList((List<Administrator>) copyCollectionDefensively(this.administrators));
    }
    //</editor-fold>

    //<editor-fold desc="Public actions" defaultstate="collapsed">

    /**
     * Returns an Object of {@link Administrator} if the provided credentials were correct.
     *
     * @param username the username.
     * @param password the password.
     * @return an Object of {@link Administrator}.
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

    /**
     * Adds a new {@link Administrator}.
     *
     * @param administrator the {@link Administrator} to add.
     * @return the {@link Administrator} with the persisted database fields.
     */
    public Administrator addLogin(Administrator administrator) {
        Administrator toSave = copyDefensively(administrator);
        persistence.persist(toSave);
        addAdministrator(toSave);
        return copyDefensively(toSave);
    }
    //</editor-fold>

    //<editor-fold desc="Private actions" defaultstate="collapsed">
    private void addAdministrator(Administrator administrator) {
        this.administrators.add(administrator);
        this.administratorObservableList.add(administrator);
    }
    //</editor-fold>
}
