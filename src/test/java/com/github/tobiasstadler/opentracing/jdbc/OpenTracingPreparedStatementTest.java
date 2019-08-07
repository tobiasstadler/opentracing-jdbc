package com.github.tobiasstadler.opentracing.jdbc;

import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;
import io.opentracing.noop.NoopTracerFactory;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.util.Collections.emptyMap;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class OpenTracingPreparedStatementTest {

    private static final String SQL = "SELECT 1";

    private final JDBCTracer jdbcTracer = spy(new JDBCTracer(NoopTracerFactory.create(), ConnectionInfo.EMPTY, emptyMap()));

    private final OpenTracingPreparedStatement preparedStatement = new OpenTracingPreparedStatement(mock(PreparedStatement.class), SQL, jdbcTracer);

    @Test
    void shouldTraceExecuteWithExecuteOperationName() throws SQLException {
        preparedStatement.execute();

        verify(jdbcTracer).trace(eq("execute"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteQueryWithExecuteQueryOperationName() throws SQLException {
        preparedStatement.executeQuery();

        verify(jdbcTracer).trace(eq("executeQuery"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteUpdateWithExecuteUpdateOperationName() throws SQLException {
        preparedStatement.executeUpdate();

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeUpdateWithExecuteUpdateOperationName() throws SQLException {
        preparedStatement.executeLargeUpdate();

        verify(jdbcTracer).trace(eq("executeUpdate"), eq(SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteBatchWithExecuteBatchOperationName() throws SQLException {
        preparedStatement.addBatch();
        preparedStatement.addBatch(SQL);
        preparedStatement.executeBatch();

        verify(jdbcTracer).trace(eq("executeBatch"), eq(SQL + ';' + SQL), any(JDBCTracer.Operation.class));
    }

    @Test
    void shouldTraceExecuteLargeBatchWithExecuteBatchOperationName() throws SQLException {
        preparedStatement.addBatch();
        preparedStatement.addBatch(SQL);
        preparedStatement.executeLargeBatch();

        verify(jdbcTracer).trace(eq("executeBatch"), eq(SQL + ';' + SQL), any(JDBCTracer.Operation.class));
    }
}
