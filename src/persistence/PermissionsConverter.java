package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Administrator;

import javax.persistence.Converter;

/**
 * Converts {@link domain.Administrator.Permission} into a database ID.
 *
 * @author Matthias Kunnen
 */
@Converter(autoApply = true)
public class PermissionsConverter extends EnumConverter<Administrator.Permission> {
    private static BiMap<Administrator.Permission, Integer> dictionary = ImmutableBiMap.of(
            Administrator.Permission.MANAGE_MATERIALS, 0,
            Administrator.Permission.MANAGE_RESERVATIONS, 2,
            Administrator.Permission.MANAGE_USERS, 3
    );

    @Override
    protected BiMap<Administrator.Permission, Integer> getConverter() {
        return dictionary;
    }
}
