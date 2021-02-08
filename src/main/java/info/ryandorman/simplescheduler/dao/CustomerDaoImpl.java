package info.ryandorman.simplescheduler.dao;

import info.ryandorman.simplescheduler.common.DBConnection;
import info.ryandorman.simplescheduler.common.L10nUtil;
import info.ryandorman.simplescheduler.common.ResultColumnIterator;
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

public class CustomerDaoImpl implements CustomerDao {
    private static final Logger sysLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String GET_ALL = "SELECT co.*, fld.*, c.* FROM customers c " +
            "LEFT JOIN first_level_divisions fld ON c.division_id = fld.division_id " +
            "LEFT JOIN countries co ON fld.country_id = co.country_id;";

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
        return null;
    }

    @Override
    public Customer create(Customer customer) {
        return null;
    }

    @Override
    public Customer update(Customer customer) {
        return null;
    }

    @Override
    public Customer delete(Customer customer) {
        return null;
    }
}
