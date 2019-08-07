package com.github.tobiasstadler.opentracing.jdbc.util;

import com.github.tobiasstadler.opentracing.jdbc.postgresql.PostgresqlJDBCUrlParser;

import java.util.Properties;

public class ConnectionInfo {

    public static final ConnectionInfo EMPTY = new ConnectionInfo(null, 0, null, null);

    private final String serverName;

    private final int portNumber;

    private final String databaseName;

    private final String user;

    public ConnectionInfo(String serverName, int portNumber, String databaseName, String user) {
        this.serverName = serverName;
        this.portNumber = portNumber;
        this.databaseName = databaseName;
        this.user = user;
    }

    public static ConnectionInfo fromJDBCUrl(String url, Properties info) {
        if (url.startsWith("jdbc:postgresql:")) {
            return PostgresqlJDBCUrlParser.parseJDBCUrl(url, info);
        }

        return EMPTY;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public boolean isDatabaseNameSet() {
        return databaseName != null;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public boolean isUserSet() {
        return user != null;
    }

    public String getUser() {
        return user;
    }
}
