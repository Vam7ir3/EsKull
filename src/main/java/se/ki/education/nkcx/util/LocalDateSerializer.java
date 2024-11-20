package se.ki.education.nkcx.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;

/* Created by: Binay Singh */

public class LocalDateSerializer extends StdSerializer<LocalDate> {

    public LocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Can be used if custom format of date is required.
        jsonGenerator.writeString(String.valueOf(localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()));//yyyy-MM-dd
    }
}
