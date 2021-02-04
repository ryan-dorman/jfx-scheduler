package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A wrapper around a JDBC connection that can be accessed throughout the application. The connections provided
 * can be new or existing due to JConnector connection pooling. The the connection properties are read from the file
 * <code>connection.properties</code>>.
 */
public class DBConnection {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static MysqlDataSource d;

    private DBConnection() {
    }

    /**
     * Provides a database connection
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (d == null) {
            Properties connectionProps = new Properties();
            connectionProps.load(DBConnection.class.getClassLoader().getResourceAsStream("connection.properties"));

            d = new MysqlDataSource();
            d.setUrl(connectionProps.getProperty("url"));
            d.setDatabaseName(connectionProps.getProperty("name"));
            d.setUser(connectionProps.getProperty("user"));
            d.setPassword(connectionProps.getProperty("pass"));
        }

        Connection conn = d.getConnection();
        conn.setAutoCommit(false);
        sysLogger.info("Database connection created: " + conn.toString());
        sysLogger.info("Database Auto Commit: " + conn.getAutoCommit());

        return conn;
    }
}
