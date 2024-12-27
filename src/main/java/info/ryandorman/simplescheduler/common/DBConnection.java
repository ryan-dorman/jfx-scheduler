package info.ryandorman.simplescheduler.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Provides straightforward access to a Database connection. This implementation uses an H2 in-memory database.
 * The database schema is initialized upon connection using the <code>initDB.sql</code> file located in the resources.
 * Only one open DB connection is allowed throughout the application. Auto-commit is turned off by default.
 * <em>Close the connection before the application exits.</em>
 */
public class DBConnection {
    /**
     * System Logger for logging connection and transaction status.
     */
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * Connection to the database.
     */
    private static Connection conn;

    /**
     * Prevents direct class instantiation. Methods should be accessed statically.
     */
    private DBConnection() {
    }

    /**
     * Initializes the database connection to the H2 in-memory database.
     * Executes the schema initialization script (<code>initDB.sql</code>) if available.
     *
     * @throws SQLException If there are issues establishing the connection or executing the initialization script.
     */
    private static void initConnection() throws SQLException {
        String url = "jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false";
        String user = "sa";
        String password = "";

        // Establish the database connection
        conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(false);
        sysLogger.info("Database connection created: " + conn.toString());

        // Initialize the schema
        try (var stmt = conn.createStatement()) {
            stmt.execute("RUNSCRIPT FROM 'classpath:initDB.sql'");
            sysLogger.info("Database schema initialized.");

            stmt.execute("RUNSCRIPT FROM 'classpath:initData.sql'");
            sysLogger.info("Database data initialized.");
        } catch (SQLException e) {
            sysLogger.severe("Failed to initialize schema: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Provides a database connection. The connection <em>should not</em> be obtained in a try-with-resources
     * to avoid closing the connection due to its implementation of <code>AutoClosable</code>.
     *
     * @return Connection Reference to the application's current database connection.
     * @throws SQLException If there is an issue establishing the connection.
     */
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            initConnection();
        }
        return conn;
    }

    /**
     * Commits any open transactions to the database.
     * This ensures that all pending changes are persisted.
     */
    public static void commit() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
                sysLogger.info("Committed open database transactions.");
            }
        } catch (SQLException e) {
            sysLogger.severe("Failed to commit transactions: " + e.getMessage());
        }
    }

    /**
     * Closes the existing connection to the database.
     * Ensures any open transactions are committed before closing.
     */
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.commit();
                conn.close();
                sysLogger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            sysLogger.severe("Failed to close database connection: " + e.getMessage());
        }
    }

    /**
     * Closes an existing <code>java.sql.PreparedStatement</code> associated with the current connection.
     *
     * @param stmt The <code>PreparedStatement</code> to be closed.
     */
    public static void close(PreparedStatement stmt) {
        try {
            if (stmt != null && !stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            sysLogger.severe("Failed to close PreparedStatement: " + e.getMessage());
        }
    }
}