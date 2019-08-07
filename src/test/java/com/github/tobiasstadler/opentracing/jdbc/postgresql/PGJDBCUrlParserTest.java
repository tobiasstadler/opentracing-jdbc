package com.github.tobiasstadler.opentracing.jdbc.postgresql;

import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PGJDBCUrlParserTest {

    @Test
    void shouldParseJDBCUrl() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql:/", new Properties());

        assertEquals("localhost", connectionInfo.getServerName());
        assertEquals(5432, connectionInfo.getPortNumber());
        assertNull(connectionInfo.getDatabaseName());
        assertNull(connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithDatabase() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql:database", new Properties());

        assertEquals("localhost", connectionInfo.getServerName());
        assertEquals(5432, connectionInfo.getPortNumber());
        assertEquals("database", connectionInfo.getDatabaseName());
        assertNull(connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithHost() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql://host/", new Properties());

        assertEquals("host", connectionInfo.getServerName());
        assertEquals(5432, connectionInfo.getPortNumber());
        assertNull(connectionInfo.getDatabaseName());
        assertNull(connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithHostAndDatabase() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql://host/database", new Properties());

        assertEquals("host", connectionInfo.getServerName());
        assertEquals(5432, connectionInfo.getPortNumber());
        assertEquals("database", connectionInfo.getDatabaseName());
        assertNull(connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithHostPortAndDatabase() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql://host:1234/database", new Properties());

        assertEquals("host", connectionInfo.getServerName());
        assertEquals(1234, connectionInfo.getPortNumber());
        assertEquals("database", connectionInfo.getDatabaseName());
        assertNull(connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithHostPortAndUser() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql://host:1234/?user=user", new Properties());

        assertEquals("host", connectionInfo.getServerName());
        assertEquals(1234, connectionInfo.getPortNumber());
        assertEquals("user", connectionInfo.getDatabaseName());
        assertEquals("user", connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithHostPortDatabaseAndUser1() {
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql://host:1234/database?user=user", new Properties());

        assertEquals("host", connectionInfo.getServerName());
        assertEquals(1234, connectionInfo.getPortNumber());
        assertEquals("database", connectionInfo.getDatabaseName());
        assertEquals("user", connectionInfo.getUser());
    }

    @Test
    void shouldParseJDBCUrlWithHostPortAndDatabaseAndUser2() {
        Properties properties = new Properties();
        properties.put("user", "user");
        ConnectionInfo connectionInfo = PostgresqlJDBCUrlParser.parseJDBCUrl("jdbc:postgresql://host:1234/database", properties);

        assertEquals("host", connectionInfo.getServerName());
        assertEquals(1234, connectionInfo.getPortNumber());
        assertEquals("database", connectionInfo.getDatabaseName());
        assertEquals("user", connectionInfo.getUser());
    }
}
