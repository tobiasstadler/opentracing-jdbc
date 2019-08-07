package com.github.tobiasstadler.opentracing.jdbc.postgresql;

import com.github.tobiasstadler.opentracing.jdbc.OpenTracingConnection;
import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;
import com.github.tobiasstadler.opentracing.jdbc.util.TracerProvider;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OpenTracingPGDataSource extends PGSimpleDataSource {

    private final Map<String, String> properties = new HashMap<>();

    @Override
    public Connection getConnection() throws SQLException {
        return new OpenTracingConnection(super.getConnection(), getTracer(getUser()));
    }

    @Override
    public Connection getConnection(String user, String password) throws SQLException {
        return new OpenTracingConnection(super.getConnection(user, password), getTracer(user));
    }

    public void setOnlyWithActiveSpan(String onlyActiveSpan) {
        properties.put(JDBCTracer.ONLY_WITH_ACTIVE_SPAN, onlyActiveSpan);
    }

    public void setSqlToIgnore(String sqlToIgnore) {
        properties.put(JDBCTracer.SQL_TO_IGNORE, sqlToIgnore);
    }

    private JDBCTracer getTracer(String user) {
        return new JDBCTracer(TracerProvider.getTracer(), new ConnectionInfo(getServerName(), getPortNumber(), getDatabaseName(), user), properties);
    }
}
