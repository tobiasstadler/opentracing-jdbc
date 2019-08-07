package com.github.tobiasstadler.opentracing.jdbc.postgresql;

import com.github.tobiasstadler.opentracing.jdbc.OpenTracingConnection;
import com.github.tobiasstadler.opentracing.jdbc.OpenTracingXAConnection;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.XAConnection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class OpenTracingPGXADataSourceIT {

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
        .withDatabaseName(USERNAME)
        .withUsername(USERNAME)
        .withPassword(PASSWORD);

    @Test
    void shouldReturnOpenTracingXAConnectionAndOpenTracingConnection1() throws SQLException {
        OpenTracingPGXADataSource xaDataSource = new OpenTracingPGXADataSource();
        xaDataSource.setServerName(postgreSQLContainer.getContainerIpAddress());
        xaDataSource.setPortNumber(postgreSQLContainer.getMappedPort(5432));
        xaDataSource.setUser(USERNAME);
        xaDataSource.setPassword(PASSWORD);

        XAConnection xaConnection = xaDataSource.getXAConnection();
        assertEquals(OpenTracingXAConnection.class, xaConnection.getClass());
        assertEquals(OpenTracingConnection.class, xaConnection.getConnection().getClass());
    }

    @Test
    void shouldReturnOpenTracingXAConnectionAndOpenTracingConnection2() throws SQLException {
        OpenTracingPGXADataSource xaDataSource = new OpenTracingPGXADataSource();
        xaDataSource.setServerName(postgreSQLContainer.getContainerIpAddress());
        xaDataSource.setPortNumber(postgreSQLContainer.getMappedPort(5432));

        XAConnection xaConnection = xaDataSource.getXAConnection(USERNAME, PASSWORD);
        assertEquals(OpenTracingXAConnection.class, xaConnection.getClass());
        assertEquals(OpenTracingConnection.class, xaConnection.getConnection().getClass());
    }
}
