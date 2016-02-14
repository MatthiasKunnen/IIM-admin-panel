package util;

import exceptions.ClassDoesNotImplementCopyConstructorException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is meant to help passing objects with no reference.
 *
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
     * @param varArgs    these objects are passed to the copy-constructor
     * @return deep copy of collection.
     */
    public static <E> Collection<E> copyCollectionDefensively(Collection<E> collection, Object... varArgs) {
        try {
            @SuppressWarnings("unchecked")
            Collection<E> newCollection = collection.getClass().newInstance();
            for (E item : collection) {
                newCollection.add(copyDefensively(item, varArgs));
            }
            return newCollection;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("Could not copy defensively!", e);
        }
    }

    /**
     * Copies an object with no ties to the original.
     *
     * @param object  the object to copy.
     * @param <E>     a class with a copy-constructor.
     * @param varArgs these objects are passed to the copy-constructor
     * @return a copy of the object.
     * @throws ClassDoesNotImplementCopyConstructorException if the object's class does not have a copy-constructor.
     */
    public static <E> E copyDefensively(E object, Object... varArgs) {
        try {
            if (varArgs.length == 0) {
                @SuppressWarnings("unchecked")
                E copy = (E) object.getClass().getConstructor(object.getClass()).newInstance(object);
                return copy;
            }
            List<Class> classes = Arrays
                    .stream(varArgs)
                    .map(Object::getClass)
                    .collect(Collectors.toList());
            classes.add(0, object.getClass());

            List<Object> parameters = new ArrayList<>(Arrays.asList(varArgs));
            parameters.add(0, object);

            @SuppressWarnings("unchecked")
            E copy = (E) object.getClass()
                    .getConstructor(classes.toArray(new Class[classes.size()]))
                    .newInstance(parameters.toArray());
            return copy;
        } catch (NoSuchMethodException e) {
            throw new ClassDoesNotImplementCopyConstructorException(e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not copy defensively!", e);
        }
    }
}
