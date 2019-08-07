package com.github.tobiasstadler.opentracing.jdbc.postgresql;

import com.github.tobiasstadler.opentracing.jdbc.util.ConnectionInfo;
import org.postgresql.Driver;
import org.postgresql.PGProperty;

import java.util.Properties;

public final class PostgresqlJDBCUrlParser {

    private PostgresqlJDBCUrlParser() {
    }

    public static ConnectionInfo parseJDBCUrl(String url, Properties info) {
        Properties p = Driver.parseURL(url, info);
        if (p != null) {
            String pgHost = p.getProperty(PGProperty.PG_HOST.getName());
            String pgPort = p.getProperty(PGProperty.PG_PORT.getName());
            String pgDbname = p.getProperty(PGProperty.PG_DBNAME.getName());
            String pgUser = p.getProperty(PGProperty.USER.getName());
            String user = pgUser != null && !pgUser.isEmpty() ? pgUser : null;

            return new ConnectionInfo(
                pgHost != null && !pgHost.isEmpty() ? pgHost : "localhost",
                Integer.parseInt(pgPort != null && !pgPort.isEmpty() ? pgPort : "5432"),
                pgDbname != null && !pgDbname.isEmpty() && !pgDbname.equals("/") ? pgDbname : user,
                user
            );
        }

        return ConnectionInfo.EMPTY;
    }
}
