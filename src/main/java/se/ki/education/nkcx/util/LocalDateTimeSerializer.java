package se.ki.education.nkcx.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //jsonGenerator.writeString(localDateTime.format(dateTimeFormatter));//yyyy-MM-dd HH:mm
        jsonGenerator.writeString(String.valueOf(localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()));//yyyy-MM-dd HH:mm
    }
}
