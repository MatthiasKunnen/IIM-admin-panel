package persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class PersistenceController {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private static PersistenceEnforcer enforcer;
    private static EntityManager manager;
    private static EntityManagerFactory factory;
    private static final String PU_NAME = "IIM";
    private static final String PU_LOCAL_NAME = "IIM_test";
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">
    public static void start() {
        factory = Persistence.createEntityManagerFactory(PU_LOCAL_NAME);
        manager = factory.createEntityManager();
        enforcer = new PersistenceEnforcer(manager);
    }

    public static void stop() {
        manager.close();
        factory.close();
    }

    public static PersistenceEnforcer getEnforcer() {
        return enforcer;
    }
    //</editor-fold>

    private PersistenceController() {

    }
}