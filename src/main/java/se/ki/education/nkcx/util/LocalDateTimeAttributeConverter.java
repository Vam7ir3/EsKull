package se.ki.education.nkcx.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/*
JPA maps Java 8 Date classes to TinyBlob.
This class is needed to add the support for Java 8 date APIs.
JPA does not support mapping for Java 8 APIs, cuz JPA 2.1 was released before Java 8.
 */

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {
    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp dbTimestamp) {
        return dbTimestamp == null ? null : dbTimestamp.toLocalDateTime();
    }
}
