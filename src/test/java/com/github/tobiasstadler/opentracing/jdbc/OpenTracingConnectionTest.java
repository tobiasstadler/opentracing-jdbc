package com.github.tobiasstadler.opentracing.jdbc;

import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.junit.jupiter.api.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

class OpenTracingConnectionTest {

    private final JDBCTracer jdbcTracer = new JDBCTracer(NoopTracerFactory.create(), ConnectionInfo.EMPTY, emptyMap());

    private final OpenTracingConnection connection = new OpenTracingConnection(mock(Connection.class), jdbcTracer);

    @Test
    void shouldReturnOpenTracingStatement1() throws SQLException {
        Statement statement = connection.createStatement();

        assertEquals(OpenTracingStatement.class, statement.getClass());
    }

    @Test
    void shouldReturnOpenTracingStatement2() throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        assertEquals(OpenTracingStatement.class, statement.getClass());
    }

    @Test
    void shouldReturnOpenTracingStatement3() throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);

        assertEquals(OpenTracingStatement.class, statement.getClass());
    }

    @Test
    void shouldReturnOpenTracingPreparedStatement1() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1");

        assertEquals(OpenTracingPreparedStatement.class, preparedStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingPreparedStatement2() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        assertEquals(OpenTracingPreparedStatement.class, preparedStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingPreparedStatement3() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);

        assertEquals(OpenTracingPreparedStatement.class, preparedStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingPreparedStatement4() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1", new int[0]);

        assertEquals(OpenTracingPreparedStatement.class, preparedStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingPreparedStatement5() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1", new String[0]);

        assertEquals(OpenTracingPreparedStatement.class, preparedStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingPreparedStatement6() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1", Statement.RETURN_GENERATED_KEYS);

        assertEquals(OpenTracingPreparedStatement.class, preparedStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingCallableStatement1() throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("SELECT 1");

        assertEquals(OpenTracingCallableStatement.class, callableStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingCallableStatement2() throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("SELECT 1", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        assertEquals(OpenTracingCallableStatement.class, callableStatement.getClass());
    }

    @Test
    void shouldReturnOpenTracingCallableStatement3() throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("SELECT 1", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);

        assertEquals(OpenTracingCallableStatement.class, callableStatement.getClass());
    }
}
