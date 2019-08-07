package com.github.tobiasstadler.opentracing.jdbc.util;

import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;

import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.tobiasstadler.opentracing.jdbc.util.JDBCLogInjector.addLog;
import static com.github.tobiasstadler.opentracing.jdbc.util.JDBCTagInjector.addTag;

public class JDBCTracer {

    public static final String ONLY_WITH_ACTIVE_SPAN = "onlyWithActiveSpan";
    public static final String SQL_TO_IGNORE = "sqlToIgnore";
    private final Tracer tracer;
    private final ConnectionInfo connectionInfo;
    private final boolean onlyWithActiveSpan;
    private final Pattern sqlToIgnore;

    public JDBCTracer(Tracer tracer, ConnectionInfo connectionInfo, Map<String, String> properties) {
        this.tracer = tracer;
        this.connectionInfo = connectionInfo;
        this.onlyWithActiveSpan = properties.get(ONLY_WITH_ACTIVE_SPAN) != null && Boolean.parseBoolean(properties.get(ONLY_WITH_ACTIVE_SPAN));
        this.sqlToIgnore = properties.get(SQL_TO_IGNORE) != null ? Pattern.compile(properties.get(SQL_TO_IGNORE), Pattern.CASE_INSENSITIVE) : null;
    }

    public <T> T trace(String operationName, String sql, Operation<T> operation) throws SQLException {
        if ((onlyWithActiveSpan && tracer.activeSpan() == null) || (sqlToIgnore != null && sqlToIgnore.matcher(sql).matches())) {
            return operation.execute();
        }

        Span span = addTag(tracer.buildSpan(operationName), connectionInfo, sql).start();
        try (Scope ignored = tracer.scopeManager().activate(span, true)) {
            try {
                return operation.execute();
            } catch (Throwable t) {
                addLog(span, t);

                throw t;
            }
        }
    }

    @FunctionalInterface
    public interface Operation<T> {

        T execute() throws SQLException;
    }
}
