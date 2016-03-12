package persistence;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import domain.Setting;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Converts {@link domain.Setting.Key} into database ids.
 *
 * @author Matthias Kunnen
 */
@Converter(autoApply = true)
public class KeyConverter implements AttributeConverter<Setting.Key, Integer> {

    private static BiMap<Setting.Key, Integer> dictionary = ImmutableBiMap.of(
            Setting.Key.KEEP_HISTORY, 1
    );

    @Override
    public Integer convertToDatabaseColumn(Setting.Key key) {
        return dictionary.get(key);
    }

    @Override
    public Setting.Key convertToEntityAttribute(Integer integer) {
        return dictionary.inverse().get(integer);
    }
}
