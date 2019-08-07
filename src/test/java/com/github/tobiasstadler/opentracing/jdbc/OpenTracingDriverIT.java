package com.github.tobiasstadler.opentracing.jdbc;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class OpenTracingDriverIT {

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
        .withDatabaseName(USERNAME)
        .withUsername(USERNAME)
        .withPassword(PASSWORD);

    private final OpenTracingDriver driver = new OpenTracingDriver();

    @Test
    void shouldReturnOpenTracingConnection() throws SQLException {
        Properties properties = new Properties();
        properties.put("user", USERNAME);
        properties.put("password", PASSWORD);

        assertEquals(OpenTracingConnection.class, driver.connect(postgreSQLContainer.getJdbcUrl().replaceFirst("jdbc:", "jdbc:opentracing:"), properties).getClass());
    }
}
