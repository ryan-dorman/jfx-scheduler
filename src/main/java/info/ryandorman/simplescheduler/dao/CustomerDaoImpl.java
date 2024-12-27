package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.ColumnIterator;
import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Allows access of persistent Customer data.
 */
public class CustomerDaoImpl implements CustomerDao {
    /**
     * System Logger
     */
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * MySQL statement to get all Customers
     */
    private static final String GET_ALL = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id;";
    /**
     * MySQL statement to get a Customer with a specified identifier
     */
    private static final String GET_BY_ID = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "WHERE c.customer_id = ?;";
    /**
     * MySQL statement to get a Customer with a first or last name like the name given
     */
    private static final String GET_BY_NAME_LIKE = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "WHERE LOWER(c.customer_name) LIKE CONCAT('%', ?, '%')";
    /**
     * MySQL statement to create a new Customer
     */
    private static final String CREATE_CUSTOMER = "INSERT INTO customers " +
            "(customer_name, address, postal_code, phone, division_id, created_by, last_updated_by) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";
    /**
     * MySQL statement to update an existing Customer
     */
    private static final String UPDATE_CUSTOMER = "UPDATE customers " +
            "SET customer_name = ?, address = ?, postal_code = ?, phone = ?, " +
            "division_id = ?, last_update = NOW(), last_updated_by = ? " +
            "WHERE customer_id = ?;";
    /**
     * MySQL statement to delete an existing Customer
     */
    private static final String DELETE_CUSTOMER = "DELETE FROM customers WHERE customer_id = ?;";

    /**
     * Creates a new class instance for this Data Access Object.
     */
    public CustomerDaoImpl() {
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Customer entity.
     *
     * @param rs <code>java.sql.ResultSet</code> to map
     * @return Customer entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Customer data
     */
    public static Customer mapResult(ResultSet rs) throws SQLException {
        ColumnIterator resultColumn = new ColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    /**
     * Maps data held in a <code>java.sql.ResultSet</code> to a Customer entity. Allows specification of
     * <code>java.sql.ResultSet</code> column Customer data starts at.
     *
     * @param rs           <code>java.sql.ResultSet</code> to map
     * @param resultColumn Column where Customer data starts
     * @return Customer entity populated with data from <code>java.sql.ResultSet</code>
     * @throws SQLException Occurs if <code>java.sql.ResultSet</code> does not contain all necessary Customer data
     */
    public static Customer mapResult(ResultSet rs, ColumnIterator resultColumn) throws SQLException {
        FirstLevelDivision division = FirstLevelDivisionDaoImpl.mapResult(rs, resultColumn);

        Customer customer = new Customer(
                rs.getInt(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                rs.getString(resultColumn.next()),
                division,
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next()),
                L10nUtil.utcToLocal(rs.getTimestamp(resultColumn.next())),
                rs.getString(resultColumn.next())
        );

        // Skip division_id column, instead we store the FirstLevelDivision class directly in Customer
        resultColumn.next();

        return customer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> getAll() {
        Connection conn;
        PreparedStatement stmt = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_ALL);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = mapResult(rs);
                customers.add(customer);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(customers.size() + " Customers returned from database by CustomerDao.getAll");
        return customers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer getById(int id) {
        Connection conn;
        PreparedStatement stmt = null;
        Customer customer = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_ID);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = mapResult(rs);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        if (customer != null) {
            sysLogger.info(customer.getId() + ":" + customer.getName()
                    + " returned from database by CustomerDao.getById=" + id);
        } else {
            sysLogger.warning("No customer returned from database by CustomerDao.getById=" + id);
        }

        return customer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> getByNameLike(String name) {
        Connection conn;
        PreparedStatement stmt = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_NAME_LIKE);

            stmt.setString(1, name.toLowerCase());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = mapResult(rs);
                customers.add(customer);
            }

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(customers.size() + " Customers returned from database by CustomerDao.getByNameLike=" + name);
        return customers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int create(Customer customer) {
        Connection conn;
        PreparedStatement stmt = null;
        int created = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_CUSTOMER);
            ColumnIterator columnIterator = new ColumnIterator(1);

            stmt.setString(columnIterator.next(), customer.getName());
            stmt.setString(columnIterator.next(), customer.getAddress());
            stmt.setString(columnIterator.next(), customer.getPostalCode());
            stmt.setString(columnIterator.next(), customer.getPhone());
            stmt.setInt(columnIterator.next(), customer.getDivision().getId());
            stmt.setString(columnIterator.next(), customer.getCreatedBy());
            stmt.setString(columnIterator.next(), customer.getUpdatedBy());
            stmt.execute();

            created = stmt.getUpdateCount();

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Customer created in the database by CustomerDao.create");
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(Customer customer) {
        Connection conn;
        PreparedStatement stmt = null;
        int updated = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_CUSTOMER);
            ColumnIterator columnIterator = new ColumnIterator(1);

            stmt.setString(columnIterator.next(), customer.getName());
            stmt.setString(columnIterator.next(), customer.getAddress());
            stmt.setString(columnIterator.next(), customer.getPostalCode());
            stmt.setString(columnIterator.next(), customer.getPhone());
            stmt.setInt(columnIterator.next(), customer.getDivision().getId());
            stmt.setString(columnIterator.next(), customer.getUpdatedBy());
            stmt.setInt(columnIterator.next(), customer.getId());
            stmt.execute();

            updated = stmt.getUpdateCount();

        } catch (SQLException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Customer updated in the database by CustomerDao.update");
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
            stmt = conn.prepareStatement(DELETE_CUSTOMER);

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

        sysLogger.info("Customer deleted from the database by CustomerDao.delete");
        return deleted;
    }
}
