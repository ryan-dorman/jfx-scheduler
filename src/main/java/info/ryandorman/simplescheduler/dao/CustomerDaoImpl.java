package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.common.ResultColumnIterator;
import info.ryandorman.simplescheduler.model.Customer;
import info.ryandorman.simplescheduler.model.FirstLevelDivision;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomerDaoImpl implements CustomerDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id;";

    private static final String GET_BY_ID = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "WHERE c.customer_id = ?;";

    private static final String GET_BY_NAME_LIKE = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id " +
            "WHERE LOWER(c.customer_name) LIKE CONCAT('%', ?, '%')";

    private static final String CREATE_CUSTOMER = "INSERT customers " +
            "(customer_name, address, postal_code, phone, created_by, last_updated_by, division_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String UPDATE_CUSTOMER = "UPDATE customers " +
            "SET customer_name = ?, address = ?, postal_code = ?, " +
            "phone = ?, create_date = ?, created_by = ?, " +
            "last_update = NOW(), last_updated_by = ?, division_id = ? " +
            "WHERE customer_id = ?;";

    private static final String DELETE_CUSTOMER = "DELETE FROM customers " +
            "WHERE customer_id = ?;";

    public static Customer mapResult(ResultSet rs) throws SQLException {
        ResultColumnIterator resultColumn = new ResultColumnIterator(1);
        return mapResult(rs, resultColumn);
    }

    public static Customer mapResult(ResultSet rs, ResultColumnIterator resultColumn) throws SQLException {
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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(customers.size() + " Customers returned from database by CustomerDao.getAll");
        return customers;
    }

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

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        if (customer != null) {
            sysLogger.info(customer.getId() + ":" + customer.getName()
                    + " returned from database by CustomerDao.getById");
        } else {
            sysLogger.warning("No customer returned from database by CustomerDao.getById=" + id);
        }

        return customer;
    }

    @Override
    public List<Customer> getByNameLike(String name) {
        Connection conn;
        PreparedStatement stmt = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(GET_BY_NAME_LIKE);

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = mapResult(rs);
                customers.add(customer);
            }

        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.close(stmt);
        }

        sysLogger.info(customers.size() + " Customers returned from database by CustomerDao.getByNameLike");
        return customers;
    }

    @Override
    public int create(Customer customer) {
        Connection conn;
        PreparedStatement stmt = null;
        int newCustomerId = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_CUSTOMER, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostalCode());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getCreatedBy());
            stmt.setString(6, customer.getUpdatedBy());
            stmt.setInt(7, customer.getDivision().getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                newCustomerId = rs.getInt(1);
            }


            if (newCustomerId == 0) {
                throw new SQLException("Creating new customer failed.");
            }
        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Customer=" + newCustomerId + " created in the database by CustomerDao.create");
        return newCustomerId;
    }

    @Override
    public int update(Customer customer) {
        Connection conn;
        PreparedStatement stmt = null;
        int updatedCustomerId = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(UPDATE_CUSTOMER, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostalCode());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getCreatedBy());
            stmt.setString(6, customer.getUpdatedBy());
            stmt.setInt(7, customer.getDivision().getId());
            stmt.setInt(8, customer.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                updatedCustomerId = rs.getInt(1);
            }

            if (updatedCustomerId == 0) {
                throw new SQLException("Updating customer failed.");
            }
        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Customer=" + updatedCustomerId + " updated in the database by CustomerDao.update");
        return updatedCustomerId;
    }

    @Override
    public int delete(int id) {
        Connection conn;
        PreparedStatement stmt = null;
        int deletedCustomerId = 0;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.prepareStatement(CREATE_CUSTOMER, Statement.RETURN_GENERATED_KEYS);

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                deletedCustomerId = rs.getInt(1);
            }

            if (deletedCustomerId == 0) {
                throw new SQLException("Deleting customer failed.");
            }
        } catch (SQLException | IOException e) {
            sysLogger.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            DBConnection.commit();
            DBConnection.close(stmt);
        }

        sysLogger.info("Customer=" + deletedCustomerId + " deleted from the database by CustomerDao.delete");
        return deletedCustomerId;
    }
}
