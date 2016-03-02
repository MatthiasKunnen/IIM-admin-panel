package persistence;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<ObjectProperty<LocalDate>, Date> {

    @Override
    public Date convertToDatabaseColumn(ObjectProperty<LocalDate> localDateObjectProperty) {
        return localDateObjectProperty == null ? null : Date.valueOf(localDateObjectProperty.getValue());
    }

    @Override
    public ObjectProperty<LocalDate> convertToEntityAttribute(Date date) {
        return date == null ? null : new SimpleObjectProperty<>(date.toLocalDate());
    }
}
