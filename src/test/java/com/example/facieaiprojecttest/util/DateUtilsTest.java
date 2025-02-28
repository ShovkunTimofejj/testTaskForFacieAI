package com.example.facieaiprojecttest.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DateUtilsTest {

    @Test
    void testIsValidDateValid() {
        String dateStr = "20230227";

        assertTrue(DateUtils.isValidDate(dateStr));
    }

    @Test
    void testIsValidDateInvalidFormat() {
        String dateStr = "2023-02-27";

        assertFalse(DateUtils.isValidDate(dateStr));
    }

    @Test
    void testIsValidDateInvalidLength() {
        String dateStr = "2023022";

        assertFalse(DateUtils.isValidDate(dateStr));
    }

    @Test
    void testIsValidDateNull() {
        String dateStr = null;

        assertFalse(DateUtils.isValidDate(dateStr));
    }

    @Test
    void testIsValidDateNonNumeric() {
        String dateStr = "2023ab27";

        assertFalse(DateUtils.isValidDate(dateStr));
    }

    @Test
    void testIsValidDateInvalidDate() {
        String dateStr = "20230232";

        assertFalse(DateUtils.isValidDate(dateStr));
    }
}

