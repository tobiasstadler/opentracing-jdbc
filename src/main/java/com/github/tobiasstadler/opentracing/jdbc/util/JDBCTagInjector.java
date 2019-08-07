package com.github.tobiasstadler.opentracing.jdbc.util;

import io.opentracing.Tracer;
import io.opentracing.tag.Tags;

final class JDBCTagInjector {

    private JDBCTagInjector() {
    }

    public static Tracer.SpanBuilder addTag(Tracer.SpanBuilder spanBuilder, ConnectionInfo connectionInfo, String sql) {
        spanBuilder
            .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CLIENT)
            .withTag(Tags.COMPONENT.getKey(), "jdbc")
            .withTag(Tags.PEER_HOSTNAME.getKey(), connectionInfo.getServerName())
            .withTag(Tags.PEER_PORT.getKey(), connectionInfo.getPortNumber());


        spanBuilder.withTag(Tags.DB_TYPE.getKey(), "sql");
        if (connectionInfo.isDatabaseNameSet()) {
            spanBuilder.withTag(Tags.DB_INSTANCE.getKey(), connectionInfo.getDatabaseName());
        }
        if (connectionInfo.isUserSet()) {
            spanBuilder.withTag(Tags.DB_USER.getKey(), connectionInfo.getUser());
        }
        if (sql != null && !sql.isEmpty()) {
            spanBuilder.withTag(Tags.DB_STATEMENT.getKey(), sql);
        }

        return spanBuilder;
    }
}
