package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Administrator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link domain.Administrator.Permission} into a database ID.
 *
 * @author Matthias Kunnen
 */
@Converter(autoApply = true)
public class PermissionsConverter implements AttributeConverter<Administrator.Permission, Integer> {
    private static BiMap<Administrator.Permission, Integer> dictionary = ImmutableBiMap.of(
            Administrator.Permission.MANAGE_MATERIALS, 0,
            Administrator.Permission.MANAGE_RESERVATIONS, 1,
            Administrator.Permission.MANAGE_USERS, 2
    );

    @Override
    public Integer convertToDatabaseColumn(Administrator.Permission permission) {
        return dictionary.get(permission);
    }

    @Override
    public Administrator.Permission convertToEntityAttribute(Integer integer) {
        return dictionary.inverse().get(integer);
    }
}
