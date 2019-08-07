package com.github.tobiasstadler.opentracing.jdbc.util;

import io.opentracing.log.Fields;
import io.opentracing.mock.MockSpan;
import io.opentracing.mock.MockTracer;
import io.opentracing.tag.Tags;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Map;

import static com.github.tobiasstadler.opentracing.jdbc.util.JDBCLogInjector.addLog;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JDBCLogInjectorTest {

    private final MockSpan span = new MockTracer().buildSpan("OperationName").start();

    @Test
    void shouldAddErrorTagAndLog() {
        addLog(span, new SQLException("Message"));

        assertEquals(true, span.tags().get(Tags.ERROR.getKey()));
        assertEquals(1, span.logEntries().size());

        Map<String, ?> fields = span.logEntries().get(0).fields();
        assertEquals(5, fields.size());
        assertEquals("error", fields.get(Fields.EVENT));
        assertEquals("exception", fields.get(Fields.ERROR_KIND));
        assertEquals(SQLException.class.getName(), fields.get(Fields.ERROR_OBJECT));
        assertEquals("Message", fields.get(Fields.MESSAGE));
        assertNotNull(fields.get(Fields.STACK));
    }
}
