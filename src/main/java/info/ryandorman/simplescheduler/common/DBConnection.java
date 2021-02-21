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
 * A wrapper around a database that allows straight forward access to a connection. The connection properties are read
 * from the file <code>connection.properties</code>. Allows only one open DB connection to be obtained throughout the
 * application.  Auto-commit is turned off by default on connections. <em>Close the connection before the application
 * exits.</em>
 */
public class DBConnection {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static MysqlDataSource d;
    private static Connection conn;

    /**
     * Constructor is private to direct prevent class instantiation.
     */
    private DBConnection() {
    }

    /**
     * Reads the database properties from <code>connection.properties</code> and sets up the datasource.
     *
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
     * Provides a database connection. The connection <em>should not</em> be obtained in a try-with-resources
     * to avoid closing the connection due to its extension of <code>AutoClosable</code>.
     *
     * @return Connection Reference to the application's current database connection
     * @throws SQLException If there is an issue establishing the connection
     * @throws IOException  If there are issues initializing the data source from the properties file
     */
    public static Connection getConnection() throws SQLException, IOException {
        if (d == null) {
            initDataSource();
        }

        if (conn == null || conn.isClosed()) {
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
     * Commits any open transactions to the database.
     */
    public static void commit() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
                sysLogger.info("Committing open database transactions for connection : " + conn.toString());
            }
        } catch (SQLException e) {
            sysLogger.severe("Database commit failed for connection: " + conn.toString());
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Closes the existing connection to the database.
     */
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
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
     * Closes an existing <code>PreparedStatement</code> associated with a connection.
     *
     * @param stmt <code>PreparedStatement</code> to be closed
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
