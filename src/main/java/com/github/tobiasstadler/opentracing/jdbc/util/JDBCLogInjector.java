package com.github.tobiasstadler.opentracing.jdbc.util;

import io.opentracing.Span;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

final class JDBCLogInjector {

    private JDBCLogInjector() {
    }

    public static Span addLog(Span span, Throwable throwable) {
        span.setTag(Tags.ERROR.getKey(), true);

        Map<String, Object> fields = new HashMap<>();
        fields.put(Fields.EVENT, "error");
        fields.put(Fields.ERROR_KIND, "exception");
        fields.put(Fields.ERROR_OBJECT, throwable.getClass().getName());

        String message = throwable.getMessage();
        if (message != null && !message.isEmpty()) {
            fields.put(Fields.MESSAGE, message);
        }

        String stackTrace = creatStackTrace(throwable);
        if (stackTrace != null && !stackTrace.isEmpty()) {
            fields.put(Fields.STACK, stackTrace);
        }

        span.log(fields);

        return span;
    }

    private static String creatStackTrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }
}
