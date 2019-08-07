package com.github.tobiasstadler.opentracing.jdbc.util;

import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.tag.Tags;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.tobiasstadler.opentracing.jdbc.util.JDBCTagInjector.addTag;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class JDBCTagInjectorTest {

    private static final String SERVER_NAME = "ServerName";

    private static final int PORT_NUMBER = 1234;

    private static final String DATABASE_NAME = "ServerName";

    private static final String USER = "ServerName";

    private static final String SQL = "SELECT 1";

    private final MockTracer.SpanBuilder spanBuilder = new MockTracer().buildSpan("OperationName");

    @Test
    void shouldAddAllTagsWhen() {
        MockSpan span = (MockSpan) addTag(spanBuilder, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), SQL).start();

        Map<String, Object> tags = span.tags();
        assertEquals(8, tags.size());
        assertEquals(Tags.SPAN_KIND_CLIENT, tags.get(Tags.SPAN_KIND.getKey()));
        assertEquals("jdbc", tags.get(Tags.COMPONENT.getKey()));
        assertEquals(SERVER_NAME, tags.get(Tags.PEER_HOSTNAME.getKey()));
        assertEquals(PORT_NUMBER, tags.get(Tags.PEER_PORT.getKey()));

        assertEquals("sql", tags.get(Tags.DB_TYPE.getKey()));
        assertEquals(DATABASE_NAME, tags.get(Tags.DB_INSTANCE.getKey()));
        assertEquals(USER, tags.get(Tags.DB_USER.getKey()));
        assertEquals(SQL, tags.get(Tags.DB_STATEMENT.getKey()));
    }

    @Test
    void shouldNotAddDbInstanceWhenDatabaseNameIsNull() {
        MockSpan span = (MockSpan) addTag(spanBuilder, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, null, USER), SQL).start();

        assertEquals(7, span.tags().size());
        assertFalse(span.tags().containsKey(Tags.DB_INSTANCE.getKey()));
    }

    @Test
    void shouldNotAddDbUserWhenUserIsNull() {
        MockSpan span = (MockSpan) addTag(spanBuilder, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, null), SQL).start();

        assertEquals(7, span.tags().size());
        assertFalse(span.tags().containsKey(Tags.DB_USER.getKey()));
    }

    @Test
    void shouldNotAddDbStatementTagWhenSqlIsNull() {
        MockSpan span = (MockSpan) addTag(spanBuilder, new ConnectionInfo(SERVER_NAME, PORT_NUMBER, DATABASE_NAME, USER), null).start();

        assertEquals(7, span.tags().size());
        assertFalse(span.tags().containsKey(Tags.DB_STATEMENT.getKey()));
    }
}
