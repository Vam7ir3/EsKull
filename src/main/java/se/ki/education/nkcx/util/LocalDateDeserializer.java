package se.ki.education.nkcx.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    protected LocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Can be used if custom format is required.
        //return LocalDate.parse(jsonParser.readValueAs(String.class), DateTimeFormatter.ISO_LOCAL_DATE);//yyyy-MM-dd
        return Instant.ofEpochMilli(jsonParser.readValueAs(Long.class)).atZone(ZoneOffset.UTC).toLocalDate();
    }
}
