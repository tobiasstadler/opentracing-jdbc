package com.github.tobiasstadler.opentracing.jdbc.postgresql;

import com.github.tobiasstadler.opentracing.jdbc.OpenTracingXAConnection;
import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;
import com.github.tobiasstadler.opentracing.jdbc.util.TracerProvider;
import org.postgresql.xa.PGXADataSource;

import javax.sql.XAConnection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OpenTracingPGXADataSource extends PGXADataSource {

    private final Map<String, String> properties = new HashMap<>();

    @Override
    public XAConnection getXAConnection() throws SQLException {
        return new OpenTracingXAConnection(super.getXAConnection(), getTracer(getUser()));
    }

    @Override
    public XAConnection getXAConnection(String user, String password) throws SQLException {
        return new OpenTracingXAConnection(super.getXAConnection(user, password), getTracer(user));
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
