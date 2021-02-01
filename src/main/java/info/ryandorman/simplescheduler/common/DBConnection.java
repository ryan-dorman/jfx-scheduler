package info.ryandorman.simplescheduler.common;

/**
 * A wrapper around a JDBC connection that can be accessed throughout the application. The singleton design prevents
 * multiple connections form being established to the database at one time. The environment variables for the connection
 * properties are read from 'resources/database.properties'.
 */
public class DBConnection {
}
