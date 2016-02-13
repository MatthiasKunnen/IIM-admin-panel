package util;

import exceptions.ClassDoesNotImplementCopyConstructorException;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * This class is meant to help passing objects with no reference.
 * @author Matthias Kunnen
 */
public final class ImmutabilityHelper {
    private ImmutabilityHelper() {
    }

    /**
     * Creates a deep copy of a collection&lt;E&gt;.
     *
     * @param collection the collection to copy.
     * @param <E>        a class with a copy-constructor.
     * @return deep copy of collection.
     */
    public static <E> Collection<E> copyCollectionDefensively(Collection<E> collection) {
        try {
            @SuppressWarnings("unchecked")
            Collection<E> newCollection = collection.getClass().newInstance();
            for (E item : collection) {
                newCollection.add(copyDefensively(item));
            }
            return newCollection;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Could not copy defensively!", e);
        }
    }

    /**
     * Copies an object with no ties to the original.
     *
     * @param object the object to copy.
     * @param <E>    a class with a copy-constructor.
     * @return a copy of the object.
     * @throws ClassDoesNotImplementCopyConstructorException if the object's class does not have a copy-constructor.
     */
    public static <E> E copyDefensively(E object) {
        try {
            @SuppressWarnings("unchecked")
            E copy = (E) object.getClass().getConstructor(object.getClass()).newInstance(object);
            return copy;
        } catch (NoSuchMethodException e) {
            throw new ClassDoesNotImplementCopyConstructorException(e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not copy defensively!", e);
        }
    }
}
