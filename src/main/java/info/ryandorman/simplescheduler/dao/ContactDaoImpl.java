package info.ryandorman.simplescheduler.dao;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */


import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.model.Contact;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Allows access of persistent Contact data.
 */
public class ContactDaoImpl implements ContactDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private final String GET_ALL = "SELECT * FROM contacts;";

    /**
     * Creates a new class instance for this Data Access Object.
     */
    public ContactDaoImpl() {
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Contact entity.
     *
     * @param rs <code>java.sql.ResultSet</code> to map
     * @return Contact entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Contact data
     */
    public static Contact mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Contact entity. Allows specification of
     * <code>java.sql.ResultSet</code> column Contact data starts at.
     *
     * @param rs           <code>java.sql.ResultSet</code> to map
     * @param resultColumn Column where Contact data starts
     * @return Contact entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Contact data
     */
    public static Contact mapResult(ResultSet rs, ColumnIterator resultColumn) throws SQLException {
        return new Contact(
                rs.getInt(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next())
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> getAll() {
        Connection conn;
        PreparedStatement stmt = null;
        ArrayList<Contact> contacts = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_ALL);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contact contact = mapResult(rs);
                contacts.add(contact);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(contacts.size() + " Contact returned from database by ContactDao.getAll");
        return contacts;
    }
}
