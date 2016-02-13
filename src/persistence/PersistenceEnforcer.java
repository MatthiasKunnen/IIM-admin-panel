package persistence;

import javax.persistence.EntityManager;

public class PersistenceEnforcer {

    //<editor-fold desc="Variables" defaultstate="collapsed">
    private EntityManager manager;
    //</editor-fold>

    //<editor-fold desc="Constructors" defaultstate="collapsed">

    public PersistenceEnforcer(EntityManager manager) {
        this.manager = manager;
    }
    //</editor-fold>

    //<editor-fold desc="Actions" defaultstate="collapsed">

    /**
     * Persist objects.
     *
     * @param objects The varargs array of objects to persist
     */
    public void persist(Object... objects) {
        manager.getTransaction().begin();
        for (Object o : objects) {
            manager.persist(o);
        }
        manager.getTransaction().commit();
    }

    /**
     * Remove objects.
     *
     * @param objects The varargs array of objects to remove
     */
    public void remove(Object... objects) {
        manager.getTransaction().begin();
        for (Object o : objects) {
            manager.remove(o);
        }
        manager.getTransaction().commit();
    }
    //</editor-fold>
}