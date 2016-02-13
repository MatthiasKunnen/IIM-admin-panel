package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Visibility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Makes it possible to map enums while still having the ability to rearrange, add or modify enums without braking the database
 * Created by matthias on 2016-02-13.
 */
@Converter(autoApply = true)
public class VisibilityConverter implements AttributeConverter<Visibility, Integer> {
    private static BiMap<Visibility, Integer> converter = ImmutableBiMap.of(
            Visibility.Administrator, 0,
            Visibility.Docent, 1,
            Visibility.Student, 2
    );

    @Override
    public Integer convertToDatabaseColumn(Visibility visibility) {
        return converter.get(visibility);
    }

    @Override
    public Visibility convertToEntityAttribute(Integer id) {
        return converter.inverse().get(id);
    }
}
