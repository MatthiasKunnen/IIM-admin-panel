package persistence;

import com.google.common.collect.BiMap;
import javax.persistence.AttributeConverter;

/**
 * Makes it possible to map enums while still having the ability to rearrange, add or modify enums without breaking the database.
 * @param <E> the object to map.
 */
public abstract class EnumConverter<E> implements AttributeConverter<E, Integer> {

    protected abstract  BiMap<E, Integer> getConverter();

    @Override
    public Integer convertToDatabaseColumn(E e) {
        return getConverter().get(e);
    }

    @Override
    public E convertToEntityAttribute(Integer integer) {
        return getConverter().inverse().get(integer);
    }
}
