package com.github.tobiasstadler.opentracing.jdbc;

import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpenTracingDriverTest {

    private final OpenTracingDriver openTracingDriver = new OpenTracingDriver();

    @Test
    void shouldAcceptOpenTracingJDBCUrl() {
        assertTrue(openTracingDriver.acceptsURL("jdbc:opentracing:"));
    }

    @Test
    void shouldNotAcceptPostgresqlJDBCUrl() {
        assertFalse(openTracingDriver.acceptsURL("jdbc:postgresql:"));
    }

    @Test
    void shouldReturnPostgresqlPropertyInfoForWrappedPostgresqlJDBCUrl() throws SQLException {
        DriverPropertyInfo[] postgresqlPropertyInfo = DriverManager.getDriver("jdbc:postgresql:").getPropertyInfo("jdbc:postgresql:", null);
        DriverPropertyInfo[] opentracingPropertyInfo = openTracingDriver.getPropertyInfo("jdbc:opentracing:postgresql:", null);

        assertEquals(postgresqlPropertyInfo.length, opentracingPropertyInfo.length);
        for (int i = 0; i < postgresqlPropertyInfo.length; ++i) {
            assertEquals(postgresqlPropertyInfo[i].name, opentracingPropertyInfo[i].name);
            assertEquals(postgresqlPropertyInfo[i].description, opentracingPropertyInfo[i].description);
            assertEquals(postgresqlPropertyInfo[i].required, opentracingPropertyInfo[i].required);
            assertEquals(postgresqlPropertyInfo[i].value, opentracingPropertyInfo[i].value);
            assertArrayEquals(postgresqlPropertyInfo[i].choices, opentracingPropertyInfo[i].choices);
        }
    }
}
