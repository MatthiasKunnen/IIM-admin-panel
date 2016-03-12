package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.User;

import javax.persistence.Converter;

/**
 * Makes it possible to map the enum {@link domain.User.Type} while still having the ability to rearrange, add or modify the enum values without breaking the database
 */
@Converter(autoApply = true)
public class UserTypeConverter extends EnumConverter<User.Type> {
    private static BiMap<User.Type, Integer> dictionary = ImmutableBiMap.of(
            User.Type.STUDENT, 0,
            User.Type.STAFF, 1
    );

    @Override
    protected BiMap<User.Type, Integer> getConverter() {
        return dictionary;
    }
}
