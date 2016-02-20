package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.User;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts enum Permission into a database ID.
 * @author Matthias Kunnen
 */
@Converter(autoApply = true)
public class PermissionsConverter implements AttributeConverter<User.Permission, Integer> {
    private static BiMap<User.Permission, Integer> converter = ImmutableBiMap.of(
            User.Permission.Material, 0,
            User.Permission.User, 1
    );

    @Override
    public Integer convertToDatabaseColumn(User.Permission permission) {
        return converter.get(permission);
    }

    @Override
    public User.Permission convertToEntityAttribute(Integer id) {
        return converter.inverse().get(id);
    }
}
