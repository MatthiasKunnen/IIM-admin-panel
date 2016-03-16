package persistence;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collection;
import java.util.List;

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
    //<editor-fold desc="Persist" defaultstate="collapsed">

    /**
     * Persist objects.
     *
     * @param objects The varargs array of objects to persist
     */
    public void persist(Object... objects) {
        startTransaction();
        for (Object o : objects) {
            manager.persist(o);
        }
        commitTransaction();
    }

    /**
     * Persist a collection of objects.
     *
     * @param objects The collection of objects to persist
     */
    public void persist(Collection objects) {
        startTransaction();
        for (Object o : objects) {
            manager.persist(o);
        }
        commitTransaction();
    }
    //</editor-fold>

    //<editor-fold desc="Remove" defaultstate="collapsed">

    /**
     * Remove objects.
     *
     * @param objects The varargs array of objects to remove
     */
    public void remove(Object... objects) {
        startTransaction();
        for (Object o : objects) {
            manager.remove(o);
        }
        commitTransaction();
    }

    /**
     * Remove a collection of objects.
     *
     * @param objects The collection of objects to remove
     */
    public void remove(Collection objects) {
        startTransaction();
        for (Object o : objects) {
            manager.remove(o);
        }
        commitTransaction();
    }
    //</editor-fold>

    //<editor-fold desc="Merge" defaultstate="collapsed">

    /**
     * Merge objects.
     *
     * @param objects The varargs array of objects to merge
     */
    public void merge(Object... objects) {
        startTransaction();
        for (Object o : objects) {
            manager.merge(o);
        }
        commitTransaction();
    }

    /**
     * Merge a collection of objects.
     *
     * @param objects The collection of objects to merge
     */
    public void merge(Collection objects) {
        startTransaction();
        for (Object o : objects) {
            manager.merge(o);
        }
        commitTransaction();
    }
    //</editor-fold>

    public <T> T getReference(Class<T> aClass, Object id){
        return manager.getReference(aClass, id);
    }

    private void startTransaction() {
        if (!manager.getTransaction().isActive())
            manager.getTransaction().begin();
    }

    private void commitTransaction() {
        manager.getTransaction().commit();
    }

    public Query getNamedQuery(String queryName) {
        return manager.createNamedQuery(queryName);
    }

    /**
     * Get all entities of type T from the database.
     *
     * @param aClass the EntityClass to retrieve.
     * @param <T>    the type of the EntityClass.
     * @return list of type Entity&lt;T&gt;
     */
    public <T> List<T> retrieve(Class<T> aClass) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<T> criteria = cb.createQuery(aClass);
        TypedQuery<T> query = manager.createQuery(criteria);
        return query.getResultList();
    }
    //</editor-fold>
}