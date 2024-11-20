package se.ki.education.nkcx.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.LocalDate;

/* Created by: Binay Singh */

/*
JPA maps Java 8 Date classes to TinyBlob.
This class is needed to add the support for Java 8 date APIs.
JPA does not support mapping for Java 8 APIs, cuz JPA 2.1 was released before Java 8.
 */

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date dbDate) {
        return dbDate == null ? null : dbDate.toLocalDate();
    }
}
