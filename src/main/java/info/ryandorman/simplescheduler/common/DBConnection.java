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
 * A wrapper around a datasource that allows straight forward access to a connection with that source. The connection
 * properties are read from the file <code>connection.properties</code>.
 */
public class DBConnection {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static MysqlDataSource d;
    private static Connection conn;

    /**
     * Constructor is private to prevent class creation.
     */
    private DBConnection() {
    }

    /**
     * Provides a database connection after ensuring the datasource is established. The connection <code>should</code>
     * be obtained in a try-with-resources to exploit Connection's extension of AutoClosable. A reference to the
     * connection is stored statically to ensure only one connection is established at any  time. If a connection does
     * not exist or has closed, a new one is opened. Auto-commit is turned off by default on new connections.
     * @return Connection
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

        if (conn == null || (conn != null && conn.isClosed())) {
            conn = d.getConnection();
            conn.setAutoCommit(false);
            sysLogger.info("Database connection created: " + conn.toString());
            sysLogger.info("Database Auto Commit: " + conn.getAutoCommit());
        }

        return conn;
    }
}
