package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Appointment;
import info.ryandorman.simplescheduler.model.Contact;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.User;

import java.io.IOException;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Allows access of persistent Appointment data.
 */
public class AppointmentDaoImpl implements AppointmentDao {
    /**
     * System Logger
     */
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * MySQL statement to get all Appointments
     */
    private static final String GET_ALL = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_id " +
            "ORDER BY app.start_time;";
    /**
     * MySQL statement to get all Appointments that start within the given date and time window
     */
    private static final String GET_BY_START_DATE_TIME_WINDOW = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_id " +
            "WHERE app.start_time BETWEEN ? AND ? " +
            "ORDER BY app.start_time;";
    /**
     * MySQL statement to get all Appointments for a specific Customer that occur within the given date and time window
     */
    private static final String GET_BY_CUSTOMER_ID_AND_DATE_TIME_WINDOW = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_id " +
            "WHERE app.customer_id = ? " +
            "AND (? BETWEEN app.start_time AND app.end_time OR ? BETWEEN app.start_time AND app.end_time OR app.start_time BETWEEN ? AND ?) " +
            "ORDER BY app.start_time;";
    /**
     * MySQL statement to get a Appointment with a specified identifier
     */
    private static final String GET_BY_ID = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_id " +
            "WHERE app.appointment_id = ? " +
            "ORDER BY app.start_time;";
    /**
     * MySQL statement to create a new Appointment
     */
    private static final String CREATE_APPOINTMENT = "INSERT INTO appointments " +
            "(title, description, location, type, start_time, end_time, customer_id, user_id, contact_id, created_by, " +
            "last_updated_by) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    /**
     * MySQL statement to update an existing Appointment
     */
    private static final String UPDATE_APPOINTMENT = "UPDATE appointments " +
            "SET title = ?, description = ?, location = ?, type = ?, start_time = ?, end_time = ?, customer_id = ?, " +
            "user_id = ?, contact_id = ?, last_update = NOW(), last_updated_by = ? " +
            "WHERE appointment_id = ?;";
    /**
     * MySQL statement to delete an existing Appointment
     */
    private static final String DELETE_APPOINTMENT = "DELETE FROM appointments WHERE appointment_id = ?;";
    /**
     * MySQL statement to delete existing Appointments for a specific Customer
     */
    private static final String DELETE_APPOINTMENT_BY_CUSTOMER_ID = "DELETE FROM appointments WHERE customer_id = ?;";

    /**
     * Creates a new class instance for this Data Access Object.
     */
    public AppointmentDaoImpl() {
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Appointment entity. Allows specification of
     * <code>java.sql.ResultSet</code> column Appointment data starts at.
     *
     * @param rs           <code>java.sql.ResultSet</code> to map
     * @param resultColumn Column where Appointment data starts
     * @return Appointment entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Appointment data
     */
    public static Appointment mapResult(ResultSet rs, ColumnIterator resultColumn) throws SQLException {
        Customer customer = CustomerDaoImpl.mapResult(rs, resultColumn);
        User user = UserDaoImpl.mapResult(rs, resultColumn);
        Contact contact = ContactDaoImpl.mapResult(rs, resultColumn);

        return new Appointment(
                rs.getInt(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                customer,
                user,
                contact,
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next())
        );
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Appointment entity.
     *
     * @param rs <code>java.sql.ResultSet</code> to map
     * @return Appointment entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Appointment data
     */
    private Appointment mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> getAll() {
        Connection conn;
        PreparedStatement stmt = null;
        List<Appointment> appointments = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_ALL);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = mapResult(rs);
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(appointments.size() + " Appointments returned from database by AppointmentDao.getAll");
        return appointments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> getByStartDateTimeWindow(ZonedDateTime start, ZonedDateTime end) {
        Connection conn;
        PreparedStatement stmt = null;
        List<Appointment> appointments = new ArrayList<>();
        Timestamp utcStart = L10nUtil.LocalToUtc(start);
        Timestamp utcEnd = L10nUtil.LocalToUtc(end);

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_START_DATE_TIME_WINDOW);

            stmt.setTimestamp(1, utcStart);
            stmt.setTimestamp(2, utcEnd);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = mapResult(rs);
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(appointments.size() + " Appointments returned from database by " +
                "AppointmentDao.getByDateTimeWindow=" + utcStart + ", " + utcEnd);
        return appointments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Appointment> getByCustomerIdAndDateTimeWindow(int customerId, ZonedDateTime start, ZonedDateTime end) {
        Connection conn;
        PreparedStatement stmt = null;
        List<Appointment> appointments = new ArrayList<>();
        Timestamp utcStart = L10nUtil.LocalToUtc(start);
        Timestamp utcEnd = L10nUtil.LocalToUtc(end);

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_CUSTOMER_ID_AND_DATE_TIME_WINDOW);

            stmt.setInt(1, customerId);
            stmt.setTimestamp(2, utcStart);
            stmt.setTimestamp(3, utcEnd);
            stmt.setTimestamp(4, utcStart);
            stmt.setTimestamp(5, utcEnd);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Appointment appointment = mapResult(rs);
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(appointments.size() + "Appointments returned from database by " +
                "AppointmentDao.getByCustomerIdAndDateTime=" + customerId + ", " + utcStart + ", " + utcEnd);
        return appointments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Appointment getById(int id) {
        Connection conn;
        PreparedStatement stmt = null;
        Appointment appointment = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_ID);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                appointment = mapResult(rs);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        if (appointment != null) {
            sysLogger.info(appointment.getId() + ":" + appointment.getTitle()
                    + " returned from database by AppointmentDao.getById=" + id);
        } else {
            sysLogger.warning("No appointment returned from database by AppointmentDao.getById=" + id);
        }
        return appointment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int create(Appointment appointment) {
        Connection conn;
        PreparedStatement stmt = null;
        int created = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_APPOINTMENT);
            ColumnIterator columnIterator = new ColumnIterator(1);

            stmt.setString(columnIterator.next(), appointment.getTitle());
            stmt.setString(columnIterator.next(), appointment.getDescription());
            stmt.setString(columnIterator.next(), appointment.getLocation());
            stmt.setString(columnIterator.next(), appointment.getType());
            stmt.setTimestamp(columnIterator.next(), L10nUtil.LocalToUtc(appointment.getStart()));
            stmt.setTimestamp(columnIterator.next(), L10nUtil.LocalToUtc(appointment.getEnd()));
            stmt.setInt(columnIterator.next(), appointment.getCustomer().getId());
            stmt.setInt(columnIterator.next(), appointment.getUser().getId());
            stmt.setInt(columnIterator.next(), appointment.getContact().getId());
            stmt.setString(columnIterator.next(), appointment.getCreatedBy());
            stmt.setString(columnIterator.next(), appointment.getUpdatedBy());
            stmt.execute();

            created = stmt.getUpdateCount();

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment created in the database by AppointmentDao.create");
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(Appointment appointment) {
        Connection conn;
        PreparedStatement stmt = null;
        int updated = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_APPOINTMENT);
            ColumnIterator columnIterator = new ColumnIterator(1);

            stmt.setString(columnIterator.next(), appointment.getTitle());
            stmt.setString(columnIterator.next(), appointment.getDescription());
            stmt.setString(columnIterator.next(), appointment.getLocation());
            stmt.setString(columnIterator.next(), appointment.getType());
            stmt.setTimestamp(columnIterator.next(), L10nUtil.LocalToUtc(appointment.getStart()));
            stmt.setTimestamp(columnIterator.next(), L10nUtil.LocalToUtc(appointment.getEnd()));
            stmt.setInt(columnIterator.next(), appointment.getCustomer().getId());
            stmt.setInt(columnIterator.next(), appointment.getUser().getId());
            stmt.setInt(columnIterator.next(), appointment.getContact().getId());
            stmt.setString(columnIterator.next(), appointment.getUpdatedBy());
            stmt.setInt(columnIterator.next(), appointment.getId());
            stmt.execute();

            updated = stmt.getUpdateCount();

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment updated in the database by AppointmentDao.update");
        return updated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(int id) {
        Connection conn;
        PreparedStatement stmt = null;
        int deleted = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_APPOINTMENT);

            stmt.setInt(1, id);
            stmt.execute();

            deleted = stmt.getUpdateCount();

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment deleted from the database by AppointmentDao.delete");
        return deleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteByCustomerId(int customerId) {
        Connection conn;
        PreparedStatement stmt = null;
        int deleted = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(DELETE_APPOINTMENT_BY_CUSTOMER_ID);

            stmt.setInt(1, customerId);
            stmt.execute();

            deleted = stmt.getUpdateCount();

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment(s) deleted from the database by AppointmentDao.deleteByCustomerId="
                + customerId);
        return deleted;
    }
}
