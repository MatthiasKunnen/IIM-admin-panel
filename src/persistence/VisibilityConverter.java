package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Visibility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link Visibility} into a database id.
 *
 * @author Matthias Kunnen
 */
@Converter(autoApply = true)
public class VisibilityConverter implements AttributeConverter<Visibility, Integer> {
    private static BiMap<Visibility, Integer> dictionary = ImmutableBiMap.of(
            Visibility.Administrator, 0,
            Visibility.Docent, 1,
            Visibility.Student, 2
    );

    @Override
    public Integer convertToDatabaseColumn(Visibility visibility) {
        return dictionary.get(visibility);
    }

    @Override
    public Visibility convertToEntityAttribute(Integer integer) {
        return dictionary.inverse().get(integer);
    }
}
