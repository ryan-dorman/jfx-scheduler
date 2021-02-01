package info.ryandorman.simplescheduler.common;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A wrapper around a JDBC connection that can be accessed throughout the application. The singleton design prevents
 * multiple connections form being established to the database at one time. The environment variables for the connection
 * properties are read from resource path set for <code>propFilePath</code>>.
 */
public class DBConnection {
    private static final String propFilePath = "/db/connection.properties";
    private static Connection connection;

    private DBConnection() throws SQLException, IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Properties connectionProps = new Properties();
        connectionProps.load(new FileInputStream(rootPath + propFilePath));

        MysqlDataSource d = new MysqlDataSource();
        d.setUrl(connectionProps.getProperty("url"));
        d.setDatabaseName(connectionProps.getProperty("name"));
        d.setUser(connectionProps.getProperty("user"));
        d.setPassword(connectionProps.getProperty("pass"));
        connection = d.getConnection();
    }

    public static Connection getConnection() throws SQLException, IOException {
        if (connection == null) {
            new DBConnection();
        }
        return connection;
    }

    public static boolean closeConnection() {
        try {
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
