package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Administrator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts enum Permission into a database ID.
 * @author Matthias Kunnen
 */
@Converter(autoApply = true)
public class PermissionsConverter implements AttributeConverter<Administrator.Permission, Integer> {
    private static BiMap<Administrator.Permission, Integer> converter = ImmutableBiMap.of(
            Administrator.Permission.MANAGE_MATERIALS, 0,
            Administrator.Permission.MANAGE_RESERVATIONS, 2,
            Administrator.Permission.MANAGE_USERS, 3
    );

    @Override
    public Integer convertToDatabaseColumn(Administrator.Permission permission) {
        return converter.get(permission);
    }

    @Override
    public Administrator.Permission convertToEntityAttribute(Integer id) {
        return converter.inverse().get(id);
    }
}
