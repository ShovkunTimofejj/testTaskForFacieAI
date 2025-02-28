package com.example.facieaiprojecttest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JacksonConfigTest {

    @Test
    void testObjectMapperCreation() {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper objectMapper = config.objectMapper();

        assertNotNull(objectMapper);
    }

    @Test
    void testObjectMapperProperties() {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper objectMapper = config.objectMapper();

        try {
            LocalDate date = LocalDate.now();
            String json = objectMapper.writeValueAsString(date);
            LocalDate deserializedDate = objectMapper.readValue(json, LocalDate.class);
            assertEquals(date, deserializedDate);
        } catch (Exception e) {
            fail("Serialization/deserialization does not work correctly");
        }

        assertTrue(objectMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) == false);
    }


    @Test
    void testXmlMapperCreation() {
        JacksonConfig config = new JacksonConfig();
        XmlMapper xmlMapper = config.xmlMapper();

        assertNotNull(xmlMapper);
    }

    @Test
    void testXmlMapperProperties() {
        JacksonConfig config = new JacksonConfig();
        XmlMapper xmlMapper = config.xmlMapper();

        assertTrue(xmlMapper.isEnabled(SerializationFeature.INDENT_OUTPUT));
        assertTrue(xmlMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) == false);
    }
}
