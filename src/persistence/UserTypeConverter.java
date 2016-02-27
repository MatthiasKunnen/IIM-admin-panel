package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.User;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Makes it possible to map the enum {@link domain.User.Type} while still having the ability to rearrange, add or modify the enum values without breaking the database
 */
@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<User.Type, Integer> {
    private static BiMap<User.Type, Integer> converter = ImmutableBiMap.of(
            User.Type.STUDENT, 0,
            User.Type.STAFF, 1
    );

    @Override
    public Integer convertToDatabaseColumn(User.Type visibility) {
        return converter.get(visibility);
    }

    @Override
    public User.Type convertToEntityAttribute(Integer id) {
        return converter.inverse().get(id);
    }
}
