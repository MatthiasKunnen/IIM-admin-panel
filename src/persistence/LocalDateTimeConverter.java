package persistence;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeConverter implements AttributeConverter<ObjectProperty<LocalDateTime>, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ObjectProperty<LocalDateTime> localDateObjectProperty) {
        return localDateObjectProperty == null ? null : Timestamp.valueOf(localDateObjectProperty.getValue());
    }

    @Override
    public ObjectProperty<LocalDateTime> convertToEntityAttribute(Timestamp timestamp) {
        return timestamp == null ? null : new SimpleObjectProperty<>(timestamp.toLocalDateTime());
    }
}