package info.ryandorman.simplescheduler.common;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A wrapper around a database that allows straight forward access to a connection with it. The connection
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
     * Reads the database properties from <code>connection.properties</code> and sets up the datasource. A ref to the
     * data source is stored statically for <code>getConnection</code> to use.
     * @throws IOException If there are issues reading the properties file
     */
    private static void initDataSource() throws IOException {
        Properties connectionProps = new Properties();
        connectionProps.load(DBConnection.class.getClassLoader().getResourceAsStream("connection.properties"));

        d = new MysqlDataSource();
        d.setUrl(connectionProps.getProperty("url"));
        d.setDatabaseName(connectionProps.getProperty("name"));
        d.setUser(connectionProps.getProperty("user"));
        d.setPassword(connectionProps.getProperty("pass"));
    }

    /**
     * Provides a database connection. The connection <code>should not</code> be obtained in a try-with-resources
     * to avoid closing the connection due to its extension of AutoClosable. A reference to the connection is stored
     * statically to ensure only one connection is established at any time. If a connection does not exist or has
     * closed, a new one is opened. Close all connection's before the application exits. Auto-commit is turned off by
     * default on new connections.
     * @return Connection
     * @throws SQLException If there is an issue establishing the connection
     * @throws IOException If there are issues initializing the data source from the properties file
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (d == null) {
            initDataSource();
        }

        if (conn == null || (conn != null && conn.isClosed())) {
            conn = d.getConnection();
            conn.setAutoCommit(false);
            sysLogger.info("Database connection created: " + conn.toString());
            sysLogger.info("Database Auto Commit: " + conn.getAutoCommit());
        } else {
            sysLogger.info("Using existing connection: " + conn.toString());
        }

        return conn;
    }

    /**
     * Close the existing connection to the database. Log the outcome and capture any errors.
     */
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                sysLogger.info("Database connection closed: " + conn.toString());
            }
        } catch (SQLException e) {
            sysLogger.severe("Database connection could not close: " + conn.toString());
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Close an existing PreparedStatement associated with a connection. Captures any errors
     * @param stmt
     */
    public static void close(PreparedStatement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            sysLogger.severe("Database statement could not close: " + stmt.toString());
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        }
    }
}
