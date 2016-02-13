package persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceController {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private PersistenceEnforcer enforcer;
    private EntityManager manager;
    private EntityManagerFactory factory;
    private final String PU_NAME = "IIM_test";
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    public void start() {
        factory = Persistence.createEntityManagerFactory(PU_NAME);
        manager = factory.createEntityManager();
        enforcer = new PersistenceEnforcer(manager);
    }

    public void stop() {
        manager.close();
        factory.close();
    }

    public PersistenceEnforcer getEnforcer() {
        return enforcer;
    }
    //</editor-fold>
}