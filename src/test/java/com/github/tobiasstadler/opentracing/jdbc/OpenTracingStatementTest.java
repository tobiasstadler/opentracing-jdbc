package com.github.tobiasstadler.opentracing.jdbc;

import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.sql.Statement;

import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class OpenTracingStatementTest {

    private static final String SQL = "SELECT 1";

    private final JDBCTracer jdbcTracer = spy(new JDBCTracer(NoopTracerFactory.create(), ConnectionInfo.EMPTY, emptyMap()));

    private final OpenTracingStatement statement = new OpenTracingStatement(mock(Statement.class), null, jdbcTracer);

    @Test
    void shouldTraceExecuteWithExecuteOperationName1() throws SQLException {
        statement.execute(SQL);

        verify(jdbcTracer).trace(eq("execute"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteWithExecuteOperationName2() throws SQLException {
        statement.execute(SQL, Statement.RETURN_GENERATED_KEYS);

        verify(jdbcTracer).trace(eq("execute"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteWithExecuteOperationName3() throws SQLException {
        statement.execute(SQL, new int[0]);

        verify(jdbcTracer).trace(eq("execute"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteWithExecuteOperationName4() throws SQLException {
        statement.execute(SQL, new String[0]);

        verify(jdbcTracer).trace(eq("execute"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteQueryWithExecuteQueryOperationName1() throws SQLException {
        statement.executeQuery(SQL);

        verify(jdbcTracer).trace(eq("executeQuery"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteUpdateWithExecuteUpdateOperationName1() throws SQLException {
        statement.executeUpdate(SQL);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteUpdateWithExecuteUpdateOperationName2() throws SQLException {
        statement.executeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteUpdateWithExecuteUpdateOperationName3() throws SQLException {
        statement.executeUpdate(SQL, new int[0]);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteUpdateWithExecuteUpdateOperationName4() throws SQLException {
        statement.executeUpdate(SQL, new String[0]);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeUpdateWithExecuteUpdateOperationName1() throws SQLException {
        statement.executeLargeUpdate(SQL);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeUpdateWithExecuteUpdateOperationName2() throws SQLException {
        statement.executeLargeUpdate(SQL, Statement.RETURN_GENERATED_KEYS);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeUpdateWithExecuteUpdateOperationName3() throws SQLException {
        statement.executeLargeUpdate(SQL, new int[0]);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeUpdateWithExecuteUpdateOperationName4() throws SQLException {
        statement.executeLargeUpdate(SQL, new String[0]);

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteBatchWithExecuteBatchOperationName() throws SQLException {
        statement.addBatch(SQL);
        statement.addBatch(SQL);
        statement.executeBatch();

        verify(jdbcTracer).trace(eq("executeBatch"), eq(SQL + ';' + SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeBatchWithExecuteBatchOperationName() throws SQLException {
        statement.addBatch(SQL);
        statement.addBatch(SQL);
        statement.executeLargeBatch();

        verify(jdbcTracer).trace(eq("executeBatch"), eq(SQL + ';' + SQL), any(JDBCTracer.Operation.class));
    }
}
