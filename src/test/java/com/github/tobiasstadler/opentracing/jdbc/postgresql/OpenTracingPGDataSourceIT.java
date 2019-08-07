package com.github.tobiasstadler.opentracing.jdbc.postgresql;

import com.github.tobiasstadler.opentracing.jdbc.OpenTracingConnection;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class OpenTracingPGDataSourceIT {

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
        .withDatabaseName(USERNAME)
        .withUsername(USERNAME)
        .withPassword(PASSWORD);

    @Test
    void shouldReturnOpenTracingConnection1() throws SQLException {
        OpenTracingPGDataSource dataSource = new OpenTracingPGDataSource();
        dataSource.setServerName(postgreSQLContainer.getContainerIpAddress());
        dataSource.setPortNumber(postgreSQLContainer.getMappedPort(5432));
        dataSource.setUser(USERNAME);
        dataSource.setPassword(PASSWORD);

        assertEquals(OpenTracingConnection.class, dataSource.getConnection().getClass());
    }

    @Test
    void shouldReturnOpenTracingConnection2() throws SQLException {
        OpenTracingPGDataSource dataSource = new OpenTracingPGDataSource();
        dataSource.setServerName(postgreSQLContainer.getContainerIpAddress());
        dataSource.setPortNumber(postgreSQLContainer.getMappedPort(5432));

        assertEquals(OpenTracingConnection.class, dataSource.getConnection(USERNAME, PASSWORD).getClass());
    }
}
