package com.github.tobiasstadler.opentracing.jdbc;

import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import com.github.tobiasstadler.opentracing.jdbc.util.JDBCTracer;
import com.github.tobiasstadler.opentracing.jdbc.util.TracerProvider;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class OpenTracingDriver implements Driver {

    static {
        Driver driver = new OpenTracingDriver();
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new IllegalStateException("Exception registering OpenTracing JDBC driver", e);
        }
    }

    private static String getRealUrl(String url) {
        return url.replaceFirst("^jdbc:opentracing:", "jdbc:");
    }

    private Map<String, String> getProperties(String url, Properties info) {
        Map<String, String> properties = new HashMap<>();
        properties.put(JDBCTracer.ONLY_WITH_ACTIVE_SPAN, Objects.toString(info.get(JDBCTracer.ONLY_WITH_ACTIVE_SPAN), null));
        properties.put(JDBCTracer.SQL_TO_IGNORE, Objects.toString(info.get(JDBCTracer.SQL_TO_IGNORE), null));

        for (String parameters : url.split(";")) {
            String[] keyAndValue = parameters.split("=", 2);
            if (keyAndValue.length != 2) {
                continue;
            }

            switch (keyAndValue[0]) {
                case JDBCTracer.ONLY_WITH_ACTIVE_SPAN:
                case JDBCTracer.SQL_TO_IGNORE:
                    properties.putIfAbsent(keyAndValue[0], keyAndValue[1]);
                default:
            }
        }

        return properties;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }

        String realUrl = getRealUrl(url);
        JDBCTracer jdbcTracer = new JDBCTracer(TracerProvider.getTracer(), ConnectionInfo.fromJDBCUrl(realUrl, info), getProperties(url, info));

        return new OpenTracingConnection(DriverManager.getConnection(realUrl, info), jdbcTracer);
    }

    @Override
    public boolean acceptsURL(String url) {
        return url.startsWith("jdbc:opentracing:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        String realUrl = getRealUrl(url);
        Driver driver = DriverManager.getDriver(realUrl);

        return driver.getPropertyInfo(realUrl, info);
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }
}
