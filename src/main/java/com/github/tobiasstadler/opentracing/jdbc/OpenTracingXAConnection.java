package com.github.tobiasstadler.opentracing.jdbc;

import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.transaction.xa.XAResource;
import java.sql.Connection;
import java.sql.SQLException;

public class OpenTracingXAConnection implements XAConnection {

    private final XAConnection delegate;

    private final JDBCTracer tracer;

    public OpenTracingXAConnection(XAConnection xaConnection, JDBCTracer tracer) {
        this.delegate = xaConnection;
        this.tracer = tracer;
    }

    @Override
    public XAResource getXAResource() throws SQLException {
        return delegate.getXAResource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new OpenTracingConnection(delegate.getConnection(), tracer);
    }

    @Override
    public void close() throws SQLException {
        delegate.close();
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        delegate.addConnectionEventListener(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        delegate.removeConnectionEventListener(listener);
    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        delegate.addStatementEventListener(listener);
    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        delegate.removeStatementEventListener(listener);
    }
}
