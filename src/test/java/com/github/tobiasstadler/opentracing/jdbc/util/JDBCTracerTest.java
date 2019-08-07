package com.github.tobiasstadler.opentracing.jdbc.util;

import io.opentracing.Scope;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.tag.Tags;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer.ONLY_WITH_ACTIVE_SPAN;
import static com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer.SQL_TO_IGNORE;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class JDBCTracerTest {

    private static final String SERVER_NAME = "ServerName";

    private static final int PORT_NUMBER = 1234;

    private static final String DATABASE_NAME = "ServerName";

    private static final String USER = "ServerName";

    private static final String OPERATION_NAME = "OperationName";

    private static final String SQL = "SELECT 1";

    private final MockTracer mockTracer = new MockTracer();

    @Test
    void shouldExecuteOperation() throws SQLException {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), emptyMap());
        JDBCTracer.Operation<Void> operation = spy(JDBCTracer.Operation.class);

        jdbcTracer.trace(OPERATION_NAME, SQL, operation);

        verify(operation).execute();
    }

    @Test
    void shouldCreateSpanOnWhenOperationWasSuccessful() throws SQLException {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), emptyMap());

        jdbcTracer.trace(OPERATION_NAME, SQL, () -> null);

        assertEquals(1, mockTracer.finishedSpans().size());

        MockSpan span = mockTracer.finishedSpans().get(0);
        assertEquals(OPERATION_NAME, span.operationName());
        assertEquals(8, span.tags().size());
        assertEquals(0, span.logEntries().size());
    }

    @Test
    void shouldCreateSpanWIthErrorOnWhenOperationThrowsAnException() {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), emptyMap());

        try {
            jdbcTracer.trace(OPERATION_NAME, SQL, () -> {
                throw new SQLException("Message");
            });
        } catch (SQLException ignored) {

        }

        assertEquals(1, mockTracer.finishedSpans().size());

        MockSpan span = mockTracer.finishedSpans().get(0);
        assertEquals(OPERATION_NAME, span.operationName());
        assertEquals(9, span.tags().size());
        assertEquals(true, span.tags().get(Tags.ERROR.getKey()));
        assertEquals(1, span.logEntries().size());
        assertEquals(5, span.logEntries().get(0).fields().size());
    }

    @Test
    void shouldCreateNoSpanWhenOnlyWithActiveSpanIsSetAndASpanIsActive() throws SQLException {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), singletonMap(ONLY_WITH_ACTIVE_SPAN, "true"));

        try (Scope ignored = mockTracer.buildSpan("foo").startActive(true)) {
            jdbcTracer.trace(OPERATION_NAME, SQL, () -> null);
        }

        assertEquals(2, mockTracer.finishedSpans().size());
    }

    @Test
    void shouldCreateNoSpanWhenOnlyWithActiveSpanIsSetAndNoSpanIsActive() throws SQLException {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), singletonMap(ONLY_WITH_ACTIVE_SPAN, "true"));

        jdbcTracer.trace(OPERATION_NAME, SQL, () -> null);

        assertEquals(0, mockTracer.finishedSpans().size());
    }

    @Test
    void shouldCreateNoSpanWhenSqlToIgnoreIsSetAndSqlDoesMatch() throws SQLException {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), singletonMap(SQL_TO_IGNORE, "\\s*SELECT 1\\s*;?"));

        jdbcTracer.trace(OPERATION_NAME, SQL, () -> null);

        assertEquals(0, mockTracer.finishedSpans().size());
    }

    @Test
    void shouldCreateNoSpanWhenSqlToIgnoreIsSetAndSqlDoesNotMatch() throws SQLException {
        JDBCTracer jdbcTracer = new JDBCTracer(mockTracer, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), singletonMap(SQL_TO_IGNORE, "SELECT 2"));

        jdbcTracer.trace(OPERATION_NAME, SQL, () -> null);

        assertEquals(1, mockTracer.finishedSpans().size());

        MockSpan span = mockTracer.finishedSpans().get(0);
        assertEquals(OPERATION_NAME, span.operationName());
        assertEquals(8, span.tags().size());
        assertEquals(0, span.logEntries().size());
    }
}
