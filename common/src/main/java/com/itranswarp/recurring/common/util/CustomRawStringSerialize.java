package com.itranswarp.recurring.common.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by changsure on 11/25/15.
 */
public class CustomRawStringSerialize extends JsonSerializer<String> {
    @Override
    public void serialize(String o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeRawValue(o);
    }
}
