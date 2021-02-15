package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.model.*;

import java.io.IOException;
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentDaoImpl implements AppointmentDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_Id;";

    private static final String GET_BY_DATE_TIME_WINDOW = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_Id " +
            "WHERE app.start >= ? " +
            "AND app.end <= ?;";

    private static final String GET_BY_ID = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_Id " +
            "WHERE app.appointment_id = ?;";

    private static final String GET_BY_CUSTOMER_ID_AND_DATE_TIME = "SELECT co.*, fld.*, c.*, u.*, con.*, app.* " +
            "FROM appointments app " +
            "LEFT JOIN customers c ON app.customer_id = c.customer_id " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "LEFT JOIN users u ON app.user_id = u.user_id " +
            "LEFT JOIN contacts con ON app.contact_id = con.contact_Id " +
            "WHERE app.customer_id = ? " +
            "AND ? BETWEEN app.start AND app.end;";

    private static final String CREATE_APPOINTMENT = "INSERT appointments " +
            "(title, description, location, type, start, end, customer_id, user_id, contact_id, created_by, " +
            "last_updated_by) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_APPOINTMENT = "UPDATE appointments " +
            "SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, customer_id = ?, " +
            "user_id = ?, contact_id = ?, last_update = NOW(), last_updated_by = ? " +
            "WHERE appointment_id = ?;";

    private static final String DELETE_APPOINTMENT = "DELETE FROM appointments WHERE appointment_id = ?;";

    private static final String DELETE_APPOINTMENT_BY_CUSTOMER_ID = "DELETE FROM appointments WHERE customer_id = ?;";

    private Appointment mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(appointments.size() + " Appointments returned from database by AppointmentDao.getAll");
        return appointments;
    }

    @Override
    public List<Appointment> getByDateTimeWindow(ZonedDateTime start, ZonedDateTime end) {
        Connection conn;
        PreparedStatement stmt = null;
        List<Appointment> appointments = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_DATE_TIME_WINDOW);

            stmt.setTimestamp(1, L10nUtil.LocalToUtc(start));
            stmt.setTimestamp(2, L10nUtil.LocalToUtc(end));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appointment = mapResult(rs);
                appointments.add(appointment);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(appointments.size() + " Appointments returned from database by " +
                "AppointmentDao.getByDateTimeWindow=" + start + ", " + end);
        return appointments;
    }

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

        } catch (SQLException | IOException e) {
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

    @Override
    public Appointment getByCustomerIdAndDateTime(int customerId, ZonedDateTime appointmentTime) {
        Connection conn;
        PreparedStatement stmt = null;
        Appointment appointment = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_CUSTOMER_ID_AND_DATE_TIME);

            stmt.setInt(1, customerId);
            stmt.setTimestamp(2, L10nUtil.LocalToUtc(appointmentTime));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                appointment = mapResult(rs);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        if (appointment != null) {
            sysLogger.info(appointment.getId() + ":" + appointment.getTitle()
                    + " returned from database by AppointmentDao.getByCustomerIdAndDateTime=" + customerId + ", "
                    + appointmentTime);
        } else {
            sysLogger.warning("No appointment returned from database by AppointmentDao.getByCustomerIdAndDateTime="
                    +  customerId + ", " + appointmentTime);
        }

        return appointment;
    }

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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment created in the database by AppointmentDao.create");
        return created;
    }

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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment updated in the database by AppointmentDao.update");
        return updated;
    }

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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Appointment deleted from the database by AppointmentDao.delete");
        return deleted;
    }

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

        } catch (SQLException | IOException e) {
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
